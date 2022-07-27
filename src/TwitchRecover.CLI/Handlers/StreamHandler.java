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
import TwitchRecover.Core.Feeds;
import TwitchRecover.Core.Live;
/**
 * StreamHandler object class which
 * handles stream prompts;
 */
public class StreamHandler {

    /**
     * Constructor and main method
     * of the StreamHandler
     * object class.
     * @param option    Integer value representing the user's selected option.
     */
    public StreamHandler(int option){
        //Integer value which represents the user's selected option.
        if(option==1){
            retrieve();
        }
        else{
            System.out.print("\n\nThis feature is currently unavailable.\nIt will be released in the 2.0 beta release.");
            //download();
        }
    }

    /**
     * This method prompts and handles
     * the retrieval of live stream links.
     */
    private void retrieve(){
        Live live=new Live();
        System.out.print(
                """


                        Live stream link retrieval:
                        Enter the channel name:\s"""
        );
        String response=CLIHandler.sc.next();
        live.setChannel(response.toLowerCase());
        Feeds feeds=live.retrieveFeeds();
        if(feeds.getFeeds().isEmpty()){
            System.out.print(
                    """

                            ERROR!
                            Unable to retrieve feeds.
                            Please make sure that the stream is live right now."""
            );
        }
        else{
            int quality=CoreHandler.selectFeeds(feeds, oType.Retrieve);
            System.out.print("M3U8 URL: "+live.getFeed(quality));
        }

    }

    /**
     * This method prompts and
     * handles the downloading of
     * a live stream.
     */
    private void download(){
        Live live=new Live();
        System.out.print(
                """


                        Live stream downloading:
                        Enter the channel name:\s"""
        );
        live.setChannel(CLIHandler.sc.next());
        Feeds feeds=live.retrieveFeeds();
        if(feeds.getFeeds().isEmpty()){
            System.out.print(
                    """

                            ERROR!
                            Unable to retrieve feeds.
                            Please make sure that the stream is live right now."""
            );
        }
        else{
            int quality=CoreHandler.selectFeeds(feeds, oType.Download);
            System.out.print("\nDownloading stream...");
            live.download(feeds.getFeed(quality));
        }
    }
}