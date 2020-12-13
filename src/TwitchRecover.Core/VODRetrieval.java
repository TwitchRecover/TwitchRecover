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

import java.util.ArrayList;
/**
 * The VOD retrieval class is the class that orchestrates
 * all of the VOD retrieval and is what is called by the
 * CLI and GUI packages.
 */
public class VODRetrieval {
    /**
     * This method retrieves the VOD M3U8
     * URLs from given String values.
     * @param name                  String value representing the streamer's name.
     * @param sID                   String value representing the stream ID.
     * @param ts                    String value representing the timestamp of the stream.
     * @param bf                    Boolean value which represents whether a VOD brute force should be carried out.
     * @return ArrayList<String>    String arraylist which represents all of the working VOD M3U8 URLs.
     */
    public static ArrayList<String> retrieveVOD(String name, String sID, String ts, boolean bf){
        ArrayList<String> results=new ArrayList<String>();
        long timestamp=Compute.getUNIX(ts);
        long streamID=Long.parseLong(sID);
        if(bf){
            results=Fuzz.BFURLs(name, streamID, timestamp);
        }
        else{
            String url=Compute.URLCompute(name, streamID, timestamp);
            results=Fuzz.verifyURL(url);
        }
        return results;
    }
}