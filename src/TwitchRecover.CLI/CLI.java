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

package TwitchRecover.CLI;

/**
 * CLI class which is the root of the CLI
 * version of Twitch Recover.
 */
public class CLI {
    /**
     * Core method of the CLI version of Twitch Recover.
     * @param args_
     */
    public static void main(String[] args_){
        new Thread(new Runnable(){
            @Override
            public synchronized void run(){
                for(; ; ) {
                    try {
                        wait();
                    }
                    catch(InterruptedException ignored) {}
                }
            }
        }).start();
        CLIHandler.main();
    }
}
