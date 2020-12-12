/*
 * Copyright (c) 2020 Daylam Tayari <daylam@tayari.gg>
 *
 * This library is free software. You can redistribute it and/or modify it under the terms of the GNU General Public License version 3 as published by the Free Software Foundation.
 * This program is distributed in the that it will be use, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/*
 * @author Daylam Tayari https://github.com/daylamtayari
 * @version 2.0
 * Github project home page: https://github.com/TwitchRecover
 * Twitch Recover repository: https://github.com/TwitchRecover/TwitchRecover
 */

package TwitchRecover.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * This class contains the fundamental methods of the core package
 * and the ones that compute the fundamental elements of the
 * core of Twitch Recover.
 */
public class Compute {
    /**
     * Main method of the compute class which
     * computes a VOD URL from given values.
     * @param name          String value representing the streamer's name.
     * @param streamID      A string representing the stream ID of a stream.
     * @param timestamp     A string value representing the timestamp of the stream
     * in standard timestamp form.
     * @return String       String value representing the completed latter part of the URL.
     */
    public String URLCompute(String name, String streamID, String timestamp){
        String baseString=name+"_"+streamID+"_"+getUNIX(timestamp);
        String hash=hash(baseString);
        String finalString=hash+"_"+baseString;
        return "/"+finalString+"/chunked/index-dvr.m3u8";
    }

    /**
     * This method gets the UNIX time from a time value in a standard
     * timestamp format.
     * @param ts        String value representing the timestamp.
     * @return long     Long value which represents the UNIX timestamp.
     */
    private long getUNIX(String ts){
        String time = ts + " UTC";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
        Date date=null;
        try{
            df.parse(time);
        }
        catch(ParseException ignored){}
        assert date != null;
        return (long) date.getTime() / 1000;
    }

    /**
     * This method computers the SHA1 hash of the base
     * string computed in the URL compute method and
     * returns the first 20 characters of the hash.
     * @param baseString    Base string for which to compute the hash for.
     * @return String       First 20 characters of the SHA1 hash of the given base string.
     * @throws NoSuchAlgorithmException
     */
    private String hash(String baseString){
        MessageDigest md= null;
        try {
            md = MessageDigest.getInstance("SHA1");
        }
        catch(NoSuchAlgorithmException ignored){}
        byte[] result=md.digest(baseString.getBytes());
        StringBuffer sb=new StringBuffer();
        for(byte val: result){
            sb.append(Integer.toString((val&0xff)+0x100, 16).substring(1));
        }
        String hash=sb.toString();
        return hash.substring(0, 20);
    }

    public ArrayList<String> getDomains(){
        ArrayList<String> domains=new ArrayList<String>();
        boolean added=false;
        try {
            URL dURL=new URL("https://raw.githubusercontent.com/TwitchRecover/TwitchRecover/main/domains.txt");
            HttpURLConnection con=(HttpURLConnection) dURL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            if(con.getResponseCode()==HttpURLConnection.HTTP_OK){
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                while((line=br.readLine()) !=null){
                    String response=line.toString();
                    domains.add(response);
                    added=true;
                }
            }
        }
        catch(IOException ignored){}
        finally{
            if(!added){
                domains.add("https://vod-secure.twitch.tv");
                domains.add("https://vod-metro.twitch.tv");
                domains.add("https://d2e2de1etea730.cloudfront.net");
                domains.add("https://dqrpb9wgowsf5.cloudfront.net");
                domains.add("https://ds0h3roq6wcgc.cloudfront.net");
                domains.add("https://dqrpb9wgowsf5.cloudfront.net");
            }
        }
        return domains;
    }
}