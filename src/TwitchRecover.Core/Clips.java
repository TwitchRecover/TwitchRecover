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

/*
 * @author Daylam Tayari https://github.com/daylamtayari
 * @version 2.0
 * Github project home page: https://github.com/TwitchRecover
 * Twitch Recover repository: https://github.com/TwitchRecover/TwitchRecover
 */

package TwitchRecover.Core;

import TwitchRecover.Core.API.ClipsAPI;
import TwitchRecover.Core.Downloader.Download;
import TwitchRecover.Core.Enums.ContentType;
import TwitchRecover.Core.Enums.FileExtension;

import java.util.ArrayList;

/**
 * The Clips object holds
 * all of the elements and
 * methods necessary to process
 * a Twitch clip.
 */
public class Clips {
    private boolean wfuzz;              //Boolean value representing whether or not the user has wfuzz installed or not.
    private long streamID;              //Long value representing the stream ID to fetch the clips from.
    private long duration;              //Long value representing the duration of the stream.
    private String slug;                //String value representing the slug of the clip.
    private String url;                 //String value representing the permanent URL of the clip.
    private String fp;                  //String value representing the file path of the downloaded clip.
    private String fn;                  //String value representing the file name of the downloaded clip.
    private ArrayList<String> results;  //String arraylist containing all of the fuzz results;
    private String fFP;                 //String value which represents the final file path of the downloaded object.
    private Integer fuzzStart;          //Integer value representing a custom fuzzing start time. If it is null, the start will be 0.
    private Integer fuzzEnd;            //Integer value representing a custom fuzzing end time. If it is null, it will be set to the stream duration.

    /**
     * The constructor of a
     * Clips object which does
     * nothing other than
     * simply instantiate the
     * object.
     */
    public Clips(){
    }

    /**
     * This method downloads a
     * clip.
     */
    public void download(){
        computeFN();
        fFP=fp+fn;
        try{
            Download.download(url, fFP);
        }
        catch(Exception ignored){}
        System.out.print("\nCLip downloaded at: "+fFP+FileExtension.MP4.getFE());
    }

    /**
     * This method downloads a Twitch
     * clip that is still up.
     */
    public void downloadSlug(){
        retrieveURL(slug, true);
        download();
    }

    /**
     * This method recovers all
     * of the clip URLs of a particular
     * stream (fuzzing the possibilities).
     */
    public void recover(){
        if(fuzzEnd==null){
            fuzzEnd=(((int) duration) * 60) + 2000;
        }
        if(fuzzStart==null){
            fuzzStart=0;
        }
        results=Fuzz.fuzz(streamID, fuzzStart, fuzzEnd, wfuzz);
    }

    /**
     * This method exports the results
     * of the retrieved URLs.
     */
    public void exportResults(){
        computeFN();
        fFP=fp+fn+FileExtension.TXT.getFE();
        FileIO.exportResults(results, fFP);
    }

    /**
     * This method retrieves
     * the permanent URL of a
     * clip and saves it to the
     * URL variable.
     * @param url       String value representing the Twitch URL of the clip.
     * @return String   String value representing the permanent URL of the clip.
     */
    public String retrieveURL(String url, boolean download){
        slug=parseSlug(url);
        this.url= ClipsAPI.getClipLink(slug, download);
        return this.url;
    }

    /**
     * This method parses a
     * given clip URL and returns
     * the slug value from it.
     * @param url       String value representing the Twitch URL of the clip.
     * @return String   String value representing the slug of the clip.
     */
    private String parseSlug(String url){
        if(url.contains("clips.twitch.tv")){
            return Compute.singleRegex("clips.twitch.tv/([a-zA-Z0-9-_]*)", url);
        }
        else if(url.contains("twitch.tv/clips")){
            return Compute.singleRegex("twitch.tv/clips/([a-zA-Z0-9-_]*)", url);
        }
        else if(url.contains("twitch.tv/")){
            return Compute.singleRegex("twitch.tv/[a-zA-Z0-9-_]*/clips/(a-zA-Z0-9-_]*)", url);
        }
        else{
            return url;
        }
    }

    /**
     * Accessor for the URL variable.
     * @return String   String value representing the permanent URL of a clip.
     */
    public String getURL(){
        return url;
    }

    /**
     * Accessor for the results arraylist.
     * @return ArrayList<String>    String arraylist containing all of the results of a fuzz.
     */
    public ArrayList<String> getResults(){
        return results;
    }

    /**
     * Accessor for the fFP variable.
     * @return String   String value representing the final file path of the outputted object.
     */
    public String getFFP(){
        return fFP;
    }

    /**
     * This method gets the complete file
     * path for an MP4 clip link to download.
     * @return String   String value representing the complete file path.
     */
    public String getDFP(){
        computeFN();
        return fp+fn+FileExtension.MP4.getFE();
    }

    /**
     * Mutator for the wfuzz boolean.
     * @param wfuzz  Boolean value representing whether or not a user has Wfuzz installed.
     */
    public void setWfuzz(boolean wfuzz){
        this.wfuzz=wfuzz;
    }

    /**
     * Mutator for the streamID variable.
     * @param streamID  Long value representing the stream ID.
     */
    public void setStreamID(long streamID){
        this.streamID=streamID;
    }

    /**
     * Mutator for the duration variable.
     * @param duration  Long value representing the duration of the stream in seconds.
     */
    public void setDuration(long duration){
        this.duration=duration;
    }

    /**
     * This mutator sets the values
     * for both the stream ID
     * and duration values.
     * @param streamID  Long value representing the stream ID of the stream in question.
     * @param duration  Long value representing duration of the stream in seconds.
     */
    public void setValues(long streamID, long duration){
        setStreamID(streamID);
        setDuration(duration);
    }

    /**
     * Mutator for the output file path.
     * @param fp    User inputted output file path.
     */
    public void setFP(String fp){
        this.fp=FileIO.adjustFP(fp);
    }

    /**
     * Mutator for the clip URL variable.
     * @param url   String value representing the MP4 URL of the clip.
     */
    public void setURL(String url){
        this.url=url;
    }

    /**
     * This mutator sets the slug
     * variable from a given
     * Twitch clip URL.
     * @param url   String value representing a Twitch clip URL.
     */
    public void setSlug(String url){
        slug=parseSlug(url);
    }

    /**
     * Mutator for the fuzzStart variable.
     * @param start     Integer value representing a custom fuzzing start time.
     */
    public void setFuzzStart(int start){
        fuzzStart=start;
    }

    /**
     * Mutator for the fuzzEnd variable.
     * @param end   Integer value representing a custom fuzzing end time.
     */
    public void setFuzzEnd(int end){
        fuzzEnd=end;
    }

    /**
     * This method computes
     * the output file name
     * for a downloaded clip.
     */
    private void computeFN(){
        if(slug==null){
            fn=FileIO.computeFN(ContentType.Clip, String.valueOf(streamID));
        }
        else{
            fn= FileIO.computeFN(ContentType.Clip, slug);
        }
    }
}