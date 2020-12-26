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

import TwitchRecover.Core.API.API;
import TwitchRecover.Core.Enums.FileExtension;
import java.util.ArrayList;

/**
 * This VOD object holds
 * all of the elements and
 * methods necessary to process
 * a VOD.
 */
public class VOD {
    private boolean subOnly;                    //Boolean value representing whether or not a VOD is sub-only.
    private boolean isDeleted;                  //Boolean value representing whether or not a VOD is still up.
    private Feeds feeds;                        //Feeds object corresponding to the VOD.
    private FileExtension fe;                   //Desired output file extension.
    private long VODID;                         //VOD ID of a VOD if it is still up.
    private String[] vodInfo;                   //String array containing the VOD info such as streamer, timestamp, etc.
    private ArrayList<String> retrievedURLs;    //Arraylist containing all of the VOD 'chunked' M3U8s of a particular VOD.

    /**
     * The constructor of a
     * VOD object which initialises
     * two boolean values based on given inputs.
     * @param subOnly       Boolean value representing whether or not the VOD is a sub-only VOD.
     * @param isDeleted     Boolean value representing whether or not the VOD has being deleted or not.
     */
    public VOD(boolean subOnly, boolean isDeleted){
        this.subOnly=subOnly;
        this.isDeleted=isDeleted;
    }

    /**
     * This method gets the corresponding
     * Feeds object to a given VOD ID.
     * @return Feeds    Feeds object corresponding to the VOD of the VOD ID.
     */
    public Feeds getVODFeeds(){
        if(!subOnly){
            feeds= API.getVODFeeds(VODID);
        }
        else{
            feeds=API.getSubVODFeeds(VODID);
        }
        return feeds;
    }

    /**
     * Accessor for a single particular
     * feed of the Feeds object based
     * on a given integer ID.
     * @param id        Integer value representing the list value of the feed to fetch.
     * @return String   Feed URL corresponding to the given ID.
     */
    public String getFeed(int id){
        return feeds.getFeed(id-1);
    }

    /**
     * Accessor for the Feeds
     * object of the VOD object.
     * @return Feeds    Feeds object of the VOD object.
     */
    public Feeds getFeeds(){
        return feeds;
    }

    /**
     * Mutator for the
     * VODID variable.
     * @param VODID     Long value which represents the VODID of the VOD.
     */
    public void setID(long VODID){
        this.VODID=VODID;
    }

    /**
     * Mutator for the file extension
     * enum which represents the user's
     * desired file output format.
     * @param fe    A FileExtensions enum which represents the user's desired output file extension.
     */
    public void setFE(FileExtension fe){
        this.fe=fe;
    }

    /**
     * Mutator for the VOD info
     * string array which contains
     * all of the information about a
     * VOD in order to compute the base URL.
     * @param info      String array containing the information about the VOD.
     */
    public void setVODInfo(String[] info){
        vodInfo=info;
    }
}