/*
 * Copyright (c) 2021 Daylam Tayari <daylam@tayari.gg>
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

package TwitchRecover.CLI.Handlers;

import TwitchRecover.Core.Compute;
/**
 * ClipHandler object class which
 * handles a clip prompt.
 */
public class ClipHandler {
    private int option;     //Integer value representing the user's desired option.

    public ClipHandler(int option){
        this.option=option;
    }

    private void DownloadClip(){

    }

    /**
     * This method checks whether a
     * given URL is indeed a clip URL.
     * @param url   String value representing the URL
     * @return boolean  Boolean value which returns true if the value is indeed a proper
     * clip URL and false if otherwise.
     */
    private boolean checkClipURL(String url){
        if(url.contains("clips.twitch.tv")){
            return !Compute.checkNullString(Compute.singleRegex("(clips.twitch.tv/[a-zA-Z]*)", url));
        }
        else if(url.contains("twitch.tv/clips")){
            return !Compute.checkNullString(Compute.singleRegex("(twitch.tv/clips/[a-zA-Z]*)", url));
        }
        else if(url.contains("clips-media-assets2.twitch.tv")){
            return !Compute.checkNullString(Compute.singleRegex("(clips-media-assets2.twitch.tv\\/[0-9]*-offset-[0-9]*.mp4)", url));
        }
        return false;
    }
}