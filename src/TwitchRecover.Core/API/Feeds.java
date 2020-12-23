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

package TwitchRecover.Core.API;
import TwitchRecover.Core.Enums.Quality;
import java.util.ArrayList;

/**
 * Feeds object which holds a list
 * of feeds and their corresponding qualities.
 */
public class Feeds {
    private ArrayList<String> feeds;        //String arraylist which holds the links of all of the feeds.
    private ArrayList<Quality> qualities;   //Quality arraylist which holds the corresponding qualities.
    //The feeds and qualities arraylist are corresponding,
    // the value at index 1 of one corresponds to the value at index 1 of the other.

    /**
     * Constructor of the feeds object
     * which initialises the feeds and
     * qualities arraylists.
     */
    public Feeds(){
        feeds=new ArrayList<String>();
        qualities=new ArrayList<Quality>();
    }

    /**
     * Mutator for the feeds arraylist which
     * sets the feeds arraylist to the
     * given arraylist.
     * @param f     String arraylist which represents what the feeds arraylist must be set to.
     */
    public void setFeeds(ArrayList<String> f){
        feeds=f;
    }

    /**
     * Mutator for the qualities arraylist which
     * sets the qualities arraylist to the
     * given arraylist.
     * @param q     Quality arraylist which represents what the qualities arraylist must be set to.
     */
    public void setQualities(ArrayList<Quality> q){
        qualities=q;
    }

    /**
     * Mutator for both the feeds and qualities arraylists,
     * adding an entry to both arraylists.
     * @param f     A string value which represents a feed URL to be added to the feeds list.
     * @param q     A quality enum which represents a quality to be added to the qualities list.
     */
    public void addEntry(String f, Quality q){
        feeds.add(f);
        qualities.add(q);
    }

    /**
     * Mutator for both the feeds and qualities arraylists,
     * adding an entry to both arraylists at one particular index.
     * @param f     A string value which represents a feed URL to be added to the feeds list.
     * @param q     A quality enum which represents a quality to be added to the qualities list.
     * @param i     An integer value representing the index at where to place the entry.
     */
    public void addEntryPos(String f, Quality q, int i){
        feeds.add(i, f);
        qualities.add(i, q);
    }

    /**
     * Mutator for the feeds arraylist whcih adds
     * the given string value to the feeds arraylist.
     * @param f     A string value which represents a feed URL to be added to the feeds list.
     */
    public void addFeed(String f){
        feeds.add(f);
    }

    /**
     * Mutator for the qualities arraylist which
     * adds the given Quality enum to the qualities arraylist.
     * @param q     A quality enum which represents a quality to be added to the qualities list.
     */
    public void addQuality(Quality q){
        qualities.add(q);
    }

    /**
     * An accessor for the feeds arraylist
     * which returns the URL of a feed at a
     * particular index of the feeds list.
     * @param i         Integer value representing the index of the feed to fetch.
     * @return String   String value representing the feed URL located at the given index.
     */
    public String getFeed(int i){
        return feeds.get(i);
    }

    /**
     * An accessor for the qualiies
     * arraylist which returns the Quality
     * enum located at a particular enum.
     * @param i         Integer value representing the index of the quality enum to fetch.
     * @return Quality  Quality enum representing the quality of the corresponding feed located at the given index.
     */
    public Quality getQuality(int i){
        return qualities.get(i);
    }

    /**
     * An accessor for the feeds arraylist
     * which returns the entire list of feeds.
     * @return ArrayList<String>    String arraylist representing the entire feeds arraylist.
     */
    public ArrayList<String> getFeeds(){
        return feeds;
    }

    /**
     * An accessor for the qualities arraylist
     * which returns the entire list of qualities.
     * @return ArrayList<Quality>   Quality arraylist which represents the entire qualities arraylist.
     */
    public ArrayList<Quality> getQualities(){
        return qualities;
    }
}