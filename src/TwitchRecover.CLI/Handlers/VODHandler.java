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

package TwitchRecover.CLI.Handlers;

import TwitchRecover.CLI.CLIHandler;
import TwitchRecover.CLI.Enums.oType;
import TwitchRecover.CLI.Enums.vType;
import TwitchRecover.CLI.Prompts;
import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.Feeds;
import TwitchRecover.Core.VOD;

/**
 * VODHandler object class which
 * handles a VOD prompt.
 */
public class VODHandler {

    public VODHandler(int option){
        //Integer value representing the user's desired option.
        if(option==3){
            retrieve();
        }
        else if(option==4){
            download();
        }
        else{
            recover();
        }
    }

    /**
     * This method process the retrieval
     * of a VOD M3U8 URL.
     */
    private void retrieve(){
        System.out.print("\nVOD URL retrieval:");
        String url=CoreHandler.promptURL(oType.Retrieve, vType.VOD);
        VOD vod=new VOD(false);
        vod.retrieveID(url);
        Feeds feeds=vod.getVODFeeds();
        int quality=CoreHandler.selectFeeds(feeds, oType.Retrieve);
        System.out.print("\nResult: "+vod.getFeed(quality-1));
    }

    /**
     * This method processes the
     * downloading of a VOD.
     */
    private void download(){
        System.out.print("\nVOD downloading:");
        String url=CoreHandler.promptURL(oType.Download, vType.VOD);
        VOD vod=new VOD(false);
        vod.retrieveID(url);
        Feeds feeds=vod.getVODFeeds();
        int quality=CoreHandler.selectFeeds(feeds, oType.Download);
        FileExtension fe=CoreHandler.userFE();
        System.out.print(
                """

                        Please enter the FILE PATH of where you want the VOD saved:
                        File path:\s"""
        );
        vod.setFP(CLIHandler.sc.next());
        System.out.print("\nDownloading...");
        vod.downloadVOD(fe, feeds.getFeed(quality-1));
        System.out.print("\nFile downloaded at: " + vod.getFFP());
    }

    /**
     * This method processes the
     * recovery of a VOD.
     */
    private void recover(){
        VODRecoveryMenu();
        int option= Prompts.getIntInput(1,2);
        VOD vod=new VOD(true);
        boolean wf;
        if(option==1){
            System.out.print("\nPlease enter the stream analytics link (supports Twitch Tracker and Stream Charts): ");
            String url=CLIHandler.sc.next();
            vod.retrieveVODURL(url);
            wf=false;
        }
        else{
            System.out.print("\nPlease enter the channel name: ");
            vod.setChannel(CLIHandler.sc.next());
            System.out.print("\nPlease enter the stream ID: ");
            vod.setStreamID(CLIHandler.sc.next());
            System.out.print("\nPlease enter whether or not you want to brute force to the minute ('y' for yes and 'n' for no): ");
            wf=CLIHandler.sc.next().equalsIgnoreCase("y");
            vod.setBF(wf);
            System.out.print("\nPlease enter the start time of the stream: ");
            vod.setTimestamp(CLIHandler.sc.next());
        }
        vod.retrieveVOD(wf);
        Feeds feeds=vod.retrieveVODFeeds();
        if(feeds==null){
            System.out.print(
                    """

                            NO RESULTS FOUND.
                            The VOD cannot be found on Twitch servers."""
            );
        }
        else{
            int quality=CoreHandler.selectFeeds(feeds, oType.Recover);
            System.out.print("\nResult: "+feeds.getFeed(quality-1));
        }

    }

    /**
     * This method prints the VOD recovery menu
     */
    private static void VODRecoveryMenu(){
        System.out.print(
                """


                        VOD Recovery:
                        To recover a VOD (time limit of maximum 60 days), you can either:
                        1. Use a stream analytics link.
                        (Supports stream links from Twitch Tracker and Stream Charts)
                        2. Input values manually."""
        );
    }
}