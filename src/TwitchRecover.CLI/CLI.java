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
 *  @version 2.0aH     2.0a Hotfix
 *  Github project home page: https://github.com/TwitchRecover
 *  Twitch Recover repository: https://github.com/TwitchRecover/TwitchRecover
 */

package TwitchRecover.CLI;

import java.util.Objects;

/**
 * CLI class which is the root of the CLI
 * version of Twitch Recover.
 */
public class CLI {

    public static String OVERRIDE_TEMP_PATH;    // Can be used to override the temporary download path

    /**
     * Core method of the CLI version of Twitch Recover.
     * @param args
     */
    public static void main(String[] args){
        extractArguments(args);
        CLIHandler.main();
    }

    /**
     * Goes through all cli arguments and sets
     * properties accordingly.
     *
     * @param args Command line arguments passed in when executing
     */
    private static void extractArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ((Objects.equals(args[i], "--temppath")
                    || Objects.equals(args[i], "-tp")) && args.length > i + 1) {
                OVERRIDE_TEMP_PATH = args[i + 1];
                break; // Exit after argument found
            }
        }
    }
}