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

package TwitchRecover.CLI.Enums;

/**
 * This enum represents the video type to handle.
 */
public enum vType {
    VOD("VOD", "stream"),
    Highlight("highlight", ""),
    Clip("clip", ""),
    Stream("stream", ""),
    Video("video", ""),
    Mass("", ""),
    M3U8("M3U8", ""),
    Results("results", "");

    String text;
    String stream;
    vType(String t, String s) {
        text = t;
        stream = s;
    }

    /**
     * Accessor for the text variable.
     * @return String   Variable for the text variable.
     */
    public String getText(){
        return text;
    }
}
