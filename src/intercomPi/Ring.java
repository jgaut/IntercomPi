package intercomPi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ring {

	private static String setOndoor = "/usr/local/bin/gpio mode 2 IN";
	private static String setOffdoor = "/usr/local/bin/gpio mode 2 OUT ; /usr/local/bin/gpio write 2 1 ;";


	public static void setRing(boolean bool){

		String cmd;
		if(bool){
			cmd = setOndoor;
		}else{
			cmd = setOffdoor;
		}

		String tabS[] = cmd.split(";");
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
	
	private static BufferedReader getOutput(Process p) {
		return new BufferedReader(new InputStreamReader(p.getInputStream()));
	}

	private static BufferedReader getError(Process p) {
		return new BufferedReader(new InputStreamReader(p.getErrorStream()));
	}

}