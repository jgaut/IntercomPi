package intercomPi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Door {
	
	private String openDoor = "/usr/local/bin/gpio mode 7 OUT ; /usr/local/bin/gpio write 7 1 ; sleep 2s ; /usr/local/bin/gpio write 7 0 ;";
	
	
	
	public void open(){
		String tabS[] = openDoor.split(";");
		for (int i = 0; i < tabS.length; i++) {
			MyLogger.log(tabS[i]);
			Process opDoor;
			try {
				opDoor = Runtime.getRuntime().exec(tabS[i]);
				BufferedReader output = getOutput(opDoor);
				BufferedReader error = getError(opDoor);
				String ligne = "";
				opDoor.waitFor();

				while ((ligne = output.readLine()) != null) {
					MyLogger.log(ligne);
				}

				while ((ligne = error.readLine()) != null) {
					MyLogger.log(ligne);
				}
				opDoor.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private BufferedReader getOutput(Process p) {
		return new BufferedReader(new InputStreamReader(p.getInputStream()));
	}

	private BufferedReader getError(Process p) {
		return new BufferedReader(new InputStreamReader(p.getErrorStream()));
	}
}
