<a href="https://paypal.me/daylamtayari"><img src="https://img.shields.io/badge//daylamtayari-%2300457C.svg?&style=for-the-badge&logo=PayPal&logoColor=white&labelColor=black"></a> 
<a href="https://cash.app/$daylamtayari"><img src="https://img.shields.io/badge//$daylamtayari-%2300C244.svg?&style=for-the-badge&logo=Cash-App&logoColor=white&labelColor=black"></a> 
<a href="https://www.blockchain.com/btc/address/15KcKrsqW6DQdyZPrgRXXmsKkyyZzHAQVX"><img src="https://img.shields.io/badge/15KcKrsqW6DQdyZPrgRXXmsKkyyZzHAQVX-%23F7931A.svg?&style=for-the-badge&logo=bitcoin&logoColor=white&labelColor=black"></a>

# Twitch Recover

### Twitch Recover is a free tool that allows you to view, recover and download all types of Twitch videos (VODs, clips, streams and highlights).
  
### There are two current versions available, the alpha of the 2.0 version which has 17 different features and the 1.2 version which is the last current stable version.  
**If you use the 2.0 alpha and experience an issue, please report the issue so I can fix it for the beta and final releases.**  

## Downloads:  
<break/>  
  
### - [2.0 Alpha](https://github.com/TwitchRecover/TwitchRecover/releases/download/2.0a/Twitch.Recover.Setup.exe): [https://github.com/TwitchRecover/TwitchRecover/releases/tag/2.0a](https://github.com/TwitchRecover/TwitchRecover/releases/tag/2.0a)  
### - [1.2 Release](https://github.com/TwitchRecover/TwitchRecover/releases/download/1.2/TwitchRecover-CLI-v1.2.exe): [https://github.com/TwitchRecover/TwitchRecover/releases/tag/1.2](https://github.com/TwitchRecover/TwitchRecover/releases/tag/1.2)  
  
## Features:
  
| Features  | 2.0 Alpha | 1.2 Release  | 2.0 Final Release |
| ------------- | ------------- | ------------- | ------------- |
| GUI  | ❌  | ❌  | ✔  |
| Get live stream feeds  | ✔  | ❌  | ✔  |
| Download live stream  | ❌  | ❌  | ✔  |
| Get VOD feeds  | ✔  | ✔  | ✔  |
| Download VOD  | ✔  | ❌  | ✔  |
| Recover VOD  | ✔  | ✔  | ✔  |
| Retrieve highlight feeds  | ✔  | ❌  | ✔  |
| Download highlight  | ✔  | ❌  | ✔  |
| Recover highlight  | ✔  | ❌  | ✔  |
| Check for muted segments  | ✔  | ❌  | ✔  |
| 'Unmute' video  | ✔  | ✔  | ✔  |
| Download M3U8  | ✔  | ❌  | ✔  |
| Convert TS files  | ✔  | ❌  | ✔  |
| Retrieve permanent clip links  | 🟡  | ❌  | 🟡  |
| Download a clip  | ✔  | ❌  | ✔  |
| Recover ALL clips from a stream  | ✔  | ❌  | ✔  |
| Download chat from live stream  | ❌  | ❌  | ✔  |
| Download chat from clip  | ❌  | ❌  | ✔  |
| Download chat from VOD/highlight  | ❌  | ❌  | ✔  |
| Mass download features  | ❌  | ❌  | ✔  |
| Mass recovery features  | ❌  | ❌  | ✔  |

### If there is a feature you don't see above and would like to see, please create a Github issue suggesting the feature.
<break/>  

## 2.0 Alpha Guide:  
  
### Installation:  
**For Windows users please use the installer.**
**For linux and MacOS users, please download the JAR and run it.**
  
1. Download the installer: [https://github.com/TwitchRecover/TwitchRecover/releases/download/2.0a/Twitch.Recover.Setup.exe](https://github.com/TwitchRecover/TwitchRecover/releases/download/2.0a/Twitch.Recover.Setup.exe)
2. Run and install the installer.
3. Launch Twitch Recover.
4. Enjoy.

## 1.2 Guide:
  
### Installation:
1. Download the exe file for your desired version.
    - [Graphical (GUI) Version (WIP, coming soon)]()
    - [Command Line Version](https://github.com/TwitchRecover/TwitchRecover/releases/download/1.2/TwitchRecover-CLI-v1.2.exe)
2. Run the exe. **Ignore the Windows Defender popup, click more info and run anyway. It is a certificate issue, not a security issue.**
3. Paste the result URL into VLC or another similar video client.  
<img src="https://i.gyazo.com/8de89763015852c0ab70aabc6447ec04.gif" width="646.5" height="426"/>

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
- **[Daylam Tayari](https://github.com/daylamtayari): Developer of Twitch Recover.**  
**Check out my Github to see my other projects, including Twitch related projects:**   **https://github.com/daylamtayari**  
**If you like this and wish to support me, feel free to send a tip via PayPal or Cashapp:  https://paypal.me/daylamtayari or [$daylamtayari](https://cash.app/$daylamtayari)**  
**Feel free to contact me via Discord (tayari#6113) or email ([daylam@tayari.gg](mailto:daylam@tayari.gg).**
  
- [Saysera](https://twitter.com/Saysera69): Helped my understanding of how some elements of Twitch's backend work.
- [Koolski](https://twitter.com/Koolski_): Designed the logo.
- [arVahedi](https://github.com/arVahedi): His Java M3U8 downloader repository helped me understand how to download M3U8 files.
- [Franiac](https://github.com/Franiac): His Twitch Leecher program helped me figure out a few APIs that I was missing.
- [Lay295](https://github.com/lay295): Helped me figure out how to 'unmute' a VOD.

## Disclaimer:

Twitch Recover is not associated with Amazon, Twitch, Twitch Tracker, Sullygnome, Streamscharts or any of their partners and parent companies.
All copyrights belong to their respective owners.
