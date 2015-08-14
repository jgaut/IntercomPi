package intercomPi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OpenDoorAuto {

	public static boolean allowToOpenDoor(String compte) {
		boolean res = false;
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		try {
			url = new URL(
					"http://1-dot-intercomwebgae.appspot.com/od/?action=open&compte="+compte);
			MyLogger.log(url.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			Gson gson = new Gson();
			while ((line = rd.readLine()) != null) {
				MyLogger.log(line);
				res = gson.fromJson(line, new TypeToken<Boolean>() {
				}.getType());
			}

			MyLogger.log("Resultat : "+res);
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;

	}

}
