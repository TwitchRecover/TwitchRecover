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
import TwitchRecover.Core.Enums.BruteForce;
import TwitchRecover.Core.Enums.ContentType;
import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.Enums.VideoType;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The VOD object holds
 * all of the elements and
 * methods necessary to process
 * a VOD.
 */
public class VOD {
    private boolean isDeleted;                  //Boolean value representing whether or not a VOD is still up.
    private Feeds feeds;                        //Feeds object corresponding to the VOD.
    private FileExtension fe;                   //Desired output file extension.
    private long VODID;                         //VOD ID of a VOD if it is still up.
    private VODInfo vodInfo;                    //VODInfo object which contains all of the VOD's information.
    private ArrayList<String> retrievedURLs;    //Arraylist containing all of the VOD 'chunked' M3U8s of a particular VOD.
    private String fp;                          //String value representing the file path of the output file.
    private String fn;                          //String value representing the file name of the output file.
    private String fFP;                         //String value which represents the final file path of the downloaded object.

    /**
     * The constructor of a
     * VOD object which initialises
     * two boolean values based on given inputs
     * and if necessary initialises the vodInfo
     * string array.
     * @param isDeleted     Boolean value representing whether or not the VOD has being deleted or not.
     */
    public VOD(boolean isDeleted){
        this.isDeleted=isDeleted;
        if(isDeleted){
            vodInfo=new VODInfo();
        }
    }

    /**
     * This method processes the downloading of a
     * VOD.
     * @param fe    FileExtension enum representing the desired output file extension.
     * @param feed  String value representing the desired feed to download.
     */
    public void downloadVOD(FileExtension fe, String feed, long VODID){
        computeFN();
        if(vodInfo.getName()==null){
            getVODFeeds();
        }
        else{
            vodInfo.setBF(BruteForce.None);
            retrieveVOD();
            retrieveVODFeeds();
        }
        fFP=fp+fn+fe.getFE();
        if(fe==FileExtension.MP4){
            String mp4URL=VideoAPI.getMP4URL(VODID);
            if(!mp4URL.equals("")){
                feed=mp4URL;
            }
        }
        try {
            Download.m3u8Download(feed, fFP);
        }
        catch (IOException ignored){}
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
     * This method gets an arraylist
     * of chunked (source quality)
     * VOD feeds from given information.
     * @return ArrayList<String>    String arraylist containing all of the source VOD feeds.
     */
    public ArrayList<String> retrieveVOD(){
        retrievedURLs=VODRetrieval.retrieveVOD(vodInfo.getName(), vodInfo.getID(), vodInfo.getTS(), vodInfo.getBF());
        return retrievedURLs;
    }

    /**
     * This method retrieves the list of
     * all possible feeds for a deleted VOD.
     * @return Feeds    Feeds object containing all possible feeds of a deleted VOD.
     */
    public Feeds retrieveVODFeeds(){
        if(retrievedURLs.isEmpty()){
            return null;
        }
        else{
            feeds=VODRetrieval.retrieveVODFeeds(retrievedURLs.get(0));
            return feeds;
        }
    }

    /**
     * This method uses a website analytics
     * link to get all the values of
     * the vodInfo array.
     * @param url   String value representing a website analytics URL.
     */
    public void retrieveVODURL(String url){
        vodInfo=WebsiteRetrieval.getData(url);
    }

    /**
     * This method gets the corresponding
     * Feeds object to a given VOD ID.
     * @return Feeds    Feeds object corresponding to the VOD of the VOD ID.
     */
    public Feeds getVODFeeds(){
        feeds=VideoAPI.getVODFeeds(VODID);
        if(feeds.getFeeds().isEmpty()){
            feeds=VideoAPI.getSubVODFeeds(VODID, VideoAPI.getVideoType(VODID,VideoAPI.getInfo(VODID).getName()));
        }
        return feeds;
    }

    /**
     * Accessor for a single particular
     * feed of the Feeds object based
     * on a given integer ID.
     * @param id        Integer value representing the list value of the feed to fetch.
     * @return String   Feed URL corresponding to the given ID.
     */
    public String getFeed(int id){
        return feeds.getFeed(id);
    }

    /**
     * Accessor for the Feeds
     * object of the VOD object.
     * @return Feeds    Feeds object of the VOD object.
     */
    public Feeds getFeeds(){
        return feeds;
    }

    /**
     * Accessor for the fFP variable.
     * @return String   String value representing the final file path of the outputted object.
     */
    public String getFFP(){
        return fFP;
    }

    /**
     * Accessor for the retrievedURLs
     * arraylist.
     * @return ArrayList<String>    The retrievedURLs string arraylist.
     */
    public ArrayList<String> getRetrievedURLs(){
        return retrievedURLs;
    }

    /**
     * Accessor for the vodInfo object.
     * @return VODInfo      VODInfo object containing the information of the VOD.
     */
    public VODInfo getVodInfo(){
        return vodInfo;
    }

    /**
     * Mutator for the
     * VODID variable.
     * @param VODID     Long value which represents the VODID of the VOD.
     */
    public void setID(long VODID){
        this.VODID=VODID;
    }

    /**
     * Mutator for the file extension
     * enum which represents the user's
     * desired file output format.
     * @param fe    A FileExtensions enum which represents the user's desired output file extension.
     */
    public void setFE(FileExtension fe){
        this.fe=fe;
    }

    /**
     * Mutator for the VOD info
     * string array which contains
     * all of the information about a
     * VOD in order to compute the base URL.
     * @param info      VODInfo object containing the information of the VOD.
     */
    public void setVODInfo(VODInfo info){
        vodInfo=info;
    }

    /**
     * Mutator for the channel name
     * value of the vodInfo array.
     * @param channel   String value representing the channel the VOD is from.
     */
    public void setChannel(String channel){
        vodInfo.setName(channel);
    }

    /**
     * Mutator for the stream ID
     * value of the vodInfo array.
     * @param streamID  String value representing the stream ID of the stream of the VOD.
     */
    public void setStreamID(String streamID){
        vodInfo.setIDS(streamID);
    }

    /**
     * Mutator for the timestamp
     * value of the vodInfo array.
     * @param timestamp     String value representing the timestamp of the start of the VOD in 'YYYY-MM-DD HH:mm:ss' format.
     */
    public void setTimestamp(String timestamp){
        vodInfo.setTimestamp(timestamp);
    }

    /**
     * Mutator for the brute force
     * value of the vodInfo array.
     * @param bf    Brute force enum value which represents the brute force type to be applied to the VOD recovery.
     */
    public void setBF(BruteForce bf){
        vodInfo.setBF(bf);
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

    public void retrieveID(String url){
        VODID=VODRetrieval.retrieveID(url);
    }

    /**
     * This method computes the file name
     * based on what info was provided.
     */
    private void computeFN(){
        if(vodInfo==null){
            fn=FileIO.computeFN(ContentType.VOD, String.valueOf(VODID));
        }
        else{
            fn=FileIO.computeFN(ContentType.VOD, String.valueOf(vodInfo.getID()));
        }
    }
}