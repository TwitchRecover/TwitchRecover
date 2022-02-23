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
 *  @version 2.0aH     2.0a Hotfix
 *  Github project home page: https://github.com/TwitchRecover
 *  Twitch Recover repository: https://github.com/TwitchRecover/TwitchRecover
 */

package TwitchRecover.Core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
     * @return String[4]    String array containing the 4 principal values (streamer's name, stream ID,
     * timestamp of the start of the stream and the duration) in that respective order. If all values of the
     * array are null, the URL is invalid.
     */
    public static String[] getData(String url) {
        String[] results = new String[4];     //0: streamer's name; 1: Stream ID; 2: Timestamp; 3: Duration.
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
        else if(url.contains("streamscharts.com/channels/") && url.contains("/streams/")) {
            return 2;   //Streams Charts URL.
        }
        return -1;
    }

    //Individual website retrieval:

    //Twitch Tracker retrieval:
    /**
     * This method gets the 4 principal values (streamer's name, stream ID, timestamp and the duration)
     * from a Twitch Tracker stream URL.
     * @param url String value representing the Twitch Tracker stream URL.
     * @return String[4]    String array containing the 4 principal values (streamer's name, stream ID,
     * timestamp of the start of the stream and the duration) in that respective order.
     */
    private static String[] getTTData(String url) throws IOException
    {
        String[] results = new String[4];

        //Get the stream duration:

        Document doc = Jsoup.connect(url).get();
        Element airTimeDom = doc.select("div.g-x-s-value").first();

        if (airTimeDom != null) {
            results[3] = airTimeDom.text();
        }

        //Get the streamer's name and the VOD ID:
        String pattern = "twitchtracker\\.com/([a-zA-Z0-9-_]*)/streams/(\\d*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if(m.find()) {
            results[0] = m.group(1);
            results[1] = m.group(2);
        }

        //Return the array:
        return results;
    }

    //Stream Charts retrieval:
    /**
     * This method gets the 4 principal values (streamer's name, stream ID, timestamp and the duration)
     * from a Stream Charts stream URL.
     * @param url String value representing the Stream Charts stream URL.
     * @return String[4]    String array containing the 4 principal values (streamer's name, stream ID,
     * timestamp of the start of the stream and the duration) in that respective order.
     */
    private static String[] getSCData(String url) throws IOException {
        String[] results = new String[4];     //0: streamer's name; 1: Stream ID; 2: Timestamp; 3: Duration.
        //Retrieve initial values:
        String pattern = "streamscharts\\.com/channels/([a-zA-Z0-9_-]*)/streams/(\\d*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if(m.find()) {
            results[0] = m.group(1);
            results[1] = m.group(2);
        }

        Document doc = Jsoup.connect(url).get();
        Element airTimeDom = doc.select("span.mx-2").first();
        Element startDateDom = doc.select("time.ml-2").first();


        // START TIME

        SimpleDateFormat streamsChartsFormatString = new SimpleDateFormat("dd MMM yyyy, HH:mm zzz", Locale.ENGLISH);
        SimpleDateFormat formatStringForCompute = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", Locale.ENGLISH);
        Date startDate = null;
        try {
            if (startDateDom != null)
            {
                startDate = streamsChartsFormatString.parse(startDateDom.text() + " GMT+00:00");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        results[2] = formatStringForCompute.format(startDate);

        if (airTimeDom != null) {
            results[3] = String.valueOf(airTimeFromString(airTimeDom.text().replace(",", "").replace(" ", "")) * 60);
        }

        return results;
    }

    private static Integer airTimeFromString(String airTimeString) {

        Pattern p = Pattern.compile("([0-9]+h)*\\s*([0-9]+m)");
        Matcher matcher = p.matcher(airTimeString);

        if (matcher.find()) {
             return matcher.group(1) == null ? Integer.parseInt(matcher.group(2).replace("m", "")) : Integer.parseInt(matcher.group(1).replace("h", ""))*60 + Integer.parseInt(matcher.group(2).replace("m", ""));
        }

        return null;
    }
}

