package intercomPi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLogger {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd|HH:mm:ss|");
	//private static Date date = new Date();
	
	public static void log(String s){
		System.out.println(dateFormat.format(new Date()) + getCallingMethod() + "|" + s);
	}
	
	private static String getCallingMethod() {
        	return trace(Thread.currentThread().getStackTrace(), 3);
    	}
 
    	private static String trace(StackTraceElement[] e, int level) {
        	if (e != null && e.length >= level) {
            		StackTraceElement s = e[level];
            		if (s != null) {
                		return s.getClassName() + "." + s.getMethodName()+"("+getLineNumber()+")";
            		}
        	}
        return null;
    }
}
