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

import TwitchRecover.Core.Enums.FileExtensions;

/**
 * This VOD object holds
 * all of the elements and
 * methods necessary to process
 * a VOD.
 */
public class VOD {
    private boolean subOnly;    //Boolean value representing whether or not a VOD is sub-only.
    private boolean isDeleted;  //Boolean value representing whether or not a VOD is still up.
    private Feeds feeds;        //Feeds object corresponding to the VOD.
    private FileExtensions fe;  //Desired output file extension.
    private long VODID;         //VOD ID of a VOD if it is still up.

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
}