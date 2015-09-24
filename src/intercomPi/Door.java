package intercomPi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Door {
	
	//private String openDoor = "/usr/local/bin/gpio mode 7 OUT ; /usr/local/bin/gpio write 7 1 ; sleep 2s ; /usr/local/bin/gpio write 7 0 ;";
	private long id;
	// create gpio controller
    GpioController gpio;
    // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
    GpioPinDigitalOutput myDoor;
    
	Door(){
		this.id=Thread.currentThread().getId();
		gpio = GpioFactory.getInstance();
		myDoor = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
	}
	
	public void open(){
		
		myDoor = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.HIGH);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		myDoor = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
		
	}
}
