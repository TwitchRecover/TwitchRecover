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

import static TwitchRecover.Core.API.API.USHER;
import TwitchRecover.Core.Feeds;

/**
 * This class handles all
 * of the API methods directly
 * related to live streams.
 */
public class LiveAPI {
    /**
     * This method gets the live feeds and
     * qualities of a live stream from
     * the channel name.
     * @param channel   String value representing the channel name to get the live feeds of.
     * @return Feeds    Feeds object holding the list of live feeds and corresponding qualities.
     */
    public static Feeds getLiveFeeds(String channel){
        String[] auth=getLiveToken(channel);    //0: Token; 1: Signature.
        return API.getPlaylist(USHER+"/api/channel/hls/"+channel+".m3u8?sig="+auth[1]+"&token="+auth[0]+"&allow_source=true&allow_audio_only=true");
    }

    /**
     * This method retrieves the
     * token and signature for a
     * live channel.
     * @param channel   String value representing the channel to get the access tokens for.
     * @return String[] String array holding the token in the first position and the signature in the second.
     * String[2]: 0: Token; 1: Signature.
     */
    private static String[] getLiveToken(String channel){
        return API.getToken(channel, false);
    }
}