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

import TwitchRecover.Core.Enums.ContentType;

/**
 * The object class for
 * the handling of live streams.
 */
public class Live {
    private String channel;     //String value representing the channel to process.
    private Feeds feeds;        //Feeds object containing all of the various feeds of a stream.
    private String fp;          //String value representing the user's desired download file path.
    private String fn;          //String value representing the file name of the output file.

    /**
     * Constructor for the live
     * object which simply
     * instantiates the live object.
     */
    public Live(){
    }

    /**
     * This  method computes the
     * file name of the output file.
     */
    private void computeFN(){
        fn=FileIO.computeFN(ContentType.Stream, channel);
    }

    /**
     * Accessor for the channel variable.
     * @return String   String value representing the channel to process.
     */
    public String getChannel(){
        return channel;
    }

    /**
     * Mutator for the feeds variable.
     * @return Feeds    Feeds object containing all of the feeds of the stream.
     */
    public Feeds getFeeds(){
        return feeds;
    }

    /**
     * Mutator for the channel variable.
     * @param channel   String value representing the channel to process.
     */
    public void setChannel(String channel){
        this.channel=channel;
    }

    /**
     * Mutator for the file path variable.
     * @param fp    String value representing the desired output file path.
     */
    public void setFP(String fp){
        this.fp=FileIO.adjustFP(fp);
    }
}