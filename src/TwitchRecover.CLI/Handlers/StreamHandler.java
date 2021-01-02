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

package TwitchRecover.CLI.Handlers;

import TwitchRecover.Core.Feeds;
import TwitchRecover.Core.Live;

import java.util.Scanner;
/**
 * StreamHandler object class which
 * handles stream prompts;
 */
public class StreamHandler {
    private int option;     //Integer value which represents the user's selected option.

    /**
     * Constructor and main method
     * of the StreamHandler
     * object class.
     * @param option    Integer value representing the user's selected option.
     */
    public StreamHandler(int option){
        this.option=option;
        if(option==1){
            retrieve();
        }
        else{
            download();
        }
    }

    /**
     * This method prompts and handles
     * the retrieval of live stream links.
     */
    private void retrieve(){
        Scanner sc=new Scanner(System.in);
        Live live=new Live();
        System.out.print(
                  "\n\nLive stream link retrieval:"
                + "\nEnter the channel name: "
        );
        live.setChannel(sc.nextLine());
        sc.close();
        Feeds feeds=live.retrieveFeeds();
        int quality=CoreHandler.selectFeeds(feeds);
        System.out.print("M3U8 URL: "+live.getFeed(quality));
    }

    /**
     * This method prompts and
     * handles the downloading of
     * a live stream.
     */
    private void download(){
        Scanner sc=new Scanner(System.in);
        Live live=new Live();
        System.out.print(
                  "\n\nLive stream downloading:"
                + "\nEnter the channel name: "
        );
        live.setChannel(sc.nextLine());
        sc.close();
        Feeds feeds=live.retrieveFeeds();
        int quality=CoreHandler.selectFeeds(feeds);
        System.out.print("\nDownloading stream...");
        live.download(feeds.getFeed(quality));
    }
}