/*
 * Copyright (c) 2020 Daylam Tayari <daylam@tayari.gg>
 *
 * This library is free software. You can redistribute it and/or modify it under the terms of the GNU General Public License version 3 as published by the Free Software Foundation.
 * This program is distributed in the that it will be use, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/**
 * @author Daylam Tayari https://github.com/daylamtayari
 * @version 2.0
 * Github project home page: https://github.com/TwitchRecover
 * Twitch Recover repository: https://github.com/TwitchRecover/TwitchRecover
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains the core method for website data
 * recovery and all of its necessary methods to retrieve
 * necessary information from the Twitch analytics websites
 * Twitch recover supports.
 */
public class WebsiteRetrieval{
    //Core methods:
    public String[] getData(String url){

    }

    /**
     * This method checks if a URL is a stream URL
     * from one of the supported analytics websites.
     * @param url       URL to be checked.
     * @return int      Integer that is either -1 if the URL is invalid or
     * a value that represents which analytics service the stream link is from.
     */
    private int checkURL(String url){
        if(url.indexOf("twitchtracker.com/")!=-1 && url.indexOf("/streams/")!=-1){
            return 1;   //Twitch Tracker URL.
        }
        else if(url.indexOf("streamscharts.com/twitch/channels/")!=-1 && url.indexOf("/streams/")!=-1){
            return 2;   //Streams Charts URL.
        }
        else if(isSG(url)){
            return 3;   //Sully Gnome URL.
        }
        return -1;
    }

    /**
     * This method checks if the inputted URL is a
     * Sully Gnome stream URL.
     * @param url       Inputted URL to be checked.
     * @return boolean  Returns true if the URL is a Sully Gnome stream URL and false otherwise.
     */
    public boolean isSG(String url){
        if(url.indexOf("sullygnome.com/channel/")!=-1 && url.indexOf("/stream/")!=-1){
            return true;
        }
        return false;
    }

    //Individual website retrieval:

    //Twitch Tracker retrieval:
    private String[] getTTData(String url){

    }
    //Stream Charts retrieval:
    private String[] getSCData(String url){

    }
}