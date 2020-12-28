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

package TwitchRecover.Core.API;

import TwitchRecover.Core.Compute;
import TwitchRecover.Core.Enums.Quality;
import TwitchRecover.Core.Feeds;
import TwitchRecover.Core.Fuzz;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
        ArrayList<String> responseContents=API.getReq("https://usher.ttvnw.net/vod/"+VODID+".m3u8?nauthsig"+auth[1]+"&nauth="+auth[0]+"&allow_source=true&player=twitchweb&allow_spectre=true&allow_audio_only=true");
        return API.parseFeeds(responseContents);
    }

    /**
     * This method retrieves the M3U8 feeds for
     * sub-only VODs by utilising values provided
     * in the public VOD metadata API.
     * @param VODID     Long value representing the VOD ID to retrieve the feeds for.
     * @return Feeds    Feeds object holding all of the feed URLs and their respective qualities.
     */
    public static Feeds getSubVODFeeds(long VODID){
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
        }
        catch (Exception ignored){}
        //Parse the JSON response:
        JSONParser parse=new JSONParser();
        JSONObject jObj= null;
        try {
            jObj = (JSONObject) parse.parse(response);
        }
        catch (ParseException ignored){}
        String previewsURL=jObj.get("seek_previews_url").toString();   //Previews URL which is used to get the core of the VOD URL.
        JSONArray resolutions=(JSONArray) jObj.get("resolutions");      //Get the list of resolutions.
        JSONArray fps=(JSONArray) jObj.get("fps");      //Get the list of FPS that correspond to each resolution.
        //Parse the previews URL to get the core of the VOD URL using regex:
        String core= Compute.singleRegex("https:\\/\\/[a-z0-9]*.cloudfront.net\\/([a-z0-9_]*)\\/storyboards\\/[0-9]*-info.json", previewsURL);
        //Get the full base URL of the VOD:
        String baseURL= Fuzz.verifyURL("/"+core+"/chunked/index-dvr.m3u8").get(0);
        //Modify the base URL to allow for the implementation of resolutions:
        baseURL=Compute.singleRegex("([a-z0-9-:\\/]*.[a-z]*.[a-z]*\\/[a-z0-9_]*\\/)chunked\\/index-dvr.m3u8", baseURL);
        //Go through the array, get the quality from the resolution and FPS and add them to the Feeds object.
        String suffix="/index-dvr.m3u8";
        feeds.addEntry(baseURL+"chunked"+suffix, Quality.Source);
        Double fpsVal;
        for(int i=resolutions.size()-2; i==0; i--){
            fpsVal=Double.valueOf(((JSONObject) fps.get(i)).toString());
            if(fpsVal>=25 && fpsVal<=35){
                fpsVal=30.000;
            }
            else if(fpsVal>=55 && fpsVal<=65){
                fpsVal=60.000;
            }
            Quality qual=Quality.getQualityRF(((JSONObject) resolutions.get(i)).toString(), fpsVal);
            feeds.addEntry(baseURL+qual.video+suffix, qual);
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
        ArrayList<String> responseContents=API.getReq("https://api.twitch.tv/api/vods/"+VODID+"/access_token");
        return API.parseToken(responseContents);
    }
}