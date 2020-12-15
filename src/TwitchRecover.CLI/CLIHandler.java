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

package TwitchRecover.CLI;

import TwitchRecover.Core.Download;
import TwitchRecover.Core.VODs;
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
            int mOption=Prompts.menu();
            if(mOption<3 || mOption==5 || mOption==7){
                videoDownload(mOption);
            }
        }
    }

    /**
     * This method handles the downloading
     * of videos for the CLI handler.
     * @param option
     */
    private static void videoDownload(int option){
        String url=null, fp=null;
        if(option<3) {
            url = Prompts.getDURL(vType.VOD);
            fp = Prompts.getOutFP(vType.VOD);
        }
        else if(option==5){
            url=Prompts.getDURL(vType.Highlight);
            fp=Prompts.getDURL(vType.Highlight);
        }
        else{
            url=Prompts.getDURL(vType.Clip);
            fp=Prompts.getDURL(vType.Clip);
        }
        boolean isM3U8 = option == 2 || (url.substring(url.lastIndexOf("." + 1)).equalsIgnoreCase("m3u8"));
        if(isM3U8 && option!=2){
            Download.m3u8Download(url, fp);
        }
        else if(isM3U8 && option==2){
            VODs.subVODDownload(url, fp);
        }
        else{
            VODs.vodDownload(url, fp);
        }
        System.out.print("\n\nFile has being succesfully downloaded!");
    }
}
