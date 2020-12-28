/*
 * Copyright (c) 2020 Daylam Tayari <daylam@tayari.gg>
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

package TwitchRecover.Core;

import TwitchRecover.Core.API.VideoAPI;
import TwitchRecover.Core.Downloader.Download;
import TwitchRecover.Core.Enums.ContentType;
import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.Enums.Quality;

import java.util.ArrayList;

/**
 * The Highlights object
 * holds all of the elements
 * and methods necessary to
 * process a highlight.
 */
public class Highlights {
    private boolean isDeleted;                  //Boolean value representing whether or not the highlight is still up.
    private Feeds feeds;                        //Feeds object corresponding to the highlight.
    private FileExtension fe;                   //Desired output file extension.
    private long highlightID;                   //Highlight ID of the highlight if it is still up.
    private String[] highlightInfo;             //String array containing the VOD info such as streamer, timestamp, etc.
    //0: Channel name; 1: Stream ID; 2. Timestamp of the start of the stream.
    private ArrayList<String> retrievedURLs;    //Arraylist containing all of the highlight's 'chunked' M3U8s of a particular VOD.
    private String fp;                          //String value represnting the file path of the output file.
    private String fn;                          //String value representing the file name of the output file.

    /**
     * The constructor of the
     * highlights object.
     * @param isDeleted     Boolean value representing whether or not the highlight is deleted.
     */
    public Highlights(boolean isDeleted){
        this.isDeleted=isDeleted;
        if(isDeleted){
            highlightInfo=new String[4];
        }
    }

    /**
     * This method processes
     * the downloading of a
     * highlight.
     * @param fe        FileExtension enum representing the desired output file extension.
     * @param feed      String value representing the M3U8 feed to download.
     */
    public void downloadHighlight(FileExtension fe, String feed){
        computeFN();
        if(highlightInfo==null){
            getHighlightFeeds();
        }
        else{
            retrieveHighlights();
            retrieveHighlightFeeds();
        }
        if(fe==FileExtension.TS || fe==FileExtension.MPEG){
            try{
                Download.m3u8Download(feed, fp+fn+fe.fileExtension);
            }
            catch(Exception ignored){}
        }
        else{
            try{
                Download.m3u8Download(feed, fp+fn+"-TEMP"+fe.fileExtension);
            }
            catch(Exception ignored){}
            //TODO: Insert converter method call.
        }
    }

    /**
     * This method gets an arraylist of
     * chunked highlight M3U8 feeds from
     * given information.
     * @return ArrayList<String>    String arraylist containing all of the source highlight feeds.
     */
    public ArrayList<String> retrieveHighlights(){
        long timestamp=Compute.getUNIX(highlightInfo[2]);
        long streamID=Long.parseLong(highlightInfo[1]);
        String url=Compute.URLCompute(highlightInfo[0], streamID, timestamp);
        //Adapt URL to a highlight M3U8 URL.
        url=url.substring(0, url.indexOf("index-dvr.m3u8"));
        url+="highlight-"+highlightID+FileExtension.M3U8;
        retrievedURLs=Fuzz.verifyURL(url);
        return retrievedURLs;
    }

    /**
     * This method retrieves the list of
     * all possible feeds for a deleted highlight.
     * @return Feeds    Feeds object containing all possible feeds of a deleted highlight.
     */
    public Feeds retrieveHighlightFeeds(){
        String coreURL=Compute.singleRegex("(https:\\/\\/[a-z0-9]*.[a-z_]*.[net||com]*\\/[a-z0-9]*\\/)chunked\\/highlight-[0-9]*.m3u8", retrievedURLs.get(0));
        for(Quality quality: Quality.values()){
            if(Fuzz.checkURL(coreURL+quality.video+"/highlight-"+highlightID+FileExtension.M3U8)){
                feeds.addEntry(coreURL+quality.video+"/highlight-"+highlightID+FileExtension.M3U8, quality);
            }
        }
        return feeds;
    }

    /**
     * This method computes the
     * file name based on the
     * highlight's information.
     */
    private void computeFN(){
        if(highlightInfo==null){
            fn=FileIO.computeFN(ContentType.Highlight, String.valueOf(highlightID));
        }
        else{
            fn=FileIO.computeFN(ContentType.Highlight, highlightInfo[1]);
        }
    }

    /**
     * This method gets the corresponding
     * Feeds object to a given highlight ID.
     * @return Feeds    Feeds object corresponding to the highlight.
     */


    public Feeds getHighlightFeeds(){
        feeds=VideoAPI.getVODFeeds(highlightID);
        return feeds;
    }

    /**
     * Accessor for a particular
     * feed from its incremental ID.
     * @param id        Integer value representing the incremental ID of the feed in question.
     * @return String   String value representing the corresponding feed URL.
     */
    public String getFeed(int id){
        return feeds.getFeed(id);
    }

    /**
     * An accessor for the local
     * Feeds object.
     * @return Feeds    Feeds object of the Highlights object.
     */
    public Feeds getFeeds(){
        return feeds;
    }

    /**
     * Accessor for the retrievedURLs
     * arraylist.
     * @return ArrayList<String>    String arraylist containing all of the retrieved arraylists.
     */
    public ArrayList<String> getRetrievedURLs(){
        return retrievedURLs;
    }

    /**
     * Mutator for the highlight ID variable.
     * @param highlightID   Long value representing the ID of the highlight in question.
     */
    public void setID(long highlightID){
        this.highlightID=highlightID;
    }

    /**
     * Mutator for the fe variable.
     * @param fe    FileExtension enum representing the user's desired output file.
     */
    public void setFE(FileExtension fe){
        this.fe=fe;
    }

    /**
     * Mutator for the entirety
     * of the highlightInfo array.
     * @param info  String array representing the highlight info array.
     */
    public void setHighlightInfo(String[] info){
        highlightInfo=info;
    }

    /**
     * Mutator for the channel name value.
     * @param channel   String value representing the channel name of the highlight.
     */
    public void setChannel(String channel){
        highlightInfo[0]=channel;
    }

    /**
     * Mutator for the highlight unique ID.
     * @param highlightID   String value representing the highlight unique ID.
     */
    public void setHighlightID(String highlightID){
        highlightInfo[1]=highlightID;
    }

    /**
     * Mutator for the timestamp
     * value of the highlightInfo array.
     * @param timestamp     String value representing the timestamp of the start of the VOD in 'YYYY-MM-DD HH:mm:ss' format.
     */
    public void setTimestamp(String timestamp){
        highlightInfo[2]=timestamp;
    }

    /**
     * This method sets the file path
     * by first adjusting the user
     * inputted file path.
     * @param fp    User inputted file path.
     */
    public void setFP(String fp){
        this.fp=FileIO.adjustFP(fp);
    }
}