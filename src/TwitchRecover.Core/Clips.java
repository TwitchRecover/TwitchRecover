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
        try{
            Download.download(url, fp+fn+FileExtension.MP4);
        }
        catch(Exception ignored){}
    }

    public void recover(){
        results=Fuzz.fuzz(streamID, duration, wfuzz);
    }

    public void exportResults(){
        computeFN();
        results.add(0, "Clip results generated using Twitch Recover - https://github.com/twitchrecover/twitchrecover");
        results.add(1, "Please consider donating if this has been useful for you - https://paypal.me/daylamtayari");
        FileIO.write(results, fp+fn+FileExtension.TXT);
    }

    /**
     * This method retrieves
     * the permanent URL of a
     * clip and saves it to the
     * URL variable.
     * @param url       String value representing the Twitch URL of the clip.
     * @return String   String value representing the permanent URL of the clip.
     */
    public String retrieveURL(String url){
        slug=parseSlug(url);
        this.url= ClipsAPI.getClipLink(slug);
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
        return Compute.singleRegex("clips.twitch.tv\\/([a-zA-Z]*)", url);
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