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

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * This class contains all of the download and their related methods.
 */
public class Download {
    /**
     * This method adjusts a given file path to ensure that it is formatted correctly.
     * @param fp String value which represents the given file path to be adjusted.
     * @return String   Adjusted file path.
     */
    private static String fpAdjust(String fp) {
        if(fp.indexOf('\\') != fp.length() - 1) {
            fp += "\\";
        }
        return fp;
    }

    /**
     * This method downloads a file and saves it at the location precised in the filepath.
     * Should not be used for VODs
     * @param url String value which represents the URL of the file to download.
     * @param fp  String value which represents the filepath of where to save the file.
     */
    protected static void download(String url, String fp) {
        try(BufferedInputStream is = new BufferedInputStream(new URL(url).openStream());
            FileOutputStream os = new FileOutputStream(fp)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while((bytesRead = is.read(dataBuffer, 0, 1024)) != -1) {
                os.write(dataBuffer, 0, bytesRead);
            }
        }
        catch(IOException ignored) {}
    }
}