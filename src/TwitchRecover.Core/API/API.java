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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import TwitchRecover.Core.Enums.Quality;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * This class handles all of the
 * API calls and retrievals.
 */
public class API {
    /**
     * This method gets the live feeds and
     * qualities of a live stream from
     * the channel name.
     * @param channel   String value representing the channel name to get the live feeds of.
     * @return Feeds    Feeds object holding the list of live feeds and corresponding qualities.
     */
    protected static Feeds getLiveFeeds(String channel){
        String[] auth=getToken(channel);
        ArrayList<String> responseContents=new ArrayList<String>();
        try{
            CloseableHttpClient httpClient=HttpClients.createDefault();
            HttpGet get=new HttpGet("");    //TODO: Configure M3U8 fetch URL.
            get.addHeader("User-Agent", "Mozilla/5.0");
            //TODO: Add any other necessary headers.
            CloseableHttpResponse httpResponse=httpClient.execute(get);
            if(httpResponse.getStatusLine().getStatusCode()==200){
                BufferedReader br=new BufferedReader(new InputStreamReader(httpResponse.getEntity()));.getContent()));
                String line;
                while((line=br.readLine())!=null){
                    responseContents.add(line);
                }
            }
        }
        catch(Exception ignored){}
        return parseFeeds(responseContents);
    }

    /**
     * Method which parses the feeds from a given
     * arraylist which includes all of the lines that
     * were read from the web query and creates and
     * returns a Feeds object which contains all of
     * the feeds and their corresponding qualities.
     * @param response  String arraylist which contains all of the lines read in the web query.
     * @return  Feeds   A Feeds object which contains all of the feed URLs and their corresponding qualities.
     */
    private static Feeds parseFeeds(ArrayList<String> response){
        Feeds feeds=new Feeds();
        for(int i=0; i<response.size(); i++){
            if(!response.get(i).startsWith("#")){
                if(response.get(i-2).contains("chunked")){      //For when the line is the source feed.
                    feeds.addEntryPos(response.get(i), Quality.Source, 0);
                    String pattern="#EXT-X-MEDIA:TYPE=VIDEO,GROUP-ID=\"chunked\",NAME=\"([0-9p]*) \\(source\\)\",AUTOSELECT=[\"YES\"||\"NO\"]*,DEFAULT=[\"YES\"||\"NO\"]*";
                    Pattern p=Pattern.compile(pattern);
                    Matcher m=p.matcher(response.get(i-2));
                    if(m.find()){
                        feeds.addEntryPos(response.get(i), Quality.getQuality(m.group(1)), 1);
                    }
                }
                else{
                    String pattern="#EXT-X-MEDIA:TYPE=VIDEO,GROUP-ID=\"([\\d]*p[36]0)\",NAME=\"([0-9p]*)\",AUTOSELECT=[\"YES\"||\"NO\"]*,DEFAULT=[\"YES\"||\"NO\"]*";
                    Pattern p=Pattern.compile(pattern);
                    Matcher m=p.matcher(response.get(i-2));
                    String quality="";
                    if(m.find()){
                        quality=m.group(1);
                    }
                    feeds.addEntry(response.get(i), Quality.getQuality(quality));
                }
            }
        }
        return feeds;
    }
}