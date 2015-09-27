package intercomPi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
		HttpURLConnection conn=null;
		BufferedReader rd;
		String line;
		MyLogger.log(id, "Thread loop");
		Ring ring = new Ring();
		Gson gson = new Gson(); 
		Type type = new TypeToken<Boolean>() {
		}.getType();
		URL url=null;
		
		while(true){
			try {
				//MyLogger.log(id, url.toString());
				url = new URL("http://1-dot-intercomwebgae.appspot.com/od/?action=ring&compte="+compte);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				rd = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				
				while (conn.getResponseCode() == HttpURLConnection.HTTP_OK && (line = rd.readLine()) != null) {
					//MyLogger.log(id, line);
					res = gson.fromJson(line, type);
				}

				//MyLogger.log(id, "Resultat : "+res);
				rd.close();
				conn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

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
