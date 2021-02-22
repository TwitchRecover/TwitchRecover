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

import TwitchRecover.Core.Compute;
import TwitchRecover.Core.Enums.Quality;
import TwitchRecover.Core.Enums.Timeout;
import TwitchRecover.Core.Feeds;
import TwitchRecover.Core.FileIO;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles all of the
 * API calls and retrievals.
 */
public class API {
    //Constants:
    protected static final String TWITCH_ACCEPT="application/vnd.twitchtv.v5+json";
    protected static String WEB_CI;
    protected static final String PERSONAL_CI="ohroxg880bxrq1izlrinohrz3k4vy6";     //DO NOT use this for your personal use. This is the client ID for Twitch Recover.
    protected static final String UTF8_CT="text/plain;charset=UTF-8";
    protected static final int HTTP_OK=200;
    //Header constants:
    protected static final String ACCEPT="Accept";
    protected static final String CI="Client-ID";
    protected static final String CT="Content-Type";
    //Domain constants:
    protected static final String USHER="https://usher.ttvnw.net";
    protected static final String GQL="https://gql.twitch.tv/gql";
    protected static final String API_D="https://api.twitch.tv";

    /**
     * Method which parses the feeds from a given
     * arraylist which includes all of the lines that
     * were read from the web query and creates and
     * returns a Feeds object which contains all of
     * the feeds and their corresponding qualities.
     * @param response  String arraylist which contains all of the lines read in the web query.
     * @return  Feeds   A Feeds object which contains all of the feed URLs and their corresponding qualities.
     */
    static Feeds parseFeeds(ArrayList<String> response){
        Feeds feeds=new Feeds();
        for(int i=0; i<response.size(); i++){
            if(!response.get(i).startsWith("#")){
                if(response.get(i-2).contains("chunked")){      //For when the line is the source feed.
                    feeds.addEntryPos(response.get(i), Quality.Source, 0);
                    if(response.get(i-2).contains("Source")){
                        feeds.addEntryPos(response.get(i), Quality.getQualityRF(Compute.singleRegex("#EXT-X-STREAM-INF:BANDWIDTH=\\d*,CODECS=\"[a-zA-Z0-9.]*,[a-zA-Z0-9.]*\",RESOLUTION=(\\d*x\\d*),VIDEO=\"chunked\"", response.get(i-1)), 60.000), 1);
                    }
                    else {
                        getQuality(feeds, i, response, true);
                    }
                }
                else if(response.get(i-2).contains("audio")){       //For when the line is an audio-only feed.
                    feeds.addEntry(response.get(i), Quality.AUDIO);
                }
                else if(response.get(i-2).contains("1080p60")){     //For resolutions greater or equal to 1080p60.
                    getQuality(feeds, i, response, false);
                }
                else{
                    feeds.addEntry(response.get(i), Quality.getQualityV(Compute.singleRegex("#EXT-X-MEDIA:TYPE=VIDEO,GROUP-ID=\"([\\d]*p[36]0)\",NAME=\"([0-9p]*)\",AUTOSELECT=[\"YES\"||\"NO\"]*,DEFAULT=[\"YES\"||\"NO\"]*", response.get(i-2))));
                }
            }
        }
        return feeds;
    }

    /**
     * This method performs a get request of a
     * specific given URL (which has to be a
     * Twitch API URL that is setup in at least
     * V5 on the backend.
     * @param url                   String value representing the URL to perform the get request on.
     * @return ArrayList<String>    String arraylist holding the entire response from the get request, each line representing an entry.
     */
    public static ArrayList<String> getReq(String url){
        ArrayList<String> responseContents=new ArrayList<String>();
        try{
            CloseableHttpClient httpClient=HttpClients.createDefault();
            HttpGet httpget=new HttpGet(url);
            httpget.addHeader(ACCEPT, TWITCH_ACCEPT);
            httpget.addHeader(CI, WEB_CI);
            CloseableHttpResponse httpResponse=httpClient.execute(httpget);
            if(httpResponse.getStatusLine().getStatusCode()==HTTP_OK){
                BufferedReader br=new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                String line;
                while ((line = br.readLine()) != null) {
                    responseContents.add(line);
                }
                br.close();
            }
        }
        catch(Exception ignored){}
        return responseContents;
    }

    /**
     * This method gets a playlist
     * file and returns the contents
     * of the playlist file.
     * @param url       String value which represents the URL of the playlist file.
     * @return Feeds    Feeds object containing all of the feeds from an M3U8 playlist.
     */
    static Feeds getPlaylist(String url){
        File downloadedFile= null;    //Creates the temp file.
        try {
            URL dURL=new URL(url);
            downloadedFile = File.createTempFile("TwitchRecover-Playlist-", ".m3u8");
            downloadedFile.deleteOnExit();
            FileUtils.copyURLToFile(dURL, downloadedFile, Timeout.CONNECT.getTime(), Timeout.READ.getTime());
        }
        catch(IOException ignored){}
        return parseFeeds(FileIO.read(downloadedFile.getAbsolutePath()));
    }

    /**
     * This method parses and returns
     * the token and signature values
     * from a given API JSON response.
     * @param response      String containing the JSON response from the API call.
     * @param isVOD         Boolean value that is true if the token is for a video and false if it is for a stream.
     * @return String[]     String array containing the token and signature values.
     * String[2]: 0: Token; 1: Signature.
     */
    protected static String[] parseToken(String response, boolean isVOD){
        String[] results=new String[2];
        //Parse JSON:
        JSONObject jO=new JSONObject(response);
        JSONObject tokenCat=jO.getJSONObject("data");
        if(isVOD){
            tokenCat=tokenCat.getJSONObject("videoPlaybackAccessToken");
        }
        else{
            tokenCat=tokenCat.getJSONObject("streamPlaybackAccessToken");
        }
        String token=tokenCat.getString("value");
        results[1]=tokenCat.getString("signature");
        //Remove back slashes from token:
        results[0]=token.replace("\\", "");
        return results;
    }

    /**
     * This method retrieves an
     * @param id            String value representing the ID to input into the query.
     * @param isVOD         Boolean value that is true if the token being retrieved is for a VOD and false if it is for a live stream.
     * @return String[]     String array containing the token and signature values.
     * String[2]: 0: Token; 1: Signature.
     */
    protected static String[] getToken(String id, boolean isVOD){
        String response="";
        if(isVOD){
            response=gqlGet("{\"operationName\": \"PlaybackAccessToken\",\"variables\": {\"isLive\": false,\"login\": \"\",\"isVod\": true,\"vodID\": \"" + id + "\",\"playerType\": \"channel_home_live\"},\"extensions\": {\"persistedQuery\": {\"version\": 1,\"sha256Hash\": \"0828119ded1c13477966434e15800ff57ddacf13ba1911c129dc2200705b0712\"}}}");
        }
        else{
            response=gqlGet("{\"operationName\": \"PlaybackAccessToken\",\"variables\": {\"isLive\": true,\"login\": \""+id+"\",\"isVod\": false,\"vodID\": \"\",\"playerType\": \"channel_home_live\"},\"extensions\": {\"persistedQuery\": {\"version\": 1,\"sha256Hash\": \"0828119ded1c13477966434e15800ff57ddacf13ba1911c129dc2200705b0712\"}}}");
        }
        return parseToken(response, isVOD);
    }

    /**
     * This method gets the quality for a source resolution
     * and a resolution that is either 1080p or higher.
     * @param feeds         Feeds object representing the feeds object to add the entries to.
     * @param i             Integer value representing at what point in the arraylist sorting that the loop is at.
     * @param response      String arraylist containing all of the lines of the playlist file.
     * @param source        Boolean value that is true if the call is being made for a source resolution and false if otherwise.
     */
    private static void getQuality(Feeds feeds, int i, ArrayList<String> response, boolean source){
        String patternF;
        String patternR;
        if(source){
            patternF = "#EXT-X-MEDIA:TYPE=VIDEO,GROUP-ID=\"chunked\",NAME=\"([0-9p]*) \\(source\\)\",AUTOSELECT=[\"YES\"||\"NO\"]*,DEFAULT=[\"YES\"||\"NO\"]*";
            patternR = "#EXT-X-STREAM-INF:BANDWIDTH=\\d*,RESOLUTION=(\\d*x\\d*),CODECS=\"[a-zA-Z0-9.]*,[a-zA-Z0-9.]*\",VIDEO=\"chunked\"";
        }
        else{
            patternF="#EXT-X-MEDIA:TYPE=VIDEO,GROUP-ID=\"1080p[0-9]*\",NAME=\"(1080p[0-9]*)\",AUTOSELECT=[\"YES\"||\"NO\"]*,DEFAULT=[\"YES\"||\"NO\"]*";
            patternR="#EXT-X-STREAM-INF:BANDWIDTH=\\d*,CODECS=\"[a-zA-Z0-9.]*,[a-zA-Z0-9.]*\",RESOLUTION=(\\d*x\\d*),VIDEO=\"1080p[0-9]*\"";
        }
        //Get the FPS:
        Pattern pF=Pattern.compile(patternF);
        Matcher mF=pF.matcher(response.get(i-2));
        Double fps=0.000;
        if(mF.find()){
            String vid=mF.group(1);
            fps=Double.parseDouble(vid.substring(vid.indexOf('p')+1));
        }
        //Get the resolution:
        Pattern pR=Pattern.compile(patternR);
        Matcher mR=pR.matcher(response.get(i-1));
        if(mF.find()){
            if(source){
                feeds.addEntryPos(response.get(i), Quality.getQualityRF(mF.group(1), fps), 1);
            }
            else{
                feeds.addEntry(response.get(i), Quality.getQualityRF(mF.group(1), fps));
            }
        }
    }

    /**
     * This method gets the contents
     * from a given HttpResponse.
     * @param httpresponse  A given HttpResponse to be parsed.
     * @return String       String value representing the response from the HttpResponse.
     * @throws IOException
     */
    protected static String getResponse(HttpResponse httpresponse) throws IOException {
        String response="";
        BufferedReader br=new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent()));
        String line;
        while ((line = br.readLine()) != null) {
            response+=line;
        }
        br.close();
        return response;
    }

    /**
     * This function performs a query to the
     * GQL API and returns the response
     * (with typename values removed).
     * @param query     String value representing the API query to perform.
     * @return String   String value containing the API response (excluding '__typename' values).
     */
    public static String gqlGet(String query){
        String response="";
        try{
            CloseableHttpClient httpClient=HttpClients.createDefault();
            HttpPost httpPost=new HttpPost(GQL);
            httpPost.addHeader(CI,WEB_CI);
            StringEntity stringEntity=new StringEntity(query);
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse httpResponse=httpClient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()==HTTP_OK){
                response+=getResponse(httpResponse);
            }
            httpResponse.close();
            httpClient.close();
        }
        catch(Exception ignored){}
        response=response.replaceAll(",\"__typename\":\"[a-zA-Z]*\"", "");
        return response;
    }

    /**
     * Mutator for the WEB_CI variable.
     * @param web_ci    String value set
     */
    public static void setWEBCI(String web_ci){
        WEB_CI=web_ci;
    }
}