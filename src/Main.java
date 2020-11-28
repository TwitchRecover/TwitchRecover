import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author daylamtayari https://github.com/daylamtayari
 * @version CLI 1.0
 * Github project file: https://github.com/TwitchRecover/TwitchRecover
 */

public class Main {
	ArrayList<String> domains=new ArrayList<String>();	//Tracks the Twitch VOD server domains.
	public static void main(String[] args) {	
		Main main=new Main();
		try {
			main.mainCLI();
		} catch (IOException e) {
		}
	}
	
	/**
	 * This is the CLI handler which runs the entire program 
	 * in the CLI for the CLI release.
	 * @throws IOException 
	 */
	public void mainCLI() throws IOException {
		//Get domains:
		try {
			getDomains();
		}
		catch(IOException e) {
			domains.add("https://vod-secure.twitch.tv");
			domains.add("https://vod-metro.twitch.tv");
			domains.add("https://d2e2de1etea730.cloudfront.net");
			domains.add("https://dqrpb9wgowsf5.cloudfront.net");
			domains.add("https://ds0h3roq6wcgc.cloudfront.net");
		}
		//User inputs:
		String name, date, url;
		date="";
		name="";
		long vodID;
		vodID=0;
		long timestamp;
		timestamp=0;
		ArrayList<String> resultURLs=new ArrayList<String>();
		Scanner sc=new Scanner(System.in);
		System.out.print(""
				+ "\nWelcome to Twitch Recover!"
				+ "\nGet the m3u8 link of any deleted Twitch VOD (up to 60 days) to then watch it in VLC or other similar programs."
				+ "\n\nInput Options:"
				+ "\n1. Input values manually:"
				+ "\n2. Input Twitch Tracker stream URL."
				+ "\n3. Input timestamp to the minute and brute force."
				+ "\nPlease enter your input choice below (1, 2 or 3):	"
				);
		String input=sc.nextLine();
		while(input.equals("1")==false && input.equals("2")==false && input.equals("3")==false) {
			System.out.print(""
					+ "\nINVALID INPUT"
					+ "\n\nInput Options:"
					+ "\n1. Input values manually:"
					+ "\n2. Input Twitch Tracker stream URL."
					+ "\n3. Input timestamp to the minute and brute force."
					+ "\nPlease enter either a '1' or a '2' or a '3' depending on your desired option:	"
					);
			input=sc.nextLine();
		}
		//Option Selection:
		if(input.equals("1") || input.equals("3")) {	//Manual inputs:
			System.out.print("\n\nPlease enter the corresponding values:");
			System.out.print("\nStreamer's name: ");
			name=sc.nextLine();
			System.out.print("\nVOD ID: ");
			try {
				vodID=Long.parseLong(sc.nextLine());
			}
			catch(NumberFormatException e) {
				System.out.print("\nINVALID VOD ID");
				System.exit(0);
			}
			System.out.print("\nTimestamp (YYYY-MM-DD HH:mm:ss): ");
			date=sc.nextLine();
		}
		else if(input.equals("2")) {	//Twitch Tracker URL: 
			System.out.print("\n\nPlease enter the Twitch Tracker stream URL: ");
			url=sc.nextLine();
			while(isValidURL(url).equals("invalid")) {
				System.out.print("\nINVALID URL\nPlease enter a valid Twitch Tracker stream URL (URL of the page of a stream): ");
				url=sc.nextLine();
			}
			try {
				String[] results=getTTData(url);
				name=results[0];
				vodID=Long.parseLong(results[1]);
				date=results[2];
			}
			catch(IOException e){
			}
		}
		try {
			timestamp=getUNIXTime(date);
		} catch (ParseException e) {
		}
		sc.close();
		//Backend computing:
		if(input.equals("3")) {
			resultURLs=BFURLs(name, vodID, timestamp);
		}
		else {
			resultURLs=getURLs(name, vodID, timestamp);
		}
		System.out.print("\n\nResults: ");
		for(int i=0; i<resultURLs.size();i++) {
			System.out.print("\n"+resultURLs.get(i));
		}
		if(resultURLs.size()==0) {
			System.out.print("\n\nNO SUCCESSFUL RESULTS WERE FOUND");
		}
	}
	
	/**
	 * Computes the SHA1 hash of the given final string and returns the first 20 values.
	 * @param finalString	Represents the string containing the streamer name, vodID and timestamp of the VOD timestamp.
	 * @return String		Returns the first 20 values of the SHA1 hash of the given string.
	 * @throws NoSuchAlgorithmException
	 */
	public String hash(String baseString) throws NoSuchAlgorithmException {
		MessageDigest md=MessageDigest.getInstance("SHA1");
		byte[] result=md.digest(baseString.getBytes());
		StringBuffer sb=new StringBuffer();
		for(int i=0; i<result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff)+0x100, 16).substring(1));
		}
		String hash=sb.toString();
		return hash.substring(0, 20);
	}
	
	/**
	 * Computes the URl (excluding the domain itself) and calls the hashing method.
	 * @param name			Streamer name.
	 * @param vodID			VOD ID.
	 * @param timestamp		UNIX Timestamp.
	 * @return String		URL of the VOD excluding the actual domain.
	 */
	public String URLCompute(String name, long vodID, long timestamp) {
		String baseString=name+"_"+Long.toString(vodID)+"_"+Long.toString(timestamp);
		try {
			String hash=hash(baseString);
			String finalString=hash+"_"+baseString;
			return "/"+finalString+"/chunked/index-dvr.m3u8";
		}
		catch(NoSuchAlgorithmException e){
			return "NoSuchAlgorithmException error"+e;
		}
	}
	
	/**
	 * This method gets the domains from the domains file from 
	 * the git repository and if unsuccessful, sets some of the 'defaults' manually.
	 * @throws IOException
	 */
	public void getDomains() throws IOException{
		URL obj=new URL("https://raw.githubusercontent.com/TwitchRecover/TwitchRecover/main/domains.txt");
		HttpURLConnection httpcon=(HttpURLConnection) obj.openConnection();
		httpcon.setRequestMethod("GET");
		httpcon.setRequestProperty("User-Agent", "Mozilla/5.0");
		int responseCode=httpcon.getResponseCode();
		if(responseCode==HttpURLConnection.HTTP_OK) {
			BufferedReader br=new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
			String inputline;
			while((inputline=br.readLine())!=null) {
				String response=inputline.toString();
				domains.add(response);
			}
		}
		else {
			domains.add("https://vod-secure.twitch.tv");
			domains.add("https://vod-metro.twitch.tv");
			domains.add("https://d2e2de1etea730.cloudfront.net");
			domains.add("https://dqrpb9wgowsf5.cloudfront.net");
			domains.add("https://ds0h3roq6wcgc.cloudfront.net");
		}
	}
	
	/**
	 * This method gets the successful VOD URLs or if 
	 * unsuccessful to connect to the internet, lists all possibilites.
	 * @param name					Name of the streamer.
	 * @param vodID					VOD ID of the VOD in question.
	 * @param timestamp				UNIX timestamp of the VOD.
	 * @return ArrayList<String>	Returns a string arraylist with all of the working VOD urls.
	 */
	public ArrayList<String> getURLs(String name, long vodID, long timestamp) {
		String baseURL=URLCompute(name, vodID, timestamp);
		ArrayList<String> URLs=new ArrayList<String>();
		for(int i=0; i<domains.size(); i++) {
			URLs.add(domains.get(i)+baseURL);
		}
		try {
			return checkURLs(URLs);
		}
		catch(IOException e) {
			URLs.add(0, "The VOD URLs were unable to be checked.");
			URLs.add(1, "Please double check the URL/inputted data for any typos.");
			URLs.add(2, "Listing all possible URLs:");
			return URLs;
		}
	}
	
	/**
	 * This method checks each of the possible VOD urls 
	 * and returns an arraylist with all of the working VOD urls 
	 * @param URLs					String arraylist of all of the possible VOD urls.
	 * @return ArrayList<String>	String arraylist with all of the working VOD urls.
	 * @throws IOException
	 */
	public ArrayList<String> checkURLs(ArrayList<String> URLs) throws IOException{
		ArrayList<String> vodURLs=new ArrayList<String>();
		for(int i=0; i<URLs.size();i++) {
			URL obj=new URL(URLs.get(i));
			HttpURLConnection httpcon=(HttpURLConnection) obj.openConnection();
			httpcon.setRequestMethod("GET");
			httpcon.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode=httpcon.getResponseCode();
			if(responseCode==HttpURLConnection.HTTP_OK) {
				vodURLs.add(URLs.get(i));
			}
		}
		return vodURLs;
	}
	
	/**
	 * This method brute forces the VOD urls using a 
	 * timestamp that only goes up to the minute (0 seconds of the 
	 * minute in questions).
	 * @param name					The streamer name.
	 * @param vodID					The ID of the VOD in question.
	 * @param timestamp				The timestamp of the VOD up to the minute with 0 seconds.
	 * @return ArrayList<String>	Returns an arraylist of working VOD urls.
	 */
	public ArrayList<String> BFURLs(String name, long vodID, long timestamp){
		ArrayList<String> results=new ArrayList<String>();
		ArrayList<String> tempResults=new ArrayList<String>();
		for(int i=0; i<60; i++) {
			tempResults=getURLs(name, vodID, timestamp+i);
			if(tempResults.size()!=0) {
				for(int j=0; j<tempResults.size();j++) {
					results.add(tempResults.get(j));
				}
			}
		}
		return results;
	}
	
	/**
	 * This method gets the UNIX timestamp of a particular given date.
	 * @param timestamp			String which represents the timestamp of the VOD in a date format.
	 * @return long				Timestamp of the VOD start time and date in UNIX time.
	 * @throws ParseException
	 */
	public long getUNIXTime(String timestamp) throws ParseException {
		String time=timestamp+" UTC";
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
		Date date=df.parse(time);
		return (long) date.getTime()/1000;
	}
	
	/**
	 * This handler checks the validity of a given URL.
	 * @param url		The given URL to check.
	 * @return String	Returns "invalid" if the URL is invalid or returns the name of the service the URL is from.
	 */
	public String isValidURL(String url) {
		if(url.indexOf("twitchtracker.com")!=-1 && url.indexOf("/streams/")!=-1) {
			return "twitchtracker";
		}
		else {
			return "invalid";
		}
	}
	
	/**
	 * This method scrapes the given TwitchTracker page and returns 
	 * the values necessary for the VOD URL discovery.
	 * @param url				The URL of the TwitchTracker page in question.
	 * @return String[]			Return a string array with the streamer's name at index 0, 
	 * the VOD ID at index 1 and the timestamp at index 2.
	 * @throws IOException
	 */
	public String[] getTTData(String url) throws IOException {
		String[] results=new String[3];
		URL obj=new URL(url);
		HttpURLConnection httpcon=(HttpURLConnection) obj.openConnection();
		httpcon.setRequestMethod("GET");
		httpcon.setRequestProperty("User_Agent", "Mozilla/5.0");
		int responseCode=httpcon.getResponseCode();
		if(responseCode==HttpURLConnection.HTTP_OK) {
			BufferedReader brt=new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
			for(int i=0; i<7;i++) {
				brt.readLine();
			}
			//Get the timestamp:
			String response=brt.readLine();
			int tsIndex=response.indexOf(" on ")+4;
			results[2]=response.substring(tsIndex,tsIndex+19);
			//Get the streamer's name and the VOD ID:
			String pattern="twitchtracker\\.com\\/([a-zA-Z0-9]*)\\/streams\\/(\\d*)";
			Pattern r=Pattern.compile(pattern);
			Matcher m=r.matcher(url);
			if(m.find()) {
				results[0]=m.group(1);
				results[1]=m.group(2);
			}
			//Return the array:
			return results;
		}
		throw new IOException();
	}
}