package intercomPi;

import java.io.IOException;

public class InterClassique {

	private String on;
	private String off;
	Runtime runtime;
	Process p;

	InterClassique(String on, String off) {
		this.on = on;
		this.off = off;
		runtime = Runtime.getRuntime();
	}

	public void putOn() {
		try {
			p = runtime.exec(on);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void putOff() {
		try {
			p = runtime.exec(off);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getOn() {
		return on;
	}

	public void setOn(String on) {
		this.on = on;
	}

	public String getOff() {
		return off;
	}

	public void setOff(String off) {
		this.off = off;
	}
}
