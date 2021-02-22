/*
 * Copyright (c) 2020, 2021 Daylam Tayari <daylam@tayari.gg>
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License version 3as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * @author Daylam Tayari daylam@tayari.gg https://github.com/daylamtayari
 * @version 2.0b
 * Github project home page: https://github.com/TwitchRecover
 * Twitch Recover repository: https://github.com/TwitchRecover/TwitchRecover
 */

package TwitchRecover.Core;

import TwitchRecover.Core.API.API;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class hosts all of the methods
 * that are executed at the booting
 * of the program.
 */
public class Boot {
    /**
     * Runner method for the startup boot methods.
     */
    public static void bootRunner(){
        retrieveWEBCI();
        Fuzz.setDomains(getDomains());
    }

    /**
     * This method retrieves the web client IDs from
     * the dedicated file in the repo and then sets
     * them to the WEB_CI variable if they are functional.
     */
    private static void retrieveWEBCI(){
        boolean added=false;
        try{
            String response="";
            ArrayList<String> responses=API.getReq("https://raw.githubusercontent.com/TwitchRecover/TwitchRecover/master/WEB_CI.txt");
            for(String s: responses){
                if(!s.startsWith("#")){
                    if(testWEBCI(s)){
                        added=true;
                        return;
                    }
                }
            }
        }
        catch(Exception ignored){}
        finally{
            if(!added){
                API.setWEBCI("kimne78kx3ncx6brgo4mv6wki5h1ko");
            }
        }
    }

    /**
     * This method tests if a web client ID is valid.
     * @param web_ci    String value representing the web client ID to test.
     * @return          Boolean value which is true if the client ID is functional and false if otherwise.
     */
    private static boolean testWEBCI(String web_ci){
        API.setWEBCI(web_ci);
        if(API.gqlGet("{\"operationName\":\"Core_Services_Spade_CurrentUser\",\"variables\":{},\"extensions\":{\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"482be6fdcd0ff8e6a55192210e2ec6db8a67392f206021e81abe0347fc727ebe\"}}}")==null){
            return false;
        }
        return true;
    }

    /**
     * This method gets all of the Twitch M3U8 VOD domains
     * from the domains file of the Twitch Recover repository.
     * @return ArrayList<String>    String arraylist representing all of the Twitch M3U8 VOD domains.
     */
    private static ArrayList<String> getDomains(){
        ArrayList<String> domains=new ArrayList<String>();
        boolean added=false;
        try {
            URL dURL=new URL("https://raw.githubusercontent.com/TwitchRecover/TwitchRecover/master/domains.txt");
            HttpURLConnection con=(HttpURLConnection) dURL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            if(con.getResponseCode()==HttpURLConnection.HTTP_OK){
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                while((line=br.readLine()) !=null){
                    String response=line.toString();
                    domains.add(response);
                    added=true;
                }
            }
        }
        catch(IOException ignored){}
        finally{
            if(!added){     //To execute if the domains from the domains file were not added as a backup.
                domains.add("https://vod-secure.twitch.tv");
                domains.add("https://vod-metro.twitch.tv");
                domains.add("https://d2e2de1etea730.cloudfront.net");
                domains.add("https://dqrpb9wgowsf5.cloudfront.net");
                domains.add("https://ds0h3roq6wcgc.cloudfront.net");
                domains.add("https://dqrpb9wgowsf5.cloudfront.net");
            }
        }
        return domains;
    }
}