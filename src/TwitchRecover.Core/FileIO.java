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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class handles all IO related to files.
 */
public class FileIO {
    public static String fn="";     //String value which holds the latter part of the individual file name.

    /**
     * This method creates a file
     * and writes to it.
     * @param values    String arraylist representing the values to write to the file.
     * @param fp        String value representing the complete file path of the file to create and write to.
     */
    public static void write(ArrayList<String> values, String fp){
        File file=new File(fp);
        try{
            FileWriter fw=new FileWriter(fp);
            for(String s: values){
                fw.write("\n"+s);
            }
        }
        catch(IOException ignored){}
    }

    /**
     * This method reads the contents of a file.
     * @param fp                    Complete filepath of the file to read.
     * @return ArrayList<String>    String arraylist representing all of the contents of the file.
     */
    public static ArrayList<String> read(String fp){
        File file=new File(fp);
        ArrayList<String> contents=new ArrayList<String>();
        try{
            Scanner sc=new Scanner(file);
            while(sc.hasNextLine()){
                contents.add(sc.nextLine());
            }
            sc.close();
        }
        catch(IOException ignored){}
        return contents;
    }
}