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

import java.util.ArrayList;

/**
 * Export object class which
 * manages the creation and
 * exporting of results.
 */
public class Export {
    private ArrayList<String> results;      //String arraylist which contains all of the results to export.
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
}