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

package client;

//import core.Download;
import client.Enums.oType;
import client.Enums.vType;
import client.Handlers.ClipHandler;
import client.Handlers.HighlightHandler;
import client.Handlers.MassHandler;
import client.Handlers.StreamHandler;
import client.Handlers.VODHandler;
import client.Handlers.VideoHandler;
import client.Prompts;
import core.FileIO;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is the handler for the entirety of the CLI
 * version of Twitch Recover.
 */
public class CLIHandler {
    // All scanners use this scanner.
    // TODO:
    // Find better way, or a scanner for each independent usage
    // but this will have to do for the alpha.
    public static Scanner sc=new Scanner(System.in);
    /**
     * Core method of the CLI handler.
     */
    protected static void main() {
        int menuOption=Prompts.menu();
        oType op;
        vType vt;
        //Call the coordinating handler object and set the vType enum variable.
        if(menuOption <= 2) {      //Stream:
            StreamHandler sh = new StreamHandler(menuOption);
            vt=vType.Stream;
        }
        else if(menuOption <= 5) {     //VOD:
            VODHandler vh = new VODHandler(menuOption);
            vt=vType.VOD;
        }
        else if(menuOption <= 8) {     //Highlight:
            HighlightHandler hh = new HighlightHandler(menuOption);
            vt=vType.Highlight;
        }
        else if(menuOption <= 12) {    //Video:
            VideoHandler vh = new VideoHandler(menuOption);
            vt=vType.Video;
        }
        else if(menuOption <= 15) {    //Clip:
            ClipHandler ch = new ClipHandler(menuOption);
            vt=vType.Clip;
        }
        else {      //Mass recovery options:
            MassHandler mh = new MassHandler(menuOption);
            vt=vType.Mass;
        }
        //Set the oType enum variable correctly matching to the operation:
        switch (menuOption) {
            case 1: case 3: case 6: case 13:
                op = oType.Retrieve;
                break;
            case 2: case 4: case 7: case 11: case 14: case 17:
                op = oType.Download;
                break;
            case 5: case 8: case 15: case 16:
                op = oType.Recover;
                break;
            case 9:
                op = oType.Check;
                break;
            case 10:
                op = oType.Unmute;
                break;
            default:
                op = oType.Convert;
                break;
        }
    }
}
