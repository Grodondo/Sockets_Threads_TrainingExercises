package servidor;

import java.io.IOException;

public class mainServidor {

	public static void main(String[] args) {
		
		ServerManager sm = new ServerManager(7000);
		
	    sm.start();
		
	}
	
}
