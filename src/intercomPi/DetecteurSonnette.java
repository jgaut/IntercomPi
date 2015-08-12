package intercomPi;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetecteurSonnette {
	/*
	 * Debut Suppression du mkfifo Creation du mkfifo Lancement de la commande
	 * d'ecoute de la sonnette Ecoute du mkfifo Tant que la valeur est
	 * inferieure au SEUIL alors Fin tant que Fin
	 */

	private String commande;
	private boolean keepRunning;
	private Pattern pattern;
	private Matcher m;
	private int seuil;
	private int saut = 10;
	private int[] q;
	private InterClassT iCT;

	private static String RESET = "/bin/sh /home/pi/soundcard/Reset.sh";
	private static String outSonnerie = "/bin/sh /home/pi/soundcard/Playback_to_Lineout.sh";

	public boolean isKeepRunning() {
		return keepRunning;
	}

	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

	private int getMoyenne(int n) {
		int sum = 0;

		for (int i = q.length - 1; i > 0; i--) {
			q[i] = q[i - 1];
		}

		q[0] = n;

		for (int i = 0; i < q.length; i++) {
			sum += q[i];
		}

		return sum / q.length;
	}

	DetecteurSonnette(String commande, int seuil, int moy, InterClassT iCT) {
		this.commande = commande;
		this.keepRunning = true;
		this.pattern = Pattern.compile("[0-9]{2}", Pattern.UNICODE_CASE);
		this.seuil = seuil;
		q = new int[moy];
		for (int i = 0; i < q.length; i++) {
			q[i] = 0;
		}

		// Sortie du son sur l'enceinte
		try {
			Runtime runtime = Runtime.getRuntime();
			Process p;
			System.out.println(RESET);
			p = runtime.exec(RESET);
			p.waitFor();
			System.out.println(outSonnerie);
			p = runtime.exec(outSonnerie);
			p.waitFor();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.iCT = iCT;
	}

	public void start() {
		System.out.println("Start detecteur sonnerie");
		while (keepRunning) {
			System.out.println("keep");
			try {
				Runtime runtime = Runtime.getRuntime();
				System.out.println(commande);
				Process p = runtime.exec(commande);
				InputStream inputStream = p.getInputStream();
				byte[] cbuf = new byte[100];
				String strVal;
				int value, lastValue = 0;
				int moyenne, lastMoyenne = 0;
				while (inputStream.read(cbuf) != -1 && keepRunning) {
					strVal = new String(cbuf, "UTF-8");
					// System.out.println(strVal);
					/* Recherche des valeurs de detection pour une sonnerie */
					m = pattern.matcher(strVal);

					DateFormat dateFormat = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss");
					Date date = new Date();
					if (m.find()) {
						value = Integer.parseInt(strVal.substring(m.start(),
								m.end()));
						moyenne = this.getMoyenne(value);
						if (value != lastValue || moyenne != lastMoyenne) {
							System.out.print(dateFormat.format(date) + " "
									+ "value " + value);
							System.out.println(" moyenne " + moyenne);
						}
						lastValue = value;
						lastMoyenne = moyenne;

						if (moyenne >= seuil && saut <= 0) {
							System.out.println("Sonnerie detectee");
							System.out.println(value);
							this.setKeepRunning(false);
							iCT.on();
						} else {
							iCT.off();
						}
						saut--;
						if (saut <= 0) {
							saut = 0;
						}
					}
				}
				p.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
