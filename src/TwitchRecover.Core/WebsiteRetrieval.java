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

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * This class contains the core method for website data
 * recovery and all of its necessary methods to retrieve
 * necessary information from the Twitch analytics websites
 * Twitch recover supports.
 */
public class WebsiteRetrieval {
    //Core methods:

    /**
     * Core method which retrieves the 4 principal values (streamer's name, stream ID, timestamp, duration)
     * of a stream and returns in a string array in that order.
     *
     * @param url URL to retrieve the values from.
     * @return String[4]    String array containing the 4 principal values (streamer's name, stream ID,
     * timestamp of the start of the stream and the duration) in that respective order. If all values of the
     * array are null, the URL is invalid.
     */
    public long[] getData(String url) {
        long[] results = new long[4];     //0: streamer's name; 1: Stream ID; 2: Timestamp; 3: Duration.
        int source = checkURL(url);
        if(source == -1) {         //Invalid URL.
            return results;
        } else if(source == 1) {     //Twitch Tracker URL.
            try {
                results = getTTData(url);
            } catch(IOException ignored) {
            }
            return results;
        } else if(source == 2) {     //Stream Charts URL.
            try {
                results = getSCData(url);
            } catch(IOException ignored) {
            }
            return results;
        } else if(source == 3) {     //Sully Gnome URL.
            try {
                results = getSGData(url);
            } catch(IOException ignored) {
            }
            return results;
        }
        return results;
    }

    /**
     * This method checks if a URL is a stream URL
     * from one of the supported analytics websites.
     *
     * @param url URL to be checked.
     * @return int      Integer that is either -1 if the URL is invalid or
     * a value that represents which analytics service the stream link is from.
     */
    private int checkURL(String url) {
        if(url.contains("twitchtracker.com/") && url.contains("/streams/")) {
            return 1;   //Twitch Tracker URL.
        } else if(url.contains("streamscharts.com/twitch/channels/") && url.contains("/streams/")){
            return 2;   //Streams Charts URL.
        } else if(isSG(url)){
            return 3;   //Sully Gnome URL.
        }
        return -1;
    }

    /**
     * This method gets the JSON return from a URL.
     * @param url       String representing the URL to get the JSON response from.
     * @return String   String response representing the JSON response of the URL.
     * @throws IOException
     */
    private String getJSON(String url) throws IOException {
        String json="";
        URL jsonFetch=new URL(url);
        HttpURLConnection httpcon=(HttpURLConnection) jsonFetch.openConnection();
        httpcon.setRequestMethod("GET");
        httpcon.setRequestProperty("User-Agent", "Mozilla/5.0");
        String readLine=null;
        if(httpcon.getResponseCode()==HttpURLConnection.HTTP_OK){
            BufferedReader br=new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            StringBuffer response=new StringBuffer();
            while((readLine=br.readLine())!=null){
                response.append(readLine);
            }
            br.close();
            json=response.toString();
        }
        return json;
    }

    /**
     * This method checks if the inputted URL is a
     * Sully Gnome stream URL.
     *
     * @param url Inputted URL to be checked.
     * @return boolean  Returns true if the URL is a Sully Gnome stream URL and false otherwise.
     */
    public boolean isSG(String url) {
        return url.contains("sullygnome.com/channel/") && url.contains("/stream/");
    }

    //Individual website retrieval:

    //Twitch Tracker retrieval:

    /**
     * This method gets the 4 principal values (streamer's name, stream ID, timestamp and the duration)
     * from a Twitch Tracker stream URL.
     *
     * @param url         String value representing the Twitch Tracker stream URL.
     * @return long[4]    Long array containing the 4 principal values (streamer's name, stream ID,
     * timestamp of the start of the stream and the duration) in that respective order.
     * @throws IOException
     */
    private long[] getTTData(String url) throws IOException {
        long[] results = new long[4];
        URL obj = new URL(url);
        HttpURLConnection httpcon = (HttpURLConnection) obj.openConnection();
        httpcon.setRequestMethod("GET");
        httpcon.setRequestProperty("User-Agent", "Mozilla/5.0");
        if(httpcon.getResponseCode() == HttpURLConnection.HTTP_OK) {
            //Get the timestamp:
            BufferedReader brt = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            for(int i = 0; i < 7; i++) {
                brt.readLine();
            }
            String response = brt.readLine();
            int tsIndex = response.indexOf(" on ") + 4;
            results[2] = Long.parseLong(response.substring(tsIndex, tsIndex + 19));
            //Get the stream duration:
            BufferedReader brtd = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            String responseD = "";
            for(int i = 0; i < 300; i++) {
                String res = brtd.readLine();
                if(res.contains("stats-value to-time-lg")) {
                    responseD = res;
                }
            }
            String durationPattern = "<div class=\"stats-value to-time-lg\">(\\d*)</div>";
            Pattern dr = Pattern.compile(durationPattern);
            Matcher dm = dr.matcher(responseD);
            if(dm.find()) {
                results[3] = Long.parseLong(dm.group(1));
            }
            //Get the streamer's name and the VOD ID:
            String pattern = "twitchtracker\\.com\\/([a-zA-Z0-9]*)\\/streams\\/(\\d*)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(url);
            if(m.find()) {
                results[0] = Long.parseLong(m.group(1));
                results[1] = Long.parseLong(m.group(2));
            }
            //Return the array:
            return results;
        }
        throw new IOException();
    }

    //Stream Charts retrieval:

    /**
     * This method gets the 4 principal values (streamer's name, stream ID, timestamp and the duration)
     * from a Stream Charts stream URL.
     * @param url           String value representing the Stream Charts stream URL.
     * @return long[4]      Long array containing the 4 principal values (streamer's name, stream ID,
     * timestamp of the start of the stream and the duration) in that respective order.
     * @throws IOException
     */
    private long[] getSCData(String url) throws IOException {
        long[] results=new long[4];     //0: streamer's name; 1: Stream ID; 2: Timestamp; 3: Duration.
        String userID;
        double duration=0.0;
        //Retrieve initial values:
        String pattern="streamscharts\\.com\\/twitch\\/channels\\/([a-zA-Z0-9]*)\\/streams\\/(\\d*)";
        Pattern r=Pattern.compile(pattern);
        Matcher m=r.matcher(url);
        if(m.find()){
            results[0]=Long.parseLong(m.group(1));
            results[1]=Long.parseLong(m.group(2));
        }

        //Retrieve user ID:
        String idJSON=getJSON("https://api.twitch.tv/v5/users/?login="+results[2]+"&client_id=ohroxg880bxrq1izlrinohrz3k4vy6");
        JSONObject joID=new JSONObject(idJSON);
        JSONObject users=joID.getJSONObject("users");
        JSONObject user=users.getJSONObject("0");
        userID=user.getString("_id");

        //Retrieve stream values:
        String dataJSON=getJSON("https://alla.streamscharts.com/api/free/streaming/platforms/1/channels/"+userID+"/streams/"+results[2]+"/statuses");
        JSONObject joD=new JSONObject(dataJSON);
        JSONArray items=joD.getJSONArray("items");
        for(int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if(i == 0) {
                results[2] = Long.parseLong(item.getString("stream_created_at"));
            }
            duration += item.getDouble("air_time");
        }
        results[3] = Long.parseLong(String.valueOf(duration * 60));
        return results;
    }

    //Sully Gnome retrieval:

    /**
     * This method gets the 4 principal values (streamer's name, stream ID, timestamp and the duration)
     * from a Sully Gnome stream URL.
     *
     * @param url         String value representing the Sully Gnome stream URL.
     * @return long[4]    Long array containing the 4 principal values (streamer's name, stream ID,
     * timestamp of the start of the stream and the duration) in that respective order.
     * @throws IOException
     */
    private long[] getSGData(String url) throws IOException {
        long[] results = new long[4];     //0: streamer's name; 1: Stream ID; 2: Timestamp; 3: Duration.
        return results;
    }
}