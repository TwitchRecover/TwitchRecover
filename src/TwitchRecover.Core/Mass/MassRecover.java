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

package TwitchRecover.Core.Mass;

import TwitchRecover.Core.FileIO;
import TwitchRecover.Core.Compute;
import TwitchRecover.Core.VOD;
import TwitchRecover.Core.Clips;
import TwitchRecover.Core.Live;
import TwitchRecover.Core.WebsiteRetrieval;
import TwitchRecover.Core.Enums.ContentType;
import TwitchRecover.Core.Enums.FileExtension;
import java.util.ArrayList;

/**
 * This object class handles all
 * of the mass recovery options.
 */
public class MassRecover {
    private ArrayList<String> read;         //String arraylist containing all of the lines of the file path to read.
    private ContentType ct;                 //Content type of the mass recovery.
    private ArrayList<String> results;      //String arraylist containing all of the results to be printed.
    private String rFP;                     //String value representing the complete file path of the file to read.
    private String fp;                      //String value representing the file path of the file to read.
    private String fn;                      //File name of the output file containing the results.
    private String fFP;                     //String value which represents the final file path of the downloaded object.

    /**
     * Constructor of the
     * MassRecover object
     * which instantiates
     * the object.
     */
    public MassRecover(){
    }

    /**
     * This method reads a file
     * and parses the response to
     * remove any comment lines.
     */
    private void read(){
        read=MassCore.parseRead(FileIO.read(fp));
    }

    /**
     * This method exports the results
     * of the mass recovery to a
     * desired file path.
     */
    private void exportResults(){
        computeFN();
        fFP=fp+fn+ FileExtension.TXT;
        FileIO.exportResults(results, fFP);
    }

    /**
     * Core method for processing
     * the recovery of content
     * which reads the file,
     * processes the inputs and
     * exports the results.
     */
    public void process(){
        read();
        if(ct==ContentType.Clip){
            processClips();
        }
        else if(ct==ContentType.VOD){
            processVODs();
        }
        else{
            processStreams();
        }
        exportResults();
    }

    /**
     * This method handles
     * the recovery of a
     * series of VOD feeds.
     */
    private void processVODs(){
        for(String line: read){
            processVOD(line);   //TODO: Make asynchronous.
        }
    }

    /**
     * This method processes
     * the recovery for individual
     * VOD links.
     * @param url   String value representing a stream link of a stream from a supported analytic website.
     */
    private void processVOD(String url){
        VOD vod=new VOD(false, true);
        vod.setVODInfo(WebsiteRetrieval.getData(url));
        results.add(vod.retrieveVOD().get(0));
    }

    /**
     * This method processes
     * the retrieval of a set
     * of M3U8 streams from
     * given channel names.
     */
    private void processStreams(){
        for(String line: read){
            processStream(line);    //TODO: Make asynchronous.
        }
    }

    /**
     * Method which individually processes
     * the retrieval of the M3U8 URL of a
     * stream.
     * @param url   String value representing the channel name or channel URL of the channel to get the M3U8 for.
     */
    private void processStream(String url){
        Live live=new Live();
        if(url.contains("twitch.tv/")){
            live.setChannel(Compute.singleRegex("twitch.tv/([a-z0-9A-Z]*)", url));
        }
        else{
            live.setChannel(url);
        }
        live.retrieveFeeds();
        results.add(live.getFeed(0));
    }

    /**
     * This method processes
     * the retrieval of the
     * permanent link of a
     * series of clip links
     * or clip slugs.
     */
    private void processClips(){
        for(String line: read){
            processClip(line);  //TODO: Make asynchronous.
        }
    }

    /**
     * This method processes the
     * retrieval of the permanent
     * link of a clip link.
     * @param url   String value representing the clip link or slug to retrieve.
     */
    private void processClip(String url){
        Clips clip=new Clips();
        results.add(clip.retrieveURL(url));
    }

    /**
     * This method computes
     * the file name for the
     * output file.
     */
    private void computeFN(){
        fn=FileIO.computeFN(ct, "Mass-"+Math.random()*1000);    //TODO: Use the Java M3U8 downloader method call for random long value, should be a long.
    }

    /**
     * Accessor for the fFP variable.
     * @return String   String value representing the final complete file path of the output file.
     */
    public String getFFP(){
        return fFP;
    }

    /**
     * Mutator for the fp variable.
     * @param fp    String value representing the file path where to place the output file.
     */
    public void setFP(String fp){
        this.fp=FileIO.adjustFP(fp);
    }
}