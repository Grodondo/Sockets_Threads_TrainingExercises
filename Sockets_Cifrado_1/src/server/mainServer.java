package server;

public class mainServer {
	
	public static void main(String[] args) {

		System.out.println("mainServer Practica -1- Cifrado");
		
		ServerManager sm = new ServerManager(7000);
		
		sm.listen();

	}

}
