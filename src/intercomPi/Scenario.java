package intercomPi;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

	private long id;
	private ArrayList<Communicator> arrayApp;
	private List<Appareil> listAppA;

	Scenario(){
		this.id = Thread.currentThread().getId();
	}

	public void launch(){

		//Auto Open Door
		if(Main.AUTOOPENDOOR){
			new OpenDoorAuto(Main.compte).start();
		}

		// IFTTT Notification
		if(Main.IFTTTNOTIF){
			new IftttRequest("https://maker.ifttt.com/trigger/ringIntercomNotif/with/key/"+Main.iftttkey).start();
		}

		// IFTTT SMS
		if(Main.IFTTTSMS){
			new IftttRequest("https://maker.ifttt.com/trigger/ringIntercomSms/with/key/"+Main.iftttkey).start();
		}
		
		//Appel sur appareil mobile
		if (Main.APPEL == true) {
			// Recuperation des appareils connectes
			MyLogger.log(id, "Recuperation des appareils connectes");
			arrayApp = new ArrayList<Communicator>();
			listAppA = RecupApp.getAppList();
			if (listAppA == null || listAppA.size() == 0) {
				MyLogger.log(id, "Aucun appareil connecte");
			} else {
				int size = listAppA.size();
				MyLogger.log(id, size + " appareil(s) connecte(s)");

				for (int i = 0; i < size; i++) {
					MyLogger.log(id, listAppA.get(i).getPortSsh() + ":"
							+ listAppA.get(i).getServer() + ":"
							+ listAppA.get(i).getPort());
					arrayApp.add(i, new Communicator(listAppA.get(i), Main.timeoutAppel,
							Main.amplificationAppel));
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
	}
}
