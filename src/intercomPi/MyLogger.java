package intercomPi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLogger {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd|HH:mm:ss|");
	//private static Date date = new Date();
	

	public static void log(long id, String s){
		
		File f = new File (Main.logfile);
		 
		try
		{
		    FileWriter fw = new FileWriter (f, true);
		 
		    fw.write(dateFormat.format(new Date()) + id + "|" + getCallingMethod() + "|" + s + "\n");
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
