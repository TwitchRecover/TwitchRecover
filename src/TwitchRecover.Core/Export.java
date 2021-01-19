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

import java.util.ArrayList;

/**
 * Export object class which
 * manages the creation and
 * exporting of results.
 */
public class Export {
    private ArrayList<String> results;      //String arraylist which contains all of the results to export.
    private String fp;                      //String variable representing the file path of where to store the results.
    private String fFP;                     //String variable representing the final file path, including the file extension, of where the results will be exported.
    private ContentType ct;                 //Content type variable which represents what type of content the results are from.
    private String id;                      //String variable representing the ID of the content in question.

    /**
     * Constructor for the export object
     * class which initiates the object
     * and adds the top notice to the results.
     */
    public Export(){
        results=new ArrayList<String>();
        results.add("# Results generated using Twitch Recover - https://github.com/twitchrecover/twitchrecover");
        results.add("# Please consider donating if this has been useful for you - https://paypal.me/daylamtayari https://cash.app/$daylamtayari BTC: 15KcKrsqW6DQdyZPrgRXXmsKkyyZzHAQVX");
        results.add("#-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Export method which exports
     * the results to the desired file.
     */
    public void export(){
        computeFN();
        FileIO.write(results, fFP);
    }

    /**
     * Adds a result value to the results list.
     * @param result    String value representing the result value to add to the results list.
     */
    public void addResult(String result){
        results.add(result);
    }

    /**
     * Adds a comment to the results list.
     * @param comment   String value representing a comment to add to the results list.
     */
    public void addComment(String comment){
        results.add("# "+comment);
    }

    /**
     * Mutator for the file path variable.
     * @param fp    String variable which represents the file path of where to store the exported results.
     * @param ct    ContentType enum representing what type of content the results are for.
     * @param id    String value representing the ID of the content the results are for. Can be null.
     */
    public void setFP(String fp, ContentType ct, String id){
        this.fp=FileIO.adjustFP(fp);
        this.ct=ct;
        this.id=id;
    }

    /**
     * Accessor for the fFP variable.
     * @return String   String variable which represents the final file path of where the file will be stored.
     */
    public String getFFP(){
        return fFP;
    }

    /**
     * This computes the final file name of the output file.
     */
    private void computeFN(){
        computeID();
        fFP=fp+"TwitchRecover-"+ct.toString()+"-Results-"+id+ FileExtension.TXT.getFE();
    }

    /**
     * This computes the ID value of
     * the file if the ID value is
     * currently null.
     */
    private void computeID(){
        if(id==null){
            id=String.valueOf((int) (Math.random()*1000000));
        }
    }
}