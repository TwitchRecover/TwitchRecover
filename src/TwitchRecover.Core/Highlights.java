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

import TwitchRecover.Core.Enums.FileExtension;
import java.util.ArrayList;

/**
 * The Highlights object
 * holds all of the elements
 * and methods necessary to
 * process a highlight.
 */
public class Highlights {
    private boolean isDeleted;                  //Boolean value representing whether or not the highlight is still up.
    private Feeds feeds;                        //Feeds object corresponding to the highlight.
    private FileExtension fe;                   //Desired output file extension.
    private long highlightID;                   //Highlight ID of the highlight if it is still up.
    private String[] highlightInfo;             //String array containing the VOD info such as streamer, timestamp, etc.
    //0: Channel name; 1: Stream ID; 2. Timestamp of the start of the stream..
    private ArrayList<String> retrievedURLs;    //Arraylist containing all of the highlight's 'chunked' M3U8s of a particular VOD.
    private String fp;                          //String value represnting the file path of the output file.
    private String fn;                          //String value representing the file name of the output file.

    /**
     * The constructor of the
     * highlights object.
     * @param isDeleted     Boolean value representing whether or not the highlight is deleted.
     */
    public Highlights(boolean isDeleted){
        this.isDeleted=isDeleted;
        if(isDeleted){
            highlightInfo=new String[3];
        }
    }
}