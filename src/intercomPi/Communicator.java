package intercomPi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/*import com.jcraft.jsch.JSch;
 import com.jcraft.jsch.JSchException;
 import com.jcraft.jsch.Session;
 */
public class Communicator extends Thread {

    /*
     * Debut Connexion a l'appareil associe (AA) Si connecte alors Attente
     * d'acceptation de la communication de la part de l'AA Si l'AA accepte
     * alors Si Verrouillage de la communication alors
     * 
     * Ouverture du flux audio de reception (thread) Envoi du port du flux audio
     * de reception
     * 
     * Attente de reception du port du flux audio d'emission Reception du port
     * du flux audio d'emission Ouverture du flux audio d'emission (thread)
     * 
     * Attente pour ordre de la part de l'AA Tant que l'ordre recu n'est pas fin
     * de communication Si ordre recu est "ouverture de porte" alors Procedure
     * d'ouverture de porte Fin si Fin tant que
     * 
     * Si non Envoi a l'AA du refus de communication (Communication deja en
     * cours avec un autre AA) Fin Si Fin si
     * 
     * Si l'AA refuse la communication alors Fin si Fin si Fermeture de la
     * communication Fin
     */
    private Appareil app;
    private Socket soc;
    private BufferedReader in;
    private PrintWriter out;
    private static boolean verrou = false;
    private String udpSend = "/usr/bin/gst-launch-0.10 alsasrc device=hw:0 ! capsfilter caps=audio/x-raw,rate=44100 ! audioconvert ! audioresample ! audio/x-raw, rate=6000, channels=1 ! speexenc quality=5 ! rtpspeexpay ! udpsink host=myip port=myport";
    private String udpReceive = "/usr/bin/gst-launch-0.10 udpsrc port=myport caps=\"application/x-rtp, media=(string)audio, clock-rate=(int)6000, encoding-name=(string)SPEEX\" ! rtpjitterbuffer latency=150 do-lost=true ! rtpspeexdepay ! speexdec ! audioconvert ! audioresample ! audio/x-raw, rate=44100 ! audioamplify amplification=myamp ! alsasink device=hw:1";
    //private String openDoor = "/usr/local/bin/gpio mode 16 OUT; /usr/local/bin/gpio write 16 0 ; sleep 2s; /usr/local/bin/gpio write 16 1; /usr/local/bin/gpio mode 16 ALT0;";
    // private String testSound
    // ="/usr/bin/gst-launch-1.0 filesrc location=/home/pi/jar/302.wav ! decodebin ! audioconvert ! audioresample ! audio/x-raw, rate=44100 ! alsasink device=hw:1";
    private String testSound = "/home/pi/jar/ding1.sh $A";
    private String volumeSet = "amixer -Dhw:1 cset numid=1 -- myvol%";
    private static String volumeOn = "amixer -Dhw:1 cset numid=2 on";
    // private int TPSPAUSE;
    private int timeout;
    private double amp;
    // private Session session;
    private int portR = 10001;
    // private String getPortUdp =
    // "netstat -laputen | grep gst-launch | awk '{ print $4 }'";
    private int VOLUME;

    // private static final int ERR=-1;
    private static final int GO = 0;
    private static final int CLOSE = 1;
    private static final int HB = 2;
    private static final int OPENDOOR = 3;
    // private static final int OK = 4;
    private static final int TS = 5;
    private static final int VOLUP = 6;
    private static final int VOLDOWN = 7;
    private static final int P50 = 8;

    private static final int volMin = 0;
    private static final int volMax = 100;

    private long id;
    private String mess;

    public Communicator(Appareil app) {
	this.id = Thread.currentThread().getId();
	this.app = app;
    }

    public Communicator(Appareil app, int timeout, double amp) {
	// this.TPSPAUSE = tpspause;
	this.app = app;
	this.timeout = timeout;
	this.amp = amp;
    }

    public void run() {
	MyLogger.log(id,"Lancement du thread : " + app.getServer() + ":"
		+ app.getPort());
	try {
	    /*
	     * SSH : port forwarding JSch jsch=new JSch();
	     * session=jsch.getSession(login, app.getServer(),
	     * app.getPortSsh()); java.util.Properties config = new
	     * java.util.Properties(); config.put("StrictHostKeyChecking",
	     * "no"); session.setConfig(config); session.setPassword(pwd);
	     * MyLogger.log(id,"Connexion ssh : "+session.toString());
	     * session.connect();
	     * MyLogger.log(id,"Mise en place du port forwarding TCP");
	     * MyLogger.log(id,"setPortForwardingL :");
	     * session.setPortForwardingL(0, "localhost", app.getPort());
	     * MyLogger.log(id,"ssh"); String [] tab =
	     * session.getPortForwardingL(); for(int i=0; i<tab.length;i++){
	     * MyLogger.log(id,tab[i]); } int p =
	     * Integer.parseInt(tab[0].split(":")[0]);
	     */

	    /* Tentative de connexion */
	    soc = new Socket();
	    // soc.connect(new InetSocketAddress("localhost", p), timeout);
	    soc.connect(new InetSocketAddress(app.getServer(), app.getPort()),
		    timeout);
	    if (soc.isConnected()) {
		MyLogger.log(id,"Connexion principale reussie (TCP)");

		in = new BufferedReader(new InputStreamReader(
			soc.getInputStream()));
		out = new PrintWriter(soc.getOutputStream());

		/* Attente de l'acceptation de la part du client */
		MyLogger.log(id,"Attente d'acceptation");

		if ((mess = in.readLine()).equals(String.valueOf(GO))) {
		    MyLogger.log(id,"Connexion acceptee (" + mess+")");

		    /* Verrouillage de la communication */
		    MyLogger.log(id,"Tentative de verrouillage de la communication (une seule communication en meme temps est possible)");
		    if (this.BlockVerrou()) {

			MyLogger.log(id,"Communication verrouillee");

			// Envoi des infos OK
			MyLogger.log(id,"Echange des informations necessaires auxcommunications secondaires en UDP");
			String ssid = "XX";
			MyLogger.log(id,soc.getLocalAddress() + ":" + portR+ ":" + ssid);
			out.println(soc.getLocalAddress() + ":" + portR + ":"+ ssid);
			out.flush();

			// Attente des infos OK
			String[] rep = in.readLine().split(":");

			// Reception d'une reponse correcte : infos OK
			if (rep != null && rep.length == 3) {

			    Runtime runtimeRec, runtimeSen, runtimeDoor, runtimeVolume;
			    Process pRec = null, pSen = null, opDoor, pVol;
			    String cRec, cSen, cVol;
			    boolean loop = false;

			    if (rep[2].equals(ssid) && !rep[2].equals("null")
				    && !ssid.equals("null")) {
				// On est dans le meme reseau WIFI

				// Ouverture du flux audio de reception (thread)
				runtimeRec = Runtime.getRuntime();
				cRec = this.udpReceive;
				cRec = cRec.replace("myport",String.valueOf(portR));
				cRec = cRec.replace("myamp",String.valueOf(amp));
				MyLogger.log(id,cRec);
				pRec = runtimeRec.exec(cRec);

				/* Mise a 50% de la sortie Dhw:1 */
				runtimeVolume = Runtime.getRuntime();
				/* Mise sur on de la sortie audio interphone */
				pVol = runtimeVolume.exec(volumeOn);
				pVol.waitFor();
				cVol = this.volumeSet;
				VOLUME = (Math.abs(this.volMax) + Math.abs(this.volMin)) / 2;
				VOLUME = this.volMin + VOLUME;
				cVol = cVol.replace("myvol",String.valueOf(VOLUME));
				System.out.println("Reglage du volume de sortie : "+ cVol);
				pVol = runtimeVolume.exec(cVol);
				pVol.waitFor();

				/*
				 * Attente de reception du port du flux audio
				 * d'emission Reception du port du flux audio
				 * d'emission Ouverture du flux audio d'emission
				 * (thread)
				 */
				String myip = rep[0];
				String myport = rep[1];
				MyLogger.log(id,"IP distante : " + myip);
				MyLogger.log(id,"Port distant : " + myport);
				runtimeSen = Runtime.getRuntime();
				cSen = this.udpSend.replace("myip", myip);
				cSen = cSen.replace("myport", myport);
				MyLogger.log(id,cSen);
				pSen = runtimeRec.exec(cSen);
				loop = true;

			    } else {
				// On n'est pas dans le meme reseau WIFI
			    }

			    while (loop) {
				int sw = Integer.parseInt(in.readLine());
				switch (sw) {
				case OPENDOOR:
				   new Door().open();
				    MyLogger.log(id,"opendoor");
				    break;
				case CLOSE:
				    loop = false;
				    MyLogger.log(id,"close");
				    break;
				case HB:
				    MyLogger.log(id,"hb");
				    break;
				case TS:
				    System.out.print("test sound : ");
				    testSound = testSound.replace("$A",String.valueOf(this.amp));
				    opDoor = Runtime.getRuntime().exec(testSound);
				    opDoor.waitFor();
				    MyLogger.log(id,"OK");
				    MyLogger.log(id,testSound);
				    break;
				case VOLUP:
				    cVol = this.volumeSet;
				    VOLUME += (Math.abs(this.volMax) + Math.abs(this.volMin)) / 20;
				    VOLUME = Math.min(VOLUME, this.volMax);
				    cVol = cVol.replace("myvol",String.valueOf(VOLUME));
				    MyLogger.log(id,"Reglage du volume de sortie : "+ cVol);
				    opDoor = Runtime.getRuntime().exec(cVol);
				    opDoor.waitFor();
				    MyLogger.log(id,"Volume UP");
				    break;
				case VOLDOWN:
				    cVol = this.volumeSet;
				    VOLUME -= (Math.abs(this.volMax) + Math.abs(this.volMin)) / 20;
				    VOLUME = Math.max(VOLUME, this.volMin);
				    cVol = cVol.replace("myvol",String.valueOf(VOLUME));
				    System.out.println("Reglage du volume de sortie : "+ cVol);
				    opDoor = Runtime.getRuntime().exec(cVol);
				    opDoor.waitFor();
				    MyLogger.log(id,"Volume DOWN");
				    break;
				case P50:
				    cVol = this.volumeSet;
				    VOLUME = (Math.abs(this.volMax) + Math.abs(this.volMin)) / 2;
				    VOLUME = this.volMin + VOLUME;
				    cVol = cVol.replace("myvol",String.valueOf(VOLUME));
				    System.out.println("Reglage du volume de sortie : "+ cVol);
				    opDoor = Runtime.getRuntime().exec(cVol);
				    opDoor.waitFor();
				    MyLogger.log(id,"Volume DOWN");
				    break;
				default:
				    System.out.println("Reception d'un message inconnu : "+ sw);
				    break;
				}
			    }

			    if (pSen != null) {
				pSen.destroy();
			    }
			    if (pRec != null) {
				pRec.destroy();
			    }
			}

		    }
		} else {
		    MyLogger.log(id,"erreur : " + mess);
		}
	    }
	    soc.close();
	    // session.disconnect();
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    System.out.print(this.toString());
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    System.out.print(this.toString());
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} /*
	 * catch (JSchException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 */
	ReleaseVerrou();
	MyLogger.log(id,"Fin du thread : " + app.getPortSsh() + ":"+ app.getServer() + ":" + app.getPort());
    }

    synchronized boolean BlockVerrou() {

	if (this.verrou) {
	    return false;
	}

	this.verrou = true;
	return this.verrou;

    }

    synchronized void ReleaseVerrou() {
	this.verrou = false;

    }

    private static BufferedReader getOutput(Process p) {
	return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static BufferedReader getError(Process p) {
	return new BufferedReader(new InputStreamReader(p.getErrorStream()));
    }
}
