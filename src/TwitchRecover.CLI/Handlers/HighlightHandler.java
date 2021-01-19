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
 *  @version 2.0b
 *  Github project home page: https://github.com/TwitchRecover
 *  Twitch Recover repository: https://github.com/TwitchRecover/TwitchRecover
 */

package TwitchRecover.CLI.Handlers;

import TwitchRecover.CLI.CLIHandler;
import TwitchRecover.CLI.Enums.oType;
import TwitchRecover.CLI.Enums.vType;
import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.Feeds;
import TwitchRecover.Core.Highlights;

/**
 * HighlightHandler object class which
 * handles the highlight prompts.
 */
public class HighlightHandler {
    private int option;     //Integer value representing the user's selected option.

    /**
     * Constructor and main handler
     * of the HighlightHandler object class.
     * @param option    Integer value that represents the user's selected option.
     */
    public HighlightHandler(int option){
        this.option=option;
        if(option==6){
            retrieve();
        }
        else if(option==7){
            download();
        }
        else{
            recover();
        }
    }

    private void retrieve(){
        System.out.print("\n\nHighlight URL retrieval:");
        String highturl=CoreHandler.promptURL(oType.Retrieve, vType.Highlight);
        Highlights highlights=new Highlights(false);
        int quality=retrieveQuality(highturl, highlights);
        System.out.print("\nResult: "+highlights.getFeed(quality));
    }

    /**
     * This method processes the downloading of a
     * highlight.
     */
    private void download(){
        System.out.print("\n\nHighlight downloading:");
        String highlightURL=CoreHandler.promptURL(oType.Download, vType.Highlight);
        Highlights highlight=new Highlights(false);
        highlight.retrieveID(highlightURL);
        Feeds feeds=highlight.getHighlightFeeds();
        int quality=CoreHandler.selectFeeds(feeds, oType.Download);
        FileExtension fe=CoreHandler.userFE();
        System.out.print(
                  "\nPlease enter the FILE PATH of where you want the highlight saved:"
                + "\nFile path: "
        );
        highlight.setFP(CLIHandler.sc.next());
        highlight.downloadHighlight(fe, feeds.getFeed(quality-1));
        System.out.print("\nFile downloaded at: " + highlight.getFFP());
    }

    private int retrieveQuality(String url, Highlights highlight){
        highlight.retrieveID(url);
        Feeds feeds=highlight.getHighlightFeeds();
        return CoreHandler.selectFeeds(feeds, oType.Retrieve);
    }

    /**
     * This method processes and handles
     * the recovery of a highlight.
     */
    private void recover(){
        Highlights highlight=new Highlights(true);
        System.out.print(
                  "\n\nHighlight recovery:"
                + "\nPlease enter the exact details about the highlight in order to retrieve it:"
                + "\nChannel name: "
        );
        highlight.setChannel(CLIHandler.sc.next());
        System.out.print("\nStream ID: ");
        highlight.setStreamID(CLIHandler.sc.next());
        System.out.print("\nHighlight ID: ");
        highlight.setID(Long.parseLong(CLIHandler.sc.next()));
        System.out.print("\nTimestamp (YYYY-MM-DD HH:mm:ss format): ");
        highlight.setTimestamp(CLIHandler.sc.next()+" "+CLIHandler.sc.next());
        highlight.retrieveHighlights();
        int quality=CoreHandler.selectFeeds(highlight.retrieveQualities(), oType.Recover);
        System.out.print("\n\nM3U8 link: "+highlight.getFeed(quality));
    }
}