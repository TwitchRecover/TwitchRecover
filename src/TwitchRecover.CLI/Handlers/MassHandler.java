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

package TwitchRecover.CLI.Handlers;

import TwitchRecover.CLI.CLIHandler;
import TwitchRecover.CLI.Enums.oType;
import TwitchRecover.CLI.Enums.vType;
import TwitchRecover.Core.FileIO;
/**
 * MassHandler object class which
 * handles mass prompts.
 */
public class MassHandler {
    private int option;     //Integer value which represents the user's selected option.

    public MassHandler(int option){
        this.option=option;
        System.out.print("\n\nMass options are not available right now.\nMass recovery options will be coming in the beta version of Twitch Recover.");
    }

    //TODO: The integration of the mass options into each of the matching handler object classes.
    //TODO: To do for the 2.0b release. Leave uncompleted for the 2.0a release.

    //Everything below should be not be considered necessarily relevant to the handling of mass downloads and recoveries.
    //If you are interested in such please check

    /**
     * This method retrieves the user's selected option for
     * the mass recovery/download features.
     * @return int  Integer value representing the user's selected option for the mass recovery/download feature.
     */
    protected int massOptions(){
        return getIntInput(1, 5);
    }

    /**
     * This method prints
     * out the mass recovery menu.
     */
    private void recoverMenu(){
        System.out.print(
                  "\n\nMass Recovery:"
                + "\n1. Retrieve stream M3U8 links."
                + "\n2. Retrieve VOD M3U8 links."
                + "\n3. Retrieve clip permanent links."
        );
    }

    /**
     * This method prints out
     * the mass download menu.
     */
    private void downloadMenu(){
        System.out.print(
                  "\n\nMass Download:"
                + "\n1. Download a VOD."
                + "\n2. Download a highlight."
                + "\n3. Download a clip."
                + "\n4. Download M3U8."
        );
    }

    /**
     * Method which retrieves the file
     * path of the location for where
     * the file with all of the URLs for
     * the mass recovery/download options.
     * @return String   String value representing the file path of the location of the source file.
     */
    protected static String getMassFP(){
        System.out.print("\n\nPlease enter the complete file path of the location of the file containing all of the URLs:\n");
        String fpInput=CLIHandler.sc.next();
        return fpInput;
    }

    /**
     * This method retrieves the directory for
     * where to save the downloads in cases for
     * mass downloads.
     * @return String   String value representing the directory for where to save the file.
     */
    protected static String getMassDir(){
        System.out.print("\n\nPlease enter the directory where you want the downloaded files to be saved:\n");
        String dir=CLIHandler.sc.next();
        return FileIO.adjustFP(dir);
    }

    /**
     * Method that retrieves the output file path
     * and adjusts it if necessary.
     * @return String   String value representing the output file path.
     */
    public static String getOutFP(vType v){
        System.out.print("\n\nPlease enter the file path of the folder where you want the " + v + " to be saved:\n");
        String fp=CLIHandler.sc.next();
        return FileIO.adjustFP(fp);
    }

    /**
     * This method retrieves the URL for
     * a particular operation.
     * @param v         vType enum representing the video type of the operation.
     * @param o         oType enum representing the operation type.
     * @return String   String value representing the retrieved URL.
     */
    public static String getURL(vType v, oType o){
        System.out.print("\n\nPlease enter the URL of the " + v.getText() + " to " + o.getText() + ":\n");
        return CLIHandler.sc.next();
    }

    /**
     * This method gets an integer input from a
     * pre-printed menu.
     * @param min       Integer value representing the minimum available option.
     * @param max       Integer value representing the maximum available option.
     * @return Integer  Integer value representing the user's selected option.
     */
    private int getIntInput(int min, int max){
        System.out.print("\nPlease enter the number of the option you want to select (number between " + min +"-" + max + " inclusive:\n");
        int input=Integer.parseInt(CLIHandler.sc.next());
        while(!(input>=min && input<=max)){
            System.out.print(
                    "\n\nERROR: Incorrect input"
                            + "\nPlease enter the number of the option you want to select (number between " + max +"-" + min + " inclusive):\n"
            );
            input=Integer.parseInt(CLIHandler.sc.next());
        }
        return input;
    }
}