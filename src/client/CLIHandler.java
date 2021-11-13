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
 *
 *
 *
 *  This project was forked and severly refactored for personal use
 *  @author Enan Ajmain https://github.com/3N4N
 *
 */

package client;

import java.util.Scanner;

import client.Handlers.StreamHandler;
import client.Handlers.VODHandler;

/**
 * This is the handler for the entirety of the CLI
 * version of Twitch Recover.
 */
public class CLIHandler {
    // All scanners use this scanner.
    // TODO:
    // Find better way, or a scanner for each independent usage
    // but this will have to do for the alpha.
    public static Scanner sc = new Scanner(System.in);
    /**
     * Core method of the CLI handler.
     */
    protected static void main() {
        System.out.println(
                "\n1. Get a live stream link"
                + "\n2. Get the link to a VOD"
        );
        int menuOption = 0;
        do {
            menuOption = sc.nextInt();
        } while (menuOption < 1 || menuOption > 2);

        switch (menuOption) {
            case 1:
                StreamHandler sh = new StreamHandler();
                break;
            case 2:
                VODHandler vh = new VODHandler();
                break;
            default:
                break;
        }
    }
}
