/*
 * Copyright (c) 2020, 2021 Daylam Tayari <daylam@tayari.gg>
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License version 3as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  @author Daylam Tayari daylam@tayari.gg https://github.com/daylamtayari
 *  @version 2.0b
 *  Github project home page: https://github.com/TwitchRecover
 *  Twitch Recover repository: https://github.com/TwitchRecover/TwitchRecover
 */

package TwitchRecover.Core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @param url URL to retrieve the values from.
     * @return VODInfo  VODInfo object containing all of the information of the stream.
     */
    public static VODInfo getData(String url) {
        VODInfo results=new VODInfo();
        int source = checkURL(url);
        if(source == -1) {         //Invalid URL.
            return results;
        }
        else if(source == 1) {     //Twitch Tracker URL.
            try {
                results = getTTData(url);
            }
            catch(IOException ignored) {}
            return results;
        }
        else if(source == 2) {     //Stream Charts URL.
            try {
                results = getSCData(url);
            }
            catch(IOException ignored) {}
            return results;
        }
        return results;
    }

    /**
     * This method checks if a URL is a stream URL
     * from one of the supported analytics websites.
     * @param url URL to be checked.
     * @return int      Integer that is either -1 if the URL is invalid or
     * a value that represents which analytics service the stream link is from.
     */
    private static int checkURL(String url) {
        if(url.contains("twitchtracker.com/") && url.contains("/streams/")) {
            return 1;   //Twitch Tracker URL.
        }
        else if(url.contains("streamscharts.com/twitch/channels/") && url.contains("/streams/")) {
            return 2;   //Streams Charts URL.
        }
        return -1;
    }

    /**
     * This method gets the JSON return from a URL.
     * @param url String representing the URL to get the JSON response from.
     * @return String   String response representing the JSON response of the URL.
     * @throws IOException
     */
    private static String getJSON(String url) throws IOException {
        String json = "";
        URL jsonFetch = new URL(url);
        HttpURLConnection httpcon = (HttpURLConnection) jsonFetch.openConnection();
        httpcon.setRequestMethod("GET");
        httpcon.setRequestProperty("User-Agent", "Mozilla/5.0");
        String readLine = null;
        if(httpcon.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            StringBuffer response = new StringBuffer();
            while((readLine = br.readLine()) != null) {
                response.append(readLine);
            }
            br.close();
            json = response.toString();
        }
        return json;
    }

    //Individual website retrieval:

    //Twitch Tracker retrieval:
    /**
     * This method gets the 4 principal values (streamer's name, stream ID, timestamp and the duration)
     * from a Twitch Tracker stream URL.
     * @param url String value representing the Twitch Tracker stream URL.
     * @return VODInfo  VODInfo object containing all of the information of the stream.
     * @throws IOException
     */
    private static VODInfo getTTData(String url) throws IOException {
        VODInfo results=new VODInfo();
        URL obj = new URL(url);
        HttpURLConnection httpcon = (HttpURLConnection) obj.openConnection();
        httpcon.setRequestMethod("GET");
        httpcon.setRequestProperty("User-Agent", "Mozilla/5.0");
        if(httpcon.getResponseCode() == HttpURLConnection.HTTP_OK) {
            //Get the timestamp:
            BufferedReader brt = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            String response;
            String responseD = "";
            for(int i = 0; i < 300; i++) {
                response = brt.readLine();
                if(i == 7) {
                    int tsIndex = response.indexOf(" on ") + 4;
                    results.setTimestamp(response.substring(tsIndex, tsIndex + 19));
                }
                //Stream duration fetcher:
                if(response.contains("stats-value to-time-lg")) {
                    responseD = response;
                }
            }

            //Get the stream duration:
            String durationPattern = "<div class=\"stats-value to-time-lg\">(\\d*)</div>";
            Pattern dr = Pattern.compile(durationPattern);
            Matcher dm = dr.matcher(responseD);
            if(dm.find()) {
                results.setDuration(Integer.parseInt(dm.group(1)));
            }
            //Get the streamer's name and the VOD ID:
            String pattern = "twitchtracker\\.com\\/([a-zA-Z0-9-_]*)\\/streams\\/(\\d*)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(url);
            if(m.find()) {
                results.setName(m.group(1));
                results.setIDS(m.group(2));
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
     * @param url String value representing the Stream Charts stream URL.
     * @return VODInfo  VODInfo object containing all of the information of the stream.
     * @throws IOException
     */
    private static VODInfo getSCData(String url) throws IOException {
        VODInfo results=new VODInfo();
        String userID;
        double duration = 0.0;
        //Retrieve initial values:
        String pattern = "streamscharts\\.com\\/twitch\\/channels\\/([a-zA-Z0-9_-]*)\\/streams\\/(\\d*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if(m.find()) {
            results.setName(m.group(1));
            results.setIDS(m.group(2));
        }

        //Retrieve user ID:
        String idJSON = getJSON("https://api.twitch.tv/v5/users/?login=" + results.getName() + "&client_id=ohroxg880bxrq1izlrinohrz3k4vy6");
        JSONObject joID = new JSONObject(idJSON);
        JSONArray users = joID.getJSONArray("users");
        JSONObject user = users.getJSONObject(0);
        userID = user.getString("_id");

        //Retrieve stream values:
        String dataJSON = getJSON("https://alla.streamscharts.com/api/free/streaming/platforms/1/channels/" + userID + "/streams/" + results.getID() + "/statuses");
        JSONObject joD = new JSONObject(dataJSON);
        JSONArray items = joD.getJSONArray("items");
        for(int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if(i == 0) {
                results.setTimestamp(item.getString("stream_created_at"));
            }
            duration += item.getDouble("air_time");
        }
        results.setDuration((int) (duration * 60));
        return results;
    }
}