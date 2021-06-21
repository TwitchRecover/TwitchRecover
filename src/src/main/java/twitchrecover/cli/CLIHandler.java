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
 *  Github project home page: https://github.com/twitchrecover
 *  Twitch Recover repository: https://github.com/twitchrecover/twitchrecover
 */

package twitchrecover.cli;

//import twitchrecover.core.Download;
import twitchrecover.cli.Enums.oType;
import twitchrecover.cli.Enums.vType;
import twitchrecover.cli.Handlers.ClipHandler;
import twitchrecover.cli.Handlers.HighlightHandler;
import twitchrecover.cli.Handlers.MassHandler;
import twitchrecover.cli.Handlers.StreamHandler;
import twitchrecover.cli.Handlers.VODHandler;
import twitchrecover.cli.Handlers.VideoHandler;
import twitchrecover.cli.Prompts;
import twitchrecover.core.FileIO;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is the handler for the entirety of the CLI
 * version of Twitch Recover.
 */
public class CLIHandler {
    public static Scanner sc=new Scanner(System.in);    //All scanners use this scanner. TODO: Find better way, or a scanner for each independent usage but this will have to do for the alpha.
    /**
     * Core method of the CLI handler.
     */
    public static void run(){
        Prompts.alphaDisclaimer();
        System.out.print("\n"); //TODO: To remove for the final version.
        Prompts.welcome();
        boolean goAgane=true;
        boolean repeat=false;
        while(goAgane){
            if(repeat){
                Prompts.clearConsole();
                Prompts.repeatWelcome();
            }
            int mOption=Prompts.menu();
            boolean agane=true;
            oType op;
            vType vt;
            while(agane) {
                //Call the coordinating handler object and set the vType enum variable.
                if(mOption <= 2) {      //Stream:
                    StreamHandler sh = new StreamHandler(mOption);
                    vt=vType.Stream;
                }
                else if(mOption <= 5) {     //VOD:
                    VODHandler vh = new VODHandler(mOption);
                    vt=vType.VOD;
                }
                else if(mOption <= 8) {     //Highlight:
                    HighlightHandler hh = new HighlightHandler(mOption);
                    vt=vType.Highlight;
                }
                else if(mOption <= 12) {    //Video:
                    VideoHandler vh = new VideoHandler(mOption);
                    vt=vType.Video;
                }
                else if(mOption <= 15) {    //Clip:
                    ClipHandler ch = new ClipHandler(mOption);
                    vt=vType.Clip;
                }
                else {      //Mass recovery options:
                    MassHandler mh = new MassHandler(mOption);
                    vt=vType.Mass;
                }
                //Set the oType enum variable correctly matching to the operation:
                if(mOption==1 || mOption==3 || mOption==6 || mOption==13){
                    op=oType.Retrieve;
                }
                else if(mOption==2 || mOption==4 || mOption==7 || mOption==11 || mOption==14 || mOption==17){
                    op=oType.Download;
                }
                else if(mOption==5 || mOption==8 || mOption==15 || mOption==16){
                    op=oType.Recover;
                }
                else if(mOption==9){
                    op=oType.Check;
                }
                else if(mOption==10){
                    op=oType.Unmute;
                }
                else{
                    op=oType.Convert;
                }
                //Repeat option prompt:
                agane=Prompts.repeat(vt, op);
            }
            goAgane=Prompts.goAgane();
        }
        sc.close();
        Prompts.exitMessage();
    }
}
