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

package TwitchRecover.Core.Downloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles all of the methods related
 * to M3U8 processing for downloads.
 */
class M3U8Handler {
    /**
     * This method retrieves all of the
     * chunks of an M3U8 URL.
     * @param url                   URL of the M3U8 file to retrieve the chunks off.
     * @return ArrayList<String>    String arraylist containing all of the chunks of the M3U8 file.
     * @throws IOException
     */
    protected static ArrayList<String> getChunks(String url) throws IOException {
        ArrayList<String> chunks=new ArrayList<String>();
        String baseURL="";
        String pattern = "([a-z0-9\\-]*.[a-z_]*.[net||com||tv]*\\/[a-z0-9_]*\\/[a-zA-Z0-9]*\\/)index[0-9a-zA-Z-]*.m3u8";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if(m.find()) {
            baseURL = "https://" + m.group(1);
        }
        String pattern2 = "([a-z0-9\\-]*.[a-z_]*.[net||com||tv]*\\/[a-zA-Z0-9_]*\\/[0-9]*\\/[a-zA-Z0-9_-]*\\/[0-9p]*\\/)";
        Pattern r2 = Pattern.compile(pattern2);
        Matcher m2 = r2.matcher(url);
        if(m2.find()) {
            baseURL = "https://" + m2.group(1);
        }
        File m3u8File=Download.tempDownload(url);
        Scanner sc=new Scanner(m3u8File);
        String line;
        while(sc.hasNextLine()){
            line=sc.nextLine();
            if(line.contains("unmuted") && !line.startsWith("#")){
                chunks.add(baseURL+line.substring(0, line.indexOf("-")+1)+"muted.ts");
            }
            else if(!line.startsWith("#")){
                chunks.add(baseURL+line);
            }
        }
        sc.close();
        return chunks;
    }
}