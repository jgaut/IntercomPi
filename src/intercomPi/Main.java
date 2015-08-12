package intercomPi;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class Main {

	//private static RecupApp recupApp;
	private static ArrayList<Communicator> arrayApp;
	private static String cmdKill = "ps h | grep '$proc' | awk '{ print $1 }'  | xargs kill ";
	private static List<Appareil> listAppA;
	private static String compte="c1";
	public static String IP;
	private static String RESET = "/bin/sh /home/pi/soundcard/Reset.sh";
	private static String Record = "/bin/sh /home/pi/soundcard/Record_from_Headset.sh";
	//private static String Record = "/bin/sh /home/pi/soundcard/Record_from_lineIn.sh";
	//private static String PlayBack = "/bin/sh /home/pi/soundcard/Playback_to_Headset.sh";
	//private static String outSonnerie = "/bin/sh /home/pi/soundcard/Playback_to_Lineout.sh";
	private static String stringDetector = "arecord -vv -Dhw:1 -f dat /dev/null -c 1";
	//private static String volumeOff = "amixer -Dhw:1 cset numid=2 off";
	private static String offPhone = "/bin/sh off.sh";
	private static String onPhone = "/bin/sh on.sh";
	//private static String playSonnerie = "/usr/bin/gst-launch-1.0 filesrc location=unknown_artist-piano_sms.mp3 ! decodebin ! audioconvert ! audioresample ! audio/x-raw, rate=44100 ! alsasink device=hw:0";
    
	private static InterClassT iCT;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		boolean keep=true;
		boolean APPEL=false;
		
		if(args.length!=2){
			//System.out.println("usage : intercom seuil amplification moyenne timeout");
			System.out.println("usage : intercom amplification timeout");
			keep=false;
		}
		

		//Par defaut, l'interphone classique est mis hors service
		//System.out.println("Passage en off de l'interphone classique");
		//iCT = new InterClassT(onPhone, offPhone, 1, 15);
		 
		while (keep) {
			//keep=false;
			System.out.println("Start Main!");
			//System.out.println("Seuil : " +args[0]);
			System.out.println("Amplification : " +args[0]);
			//System.out.println("Moyenne : " +args[2]);
			System.out.println("timeout :" +args[1]);
			
			/* Nettoyage des anciens process */
			/*
			Runtime runtime = Runtime.getRuntime();
			runtime.traceInstructions(true);
			runtime.traceMethodCalls(true);
			Process p;
			try {
				System.out.println(cmdKill.replace("$proc", "arecord"));
				p = runtime.exec(cmdKill.replace("$proc", "arecord"));
				p.waitFor();
				System.out.println(cmdKill.replace("$proc", "gst-launch-0.10"));
				p = runtime.exec(cmdKill.replace("$proc", "gst-launch-0.10"));
				p.waitFor();
				
				System.out.println(RESET);
				p = runtime.exec(RESET);
				p.waitFor();
				
				System.out.println(Record);
				p = runtime.exec(Record);
				p.waitFor();
				
				//p = runtime.exec(PlayBack);
				p.waitFor();
							
				//p = runtime.exec(volumeOff);
				//p.waitFor();
				
				p.destroy();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/

			//Creation de la sonnette
			//Sonnette sonnette = new Sonnette();
			
			/*
			 * Debut Tant que Infini Tant qu'une sonnerie n'est pas detectee Fin
			 * tant que Recuperation des appareils connectes Pour chaque
			 * appareil connecte Lancer un Thread de communication : Gestion de
			 * la communication Attente de la fin de tous les threads de
			 * communication Fin tant que Fin
			 */

			//List<String> ips = new ArrayList<String>();
			 /*
			try{
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		 
		         while (interfaces.hasMoreElements()) {  // carte reseau trouvee
		            NetworkInterface interfaceN = (NetworkInterface)interfaces.nextElement(); 
		            Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
		            while (ienum.hasMoreElements()) {  // retourne l adresse IPv4 et IPv6
		                InetAddress ia = ienum.nextElement();
		                String adress = ia.getHostAddress().toString();
		                if(!adress.startsWith("127")){
		                	IP=adress;
		                }       
		            }
		        }
		    }
		    catch(Exception e){
		        System.out.println("pas de carte reseau");
		        e.printStackTrace();
		    }
			
			System.out.println("Mon IP : "+IP);*/
			// Construction du detecteur de sonnerie
			//DetecteurSonnette detector = new DetecteurSonnette(stringDetector, Integer.parseInt(args[0]), Integer.parseInt(args[2]), iCT);
			DetecteurSonnetteNew detectorNew = new DetecteurSonnetteNew();
			
			// Lancement du detecteur de sonnerie
			//detector.start();
			detectorNew.start();
			//Si sortie alors sonnerie !!!
			
			//Jouer la sonnerie
			//sonnette.start();
			
			//Envoi email
			Process email;
			Runtime runEmail= Runtime.getRuntime();;
			try {
				System.out.println("Envoi d'email");
				email = runEmail.exec("/home/pi/jar/email.sh");
				InputStream ips = email.getErrorStream();
				byte[] buff = new byte[100];
				if(ips.read(buff) != -1){
					String valeur = new String(buff, "UTF-8");
					System.out.println(valeur);
				}
				
				ips.close();
			} catch (IOException e1) { 
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(APPEL == true){
				// Recuperation des appareils connectes
				System.out.println("Recuperation des appareils connectes");
				arrayApp = new ArrayList<Communicator>();
				listAppA = RecupApp.getAppList();
				if(listAppA==null || listAppA.size()==0){
					System.out.println("Aucun appareil connecte !!");
				}else{
					int size = listAppA.size(); 
					System.out.println(size+" appareil(s) connecte(s)");
					
					for (int i = 0; i < size; i++) {
						System.out.println(listAppA.get(i).getPortSsh()+":"+listAppA.get(i).getServer()+":"+listAppA.get(i).getPort());
						arrayApp.add(i, new Communicator(listAppA.get(i), 1000, Double.parseDouble(args[1])));
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
					
					System.out.println("Fin des appels");
				}
			}
			
			//keep=false;
		}
	}

	public static String getCompte() {
		return compte;
	}


}
