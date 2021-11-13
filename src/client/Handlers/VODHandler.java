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
import client.Enums.VideoType;
import client.ClipBoard;
import core.Enums.FileExtension;
import core.Feeds;
import core.VOD;

/**
 * VODHandler object class which
 * handles a VOD prompt.
 */
public class VODHandler {
    private ClipBoard clipboard = new ClipBoard();

    public VODHandler() {
        retrieve();
    }

    /**
     * This method process the retrieval
     * of a VOD M3U8 URL.
     */
    private void retrieve() {
        System.out.print("\nVOD URL retrieval:");
        String url = CoreHandler.promptURL(VideoType.VOD);
        VOD vod = new VOD(false);
        vod.retrieveID(url);
        Feeds feeds = vod.getVODFeeds();
        int quality = CoreHandler.selectFeeds(feeds);
        String vodlink = vod.getFeed(quality-1);
        System.out.print("\nLink: " + vodlink);
        clipboard.copyText(vodlink);
        System.out.print("\nLink is copied to clipboard\n");
    }
}
