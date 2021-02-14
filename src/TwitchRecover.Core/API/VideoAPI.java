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

package TwitchRecover.Core.API;

import static TwitchRecover.Core.API.API.*;
import TwitchRecover.Core.Compute;
import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.Enums.Quality;
import TwitchRecover.Core.Enums.VideoType;
import TwitchRecover.Core.Feeds;
import TwitchRecover.Core.Fuzz;
import TwitchRecover.Core.VODInfo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

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
        return API.getPlaylist(USHER+"/vod/"+VODID+".m3u8?sig="+auth[1]+"&token="+auth[0]+"&allow_source=true&player=twitchweb&allow_spectre=true&allow_audio_only=true");
    }

    /**
     * This method retrieves the M3U8 feeds for
     * sub-only VODs by utilising values provided
     * in the public VOD metadata API.
     * @param VODID     Long value representing the VOD ID to retrieve the feeds for.
     * @return Feeds    Feeds object holding all of the feed URLs and their respective qualities.
     */
    public static Feeds getSubVODFeeds(long VODID, VideoType vt){
        Feeds feeds=new Feeds();
        String response=getKraken(VODID);
        JSONObject jO=new JSONObject(response);
        String baseURL="";
        if(vt!=VideoType.UPLOAD){
            baseURL=Compute.singleRegex("https:\\/\\/[a-z0-9]*.cloudfront.net\\/([a-z0-9_]*)\\/storyboards\\/[0-9]*-info.json",jO.getString("seek_previews_url").toLowerCase());
        }
        else{
            baseURL=Compute.singleRegex("https:\\/\\/[a-z0-9]*.cloudfront.net\\/([a-z]*\\/[0-9]*\\/[a-z0-9_-]*)\\/storyboards\\/[0-9]*-info.json",jO.getString("seek_previews_url").toLowerCase());
        }
        String token=getVODToken(VODID)[0];
        JSONObject jo = new JSONObject(token);
        JSONArray restricted = jo.getJSONObject("chansub").getJSONArray("restricted_bitrates");
        if(vt==VideoType.HIGHLIGHT){
            String domain=Compute.singleRegex("(https:\\/\\/[a-z0-9\\-]*.[a-z_]*.[net||com||tv]*\\/[a-z0-9_]*\\/)chunked\\/highlight-[0-9]*.m3u8", Fuzz.verifyURL("/"+baseURL+"/chunked/highlight-"+VODID+".m3u8").get(0).toLowerCase());
            for(int i=0;i<restricted.length();i++){
                feeds.addEntry(domain+restricted.get(i).toString()+"/highlight-"+VODID+FileExtension.M3U8.getFE(), Quality.getQualityV(restricted.get(i).toString()));
            }
        }
        else if(vt==VideoType.ARCHIVE) {
            String domain = Compute.singleRegex("(https:\\/\\/[a-z0-9\\-]*.[a-z_]*.[net||com||tv]*\\/[a-z0-9_]*\\/)chunked\\/index-dvr.m3u8", Fuzz.verifyURL("/"+baseURL + "/chunked/index-dvr.m3u8").get(0).toLowerCase());
            for(int i = 0; i < restricted.length(); i++) {
                feeds.addEntry(domain + restricted.get(i).toString() + "/index-dvr" + FileExtension.M3U8.getFE(), Quality.getQualityV(restricted.get(i).toString()));
            }
        }
        else if(vt==VideoType.UPLOAD){
            String domain = Compute.singleRegex("(https:\\/\\/[a-z0-9\\-]*.[a-z_]*.[net||com||tv]*\\/[a-z]*\\/[0-9]*\\/[a-z0-9_-]*\\/)audio_only\\/index-dvr.m3u8", Fuzz.verifyURL("/"+baseURL + "/audio_only/index-dvr.m3u8").get(0).toLowerCase());
            for(int i = 0; i < restricted.length(); i++) {
                feeds.addEntry(domain + restricted.get(i).toString() + "/index-dvr" + FileExtension.M3U8.getFE(), Quality.getQualityV(restricted.get(i).toString()));
            }
        }
        return feeds;
    }

    /**
     * This function performs the API
     * query for the videos endpoints
     * for Twitch's Kraken API.
     * @param VODID     Long value representing the VOD ID.
     * @return String   API query response.
     */
    public static String getKraken(long VODID){
        String response="";
        try{
            CloseableHttpClient httpClient= HttpClients.createDefault();
            HttpGet httpGet=new HttpGet(API_D+"/kraken/videos/"+VODID);
            httpGet.addHeader(ACCEPT, TWITCH_ACCEPT);
            httpGet.addHeader(CI, WEB_CI);
            CloseableHttpResponse httpResponse=httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode()==HTTP_OK){
                response+=getResponse(httpResponse);
            }
            httpResponse.close();
            httpClient.close();
        }
        catch (Exception ignored){}
        return response;
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

    /**
     * This method returns the broadcast
     * type of a particular Twitch video.
     * @param VODID         String value representing the video ID to query.
     * @return VideoType    VideoType enum which represents the broadcast type of the Twitch video in question.
     */
    public static VideoType getVideoType(long VODID, String name){
        String response=gqlGet("{\"operationName\":\"VideoMetadata\",\"variables\":{\"channelLogin\":\""+name+"\",\"videoID\":\""+VODID+"\", \"broadcastType\":\"\"},\"extensions\":{\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"226edb3e692509f727fd56821f5653c05740242c82b0388883e0c0e75dcbf687\"}}}");
        //Parse the JSON:
        JSONObject jo=new JSONObject(response);
        return VideoType.getVideoType(jo.getJSONObject("data").getJSONObject("video").getString("broadcastType"));
    }

    /**
     * This method gets the MP4 video URL of a
     * video that is still up, if the MP4 URL
     * has been generated by the creator.
     * @param ID        Long value representing the video ID.
     * @return String   String value representing the MP4 URL if it exists or a blank string if it does not exist.
     */
    public static String getMP4URL(long ID){
        String response=API.gqlGet("{\"operationName\":\"VideoManager_VideoDownload\",\"variables\":{\"videoID\":\""+ID+"\", \"broadcastType\":\"\"},\"extensions\":{\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"dc73ae4ca87da62676a42a61866bbe725b41e8859077f438b8718e2083b6db3c\"}}}");
        //Parse the JSON:
        JSONObject jo=new JSONObject(response);
        return jo.getJSONObject("data").getJSONObject("video").getJSONObject("download").getString("url");
    }

    /**
     * This method gets the channel name and stream ID
     * from a video with a video ID.
     * @param id            Long value representing the VOD ID.
     * @return VODInfo      VODInfo object which contains the VOD ID, stream ID and the channel name.
     */
    public static VODInfo getInfo(long id){
        String response=API.gqlGet("{\"operationName\": \"ChannelVideoCore\",\"variables\": {\"videoID\": \""+id+"\"},\"extensions\": {\"persistedQuery\": {\"version\": 1,\"sha256Hash\": \"ce114698319f9fa4a1e375ab0dfb65304c9db244ef440bf530b1414d79e7e9f2\"}}}");
        //Parse the JSON:
        JSONObject jo=new JSONObject(response);
        jo=jo.getJSONObject("data").getJSONObject("video").getJSONObject("owner");
        VODInfo vodInfo=new VODInfo();
        vodInfo.setVODID(id);
        vodInfo.setName(jo.getString("login"));
        return vodInfo;
    }
}