package intercomPi;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class InterClassT {

	private static Date date;
	String cmdOff, cmdOn;
	int min, sec;
	Process p;
	private boolean position = true;

	InterClassT(String cmdOn, String cmdOff, int min, int sec) {
		this.cmdOn = cmdOn;
		this.cmdOff = cmdOff;
		this.min = min;
		this.sec = sec;
		date = new GregorianCalendar().getTime();
		this.offForce();
	}

	public void on() {
		try {
			System.out.println(this.getClass().toString() + " " + cmdOn);
			p = Runtime.getRuntime().exec(cmdOn);
			p.waitFor();
			date = new GregorianCalendar().getTime();
			position = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void offForce() {
		try {
			// System.out.println(this.getClass().toString() + " " + cmdOff);
			p = Runtime.getRuntime().exec(cmdOff);
			p.waitFor();
			position = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void off() {
		if (position != false) {
			// System.out.println("Autres passages : " +
			// c.getTime().toString());
			Calendar c1 = new GregorianCalendar();
			c1.setTime(date);
			c1.add(Calendar.MINUTE, min);
			c1.add(Calendar.SECOND, sec);

			Calendar c2 = new GregorianCalendar();

			if (c2.getTime().after(c1.getTime())) {
				this.offForce();
			} else {
				System.out.println(c2.getTime() + " after " + c1.getTime());
			}
		}
	}

}
