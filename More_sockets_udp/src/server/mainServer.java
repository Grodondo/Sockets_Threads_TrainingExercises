package server;

import java.net.SocketException;

public class mainServer {
	public static void main(String[] args) throws SocketException {

		System.out.println("mainServer Practica -1-");
		
		ServerManager sm = new ServerManager(7000);
		
		sm.start();

	}

}
