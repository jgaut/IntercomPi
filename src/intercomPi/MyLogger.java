package intercomPi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLogger {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd|HH:mm:ss|");
	//private static Date date = new Date();
	

	public static void log(String s){
		
		File f = new File ("/var/lib/intercom/intercom.log");
		 
		try
		{
		    FileWriter fw = new FileWriter (f, true);
		 
		    fw.write(dateFormat.format(new Date()) + getCallingMethod() + "|" + s + "\n");
		    fw.close();
		}
		catch (IOException exception)
		{
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}

	}
	
	private static String getCallingMethod() {
        	return trace(Thread.currentThread().getStackTrace(), 3);
    	}
 
    	private static String trace(StackTraceElement[] e, int level) {
        	if (e != null && e.length >= level) {
            		StackTraceElement s = e[level];
            		if (s != null) {
                		return s.getClassName() + "." + s.getMethodName()+"("+s.getLineNumber()+")";
            		}
        	}
        return null;
    }
}
