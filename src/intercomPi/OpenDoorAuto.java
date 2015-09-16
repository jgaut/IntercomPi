package intercomPi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OpenDoorAuto extends Thread{

	private String compte;
	private long id;
	
	OpenDoorAuto(String compte){
		this.compte=compte;
		
	}
	
	public void run() {
		this.id=Thread.currentThread().getId();
		boolean res = false;
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		MyLogger.log(id, "Auto Open Door ?");
		try {
			url = new URL(
					"http://1-dot-intercomwebgae.appspot.com/od/?action=open&compte="+compte);
			MyLogger.log(id, url.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			Gson gson = new Gson();
			while ((line = rd.readLine()) != null) {
				MyLogger.log(id, line);
				res = gson.fromJson(line, new TypeToken<Boolean>() {
				}.getType());
			}

			MyLogger.log(id, "Resultat : "+res);
			rd.close();
		} catch (IOException e) {
			Thread.currentThread().interrupt();
			MyLogger.log(id,e.getMessage());
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			MyLogger.log(id,e.getMessage());
		}

		if(res){
			new Door().open();
		}

	}

}
