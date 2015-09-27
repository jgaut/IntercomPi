package intercomPi;

import java.util.concurrent.Callable;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;

public class DetectorGPIO extends Thread{

	// create gpio controller
    private GpioController gpio;
    // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
    private GpioPinDigitalInput myGpio;
    static boolean verrou = false;
    private long id;
    
	DetectorGPIO(){
		this.id = Thread.currentThread().getId();
		gpio = GpioFactory.getInstance();
		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.HIGH);
		myGpio = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
	}
	
	public void run(){
		
		// create a gpio callback trigger on gpio pin#4; when #4 changes state, perform a callback
        // invocation on the user defined 'Callable' class instance
		myGpio.addTrigger(new GpioCallbackTrigger(new Callable<Void>() {
            public Void call() throws Exception {
            	//MyLogger.log(Thread.currentThread().getId(), "Changement de valeur pour le pin 1");
            	//MyLogger.log(Thread.currentThread().getId(), myGpio.getState().toString());
            	if(myGpio!=null && myGpio.getState().isHigh()){
            		if(BlockVerrou()){
            			MyLogger.log(id, "Sonnette & verrou bloque");
            			MyLogger.log(id, "Lancement du scenario...");	
            			Scenario scen = new Scenario();
                		scen.launch();
                		//Attente avant de relacher le verrou
                		try {
                			Thread.sleep(5000);
                		} catch (InterruptedException e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		}
                		ReleaseVerrou();
            			MyLogger.log(Thread.currentThread().getId(), "Fin de scenario et verrou debloque");
            		}
            	}
                return null;
            }
        }));
    
	}
	
	synchronized boolean BlockVerrou() {

		if (DetectorGPIO.verrou) {
			return false;
		}

		DetectorGPIO.verrou = true;
		return DetectorGPIO.verrou;

	}

	synchronized void ReleaseVerrou() {
		DetectorGPIO.verrou = false;

	}
	
}
