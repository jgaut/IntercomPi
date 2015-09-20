package intercomPi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Main {

	private static RecupApp recupApp;
	private static ArrayList<Communicator> arrayApp;
	// private static String cmdKill =
	// "ps h | grep '$proc' | awk '{ print $1 }'  | xargs kill ";
	private static List<Appareil> listAppA;
	private static String compte = "c1";
	public static String IP;
	public static String logfile;
	private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static long id = Thread.currentThread().getId();
	
	// private static String RESET = "/bin/sh /home/pi/soundcard/Reset.sh";
	// private static String Record =
	// "/bin/sh /home/pi/soundcard/Record_from_Headset.sh";
	// private static String Record =
	// "/bin/sh /home/pi/soundcard/Record_from_lineIn.sh"; 
	// private static String PlayBack =
	// "/bin/sh /home/pi/soundcard/Playback_to_Headset.sh";
	// private static String outSonnerie =
	// "/bin/sh /home/pi/soundcard/Playback_to_Lineout.sh";
	// private static String stringDetector =
	// "arecord -vv -Dhw:1 -f dat /dev/null -c 1";
	// private static String volumeOff = "amixer -Dhw:1 cset numid=2 off";
	// private static String offPhone = "/bin/sh off.sh";
	// private static String onPhone = "/bin/sh on.sh";
	// private static String playSonnerie =
	// "/usr/bin/gst-launch-1.0 filesrc location=unknown_artist-piano_sms.mp3 ! decodebin ! audioconvert ! audioresample ! audio/x-raw, rate=44100 ! alsasink device=hw:0";

	// private static InterClassT iCT;

	public static void main(String[] args) {

		Properties prop = new Properties();
		/* Ici le fichier contenant les donnees de configuration est nomme'db.myproperties' */
		FileInputStream in;
		try {
			in = new FileInputStream("/home/pi/IntercomPi/conf.properties");
			prop.load(in);
			in.close();
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}


		// Extraction des proprietes
		String compte = String.valueOf(prop.getProperty("compte"));
		boolean LOOP = Boolean.valueOf(prop.getProperty("loop"));
		boolean APPEL = Boolean.valueOf(prop.getProperty("appel")); 
		boolean IFTTTSMS = Boolean.valueOf(prop.getProperty("iftttsms"));
		boolean IFTTTNOTIF = Boolean.valueOf(prop.getProperty("iftttnotif")); 
		boolean AUTOOPENDOOR = Boolean.valueOf(prop.getProperty("autoopendoor"));
		int interval = Integer.valueOf(prop.getProperty("interval"));
		String iftttkey = String.valueOf(prop.getProperty("iftttkey"));
		logfile = String.valueOf(prop.getProperty("logfile"))+"/"+dateFormat.format(new Date())+".log";
		//logfile = String.valueOf(prop.getProperty("logfile"))+"/intercom.log";
		 
		try {
			System.setOut(new PrintStream(logfile));
			System.setErr(new PrintStream(logfile));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		MyLogger.log(id, "Initialisation...");

		String curDir = System.getProperty("user.dir");
		MyLogger.log(id, "Le repertoire courant est: "+curDir);
		
		
		MyLogger.log(id, "Ring Thread");
		new RingThread(compte, interval).start();
		
		while (LOOP) { 
			MyLogger.log(id, "Loop...");

			

			// Par defaut, l'interphone classique est mis hors service
			// MyLogger.log("Passage en off de l'interphone classique");
			// iCT = new InterClassT(onPhone, offPhone, 1, 15);

			// MyLogger.log("Seuil : " +args[0]);
			//MyLogger.log("Amplification : " + args[0]);
			// MyLogger.log("Moyenne : " +args[2]);
			//MyLogger.log("timeout :" + args[1]);

			/* Nettoyage des anciens process */
			/*
			 * Runtime runtime = Runtime.getRuntime();
			 * runtime.traceInstructions(true); runtime.traceMethodCalls(true);
			 * Process p; try { MyLogger.log(cmdKill.replace("$proc",
			 * "arecord")); p = runtime.exec(cmdKill.replace("$proc",
			 * "arecord")); p.waitFor();
			 * MyLogger.log(cmdKill.replace("$proc", "gst-launch-0.10"));
			 * p = runtime.exec(cmdKill.replace("$proc", "gst-launch-0.10"));
			 * p.waitFor();
			 * 
			 * MyLogger.log(RESET); p = runtime.exec(RESET); p.waitFor();
			 * 
			 * MyLogger.log(Record); p = runtime.exec(Record);
			 * p.waitFor();
			 * 
			 * //p = runtime.exec(PlayBack); p.waitFor();
			 * 
			 * //p = runtime.exec(volumeOff); //p.waitFor();
			 * 
			 * p.destroy();
			 * 
			 * } catch (IOException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); } catch (InterruptedException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */

			// Creation de la sonnette
			// Sonnette sonnette = new Sonnette();

			/*
			 * Debut Tant que Infini Tant qu'une sonnerie n'est pas detectee Fin
			 * tant que Recuperation des appareils connectes Pour chaque
			 * appareil connecte Lancer un Thread de communication : Gestion de
			 * la communication Attente de la fin de tous les threads de
			 * communication Fin tant que Fin
			 */

			// List<String> ips = new ArrayList<String>();
			/*
			 * try{ Enumeration<NetworkInterface> interfaces =
			 * NetworkInterface.getNetworkInterfaces();
			 * 
			 * while (interfaces.hasMoreElements()) { // carte reseau trouvee
			 * NetworkInterface interfaceN =
			 * (NetworkInterface)interfaces.nextElement();
			 * Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
			 * while (ienum.hasMoreElements()) { // retourne l adresse IPv4 et
			 * IPv6 InetAddress ia = ienum.nextElement(); String adress =
			 * ia.getHostAddress().toString(); if(!adress.startsWith("127")){
			 * IP=adress; } } } } catch(Exception e){
			 * MyLogger.log("pas de carte reseau"); e.printStackTrace(); }
			 * 
			 * MyLogger.log("Mon IP : "+IP);
			 */

			//Commande de deblocage unitaire pour test : gpio mode 1 out; gpio write 1 1; gpio mode 1 IN;
			DetecteurSonnetteNew detectorNew = new DetecteurSonnetteNew();
			// Lancement du detecteur de sonnerie
			detectorNew.start();
			// Si sortie alors sonnerie !!!

			// Jouer la sonnerie
			// sonnette.start();

			//Auto Open Door
			if(AUTOOPENDOOR){
				new OpenDoorAuto(compte).start();
			}

			// IFTTT Notification
			if(IFTTTNOTIF){
				new IftttRequest("https://maker.ifttt.com/trigger/ringIntercomNotif/with/key/"+iftttkey).start();
			}

			// IFTTT SMS
			if(IFTTTSMS){
				new IftttRequest("https://maker.ifttt.com/trigger/ringIntercomSms/with/key/"+iftttkey).start();
			}


			//Appel sur appareil mobile
			if (APPEL == true) {
				// Recuperation des appareils connectes
				MyLogger.log(id, "Recuperation des appareils connectes");
				arrayApp = new ArrayList<Communicator>();
				listAppA = RecupApp.getAppList();
				if (listAppA == null || listAppA.size() == 0) {
					MyLogger.log(id, "Aucun appareil connecte !!");
				} else {
					int size = listAppA.size();
					MyLogger.log(id, size + " appareil(s) connecte(s)");

					for (int i = 0; i < size; i++) {
						MyLogger.log(id, listAppA.get(i).getPortSsh() + ":"
								+ listAppA.get(i).getServer() + ":"
								+ listAppA.get(i).getPort());
						arrayApp.add(i, new Communicator(listAppA.get(i), 1000,
								Double.parseDouble(args[1])));
						arrayApp.get(i).start();
					}

					// Attente de la fin de tous les threads
					for (int i = 0; i < arrayApp.size(); i++) {
						try {
							arrayApp.get(i).join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					MyLogger.log(id, "Fin des appels");
				}
			}

			// LOOP=false;
		}
		MyLogger.log(id, "Fin du porgramme");

	}

	public static String getCompte() {
		return compte;
	}

}
