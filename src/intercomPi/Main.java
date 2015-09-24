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

	public static String compte;
	public static String IP;
	public static String logfile;
	private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static long id = Thread.currentThread().getId();
	public static boolean APPEL;
	public static boolean IFTTTSMS;
	public static boolean IFTTTNOTIF;
	public static boolean AUTOOPENDOOR;
	public static boolean LOOP;
	public static String iftttkey;
	
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
		compte = String.valueOf(prop.getProperty("compte"));
		LOOP = Boolean.valueOf(prop.getProperty("loop"));
		APPEL = Boolean.valueOf(prop.getProperty("appel")); 
		IFTTTSMS = Boolean.valueOf(prop.getProperty("iftttsms"));
		IFTTTNOTIF = Boolean.valueOf(prop.getProperty("iftttnotif")); 
		AUTOOPENDOOR = Boolean.valueOf(prop.getProperty("autoopendoor"));
		int interval = Integer.valueOf(prop.getProperty("interval"));
		iftttkey = String.valueOf(prop.getProperty("iftttkey"));
		//logfile = String.valueOf(prop.getProperty("logfile"))+"/"+dateFormat.format(new Date())+".log";
		logfile = String.valueOf(prop.getProperty("logfile"))+"/intercom.log";
		 
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

		
		DetectorGPIO gpioD = new DetectorGPIO();
		gpioD.start();
		
		MyLogger.log(id, "Fin du main");	

	}

	public static String getCompte() {
		return compte;
	}

}
