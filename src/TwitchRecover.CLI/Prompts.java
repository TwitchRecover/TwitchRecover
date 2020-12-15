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

package TwitchRecover.CLI;

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
    protected static void welcome(){
        System.out.print(
                  "\nWelcome to Twitch Recover!"
                + "\nThe number one Twitch recovery tool created by Daylam Tayari (https://github.com/daylamtayari)"
                + "\nIf you like this tool please help support me: https://paypal.me/daylamtayari https://cash.app/$daylamtayari"
                + "\n"
        );
    }

    /**
     * This method prints the main menu
     * and returns the option the user selected.
     * @return int  Integer value representing the option that the user selected.
     */
    protected static int menu(){
        Scanner sc=new Scanner(System.in);
        menuPrinter();
        System.out.print("\nPlease enter the number of the option you want to select (number between 1-9 inclusive:\n");
        int input=Integer.parseInt(sc.nextLine());
        while(!(input>0 && input<10)){
            System.out.print(
                    "\n\nINCORRECT INPUT"
                    + "\nPLEASE ENTER THE NUMBER OF THE OPTION YOU WANT TO SELECT (number between 1-9 inclusive):\n"
            );
            input=Integer.parseInt(sc.nextLine());
        }
        return input;
    }

    /**
     * This method prints the main menu options.
     */
    private static void menuPrinter(){
        System.out.print(
                "\nMenu:"
                        + "\nVODs:"
                        + "\n1. Download a VOD."
                        + "\n2. Download a sub-only VOD (without being subbed to that channel)."
                        + "\n3. Recover a VOD - 60 days maximum."
                        + "\n4. View a muted M3U8 VOD - If a M3U8 VOD has muted segments, use this to view it in its entirety."
                        + "\nHighlights:"
                        + "\n5. Download a highlight."
                        + "\n6. Recover a highlight."
                        + "\nClips:"
                        + "\n7. Download a clip."
                        + "\n8. Recover all clips from a stream - NO time limit."
                        + "\n9. Mass recover options:"
        );
    }
}