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

package TwitchRecover.Core.Enums;

/**
 * This enum represents the
 * different video quality options.
 */
public enum Quality {
    Source("Source", "chunked", "source", 0.00),
    QUADK60("4k60fps", "1080p60", "3840x2160", 60.000),
    QUADK("4k30fps", "1080p30", "3840x2160", 30.000),
    QHD4k60("2580p60fps", "1080p60fps", "2580x1080", 60.000),
    QHD4k("2580p30fps", "1080p30", "2580x1080", 30.000),
    QHD60("1440p60fps", "1080p60", "2560x1440", 60.000),
    QHD("1440p30fps", "1080p30", "2560x1440", 60.000),
    FHD60("1080p60fps", "1080p60", "1920x1080", 60.000),
    FHD("1080p30fps", "1080p30", "1920x1080", 30.000),
    MHD60("900p60fps", "900p60", "1600x900", 60.000),
    MHD("900p30fps", "900p30", "1600x900", 30.000),
    HD60("720p60fps", "720p60", "1280x720", 60.000),
    HD("720p30fps", "720p30", "1280x720", 30.000),
    SHD160("480p60fps", "480p60", "852x480", 60.000),
    SHD1("480p30fps", "480p30", "852x480", 30.000),
    SHD260("360p60fps", "360p60", "640x360", 60.000),
    SHD2("360p30fps", "360p30", "640x360", 30.000),
    LHD60("160p60fps", "160p60", "284x160", 60.000),
    LHD("160p30fps", "160p30", "284x160", 30.000);

    String text;
    public String video;
    String resolution;
    double fps;
    Quality(String t, String v, String r, double f){
        text=t;
        video=v;
        resolution=r;
        fps=f;
    }

    /**
     * This method searches through
     * the quality enum for the quality
     * enum which matches a given
     * video value.
     * @param qual      String value representing the video quality to search for.
     * @return Quality  Quality enum which matches the inputted string video value, or null if none were found.
     */
    public static Quality getQualityV(String qual){
        for(Quality quality: Quality.values()){
            if(quality.video.equals(qual)){
                return quality;
            }
        }
        return null;
    }

    /**
     * This method searches through the
     * quality enum for the quality enum
     * which matches a given resolution.
     * @param resolution    String value representing the resolution value to search for.
     * @return Quality      Quality enum which matches the inputted resolution value, or null if none is found.
     */
    public static Quality getQualityR(String resolution){
        for(Quality quality: Quality.values()){
            if(quality.resolution.equals(resolution)){
                return quality;
            }
        }
        return null;
    }

    /**
     * This method searches through the
     * quality enum for the quality enum
     * which matches a given resolution
     * and FPS value.
     * @param resolution    String value representing a resolution value to search for.
     * @param fps           Double value representing an FPS value to search for.
     * @return Quality      Quality enum which matches the given resolution and FPS values, or null if none was found.
     */
    public static Quality getQualityRF(String resolution, double fps){
        for(Quality quality: Quality.values()){
            if(quality.resolution.equals(resolution) && quality.fps==fps){
                return quality;
            }
        }
        return null;
    }
}