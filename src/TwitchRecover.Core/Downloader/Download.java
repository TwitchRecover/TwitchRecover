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

package TwitchRecover.Core.Downloader;

import TwitchRecover.Core.Enums.Timeout;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * This class holds all of the downloading methods
 * and handles all of the downloads.
 */
public class Download {
    /**
     * This method downloads a file from a
     * given URL and downloads it at a given
     * file path.
     * @param url
     * @param fp
     * @throws IOException
     */
    public static void download(String url, String fp) throws IOException {
        URL dURL=new URL(url);
        File dFile=new File(fp);
        FileUtils.copyURLToFile(dURL, dFile, Timeout.CONNECT.time, Timeout.READ.time);
    }

    public static void m3u8Download(String url, String fp){

    }

    /**
     * This method creates a temporary download
     * from a URL.
     * @param url       URL of the file to be downloaded.
     * @return File     File object of the file that will be downloaded and is returned.
     * @throws IOException
     */
    protected static File tempDownload(String url) throws IOException{
        URL dURL=new URL(url);
        String prefix=FilenameUtils.getBaseName(dURL.getPath());
        if(prefix.length()<10){     //This has to be implemented since the prefix value of the createTempFile method
            prefix="00"+prefix;     //which we use to create a temp file, has to be a minimum of 3 characters long.
        }
        else if(prefix.length()<100){
            prefix="0"+prefix;
        }
        File downloadedFile=File.createTempFile(FileHandler.TEMP_FOLDER_PATH+File.separator+prefix, FilenameUtils.getExtension(dURL.getPath()));    //Creates the temp file.
        downloadedFile.deleteOnExit();
        FileUtils.copyURLToFile(dURL, downloadedFile, Timeout.CONNECT.time, Timeout.READ.time);
        return downloadedFile;
    }
}