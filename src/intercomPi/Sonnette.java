package intercomPi;

import java.io.IOException;

public class Sonnette extends Thread {

	private static String playSonnerie = "/usr/bin/gst-launch-0.10 filesrc location=unknown_artist-piano_sms.mp3 ! decodebin ! audioconvert ! audioresample ! audio/x-raw, rate=44100 ! alsasink device=hw:0";

	public void run() {

		// Sonnerie detectee alors on rend la main (peu importe son etat) a
		// l'interphone classique + Sonnerie
		try {
			Runtime.getRuntime().exec(playSonnerie);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

}
