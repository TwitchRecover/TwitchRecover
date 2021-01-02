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

package TwitchRecover.CLI.Handlers;

import TwitchRecover.Core.Clips;
import TwitchRecover.Core.Compute;
import TwitchRecover.Core.WebsiteRetrieval;

import java.util.ArrayList;
import java.util.Scanner;
/**
 * ClipHandler object class which
 * handles a clip prompt.
 */
public class ClipHandler {
    private int option;     //Integer value representing the user's desired option.

    /**
     * Constructor and main method of
     * the clip handler object class.
     * @param option    Integer value representing the user's selected option.
     */
    public ClipHandler(int option){
        this.option=option;
        if(option==13){
            downloadClip();
        }
        else if(option==14){
            retrievePerma();
        }
        else{
            recoverClips();
        }
    }

    /**
     * This method downloads a clip
     * from a user inputted URL.
     */
    private void downloadClip(){
        Scanner sc=new Scanner(System.in);
        System.out.print(
                  "\nClip downloading:"
                + "\nPlease enter the link of the clip to download: "
        );
        String clipURL=sc.nextLine();
        while(!checkClipURL(clipURL)){
            System.out.print(
                      "\n\nERROR: Invalid Twitch clip link."
                    + "\nThe link must be a Twitch clip link or a Twitch server clip link."
                    + "\nPlease enter the link of the clip to download: "
            );
            clipURL=sc.nextLine();
        }
        Clips clip=new Clips();
        System.out.print(
                  "\nPlease enter the FILE PATH of where you want the clip saved:"
                + "\nFile path: "
        );
        clip.setFP(sc.nextLine());
        sc.close();
        if(clipURL.substring(clipURL.lastIndexOf(".")).equals(".mp4")){
            clip.setURL(clipURL);
            clip.download();
        }
        else{
            clip.setSlug(clipURL);
            clip.downloadSlug();
        }
    }

    /**
     * This method checks whether a
     * given URL is indeed a clip URL.
     * @param url   String value representing the URL
     * @return boolean  Boolean value which returns true if the value is indeed a proper
     * clip URL and false if otherwise.
     */
    private boolean checkClipURL(String url){
        if(url.contains("clips.twitch.tv")){
            return !Compute.checkNullString(Compute.singleRegex("(clips.twitch.tv/[a-zA-Z]*)", url));
        }
        else if(url.contains("twitch.tv/clips")){
            return !Compute.checkNullString(Compute.singleRegex("(twitch.tv/clips/[a-zA-Z]*)", url));
        }
        else if(url.contains("clips-media-assets2.twitch.tv")){
            return !Compute.checkNullString(Compute.singleRegex("(clips-media-assets2.twitch.tv\\/[0-9]*-offset-[0-9]*.mp4)", url));
        }
        else{
            return !Compute.checkNullString(Compute.singleRegex("([a-z]*)", url));
        }
    }

    /**
     * This method retrieves the permanent
     * link of a clip.
     */
    private void retrievePerma(){
        Scanner sc=new Scanner(System.in);
        System.out.print(
                  "\n\nPermanent link retrieval:"
                + "\nPlease enter the link of the Twitch clip to get the permanent link for: "
        );
        String clipURL=sc.nextLine();
        while(!checkClipURL(clipURL) && !clipURL.contains("clips-media-assets2.twitch.tv") && clipURL.toLowerCase().contains("twitch.tv")){
            System.out.print(
                      "\n\nERROR: Invalid link."
                    + "\nPlease enter the link of a Twitch clip, a clips.twitch.tv/... or twitch.tv/clips/... URL."
                    + "\nEnter link: "
            );
            clipURL=sc.nextLine();
        }
        sc.close();
        Clips clip=new Clips();
        String permaLink=clip.retrieveURL(clipURL);
        System.out.print(
                  "\n\nPermanent clip link: " + permaLink + "."
        );
    }

    /**
     * This method processes the prompting
     * and handling for clip recovery.
     */
    private void recoverClips(){
        Scanner sc=new Scanner(System.in);
        System.out.print(
                  "\n\nClip Recovery:"
                + "\nDISCLAIMER: Please install and use Wfuzz. Otherwise, recovery will be EXTREMELY, EXTREMELY slow."
                + "\nInstructions to install Wfuzz are available here: https://github.com/TwitchRecover/TwitchRecover/wiki/Wfuzz-Integration"
                + "\n\n1. Input stream information manually."
                + "\n2. Input the stream link from a Twitch analytics website (Twitch Tracker or Streamscharts)."
                + "\nPlease enter your desired option, 1 or 2: "
        );
        int sourceInput=Integer.parseInt(sc.nextLine());
        while(!(sourceInput==1 || sourceInput==2)){
            System.out.print(
                      "\nInvalid input."
                    + "\nPlease enter either '1' or '2' based on your desired selection: "
            );
            sourceInput=Integer.parseInt(sc.nextLine());
        }
        Clips clip=new Clips();
        if(sourceInput==1){
            System.out.print("\nPlease input the stream ID: ");
            clip.setStreamID(Long.parseLong(sc.nextLine()));
            System.out.print("\nPlease input the stream duration in minutes: ");
            clip.setDuration((Long.parseLong(sc.nextLine())*60));
        }
        else{
            System.out.print("\nPlease input the stream link from an analytics website (Twitch Tracker or Streamscharts): ");
            String[] data= WebsiteRetrieval.getData(sc.nextLine());
            clip.setValues(Long.parseLong(data[1]), Long.parseLong(data[3]));
        }
        System.out.print(
                  "\nPlease enter y if you have Wfuzz installed and n if not: "
        );
        String wfuzz=sc.nextLine();
        while(!(wfuzz.equals("y") || wfuzz.equals("n"))){
            System.out.print(
                      "\n\nERROR: Incorrect input."
                    + "\nPlease enter 'y' if you have Wfuzz installed or 'n' if not: "
            );
            wfuzz=sc.nextLine();
        }
        clip.setWfuzz(wfuzz.equals("y"));
        System.out.print("\nRecovering clips...");
        clip.recover();
        ArrayList<String> results=clip.getResults();
        System.out.print("\n\nResults: ");
        for(String result: results){
            System.out.print("\n"+result);
        }
        System.out.print("\n\nDo you wish to export the results ('y' for yes, 'n' for no)?: "); //TODO: Add boolean checker for beta and final release.
        if(sc.nextLine().equals("y")){
            System.out.print("\nPlease input the file path where to export the results: ");
            clip.setFP(sc.nextLine());
            clip.exportResults();
        }
        sc.close();
    }
}