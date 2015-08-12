package intercomPi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RecupApp {

	public static List<Appareil> getAppList() {
		List<Appareil> appList = null;
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		try {
			url = new URL("http://jintercom13.appspot.com/jintercom13?compte=c1&action=compte");
			//url = new URL("http://www.google.fr");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			Gson gson = new Gson();
			while ((line = rd.readLine()) != null) {
				appList = gson.fromJson(line, new TypeToken<List<Appareil>>(){}.getType());
			}
			
			System.out.println("Fin de la recup des AppA");
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return appList;
		
	}

}
