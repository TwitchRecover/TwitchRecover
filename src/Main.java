import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main {
	ArrayList<String> domains=new ArrayList<String>();	//Tracks the Twitch VOD server domains.
	public static void main(String[] args) {
//		try {
//			getDomains();
//		}
//		catch(IOException e) {
//			domains.add("https://vod-secure.twitch.tv");
//			domains.add("https://vod-metro.twitch.tv");
//			domains.add("https://d2e2de1etea730.cloudfront.net");
//			domains.add("https://dqrpb9wgowsf5.cloudfront.net");
//			domains.add("https://ds0h3roq6wcgc.cloudfront.net");
//		}
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
	public String URLCompute(String name, int vodID, long timestamp) {
		String baseString=name+"_"+Integer.toString(vodID)+"_"+Long.toString(timestamp);
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
	public ArrayList<String> getURLs(String name, int vodID, long timestamp) {
		String baseURL=URLCompute(name, vodID, timestamp);
		ArrayList<String> URLs=new ArrayList<String>();
		for(int i=0; i<domains.size(); i++) {
			URLs.add(domains.get(i)+baseURL);
		}
		try {
			return checkURLs(URLs);
		}
		catch(IOException e) {
			URLs.add(0, "No successful connection was made.");
			URLs.add(1, "Listing all possible URLs:");
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
	public ArrayList<String> BFURLs(String name, int vodID, long timestamp){
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
		DateFormat df=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		Date date=df.parse(timestamp);
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
			for(int i=0; i<8;i++) {
				brt.readLine();
			}
			String response=brt.readLine();
			int contentIndex=response.indexOf("content");
			response=response.substring(contentIndex+contentIndex+9);
			//Get the streamer's name:
			int nameIndex=response.indexOf(" ");
			results[0]=response.substring(0, nameIndex);
			//Get the VOD ID:
			String vodID=url.substring(url.indexOf(results[0])+results[0].length()+9);
			if(vodID.indexOf("/")!=-1) {
				vodID=vodID.substring(0, vodID.indexOf("/"));
			}
			results[1]=vodID;
			//Get timestamp:
			int tsIndex=response.indexOf(" on ")+5;
			results[2]=response.substring(tsIndex, tsIndex+19);
			//Return the array:
			return results;
		}
		throw new IOException();
	}
}