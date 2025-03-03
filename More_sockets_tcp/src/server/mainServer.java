package server;

public class mainServer {
		
	public static void main(String[] args) {
		
		ServerManager sm = new ServerManager(7000);
		
		sm.start();

	}

}

