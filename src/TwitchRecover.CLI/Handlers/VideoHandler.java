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
import TwitchRecover.CLI.Prompts;
import TwitchRecover.Core.Downloader.Download;
import TwitchRecover.Core.Enums.ContentType;
import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.FileIO;
import TwitchRecover.Core.VODRetrieval;

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
            checkMute();
        }
        else if(option==10){
            unmute();
        }
        else if(option==11){
            downloadM3U8();
        }
        else{
            convert();
        }
    }

    /**
     * This method processes the
     * checking for muted segments
     * of a specific M3U8.
     */
    private void checkMute(){
        System.out.print("\nMuted segments check:");
        System.out.print("\nPlease enter the link of the M3U8 to check: ");
        String url=CLIHandler.sc.next();
        if(VODRetrieval.hasMuted(url)){
            System.out.print("\nM3U8 contains muted segments.");
        }
        else{
            System.out.print("\nM3U8 DOES NOT contain any muted segments.");
        }
    }

    /**
     * This method processes the
     * unmuting of a video.
     */
    private void unmute(){
        System.out.print(
                  "\nM3U8 unmuting:"
                + "\nDisclaimer: This 'unmuting' process allows you to watch the muted segments of an M3U8 but does not allow you to HEAR the audio from those segments."
                + "\n1. Insert URL."
                + "\n2. Insert file."
        );
        int input= Prompts.getIntInput(1,2);
        boolean isFile;
        String value;
        if(input==1){
            isFile=false;
            System.out.print("\nPlease input the URL of the M3U8 to unmute: ");
            value=CLIHandler.sc.next();
        }
        else{
            isFile=true;
            System.out.print("\nPlease input the complete file path of the M3U8 to unmute: ");
            value=CLIHandler.sc.next();
        }
        System.out.print(
                  "\nPlease enter the FILE PATH of where you want the unmuted M3U8 saved:"
                + "\nFile path: "
        );
        String fp=FileIO.adjustFP(CLIHandler.sc.next())+FileIO.computeFN(ContentType.M3U8, String.valueOf((int) (Math.random()*1000000)))+FileExtension.M3U8.getFE();
        System.out.print("\n'Unmuting'...");
        VODRetrieval.unmute(value, isFile, fp);
        System.out.print("\nUnmuted file at: "+fp);
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
                  "\nPlease enter the FILE PATH of where you want the M3U8 saved:"
                + "\nFile path: "
        );
        String fp= FileIO.adjustFP(CLIHandler.sc.next());
        String fn=FileIO.computeFN(ContentType.Video, String.valueOf((int) (Math.random()*1000000)));
        FileExtension fe=CoreHandler.userFE();
        String fFP=fp+fn+fe.getFE();
        System.out.print("\nDownloading...");
        try {
            Download.m3u8Download(url, fFP);
        }
        catch(IOException ignored){}
        System.out.print("\nFile downloaded at: "+fFP);
    }

    /**
     * This method processes the
     * conversion of a TS video file
     * to an MP4 or other video format.
     */
    private void convert(){
        System.out.print("\nTS file conversion:");
        System.out.print("\nPlease enter the complete file path of the TS file to convert: ");
        String fp=CLIHandler.sc.next();
        FileExtension fe=CoreHandler.userFE();
        String fFP=FileIO.convert(fp, fe);
        System.out.print("\nFile converted, new file located at: "+fFP);
    }
}