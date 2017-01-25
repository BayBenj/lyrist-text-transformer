package main;/*
 * Found at http://vyvaks.wordpress.com/2006/05/27/does-runtimeexec-hangs-in-java/
 * with pieces from http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4
 */

import java.io.*;
//import org.apache.log4j.Logger;
import java.util.List;
import java.util.ArrayList;

public class CommandlineExecutor{

	//private static Logger logger = Logger.getLogger(main.CommandlineExecutor.class);

	public static void execute(String cmd, String stdOutfile){
		List<String> parameters = new ArrayList<String>();
		boolean inParen = false;
		StringBuilder str = null;
		String[] ungroupedParams = cmd.split("\\s+");

		for(String token: ungroupedParams){
			if(!inParen && token.startsWith("\"")){
				str = new StringBuilder(token);
				inParen = true;
			}
			else if(inParen){
				str.append(" " + token);
				if(token.endsWith("\"")){
					parameters.add(str.toString().substring(1,str.length()-1));
					inParen = false;
				}
			}
			else{
				parameters.add(token);
			}
		}
		execute(parameters.toArray(new String[parameters.size()]),stdOutfile);
	}
	/*
	 * Executes a commandline command and allows for catching of output to a file
	 *
	 * @param String cmd - the full command-line command; must not be null
	 * @param String stdOutfile - name of file to which to catch stout output; if null, output is not caught.
	 */
	public static void execute(String[] cmd, String stdOutfile){
		//logger.info("CMD:" + Arrays.asList(cmd) + (stdOutfile == null? "": " REDIRECT> " + stdOutfile));
		try
		{            
			FileOutputStream fos = (stdOutfile != null ? new FileOutputStream(stdOutfile) : null);
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);
			// any error message?
			ProcessHandler errorGobbler = new ProcessHandler(proc.getErrorStream(), "ERROR");

			// any output?
			ProcessHandler outputGobbler = new ProcessHandler(proc.getInputStream(), "OUTPUT", fos);

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = proc.waitFor();
			//logger.info("ExitValue: " + exitVal);
			if(fos != null){
				fos.flush();
			}    
		} 
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
}
