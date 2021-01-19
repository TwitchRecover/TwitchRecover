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
import TwitchRecover.Core.Enums.FileExtension;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

/**
 * This class handles all
 * of the API methods directly
 * related to clips.
 */
public class ClipsAPI {
    /**
     * This method returns the
     * permanent clip link of a
     * clip from a given slug.
     * @param slug      String value representing the clip's slug.
     * @return String   String value representing the permanent link of the clip.
     */
    public static String getClipLink(String slug, boolean download){
        String response="";
        //API Query:
        try{
            CloseableHttpClient httpClient= HttpClients.createDefault();
            HttpGet httpget=new HttpGet(API_D+"/kraken/clips/"+slug);
            httpget.addHeader(ACCEPT, TWITCH_ACCEPT);
            httpget.addHeader(CI, PERSONAL_CI);
            CloseableHttpResponse httpResponse=httpClient.execute(httpget);
            if(httpResponse.getStatusLine().getStatusCode()==HTTP_OK){
                response=getResponse(httpResponse);
            }
        }
        catch(Exception ignored){}
        try{
            JSONObject jo=new JSONObject(response);
            String streamID=jo.getString("broadcast_id");
            JSONObject vod=jo.getJSONObject("vod");
            if(download){
                String url=vod.getString("preview_image_url");
                return url.substring(0, url.indexOf("-preview")) + FileExtension.MP4.getFE();
            }
            else {
                int offset = vod.getInt("offset") + 26;
                return "https://clips-media-assets2.twitch.tv/" + streamID + "-offset-" + offset + FileExtension.MP4.getFE();
            }
        }
        catch(Exception e){
            return "ERROR: Clip could not be found.";
        }
    }
}