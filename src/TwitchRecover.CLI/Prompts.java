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

package TwitchRecover.CLI;

import TwitchRecover.CLI.Enums.oType;
import TwitchRecover.CLI.Enums.vType;

/**
 * This class holds all of the prompts
 * and the prompt handlers for the
 * CLI version of the program.
 */
public class Prompts {
    /**
     * This prints the welcome prompt.
     */
    public static void welcome(){
        System.out.print(
                  "\nWelcome to Twitch Recover!"
                + "\nThe number one Twitch recovery tool created by Daylam Tayari (https://github.com/daylamtayari)"
                + "\nIf you like this tool please help support me: https://paypal.me/daylamtayari https://cash.app/$daylamtayari BTC: 15KcKrsqW6DQdyZPrgRXXmsKkyyZzHAQVX"
                + "\n"
        );
    }

    /**
     * This method gets an integer input from a
     * pre-printed menu.
     * @param min       Integer value representing the minimum available option.
     * @param max       Integer value representing the maximum available option.
     * @return Integer  Integer value representing the user's selected option.
     */
    public static int getIntInput(int min, int max){
        System.out.print("\nPlease enter the number of the option you want to select (number between " + min +"-" + max + " inclusive): ");
        int input=Integer.parseInt(CLIHandler.sc.next());
        while(!(input>=min && input<=max)){
            System.out.print(
                      "\n\nERROR: Incorrect input"
                    + "\nPlease enter the number of the option you want to select (number between " + min +"-" + max + " inclusive): "
            );
            input=Integer.parseInt(CLIHandler.sc.next());
        }
        return input;
    }

    /**
     * This method prints the main menu
     * and returns the option the user selected.
     * @return int  Integer value representing the option that the user selected.
     */
    public static int menu(){
        menuPrinter();
        return getIntInput(1,17);
    }

    /**
     * This method prints the main menu options.
     */
    private static void menuPrinter(){
        System.out.print(
                  "\n\nMenu:"
                + "\nStreams:"
                + "\n1. Get a live stream link (get the M3U8 stream link of a live stream)."
                + "\n2. Download a stream live. (Currently unavailable, coming in the beta)"
                + "\n\nVODs:"
                + "\n3. Get the link to a VOD (including sub-only)."
                + "\n4. Download a VOD (including sub-only)."
                + "\n5. Recover a VOD - 60 days maximum (can be less in rare cases)."
                + "\n\nHighlights:"
                + "\n6. Retrieve the link to a highlight."
                + "\n7. Download a highlight."
                + "\n8. Recover a highlight."
                + "\n\nVideos:"
                + "\n9. Check if a VOD/highlight has muted segments."
                + "\n10. 'Unmute' a VOD/highlight (be able to view the muted segments of the M3U8)."
                + "\n11. Download an M3U8 file."
                + "\n12. Convert a TS file to MP4."
                + "\n\nClips:"
                + "\n13. Retrieve permanent link of a clip - never deleted."
                + "\n14. Download a clip."
                + "\n15. Recover ALL clips from a stream - NO time limit."
                + "\n\nMass options: (Currently unavailable, coming in the beta)"
                + "\n16. Mass recover options. (Currently unavailable, coming in the beta)"
                + "\n17. Mass download options. (Currently unavailable, coming in the beta)"
        );
    }

    /**
     * This method queries the user on
     * whether or not they wish to repeat
     * the operation they just completed.
     * @param v         vType enum representing the video type to handle.
     * @param o         oType enum representing the operation type to handle.
     * @return boolean  Boolean value representing whether or not the user wishes to repeat the operation they just completed.
     */
    protected static boolean repeat(vType v, oType o){
        String midWord="another";
        if(o==oType.Output){
            midWord="the";
        }
        if(v!=vType.Mass) {
            System.out.print("\n\nDo you want to " + o.getText() + " " + midWord + " " + v.getText() + "?");
        }
        else{
            System.out.print("\n\nDo you want to perform a new mass "+ o.getText()+".");
        }
        System.out.print("\nEnter y for yes and n for no: ");
        String response=CLIHandler.sc.next();
        while(!(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n"))){
            System.out.print(
                      "\nINCORRECT INPUT"
                    + "\nPlease enter either a 'y' or a 'n' corresponding to your desired selection: "
            );
            response=CLIHandler.sc.next();
        }
        return response.equalsIgnoreCase("y");
    }

    /**
     * This method prompts the user if they
     * want to continue using the program
     * or if they want to exit.
     * @return Boolean      Boolean value which is true if the user wants to continue or false if not.
     */
    protected static boolean goAgane(){
        System.out.print(
                "\n\nDo you want to continue using the program/perform a new operation?"
                + "\nPlease enter 'y' for yes and 'n' for no: "
        );
        String response=CLIHandler.sc.next();
        while(!(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n"))){
            System.out.print(
                    "\nINCORRECT INPUT"
                    + "\nPlease enter either a 'y' or a 'n' corresponding to your desired selection: "
            );
            response=CLIHandler.sc.next();
        }
        return response.equalsIgnoreCase("y");
    }

    /**
     * This method prints the repeat welcome
     * prompt for when the user wants to continue
     * using the program but perform a separate operation.
     */
    protected static void repeatWelcome(){
        System.out.print(
                  "\nIf you like this tool and find it useful, please consider supporting me."
                + "\nCheck out my projects - https://github.com/daylamtayari"
                + "\nSupport me financially - https://paypal.me/daylamtayari https://cash.app/$daylamtayari BTC: 15KcKrsqW6DQdyZPrgRXXmsKkyyZzHAQVX"
        );
    }

    /**
     * This method clears the
     * console and resets the
     * cursor to the top.
     */
    protected static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * This message prints the exit message
     * of the CLI version of the program.
     */
    protected static void exitMessage(){
        System.out.print(
                  "\n\nThank you for using Twitch Recover!"
                + "\nIf this tool was useful, please consider support me."
                + "\nCheck out my projects - https://github.com/daylamtayari"
                + "\nSupport me financially - https://paypal.me/daylamtayari https://cash.app/$daylamtayari BTC: 15KcKrsqW6DQdyZPrgRXXmsKkyyZzHAQVX"
        );
    }

    /**
     * This method prints the disclaimer that
     * at this point, the program is only at
     * its alpha stage.
     */
    protected static void alphaDisclaimer(){
        System.out.print(
                  "\nDISCLAIMER:"
                + "\nThis is only the alpha version of this program."
                + "\nThis should not be treated as a complete and final program."
                + "\nThere may be bugs and issues, which if you do experience please report on the Github project page - https://github.com/twitchrecover/twitchrecover"
                + "\nThank you for your understanding."
        );
    }
}