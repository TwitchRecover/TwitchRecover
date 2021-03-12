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

import TwitchRecover.Core.Enums.BruteForce;
import TwitchRecover.Core.Enums.Quality;

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
    private static ArrayList<String> domains;

    /**
     * This is the core method for fuzzing all of the
     * clips of a particular stream.
     * @param streamID Long value which represents the stream ID for which clips should be fuzzed for.
     * @param start    Integer value representing the fuzzing start value.
     * @param end      Integer value representing the fuzzing end value.
     * @param wfuzz    Boolean which represents whether Wfuzz is installed and should be used or not.
     * @return ArrayList<String>    String arraylist which holds all of the results of clips.
     */
    public static ArrayList<String> fuzz(long streamID, int start, int end, boolean wfuzz) {
        ArrayList<String> results = new ArrayList<String>();
        if(wfuzz) {
            results = wfuzz(streamID, start, end);
        }
        else {
            results = jFuzz(streamID, start, end);
        }
        return results;
    }

    /**
     * Method which utlises Wfuzz for fuzzing clips from a stream.
     * @param streamID Long value which represents the stream ID for which clips should be fuzzed for.
     * @param start    Integer value representing the start value of the fuzzing.
     * @param end      Integer value representing the end value of the fuzzing.
     * @return ArrayList<String>    String arraylist which holds all of the results of clips.
     */
    private static ArrayList<String> wfuzz(long streamID, int start, int end) {
        ArrayList<String> fuzzRes = new ArrayList<String>();
        String command = "wfuzz -o csv -z range,"+start+"-" + end + " --hc 404 https://clips-media-assets2.twitch.tv/" + streamID + "-offset-FUZZ.mp4";
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
                        if(Integer.parseInt(wm.group(1)) % 900 == 0 && true) {   //TODO: Fix the CLI boolean usage.
                            quarters++;
                            if(found==1){
                                System.out.print("\n" + (quarters / 4) + " hours into the VOD. " + found + " clip found so far. Continuing to find clips...");
                            }
                            else{
                                System.out.print("\n" + (quarters / 4) + " hours into the VOD. " + found + " clips found so far. Continuing to find clips...");
                            }
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
     * @param start    Integer value representing the start fuzzing value.
     * @param end      Integer value representing the end fuzzing value.
     * @return ArrayList<String>    String arraylist which holds all of the results of clips.
     */
    private static ArrayList<String> jFuzz(long streamID, int start, int end) {
        ArrayList<String> jfuzzRes = new ArrayList<String>();
        String baseURL = "https://clips-media-assets2.twitch.tv/" + streamID + "-offset-";
        for(int i = start; i < end; i++) {
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
     * @param timestamp             Long value representing the UNIX timestamp of the minute or hour in question.
     * @return ArrayList<String>    String arraylist which represents the working
     * VOD M3U8 URLs.
     */
    protected static ArrayList<String> BFURLs(String name, long streamID, long timestamp, BruteForce bf){
        ArrayList<String> results=new ArrayList<String>();
        if(bf==BruteForce.Minute){
            for(int i=0; i<60; i++){
                String url=Compute.URLCompute(name, streamID, timestamp+i);
                for(int j=0; j<domains.size();j++){
                    if(checkURL(domains.get(j)+url)){
                        ArrayList<String> vResults=verifyURL(url);
                        results.addAll(vResults);
                    }
                }
            }
        }
        else if(bf==BruteForce.Hour){
            for(int j=0; j<60; j++){
                for(int i=0; i<60; i++){
                    String url=Compute.URLCompute(name, streamID, timestamp+j+i);
                    for(int k=0; k<domains.size();k++){
                        if(checkURL(domains.get(k)+url)){
                            ArrayList<String> vResults=verifyURL(url);
                            results.addAll(vResults);
                        }
                    }
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
    public static ArrayList<String> verifyURL(String url){
        ArrayList<String> results=new ArrayList<String>();
        for(String d: domains){
            if(checkURL(d+url)){
                results.add(d+url);
            }
        }
        return results;
    }

    /**
     * This method fuzzes all
     * of the possible qualities
     * for a specific URL.
     * @param part1     String value representing the part of the URL prior to the quality value.
     * @param part2     String value representing the part of the URL after the quality value.
     * @return Feeds    Feeds object containing the list of feeds found and their respective qualities.
     */
    public static Feeds fuzzQualities(String part1, String part2){
        Feeds feeds=new Feeds();
        for(Quality qual: Quality.values()){
            if(checkURL(part1+qual.getVideo()+part2)){
                feeds.addEntry(part1+qual.getVideo()+part2, qual);
            }
        }
        return feeds;
    }

    /**
     * Mutator for the domains arraylist.
     * @param domainList
     */
    public static void setDomains(ArrayList<String> domainList){
        domains=domainList;
    }
}