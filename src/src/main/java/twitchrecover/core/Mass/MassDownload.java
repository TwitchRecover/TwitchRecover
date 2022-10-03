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

package twitchrecover.core.Mass;

import twitchrecover.core.Clips;
import twitchrecover.core.Downloader.Download;
import twitchrecover.core.Enums.ContentType;
import twitchrecover.core.Enums.FileExtension;
import twitchrecover.core.FileIO;
import twitchrecover.core.Highlights;
import twitchrecover.core.VOD;

import java.util.ArrayList;
/**
 * This object class handles all of the
 * mass download operations.
 */
public class MassDownload {
    private ArrayList<String> read;         //String arraylist containing all of the lines of the file path to read.
    private ContentType ct;                 //Content type of the mass download.
    private String rFP;                     //String value representing the complete file path of the file to read.
    private String fp;                      //String value representing the file path of the file to read.
    private FileExtension fe;               //FileExtension enum representing the user's desired output file extension.

    /**
     * Constructor of the MassDownload
     * object which instantiates the object.
     */
    public MassDownload(){
    }

    /**
     * This is the core downloader
     * method for mass downloads objects.
     */
    public void download(){
        read();
        if(ct==ContentType.Clip){
            downloadClips();
        }
        else if(ct==ContentType.Highlight){
            downloadHighlights();
        }
        else if(ct==ContentType.VOD){
            downloadVODs();
        }
    }

    /**
     * This method reads a file and
     * parses the response.
     */
    private void read(){
        read=FileIO.parseRead(FileIO.read(rFP));
    }

    /**
     * This method downloads a series
     * of VODs.
     */
    private void downloadVODs(){
        for(String line: read){
            Thread thread=new Thread(() ->{
               downloadVOD(line);
            });
            thread.start();
        }
    }

    /**
     * This method downloads a VOD
     * from a given feed URL.
     * @param url   String value representing the feed URL to download.
     */
    private void downloadVOD(String url){
        VOD vod=new VOD(false);
        vod.setFP(fp);
        vod.retrieveID(url);
        String feed=vod.getFeed(1);
        vod.downloadVOD(fe, feed);
    }

    /**
     * This method downloads a series
     * of highlights.
     */
    private void downloadHighlights(){
        for(String line: read){
            Thread thread=new Thread(() ->{
                downloadHighlight(line);
            });
            thread.start();
        }
    }

    /**
     * This method downloads a highlight.
     * @param url   String value representing the Twitch URL of the highlight to download.
     */
    private void downloadHighlight(String url){
        Highlights highlight=new Highlights(false);
        highlight.retrieveID(url);
        highlight.setFP(fp);
        String feed= highlight.getFeed(1);
        highlight.downloadHighlight(fe, feed);
    }

    /**
     * This method downloads a
     * series of clips.
     */
    private void downloadClips(){
        for(String line: read){
            Thread thread=new Thread(() ->{
                downloadClip(line);
            });
            thread.start();
        }
    }

    /**
     * This method downloads an individual
     * clip from either the MP4 link or the
     * Twitch clip link.
     * @param url   String value representing either the Twitch clip link or the MP4 link.
     */
    private void downloadClip(String url){
        Clips clip=new Clips();
        clip.setFP(fp);
        if(url.lastIndexOf(".mp4")==url.length()-5){
            clip.setSlug(String.valueOf(Math.random()*1000));
            String ffp=clip.getDFP();
            try {
                Download.download(url, ffp);
            }
            catch(Exception ignored){}
        }
        else{
            clip.setSlug(url);
            clip.download();
        }
    }

    /**
     * Mutator for the fe variable.
     * @param fe    FileExtension enum which represents the user's desired file output file extension.
     */
    public void setFE(FileExtension fe){
        this.fe=fe;
    }
}