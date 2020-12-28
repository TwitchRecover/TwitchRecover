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

/**
 * The Clips object holds
 * all of the elements and
 * methods necessary to process
 * a Twitch clip.
 */
public class Clips {
    private boolean wfuzz;      //Boolean value representing whether or not the user has wfuzz installed or not.
    private long streamID;      //Long value representing the stream ID to fetch the clips from.
    private long duration;      //Long value representing the duration of the stream.
    private String slug;        //String value representing the slug of the clip.
    private String url;         //String value representing the permanent URL of the clip.
    private String fp;          //String value representing the file path of the downloaded clip.
    private String fn;          //String value representing the file name of the downloaded clip.

    /**
     * The constructor of a
     * Clips object which does
     * nothing other than
     * simply instantiate the
     * object.
     */
    public Clips(){
    }
}