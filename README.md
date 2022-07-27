### If you want to help:

* #### Bitcoin wallet: 1DXrtXnrCKdDm24x8ioXY8ZkNWDDCxEFg4

# Twitch Recover

### Twitch Recover is a free tool that allows you to view, recover and download all types of Twitch videos (VODs, clips, streams and highlights).
  
### The current version available is the alpha of the 2.1 version which has 17 different features.
**If you use the 2.1 alpha and experience an issue, please report the issue so I can fix it for the beta and final releases.**  

### Twitch has just started fully deleting a greater percentage of VODs also from their VOD servers when a streamer deletes the VOD. If you cannot find a VOD it is because that one has fallen fate to Twitch's updated deletion process.

## Downloads:  
<break/>  
  
### - [2.1 Alpha](https://github.com/NosferatuZoddd/TwitchRecover/releases/tag/2.1a): [https://github.com/NosferatuZoddd/TwitchRecover/releases/tag/2.1a](https://github.com/NosferatuZoddd/TwitchRecover/releases/tag/2.1a)  
 
## Features:

| Features  | 2.1 Alpha | 3.0 Beta | 4.0 Final Release |  
| ------------- |-----------|----------|-------------------|
| GUI  | ❌         | ✔        | ✔                 |
| Get live stream feeds  | ✔         | ✔        | ✔                 |
| Download live stream  | ❌         | ✔        | ✔                 |
| Get VOD feeds  | ✔         | ✔        | ✔                 |
| Download VOD  | ✔         | ✔        | ✔                 |
| Recover VOD  | ✔         | ✔        | ✔                 |
| Retrieve highlight feeds  | ✔         | ✔        | ✔                 |
| Download highlight  | ✔         | ✔        | ✔                 |
| Recover highlight  | ✔         | ✔        | ✔                 |
| Check for muted segments  | ✔         | ✔        | ✔                 |
| 'Unmute' video  | ✔         | ✔        | ✔                 |
| Download M3U8  | ✔         | ✔        | ✔                 |
| Convert TS files  | ✔         | ✔        | ✔                 |
| Retrieve permanent clip links  | 🟡        | 🟡       | 🟡                |
| Download a clip  | ✔         | ✔        | ✔                 |
| Recover ALL clips from a stream  | ✔         | ✔        | ✔                 |
| Download chat from live stream  | ❌         | ✔        | ✔                 |
| Download chat from clip  | ❌         | ✔        | ✔                 |
| Download chat from VOD/highlight  | ❌         | ✔        | ✔                 |
| Mass download features  | ❌         | ✔        | ✔                 |
| Mass recovery features  | ❌         | ✔        | ✔                 |
| User preferences  | ❌         | ✔        | ✔                 |
| Multi language support (10+)  | ❌         | ❌        | ✔                 |
| Direct Twitch Recover URLs  | ❌         | ❌        | ✔                 |
| Detailed wiki and video tutorials  | ❌         | ❌        | ✔                 |
| Website  | ❌         | ❌        | ✔                 |
| Browser extension  | ❌         | ❌        | ✔                 |

### If there is a feature you don't see above and would like to see, please create a Github issue suggesting the feature.
<break/>  

## 2.1 Alpha Guide:  
  
### Installation:  
**Available on all platforms (Windows, Linux, macOS)**
  
1. Download the latest release: [https://github.com/NosferatuZoddd/TwitchRecover/releases/](https://github.com/NosferatuZoddd/TwitchRecover/releases/)
2. Launch Twitch Recover.
3. Enjoy.

### Can't play M3U8!

You just retrieved a VOD or highlight but when you paste it into VLC it won't load or you can't watch the whole video.
   
#### Check if it has muted segments. Use option 9 of Twitch Recover to check if the video is muted/has muted segments.   
#### If it does, use option 10 to unmute the video and then open that new M3U8 file in VLC and you can watch it. 
  
#### If the M3U8 still won't play, please create a Github issue so I can look into the issue.

This is caused by how the playlist of Twitch M3U8 videos which have muted segments are structured. 
This results in when you try playing those muted segments in VLC (or other video player), it won't be able to reach it and cause it to be unable to play it.  
When unmuted using Twitch Recover, simply open the file in VLC or other similar video players and you should be able to watch it as usual.  

### Guide:
### Using a Twitch Tracker link:
**You can use the Twitch Tracker link of a stream to directly get the VOD links.**  
**Links must be in the following format: twitchtracker.com/[streamer]/streams/[stream ID]**  
i.e. https://twitchtracker.com/tayarics/streams/40715936990  

Select option 2 and paste the link and you will get the VOD links.

#### Manually inputting the stream information:
1. Select option 1.
2. Input the streamer's name.
3. Input the Stream ID.  
    **The unique Stream ID, not what comes after 'videos/...'.** You can get it a variety of ways but the simplest are using Twitch Tracker or Sully Gnome, they are the string of digits in the stream page's URL.
4. Enter the timestamp of the start of the stream in the 'YYYY-MM-DD HH:mm:ss' format.

#### Brute forcing the seconds:
**Only use this option when you do not have the time in seconds of the stream's start, only the time up to minutes.**
1. Select option 3.
2. Input the streamer's name.
3. Input the Stream ID **(the disclaimer in bold right above also applies)**.
4. Input the timestamp in the same format but set the seconds value to 00.
    **'YYYY-MM-DD HH:mm:00'**

## Credits:
- **[NosferatuZoddd](https://github.com/NosferatuZoddd): I continue to work on the application, improve and optimize the app.**
- **[Daylam Tayari](https://github.com/daylamtayari): The first developer of Twitch Recover.**
- [Saysera](https://twitter.com/Saysera69): Helped him understanding of how some elements of Twitch's backend work.
- [Koolski](https://twitter.com/Koolski_): Designed the logo.
- [arVahedi](https://github.com/arVahedi): His Java M3U8 downloader repository helped me understand how to download M3U8 files.
- [Franiac](https://github.com/Franiac): His Twitch Leecher program helped him figure out a few APIs that I was missing.
- [Lay295](https://github.com/lay295): Helped him figure out how to 'unmute' a VOD.


## Disclaimer:

Twitch Recover is not associated with Amazon, Twitch, Twitch Tracker, Sullygnome, Streamscharts or any of their partners and parent companies.
All copyrights belong to their respective owners.
