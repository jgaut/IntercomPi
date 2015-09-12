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

	RingThread(String compte, int interval){
		this.interval = interval;
		this.compte = compte;
	}

	public void run() {
		boolean res = false;
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		MyLogger.log("Thread loop");
		while(true){
			try {
				url = new URL(
						"http://1-dot-intercomwebgae.appspot.com/od/?action=ring&compte="+compte);
				//MyLogger.log(url.toString());
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				rd = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				Gson gson = new Gson();
				while ((line = rd.readLine()) != null) {
					//MyLogger.log(line);
					res = gson.fromJson(line, new TypeToken<Boolean>() {
					}.getType());
				}

				//MyLogger.log("Resultat : "+res);
				rd.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//Si auto open door prevu alors on met sur off la sonnette
			//if(res){
				//Ring.setRing(false);
			//}
			Ring.setRing(!res);
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

}
