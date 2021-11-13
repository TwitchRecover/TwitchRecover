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

package client.Handlers;

import client.CLIHandler;
import client.ClipBoard;
import core.Feeds;
import core.Live;

/**
 * StreamHandler object class which
 * handles stream prompts;
 */
public class StreamHandler {
    private ClipBoard clipboard = new ClipBoard();

    /**
     * Constructor and main method
     * of the StreamHandler
     * object class.
     */
    public StreamHandler() {
        retrieve();
    }

    /**
     * This method prompts and handles
     * the retrieval of live stream links.
     */
    private void retrieve() {
        Live live = new Live();
        System.out.print(
                  "\n\nLive stream link retrieval:"
                + "\nEnter the channel name: "
        );
        String response = CLIHandler.sc.next();
        live.setChannel(response.toLowerCase());
        Feeds feeds = live.retrieveFeeds();
        if(feeds.getFeeds().isEmpty()){
            System.out.print(
                    "\nERROR!"
                    + "\nUnable to retrieve feeds."
                    + "\nPlease make sure that the stream is live right now.\n"
            );
            return;
        }

        int quality = CoreHandler.selectFeeds(feeds);
        String streamlink = live.getFeed(quality);
        System.out.println("\nM3U8 URL: " + streamlink);
        clipboard.copyText(streamlink);
        System.out.println("\nLink is copied to clipboard");
    }
}
