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

package TwitchRecover.Core.API;

import TwitchRecover.Core.Compute;
import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.Enums.Quality;
import TwitchRecover.Core.Feeds;
import TwitchRecover.Core.Fuzz;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This class handles all
 * of the API methods directly
 * related to VODs.
 */
public class VideoAPI {
    /**
     * This method gets the list of feeds
     * of a VOD that is still up from the
     * VOD ID.
     * This is NOT to be used for sub-only VODs.
     * @param VODID     Long value representing the VOD ID.
     * @return Feeds    Feeds object holding the list of VOD feeds and their corresponding qualities.
     */
    public static Feeds getVODFeeds(long VODID){
        String[] auth=getVODToken(VODID);  //0: Token; 1: Signature.
        return API.getPlaylist("https://usher.ttvnw.net/vod/"+VODID+".m3u8?sig="+auth[1]+"&token="+auth[0]+"&allow_source=true&player=twitchweb&allow_spectre=true&allow_audio_only=true");
    }

    /**
     * This method retrieves the M3U8 feeds for
     * sub-only VODs by utilising values provided
     * in the public VOD metadata API.
     * @param VODID     Long value representing the VOD ID to retrieve the feeds for.
     * @return Feeds    Feeds object holding all of the feed URLs and their respective qualities.
     */
    public static Feeds getSubVODFeeds(long VODID, Boolean highlight){
        Feeds feeds=new Feeds();
        //Get the JSON response of the VOD:
        String response="";
        try{
            CloseableHttpClient httpClient= HttpClients.createDefault();
            HttpGet httpget=new HttpGet("https://api.twitch.tv/kraken/videos/"+VODID);
            httpget.addHeader("User-Agent", "Mozilla/5.0");
            httpget.addHeader("Accept", "application/vnd.twitchtv.v5+json");
            httpget.addHeader("Client-ID", "kimne78kx3ncx6brgo4mv6wki5h1ko");
            CloseableHttpResponse httpResponse=httpClient.execute(httpget);
            if(httpResponse.getStatusLine().getStatusCode()==200){
                BufferedReader br=new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                String line;
                while ((line = br.readLine()) != null) {
                    response+=line;
                }
                br.close();
            }
            httpResponse.close();
            httpClient.close();
        }
        catch (Exception ignored){}
        //Parse the JSON response:
        JSONObject jO=new JSONObject(response);
        String baseURL= Compute.singleRegex("https:\\/\\/[a-z0-9]*.cloudfront.net\\/([a-z0-9_]*)\\/storyboards\\/[0-9]*-info.json",jO.getString("seek_previews_url"));
        String token=getVODToken(VODID)[0];
        JSONObject jo = new JSONObject(token);
        JSONArray restricted = jo.getJSONObject("chansub").getJSONArray("restricted_bitrates");
        if(highlight){
            String domain=Compute.singleRegex("(https:\\/\\/[a-z0-9\\-]*.[a-z_]*.[net||com||tv]*\\/[a-z0-9_]*\\/)chunked\\/highlight-[0-9]*.m3u8", Fuzz.verifyURL("/"+baseURL+"/chunked/highlight-"+VODID+".m3u8").get(0));
            for(int i=0;i<restricted.length();i++){
                feeds.addEntry(domain+restricted.get(i).toString()+"/highlight-"+VODID+FileExtension.M3U8.fileExtension, Quality.getQualityV(restricted.get(i).toString()));
            }
        }
        else {
            String domain = Compute.singleRegex("(https:\\/\\/[a-z0-9\\-]*.[a-z_]*.[net||com||tv]*\\/[a-z0-9_]*\\/)chunked\\/index-dvr.m3u8", Fuzz.verifyURL("/"+baseURL + "/chunked/index-dvr.m3u8").get(0));
            for(int i = 0; i < restricted.length(); i++) {
                feeds.addEntry(domain + restricted.get(i).toString() + "/index-dvr" + FileExtension.M3U8.fileExtension, Quality.getQualityV(restricted.get(i).toString()));
            }
        }
        return feeds;
    }

    /**
     * This method retrieves the
     * token and signature values
     * for a VOD.
     * @param VODID     Long value representing the VOD ID to get the token and signature for.
     * @return String[] String array holding the token in the first position and the signature in the second position.
     * String[2]: 0: Token; 1: Signature.
     */
    private static String[] getVODToken(long VODID){
        return API.getToken(String.valueOf(VODID), true);
    }
}