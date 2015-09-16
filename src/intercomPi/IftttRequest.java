package intercomPi;

import java.io.IOException;

public class IftttRequest extends Thread{

	private String url;
	private long id;
	
	IftttRequest(String url){
		this.url = url;
		
	}
	
	public void run (){
		this.id=Thread.currentThread().getId();
		Process ifttt;
		Runtime runIfttt = Runtime.getRuntime();

		MyLogger.log(id, "Envoi d'une requete IFTTT");
		try {
			ifttt = runIfttt.exec("/usr/bin/curl -X POST "+url);
			ifttt.waitFor();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
