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

package TwitchRecover.Core;

import TwitchRecover.Core.Enums.ContentType;
import TwitchRecover.Core.Enums.FileExtension;
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
            for(int i=0; i<values.size();i++){
                fw.write(values.get(i));
                if(!(i==values.size()-1)){
                    fw.write("\n");
                }
            }
            fw.close();
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

    /**
     * This method adjusts a user
     * inputted file path.
     * @param fp        File path inputted by the user, to be adjusted.
     * @return String   String value representing the adjusted file path.
     */
    public static String adjustFP(String fp){
        if(fp.indexOf('\\')!=fp.length()-1){
            fp+="\\";
        }
        return fp;
    }

    /**
     * This method checks if a file
     * currently exists at the specific
     * location.
     * @param fp        String value representing the complete file path to check for (excluding file extension).
     * @param fe        FileExtensions enum which represents the anticipated file extension of the file.
     * @return boolean  Boolean value representing whether or not a file alredy exists at that location or not.
     */
    public static boolean checkFileExistence(String fp, FileExtension fe){
        File location=new File(fp+fe);
        return location.exists();
    }

    /**
     * This method computes the file name for a
     * content to be downloaded from the given
     * ID and content type.
     * @param ct        Content type enum representing the content type of the content in question.
     * @param id        String value representing the ID (clip slug, VOD ID, etc.) of the content.
     * @return String   String value representing the compute file name (excluding file extension).
     */
    public static String computeFN(ContentType ct, String id){
        return "TwitchRecover-"+ct.toString()+"-"+id;
    }

    /**
     * This method exports the results of retrieved URLs.
     * @param results   String arraylist containing all of the results to be exported.
     * @param fp        String value representing the complete final file path of the output file.
     */
    public static void exportResults(ArrayList<String> results, String fp){
        results.add(0, "# Results generated using Twitch Recover - https://github.com/twitchrecover/twitchrecover");
        results.add(1, "# Please consider donating if this has been useful for you - https://paypal.me/daylamtayari");
        write(results, fp);
    }

    /**
     * This method exports all
     * of the feeds of a feed
     * object to a file.
     * @param feeds     Feeds object to export.
     * @param fp        String value representing the complete file path of the file to output.
     */
    protected static void exportFeeds(Feeds feeds, String fp){
        ArrayList<String> results=new ArrayList<String>();
        for(int i=0; i<feeds.getFeeds().size(); i++){
            results.add("# Quality: "+feeds.getQuality(i).getText());
            results.add(feeds.getFeed(i));
        }
        exportResults(results, fp);
    }

    /**
     * This method 'converts' a file
     * from one file extension to another.
     * Both old and new file extensions must
     * be compatible since all this does is
     * change the file extension.
     * @param fp        The current complete file path of the file to convert.
     * @param fe        FileExtension enum value representing the desired output file extension.
     * @return String   String value representing the absolute file path of the new file.
     */
    public static String convert(String fp, FileExtension fe){
        File old=new File(fp);
        String newFP=fp.substring(0, fp.lastIndexOf("."))+fe.getFE();
        File newF=new File(newFP);
        old.renameTo(newF);
        return newF.getAbsolutePath();
    }

    /**
     * This method parses an arraylist of
     * values that are from a read file and
     * removes line which are deemed to be
     * comments.
     * @param read                  String arraylist containing the raw lines of values read from a file.
     * @return ArrayList<String>    String arraylist containing the read values excluding comment lines.
     */
    public static ArrayList<String> parseRead(ArrayList<String> read){
        read.removeIf(line -> line.startsWith("#") || line.isEmpty() || line.startsWith("//") || line.startsWith("/**") || line.startsWith("**/") || line.startsWith("*"));
        return read;
    }
}