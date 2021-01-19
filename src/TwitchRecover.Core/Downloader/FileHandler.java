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

import TwitchRecover.Core.Enums.FileExtension;
import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NavigableMap;

/**
 * This class handles all of the file handling
 * for the Download package.
 */
class FileHandler {
    protected static Path TEMP_FOLDER_PATH;    //Variable which holds the folder path of the temp folder.

    /**
     * This method creates a temp folder where all of the temporary TS
     * files (M3U8 parts) will be saved.
     * @throws IOException
     */
    protected static void createTempFolder() throws IOException {
        TEMP_FOLDER_PATH= Files.createTempDirectory("TwitchRecover-").toAbsolutePath();
        File tempDir=new File(String.valueOf(TEMP_FOLDER_PATH));
        tempDir.deleteOnExit();
    }

    /**
     * This method merges all of the
     * segmented files of the M3U8 playlist
     * into a single file.
     * @param segmentMap    Navigable map holding the index and file objects of all the segment files.
     * @param fp            Final file path of the file.
     */
    protected static String mergeFile(NavigableMap<Integer, File> segmentMap, String fp){
        File output=new File(fp);
        segmentMap.forEach((key, segment) -> {
            try{
                fileMerger(segment, output);
            }
            catch(Exception ignored){}
        });
        return output.getAbsolutePath();
    }

    /**
     * Method which merges two files together.
     * @param input     File to be merged.
     * @param output    File to be merged into.
     * @throws IOException
     */
    private static void fileMerger(File input, File output) throws IOException {
        @Cleanup OutputStream os= new BufferedOutputStream(new FileOutputStream(output, true));
        @Cleanup InputStream is=new BufferedInputStream(new FileInputStream(input));
        IOUtils.copy(is, os);
        is.close();
        os.close();
    }
}