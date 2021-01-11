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

import TwitchRecover.CLI.CLIHandler;
import TwitchRecover.Core.Downloader.Download;
import TwitchRecover.Core.Enums.ContentType;
import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.FileIO;
import java.io.IOException;

/**
 * VideoHandler object class which
 * handles video prompts.
 */
public class VideoHandler {
    private int option;     //Integer value representing the user's selected option;

    /**
     * Constructor of the VideoHandler
     * object class which handles video prompts.
     * @param option int    Integer value representing the user's selected option.
     */
    public VideoHandler(int option){
        this.option=option;
        if(option==9){
            //checkMute();
        }
        else if(option==10){
            //unmute();
        }
        else if(option==11){
            downloadM3U8();
        }
        else{
            //convert();
        }
    }

    /**
     * This method processes the downloading
     * of an M3U8 URL.
     */
    private void downloadM3U8(){
        System.out.print("\nM3U8 downloading:");
        System.out.print("\nPlease enter the link of the M3U8 file to download: ");
        String url=CLIHandler.sc.next();
        System.out.print(
                  "\nPlease enter the FILE PATH of where you want the VOD saved:"
                + "\nFile path: "
        );
        String fp= FileIO.adjustFP(CLIHandler.sc.next());
        String fn=FileIO.computeFN(ContentType.Video, String.valueOf((int) (Math.random() * 10000000)));
        FileExtension fe=CoreHandler.userFE();
        String fFP=fp+fn+fe.fileExtension;
        System.out.print("\nDowloading...");
        try {
            Download.m3u8Download(url, fFP);
        }
        catch(IOException ignored){}
        System.out.print("\nFile downloaded at: "+fFP);
    }
}