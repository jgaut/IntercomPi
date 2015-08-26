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
	
<<<<<<< Updated upstream
	public static void log(String s){
		System.out.println(dateFormat.format(new Date()) + getCallingMethod() + "|" + s);
=======
	public static void log(String s){ 
		//System.out.println(dateFormat.format(date) + s);
		
		File f = new File ("/var/lib/intercom/intercom.log");
		 
		try
		{
		    FileWriter fw = new FileWriter (f, true);
		 
		    fw.write(dateFormat.format(date) + s + "\n");
		    fw.close();
		}
		catch (IOException exception)
		{
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
>>>>>>> Stashed changes
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
