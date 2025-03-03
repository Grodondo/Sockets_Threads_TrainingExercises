package server;

import java.io.IOException;

public class mainServer {

	public static void main(String[] args) {
		
		ServerManager sm = new ServerManager(7000,  "mega_key");
		
	    sm.start();
		
	}
	
}
