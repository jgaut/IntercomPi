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
    GpioController gpio;
    // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
    GpioPinDigitalInput myDoor;
    
	DetectorGPIO(){
		gpio = GpioFactory.getInstance();
		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.HIGH);
		myDoor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
	}
	
	public void run(){
		
		// create a gpio callback trigger on gpio pin#4; when #4 changes state, perform a callback
        // invocation on the user defined 'Callable' class instance
		myDoor.addTrigger(new GpioCallbackTrigger(new Callable<Void>() {
            public Void call() throws Exception {
            	MyLogger.log(Thread.currentThread().getId(), "Changement de valeur pour le pin 1");
            	MyLogger.log(Thread.currentThread().getId(), myDoor.getState().toString());
            	if(myDoor!=null && myDoor.getState().isHigh()){
            		MyLogger.log(Thread.currentThread().getId(), "Sonnette !!");
            		Scenario scen = new Scenario();
            		scen.launch();
            	}
                return null;
            }
        }));
    
	}
	
}
