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

import TwitchRecover.CLI.CLIHandler;
import TwitchRecover.CLI.Enums.oType;
import TwitchRecover.CLI.Enums.vType;
import TwitchRecover.Core.Compute;
import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.Enums.Quality;
import TwitchRecover.Core.Feeds;

/**
 * This class handles the elements which
 * are common to at least 2 handler classes.
 */
public class CoreHandler {
    /**
     * This method checks whether or
     * not a specific link is a Twitch
     * video link.
     * @param url       String value representing the URL value to check.
     * @return boolean  Boolean value which is true if the URL is indeed a Twitch video link and false otherwise.
     */
    protected static boolean isVideo(String url){
        return Compute.singleRegex("(twitch.tv/[0-9]*)", url)!=null;
    }

    /**
     * This method offers a list of file
     * extensions for an output file
     * for the user to select for the
     * output file.
     * @return FileExtension    FileExtension enum which represents the file extension the value wants the output file to be.
     */
    protected static FileExtension userFE(){
        System.out.print(
                  "\n\nPlease enter the file extension of the desired output file:"
                + "\n1. TS."
                + "\n2. MPEG."
                + "\n3. MP4."
                + "\n4. MOV."
                + "\n5. AVI."
                + "\nPlease enter your desired file extension: "
        );
        int selection=Integer.parseInt(CLIHandler.sc.next());
        while(!(selection>0 && selection<=5)){
            System.out.print(
                      "\n\nERROR: Invalid input!"
                    + "\nPlease enter a valid number input: "
            );
            selection=Integer.parseInt(CLIHandler.sc.next());
        }
        if(selection==1){
            return FileExtension.TS;
        }
        else if(selection==2){
            return FileExtension.MPEG;
        }
        else if(selection==3){
            return FileExtension.MP4;
        }
        else if(selection==4){
            return FileExtension.MOV;
        }
        else{
            return FileExtension.AVI;
        }
    }

    protected static int selectFeeds(Feeds feeds, oType o){
        System.out.print("\n\nQualities available:");
        int i=1;
        for(Quality qual: feeds.getQualities()){
            System.out.print("\n"+i+". "+qual.text);
            i++;
        }
        System.out.print("\nPlease enter the desired quality you want to "+o.text+": ");
        String selection=CLIHandler.sc.next();
        return Integer.parseInt(selection);
    }

    /**
     * This method prompts the user for
     * the highlight URL to handle
     * and makes sure that the URL is
     * valid.
     * @param op        oType enum which represents what operation to prompt the user the URL for.
     * @return String   String value which represents the highlight URL the user inputted.
     */
    protected static String promptURL(oType op, vType v){
        System.out.print("\nPlease enter the link of the "+v.text+" to "+op.text+": ");
        String highlightURL=CLIHandler.sc.next();
        while(!CoreHandler.isVideo(highlightURL)){
            System.out.print(
                      "\n\nERROR: Invalid "+v.text+" link."
                    + "\nPlease enter a valid "+v.text+" URL."
            );
            highlightURL=CLIHandler.sc.next();
        }
        return highlightURL;
    }
}