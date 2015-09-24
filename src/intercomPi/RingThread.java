package intercomPi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RingThread extends Thread{

	private int interval;
	private String compte;
	private long id;

	RingThread(String compte, int interval){
		this.interval = interval;
		this.compte = compte;
		
	}

	public void run() {
		this.id = Thread.currentThread().getId();
		boolean res = false;
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		MyLogger.log(id, "Thread loop");
		Ring ring = new Ring();
		
		while(true){
			try {
				url = new URL("http://1-dot-intercomwebgae.appspot.com/od/?action=ring&compte="+compte);
				//MyLogger.log(id, url.toString());
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				rd = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				
				while (conn.getResponseCode() == HttpURLConnection.HTTP_OK && (line = rd.readLine()) != null) {
					//MyLogger.log(id, line);
					res = new Gson().fromJson(line, new TypeToken<Boolean>() {
					}.getType());
				}

				//MyLogger.log(id, "Resultat : "+res);
				rd.close();
				conn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//Si auto open door prevu alors on met sur off la sonnette
			//if(res){
				//Ring.setRing(false);
			//}
			ring.setRing(!res);
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

}
