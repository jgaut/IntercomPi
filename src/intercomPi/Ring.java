package intercomPi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Ring {

	private long id;
	
	// create gpio controller
    GpioController gpio;
    // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
    GpioPinDigitalOutput myGpio;

	//private static boolean status=false;

	Ring(){
		this.id=Thread.currentThread().getId();
		gpio = GpioFactory.getInstance();
		myGpio = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
	}
	
	public void setRing(boolean bool){
			
			if(bool){
				myGpio.setState(PinState.LOW);
			}else{
				myGpio.setState(PinState.HIGH);
			}

		}


}
