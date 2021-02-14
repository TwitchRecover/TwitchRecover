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

package TwitchRecover.Core;

import TwitchRecover.Core.API.VideoAPI;
import TwitchRecover.Core.Downloader.Download;
import TwitchRecover.Core.Enums.ContentType;
import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.Enums.VideoType;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String fFP;                         //String value which represents the final file path of the downloaded object.

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
        System.out.print("\nDownloading highlight...");
        fFP=fp+fn+fe.getFE();
        try{
            Download.m3u8Download(feed, fFP);
        }
        catch(Exception ignored){}
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
        url+="highlight-"+highlightID+FileExtension.M3U8.getFE();
        retrievedURLs=Fuzz.verifyURL(url);
        return retrievedURLs;
    }

    /**
     * This method retrieves
     * all existing qualities
     * of a highlight.
     * @return Feeds    Feeds object containing all of the feeds and their corresponding qualities that could be found.
     */
    public Feeds retrieveQualities(){
        String part1=null;
        String part2=null;
        String pattern="([vod\\-secure.twitch.tv]*|[vod\\-metro.twitch.tv]*|[[0-9a-zA-Z]*.cloudfront.net]*)(\\/[a-zA-Z0-9_]*\\/)chunked(\\/highlight-[0-9]*\\.m3u8)";
        Pattern r= Pattern.compile(pattern);
        Matcher m=r.matcher(retrievedURLs.get(0));
        if(m.find()){
            part1=m.group(1)+m.group(2);
            part2=m.group(3);
        }
        feeds=Fuzz.fuzzQualities("https://"+part1, part2);
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
     * This method exports the results
     * of the retrieved URLs.
     */
    public void exportResults(){
        computeFN();
        fFP=fp+fn+FileExtension.TXT.getFE();
        FileIO.exportResults(retrievedURLs, fFP);
    }

    /**
     * This method exports the feeds
     * object of the object class.
     */
    public void exportFeed(){
        computeFN();
        fFP=fp+fn+FileExtension.TXT.getFE();
        FileIO.exportFeeds(feeds, fFP);
    }

    /**
     * This method gets the corresponding
     * Feeds object to a given highlight ID.
     * @return Feeds    Feeds object corresponding to the highlight.
     */


    public Feeds getHighlightFeeds(){
        feeds=VideoAPI.getVODFeeds(highlightID);
        if(feeds.getFeeds().isEmpty()){
            feeds=VideoAPI.getSubVODFeeds(highlightID, VideoType.HIGHLIGHT);
        }
        return feeds;
    }

    /**
     * Accessor for a particular
     * feed from its incremental ID.
     * @param id        Integer value representing the incremental ID of the feed in question.
     * @return String   String value representing the corresponding feed URL.
     */
    public String getFeed(int id){
        return feeds.getFeed(id-1);
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
     * Accessor for the fFP variable.
     * @return String   String value representing the final and complete file path of the outputted object.
     */
    public String getFFP(){
        return fFP;
    }

    /**
     * Accessor for the highlight ID value.
     * @return Long     Long value representing the highlight ID.
     */
    public long getHighlightID() {
        return highlightID;
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
     * @param streamID   String value representing the stream unique ID.
     */
    public void setStreamID(String streamID){
        highlightInfo[1]=streamID;
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
     * This method retrieves the highlight
     * ID from a given Twitch highlight URL.
     * @param url   String value representing the Twitch highlight URL.
     */
    public void retrieveID(String url){
        highlightID=VODRetrieval.retrieveID(url);
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