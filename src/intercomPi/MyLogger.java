package intercomPi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLogger {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd|HH:mm:ss|");
	private static Date date = new Date();
	
	public static void log(String s){
		System.out.println(dateFormat.format(date) + s);
	}
}
