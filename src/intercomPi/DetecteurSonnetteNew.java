package intercomPi;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetecteurSonnetteNew {
	/*
	 * Debut Suppression du mkfifo Creation du mkfifo Lancement de la commande
	 * d'ecoute de la sonnette Ecoute du mkfifo Tant que la valeur est
	 * inferieure au SEUIL alors Fin tant que Fin
	 */

	DetecteurSonnetteNew() {
	}

	public void start() {
		
		System.out.println("Start new detecteur sonnerie");
		
		int[] tab = new int[10];
		byte[] buff = new byte[100];
		int nbB = 10;
		String[] gInit = {"/usr/local/bin/gpio mode 1 in","/usr/local/bin/gpio mode 4 out","/usr/local/bin/gpio write 4 1"};
		String gRead = "/usr/local/bin/gpio read 1";
		Runtime runtime;
		Process p;
		InputStream ips;
		

		try {
			runtime = Runtime.getRuntime();
			System.out.println("Initialisation des PIN : ");
			for(int i=0;i<gInit.length;i++){
				System.out.println(gInit[i]);
				p = runtime.exec(gInit[i]);
				ips = p.getErrorStream();
				if(ips.read(buff) != -1){
					String valeur = new String(buff, "UTF-8");
					System.out.println(valeur);
				}
				
				ips.close();
				p.waitFor();
			}
			System.out.println();

			System.out.println("Commande de lecture du PIN : ");
			System.out.println(gRead);
			System.out.println();
		
			
			//Chargement du buffeur
			/*for(int i=0; i>10; i++){
				p = runtime.exec(gRead);		
				ips = p.getInputStream();
				if(ips.read(buff) != -1){
					tab[i]=new Integer (new String(buff, "UTF-8").substring(0, 1));
				}
			}*/
			
			
			Integer valeur=0;
			while (valeur ==0) {
				p = runtime.exec(gRead);		
				ips = p.getInputStream();
				if(ips.read(buff) != -1){
					valeur = new Integer (new String(buff, "UTF-8").substring(0, 1));
					//System.out.println(valeur);
				}
				
				ips.close(); 
			}
			
			System.out.println("Sonnerie detectee : "+valeur);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
