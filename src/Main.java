import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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
	public String URLCompute(String name, int vodID, int timestamp) {
		String baseString=name+"_"+Integer.toString(vodID)+"_"+Integer.toString(timestamp);
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
	 * THis method gets the successful VOD URLs or if 
	 * unsuccessful to connect to the internet, lists all possibilites.
	 * @param name					Name of the streamer.
	 * @param vodID					VOD ID of the VOD in question.
	 * @param timestamp				UNIX timestamp of the VOD.
	 * @return ArrayList<String>	Returns a string arraylist with all of the working VOD urls.
	 */
	public ArrayList<String> getURLs(String name, int vodID, int timestamp) {
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
}