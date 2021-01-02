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

import java.util.Scanner;
/**
 * ClipHandler object class which
 * handles a clip prompt.
 */
public class ClipHandler {
    private int option;     //Integer value representing the user's desired option.

    public ClipHandler(int option){
        this.option=option;
    }

    /**
     * This method downloads a clip
     * from a user inputted URL.
     */
    private void DownloadClip(){
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
        Clips clip=new Clips();
        String permaLink=clip.retrieveURL(clipURL);
        System.out.print(
                  "\n\nPermanent clip link: " + permaLink + "."
        );
    }
}