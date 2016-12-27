package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

//import org.apache.log4j.Logger;

public class ProcessHandler extends Thread {

	//private static Logger logger = Logger.getLogger(external.ProcessHandler.class);

	InputStream is;
	String type;
	OutputStream os;

	public ProcessHandler(InputStream is, String type) {
		this(is, type, null);
	}

	public ProcessHandler(InputStream is, String type, OutputStream redirect) {
		this.is = is;
		this.type = type;
		this.os = redirect;
	}

	public void run() {
		try
		{
			PrintWriter pw = null;
			if (os != null){
				pw = new PrintWriter(os);
			}

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line=null;
			while ( (line = br.readLine()) != null)
			{
				if (pw != null){
					pw.println(line);
				}
//				else logger.info(type + ">" + line);    
			}
			br.close();
			if (pw != null){
				pw.flush();
			}
		} 
		catch (IOException ioe)
		{
			ioe.printStackTrace();  
		}
	}		
}
