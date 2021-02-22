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

import TwitchRecover.Core.Enums.BruteForce;

/**
 * Object class which conatains
 * the VOD information.
 */
public class VODInfo {
    private String channelName;     //String value representing the channel name.
    private long streamID;          //Long value representing the stream ID.
    private String timestamp;       //String value representing the timestamp in YYYY-MM-DD HH:mm:ss format.
    private long unixTimestamp;     //Long value representing the UNIX timestamp.
    private BruteForce bruteForce;  //Brute force value which represents the type of brute force, if any, to be applied.
    private int duration;           //Integer value representing the duration of the stream in minutes.
    private long VODID;             //Long value representing the VOD ID of the VOD.

    /**
     * Constructor of the object class
     * which instantiates a new
     * VOD Info object.
     */
    public VODInfo(){
    }

    //Mutators:

    /**
     * Mutator for the channelName variable.
     * @param name  String value representing the channel name.
     */
    public void setName(String name){
        channelName=name;
    }

    /**
     * Mutator for the streamID variable.
     * @param ID    Long value representing the stream ID.
     */
    public void setID(long ID){
        streamID=ID;
    }

    /**
     * Mutator for the streamID variable from a string value.
     * @param ID    String value representing the stream ID.
     */
    public void setIDS(String ID){
        streamID=Long.parseLong(ID);
    }

    /**
     * Mutator for the string timestamp variable.
     * @param ts    String value representing the start timestamp.
     */
    public void setTimestamp(String ts){
        timestamp=ts;
    }

    /**
     * Mutator for the UNIX timestamp variable.
     * @param unix  Long value representing the UNIX timestamp.
     */
    public void setUnix(long unix){
        unixTimestamp=unix;
    }

    /**
     * Mutator for the brute force boolean.
     * @param bf    Brute force enum value representing the level of brute force applied.
     */
    public void setBF(BruteForce bf){
        bruteForce=bf;
    }

    /**
     * Mutator for the duration variable.
     * @param d     Integer value representing the duration of the stream in question in minutes.
     */
    public void setDuration(int d){
        duration=d;
    }

    /**
     * Mutator for the VODID variable.
     * @param v     Long value representing the VOD ID of the stream in question.
     */
    public void setVODID(long v){
        VODID=v;
    }

    //Accessors:

    /**
     * Accessor for the channelName variable.
     * @return String   String value representing the channel name.
     */
    public String getName(){
        return channelName;
    }

    /**
     * Accessor for the stream ID variable.
     * @return Long     Long value representing the stream ID.
     */
    public long getID(){
        return streamID;
    }

    /**
     * Accessor for the UNIX timestamp variable, and computes it if it the value has not been computed yet.
     * @return Long     Value of the UNIX timestamp.
     */
    public long getTS(){
        if(unixTimestamp==0L){
            unixTimestamp=Compute.getUNIX(timestamp);
        }
        return unixTimestamp;
    }

    /**
     * Accessor for the string value of the timestamp.
     * @return String   String value of the timestamp value.
     */
    public String getSTS(){
        return timestamp;
    }

    /**
     * Accessor for the brute force boolean value.
     * @return BruteForce  Brute Force enum value representing the extent of brute force to apply.
     */
    public BruteForce getBF(){
        return bruteForce;
    }

    /**
     * Accessor for the duration variable.
     * @return int      Integer value representing the duration of the stream in minutes.
     */
    public int getD(){
        return duration;
    }

    /**
     * Accessor for the VODID variable.
     * @return long     Long value representing the VOD ID of the stream.
     */
    public long getVODID(){
        return VODID;
    }
}