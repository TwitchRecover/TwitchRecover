/*
 * Copyright (c) 2021 Daylam Tayari <daylam@tayari.gg>
 *
 * This library is free software. You can redistribute it and/or modify it under the terms of the GNU General Public License version 3 as published by the Free Software Foundation.
 * This program is distributed in the that it will be use, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
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
    Video("", ""),
    Mass("", ""),
    Results("results", "");

    public String text;
    String stream;
    vType(String t, String s) {
        text = t;
        stream = s;
    }
}
