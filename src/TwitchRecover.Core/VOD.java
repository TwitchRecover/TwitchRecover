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

import java.io.IOException;
import java.util.ArrayList;

/**
 * The VOD object holds
 * all of the elements and
 * methods necessary to process
 * a VOD.
 */
public class VOD {
    private boolean subOnly;                    //Boolean value representing whether or not a VOD is sub-only.
    private boolean isDeleted;                  //Boolean value representing whether or not a VOD is still up.
    private Feeds feeds;                        //Feeds object corresponding to the VOD.
    private FileExtension fe;                   //Desired output file extension.
    private long VODID;                         //VOD ID of a VOD if it is still up.
    private String[] vodInfo;                   //String array containing the VOD info such as streamer, timestamp, etc.
    //0: Channel name; 1: Stream ID; 2. Timestamp of the start of the stream; 3: Brute force boolean.
    private ArrayList<String> retrievedURLs;    //Arraylist containing all of the VOD 'chunked' M3U8s of a particular VOD.
    private String fp;                          //String value represnting the file path of the output file.
    private String fn;                          //String value representing the file name of the output file.
    private String fFP;                         //String value which represents the final file path of the downloaded object.

    /**
     * The constructor of a
     * VOD object which initialises
     * two boolean values based on given inputs
     * and if necessary initialises the vodInfo
     * string array.
     * @param subOnly       Boolean value representing whether or not the VOD is a sub-only VOD.
     * @param isDeleted     Boolean value representing whether or not the VOD has being deleted or not.
     */
    public VOD(boolean subOnly, boolean isDeleted){
        this.subOnly=subOnly;
        this.isDeleted=isDeleted;
        if(isDeleted){
            vodInfo=new String[4];
        }
    }

    /**
     * This method processes the downloading of a
     * VOD.
     * @param fe    FileExtension enum representing the desired output file extension.
     * @param feed  String value representing the desired feed to download.
     */
    public void downloadVOD(FileExtension fe, String feed){
        computeFN();
        if(vodInfo==null){
            getVODFeeds();
        }
        else{
            retrieveVOD();
            retrieveVODFeeds();
        }
        if(fe==FileExtension.TS || fe==FileExtension.MPEG){
            fFP=fp+fn+fe.fileExtension;
            try {
                Download.m3u8Download(feed, fFP);
            }
            catch (IOException ignored){}
        }
        else{
            fFP=fp+fn+fe.fileExtension;
            try{
                Download.m3u8Download(feed, fp+fn+"-TEMP"+fe.fileExtension);
            }
            catch(Exception ignored){}
            //TODO: Insert converter method call.
        }
    }

    /**
     * This method exports the results
     * of the retrieved URLs.
     */
    public void exportResults(){
        computeFN();
        fFP=fp+fn+FileExtension.TXT.fileExtension;
        FileIO.exportResults(retrievedURLs, fFP);
    }

    /**
     * This method exports the feeds
     * object of the object class.
     */
    public void exportFeed(){
        computeFN();
        fFP=fp+fn+FileExtension.TXT.fileExtension;
        FileIO.exportFeeds(feeds, fFP);
    }

    /**
     * This method gets an arraylist
     * of chunked (source quality)
     * VOD feeds from given information.
     * @return ArrayList<String>    String arraylist containing all of the source VOD feeds.
     */
    public ArrayList<String> retrieveVOD(){
        retrievedURLs=VODRetrieval.retrieveVOD(vodInfo[0], vodInfo[1], vodInfo[2], Boolean.parseBoolean(vodInfo[3]));
        return retrievedURLs;
    }

    /**
     * This method retrieves the list of
     * all possible feeds for a deleted VOD.
     * @return Feeds    Feeds object containing all possible feeds of a deleted VOD.
     */
    public Feeds retrieveVODFeeds(){
        feeds=VODRetrieval.retrieveVODFeeds(retrievedURLs.get(0));
        return feeds;
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
        if(!subOnly){
            feeds= VideoAPI.getVODFeeds(VODID);
        }
        else{
            feeds= VideoAPI.getSubVODFeeds(VODID);
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
        return feeds.getFeed(id-1);
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
     * @param info      String array containing the information about the VOD.
     */
    public void setVODInfo(String[] info){
        vodInfo=info;
    }

    /**
     * Mutator for the channel name
     * value of the vodInfo array.
     * @param channel   String value representing the channel the VOD is from.
     */
    public void setChannel(String channel){
        vodInfo[0]=channel;
    }

    /**
     * Mutator for the stream ID
     * value of the vodInfo array.
     * @param streamID  String value representing the stream ID of the stream of the VOD.
     */
    public void setStreamID(String streamID){
        vodInfo[1]=streamID;
    }

    /**
     * Mutator for the timestamp
     * value of the vodInfo array.
     * @param timestamp     String value representing the timestamp of the start of the VOD in 'YYYY-MM-DD HH:mm:ss' format.
     */
    public void setTimestamp(String timestamp){
        vodInfo[2]=timestamp;
    }

    /**
     * Mutator for the brute force
     * value of the vodInfo array.
     * @param bf    Boolean value representing whether or not the VOD start timestamp is to the second or to the minute.
     */
    public void setBF(boolean bf){
        vodInfo[3]=String.valueOf(bf);
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

    /**
     * This method computes the file name
     * based on what info was provided.
     */
    private void computeFN(){
        if(vodInfo==null){
            fn=FileIO.computeFN(ContentType.VOD, String.valueOf(VODID));
        }
        else{
            fn=FileIO.computeFN(ContentType.VOD, vodInfo[1]);
        }
    }
}