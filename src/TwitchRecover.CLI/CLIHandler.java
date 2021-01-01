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

package TwitchRecover.CLI;

//import TwitchRecover.Core.Download;
import TwitchRecover.CLI.Enums.oType;
import TwitchRecover.CLI.Enums.vType;
import TwitchRecover.Core.FileIO;

import java.util.ArrayList;

/**
 * This is the handler for the entirety of the CLI
 * version of Twitch Recover.
 */
public class CLIHandler {
    /**
     * Core method of the CLI handler.
     */
    protected static void main(){
        Prompts.welcome();
        boolean goAgane=true;
        while(goAgane){
            int mOption= Prompts.menu();
            if(mOption<3 || mOption==5 || mOption==7){
                videoDownload(mOption);
            }
            else if(mOption==3 || mOption==5){
                videoRecover(mOption);
            }
        }
    }

    /**
     * This method prints the results
     * and also if the user desires,
     * export the results to a file.
     * @param results   String arraylist holding all of the results to be outputted.
     */
    private static void printResults(ArrayList<String> results){
        System.out.print("\n\nResults:\n");
        if(results.size()==0){
            System.out.print("\nNO RESULTS FOUND");
        }
        else{
            for(String s: results){
                System.out.print("\n"+s);
            }
        }
        if(Prompts.repeat(vType.Results, oType.Output)){
            String fp= Prompts.getOutFP(vType.Results)+"TwitchRecover-"+FileIO.fn+".txt";
            results.add(0, "Results generated using Twitch Recover. https://github.com/TwitchRecover/TwitchRecover");
            FileIO.write(results, fp);
        }
    }

    /**
     * This method handles the downloading
     * of videos for the CLI handler.
     * @param option    Integer value representing the user's input.
     */
    private static void videoDownload(int option){
        String url=null, fp=null;
        if(option<3) {
            url = Prompts.getURL(vType.VOD, oType.Download);
            fp = Prompts.getOutFP(vType.VOD);
        }
        else if(option==5){
            url= Prompts.getURL(vType.Highlight, oType.Download);
            fp= Prompts.getURL(vType.Highlight, oType.Download);
        }
        else{
            url= Prompts.getURL(vType.Clip, oType.Download);
            fp= Prompts.getURL(vType.Clip, oType.Download);
        }
        boolean isM3U8 = option == 2 || (url.substring(url.lastIndexOf("." + 1)).equalsIgnoreCase("m3u8"));
        if(isM3U8 && option!=2){
            //Download.m3u8Download(url, fp);
        }
        else if(isM3U8 && option==2){
            //VOD.subVODDownload(url, fp);
        }
        else{
            //VOD.vodDownload(url, fp);
        }
        System.out.print("\n\nFile has being succesfully downloaded!");
    }

    /**
     * This method handles the recovery
     * for all videos for the CLI handler.
     * @param option    Integer value representing the user's input.
     */
    private static void videoRecover(int option){
        String url=null, fp=null;
        ArrayList<String> results;
        boolean repeat=true;
        if(option==3){
            while(repeat){
               //printResults(VOD.vodRecover(Prompts.VODRecovery()));
                repeat= Prompts.repeat(vType.VOD, oType.Recover);
            }
        }
        else {
            while(repeat){
                //printResults(Highlights.recover(Prompts.getURL(vType.Highlight, oType.Recover)));
                repeat= Prompts.repeat(vType.Highlight, oType.Recover);
            }
        }
    }
}
