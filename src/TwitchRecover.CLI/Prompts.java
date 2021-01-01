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

package TwitchRecover.CLI;

import TwitchRecover.CLI.Enums.oType;
import TwitchRecover.CLI.Enums.vType;

import java.util.Scanner;

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
                + "\nIf you like this tool please help support me: https://paypal.me/daylamtayari https://cash.app/$daylamtayari"
                + "\n"
        );
    }

    private static int getIntInput(int min, int max){
        Scanner sc=new Scanner(System.in);
        System.out.print("\nPlease enter the number of the option you want to select (number between " + min +"-" + max + " inclusive:\n");
        int input=Integer.parseInt(sc.nextLine());
        while(!(input>=min && input<=max)){
            System.out.print(
                      "\n\nINCORRECT INPUT"
                    + "\nPLEASE ENTER THE NUMBER OF THE OPTION YOU WANT TO SELECT (number between " + max +"-" + min + " inclusive):\n"
            );
            input=Integer.parseInt(sc.nextLine());
        }
        sc.close();
        return input;
    }

    /**
     * This method prints the main menu
     * and returns the option the user selected.
     * @return int  Integer value representing the option that the user selected.
     */
    public static int menu(){
        Scanner sc=new Scanner(System.in);
        menuPrinter();
        return getIntInput(1,9);
    }

    /**
     * This method prints the main menu options.
     */
    private static void menuPrinter(){
        System.out.print(
                  "\n\nMenu:"
                + "\nStreams:"
                + "\n1. Watch a stream live (get the M3U8 stream link of a live stream)."
                + "\n2. Download a stream live."
                + "\n\nVODs:"
                + "\n3. Download a VOD."
                          +"\n4. Get the link to a VOD (including sub-only)"
                + "\n5. Download a sub-only VOD (you do NOT need to be subbed)."
                + "\n6. Recover a VOD - 60 days maximum (can be less in rare cases)."
                + "\n\nHighlights:"
                + "\n7. Download a highlight."
                + "\n8. Recover a highlight."
                + "\n\nVideos:"
                + "\n9. Check if a VOD/highlight has muted segments."
                + "\n10. 'Unmute' a VOD/highlight (be able to view the muted segments of the M3U8)."
                + "\n11. Download an M3U8 file."
                + "\n12. Convert a TS file to MP4."
                + "\n\nClips:"
                + "\n13. Download a clip."
                + "\n14. Retrieve permanent link of a clip - never deleted."
                + "\n15. Recover ALL clips from a stream - NO time limit."
                + "\n\nMass options:"
                + "\n16. Mass recover options."
                + "\n17. Mass download options."
        );
    }

    /**
     * This method retrieves the user's selected option for
     * the mass recovery/download features.
     * @return int  Integer value representing the user's selected option for the mass recovery/download feature.
     */
    protected static int massOptions(){
        Scanner sc=new Scanner(System.in);
        massMenu();
        return getIntInput(1,5);
    }

    /**
     * This method prints the menu of
     * the masss recovery/download options.
     */
    private static void massMenu(){
        System.out.print(
                  "\n\nMass recover allows for the mass recovery or downloads of a specific option."
                + "\nThe source of all of the links to recover/download must all be in the same file."
                + "\nMass recovery options:"
                + "\n1. Mass download VODs."
                + "\n2. Mass recover VODs."
                + "\n3. Mass download highlights."
                + "\n4. Mass recover highlights."
                + "\n5. Mass download clips."
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
        Scanner sc=new Scanner(System.in);
        System.out.print("\n\nPlease enter the complete file path of the location of the file containing all of the URLs:\n");
        String fpInput=sc.nextLine();
        sc.close();
        return fpInput;
    }

    /**
     * This method retrieves the directory for
     * where to save the downloads in cases for
     * mass downloads.
     * @return String   String value representing the directory for where to save the file.
     */
    protected static String getMassDir(){
        Scanner sc=new Scanner(System.in);
        System.out.print("\n\nPlease enter the directory where you want the downloaded files to be saved:\n");
        String dir=sc.nextLine();
        sc.close();
        return fpAdjust(dir);
    }

    /**
     * Method that retrieves the output file path
     * and adjusts it if necessary.
     * @return String   String value representing the output file path.
     */
    public static String getOutFP(vType v){
        Scanner sc=new Scanner(System.in);
        System.out.print("\n\nPlease enter the file path of the folder where you want the " + v + " to be saved:\n");
        String fp=sc.nextLine();
        sc.close();
        return fpAdjust(fp);
    }

    /**
     * This method retrieves the URL for
     * a particular operation.
     * @param v         vType enum representing the video type of the operation.
     * @param o         oType enum representing the operation type.
     * @return String   String value representing the retrieved URL.
     */
    public static String getURL(vType v, oType o){
        Scanner sc=new Scanner(System.in);
        System.out.print("\n\nPlease enter the URL of the " + v.text + " to " + o.text + ":\n");
        String input=sc.nextLine();
        sc.close();
        return input;
    }

    /**
     * This method queries the user on
     * whether or not they wish to repeat
     * the operation they just completed.
     * @param v         vType enum representing the video type to handle.
     * @param o         oType enum representing the operation type to handle.
     * @return boolean  Boolean value representing whether or not the user wishes to repeat the operation they just completed.
     */
    public static boolean repeat(vType v, oType o){
        Scanner sc=new Scanner(System.in);
        String midWord="another";
        if(o==oType.Output){
            midWord="the";
        }
        System.out.print(
                  "\nDo you want to " + o.text + " " + midWord + " " + v.text + "?"
                + "\nEnter y for yes and n for no: "
        );
        while(!(sc.nextLine().equalsIgnoreCase("y") || sc.nextLine().equalsIgnoreCase("n"))){
            System.out.print(
                      "\nINCORRECT INPUT"
                    + "\nPlease enter either a 'y' or a 'n' corresponding to your desired selection: "
            );
        }
        boolean result=sc.nextLine().equalsIgnoreCase("y");
        sc.close();
        return result;
    }

    /**
     * This method adaps a file
     * path to ensure that it is
     * properly treated as a directory.
     * @param fp        String value representing the file path to adjust.
     * @return String   String value representing the adjusted file path.
     */
    private static String fpAdjust(String fp){
        if(fp.indexOf("\\")!=fp.length()-1){
            fp+="\\";
        }
        return fp;
    }

    /**
     * This method retrieves either
     * the manual values of the VOD to
     * be recovered or the Twitch analytics URL.
     * @return String[]     String array holding either the Twitch analytics URL or the manual values.
     */
    protected static String[] VODRecovery(){
        String[] values;
        VODRecoveryMenu();
        if(getIntInput(1,2)==1){
            values=new String[1];
            values[0]=getURL(vType.VOD, oType.Recover);
        }
        else{
            values=new String[3];
            Scanner sc=new Scanner(System.in);
            System.out.print("\n\nStreamer's username: ");
            values[0]=sc.nextLine();
            System.out.print("\nStream ID: ");
            values[1]=sc.nextLine();
            System.out.print("\nTimestamp of the start of the stream (in the YYYY-MM-DD HH:mm:ss format): ");
            values[2]=sc.nextLine();
        }
        return values;
    }

    /**
     * This method prints the VOD recovery menu
     */
    private static void VODRecoveryMenu(){
        System.out.print(
                "\n\nVOD Recovery:"
                + "\nTo recover a VOD (time limit of maximum 60 days), you can either:"
                + "\n1. Use a stream analytics link."
                + "\n(Supports stream links from Twitch Tracker and Stream Charts)"
                + "\n2. Input values manually."
        );
    }
}