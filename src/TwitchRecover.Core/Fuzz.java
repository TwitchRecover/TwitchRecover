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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains the fuzzing methods and the
 * core fuzzing method which is called to find a clip.
 */
public class Fuzz {
    /**
     * This is the core method for fuzzing all of the
     * clips of a particular stream.
     * @param streamID Long value which represents the stream ID for which clips should be fuzzed for.
     * @param duration Long value which represents the duration of the stream.
     * @param wfuzz    Boolean which represents whether Wfuzz is installed and should be used or not.
     * @param cli      Boolean which represents whether the method calling this is the CLI or GUI version.
     * @return ArrayList<String>    String arraylist which holds all of the results of clips.
     */
    public static ArrayList<String> fuzz(long streamID, long duration, boolean wfuzz, boolean cli) {
        ArrayList<String> results = new ArrayList<String>();
        int reps = (((int) duration) * 60) + 2000;
        if(wfuzz) {
            results = wfuzz(streamID, reps, cli);
        }
        else {
            results = jFuzz(streamID, reps);
        }
        return results;
    }

    /**
     * Method which utlises Wfuzz for fuzzing clips from a stream.
     * @param streamID Long value which represents the stream ID for which clips should be fuzzed for.
     * @param reps     Integer value which represents the maximum range for a particular stream.
     * @param cli      Boolean which represents whether the method calling this is the CLI or GUI version.
     * @return ArrayList<String>    String arraylist which holds all of the results of clips.
     */
    private static ArrayList<String> wfuzz(long streamID, int reps, boolean cli) {
        ArrayList<String> fuzzRes = new ArrayList<String>();
        String command = "wfuzz -o csv -z range,0-" + reps + " --hc 404 https://clips-media-assets2.twitch.tv/" + streamID + "-offset-FUZZ.mp4";
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean atResults = false;
            Pattern wp = Pattern.compile("(\\d*),(\\d*),(\\d*,\\d*,\\d*),(\\d*),(\\d*)");
            double quarters = 0;
            int found = 0;
            while((line = br.readLine()) != null) {
                if(atResults) {
                    Matcher wm = wp.matcher(line);
                    if(wm.find()) {
                        if(Integer.valueOf(wm.group(1)) % 900 == 0 && cli) {
                            quarters++;
                            System.out.print("\n" + (quarters / 4) + " hours into the VOD. " + found + " clips found so far. Continuing to find clips.");
                        }
                        if(wm.group(2).equals("200")) {
                            found++;
                            fuzzRes.add("https://clips-media-assets2.twitch.tv/" + streamID + "-offset-" + wm.group(4) + ".mp4");
                        }
                    }
                }
                else if(line.indexOf("id,") == 0) {
                    atResults = true;
                }
            }
        }
        catch(IOException e) {
            fuzzRes.add("Error using Wfuzz. Please make sure you have installed Wfuzz correctly and it is working.");
        }
        return fuzzRes;
    }

    /**
     * Method which utilises available fuzzing tools in JSE8 to fuzz for
     * clips from a given stream.
     * NOTICE: Extremely slow.
     * @param streamID Long value which represents the stream ID for which clips should be fuzzed for.
     * @param reps     Integer value which represents the maximum range for a particular stream.
     * @return ArrayList<String>    String arraylist which holds all of the results of clips.
     */
    private static ArrayList<String> jFuzz(long streamID, int reps) {
        ArrayList<String> jfuzzRes = new ArrayList<String>();
        String baseURL = "https://clips-media-assets2.twitch.tv/" + streamID + "-offset-";
        for(int i = 0; i < reps; i++) {
            String clip = baseURL + i + ".mp4";
            try {
                new URL(clip).openStream();
                jfuzzRes.add(clip);
            }
            catch(IOException ignored) {}
        }
        return jfuzzRes;
    }

    /**
     * This method gets all of the Twitch M3U8 VOD domains
     * from the domains file of the Twitch Recover repository.
     * @return ArrayList<String>    String arraylist representing all of the Twitch M3U8 VOD domains.
     */
    private static ArrayList<String> getDomains(){
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
            if(!added){     //To execute if the domains from the domains file were not added as a backup.
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

    /**
     * Checks if a URL is up by querying it
     * and checking if it returns a 200 response code.
     * @param url       URL to check.
     * @return boolean  Boolean value which is true if querying the URL returns a
     * 200 response code or false if otherwise.
     */
    protected static boolean checkURL(String url){
        try {
            URL uObj=new URL(url);
            HttpURLConnection con = (HttpURLConnection) uObj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            if(con.getResponseCode()==HttpURLConnection.HTTP_OK){
                return true;
            }
            return false;
        }
        catch(IOException ignored) {
            return false;
        }
    }

    /**
     * This method is responsible for brute forcing the
     * VOD URLs based on a timestamp that is correct
     * up to the minute.
     * @param name                  String value which represents the streamer's name.
     * @param streamID              Long value representing the stream ID.
     * @param timestamp             Long value representing the UNIX timestamp of the minute in question.
     * @return ArrayList<String>    String arraylist which represents the working
     * VOD M3U8 URLs.
     */
    protected static ArrayList<String> BFURLs(String name, long streamID, long timestamp){
        ArrayList<String> results=new ArrayList<String>();
        for(int i=0; i<60; i++){
            String url=Compute.URLCompute(name, streamID, timestamp+i);
            if(checkURL(url)){
                ArrayList<String> vResults=verifyURL(url);
                for(String u: vResults){
                    results.add(u);
                }
            }
        }
        return results;
    }

    /**
     * Checks each completed URL based on the given
     * URL value and the domains.
     * @param url                   String value representing the URL to verify.
     * @return ArrayList<String>    String arraylist representing the
     * working VOD M3U8 URLs.
     */
    protected static ArrayList<String> verifyURL(String url){
        ArrayList<String> domains=getDomains();
        ArrayList<String> results=new ArrayList<String>();
        for(String d: domains){
            if(checkURL(d+url)){
                results.add(d+url);
            }
        }
        return results;
    }
}