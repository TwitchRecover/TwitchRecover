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

package TwitchRecover.Core.Mass;

import TwitchRecover.Core.Enums.ContentType;

import java.util.ArrayList;
/**
 * This object class handles all of the
 * mass download operations.
 */
public class MassDownload {
    private ArrayList<String> read;         //String arraylist containing all of the lines of the file path to read.
    private ContentType ct;                 //Content type of the mass download.
    private String rFP;                     //String value representing the complete file path of the file to read.
    private String fp;                      //String value representing the file path of the file to read.
    private String fn;                      //File name of the output file containing the results.
    private String fFP;                     //String value which represents the final file path of the downloaded object.

    /**
     * Constructor of the MassDownload
     * object which instantiates the object.
     */
    public MassDownload(){
    }
}