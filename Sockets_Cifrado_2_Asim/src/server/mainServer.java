package server;

import java.io.IOException;

public class mainServer {

	public static void main(String[] args) throws Exception {
		
		ServerManager sm = new ServerManager(7000);
		
	    sm.start();
		
	}
	
}
