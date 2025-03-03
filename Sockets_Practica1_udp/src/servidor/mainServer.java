package servidor;

public class mainServer {
	public static void main(String[] args) {

		System.out.println("mainServer Practica -1-");
		
		ServerManager sm = new ServerManager(7000);
		
		sm.listen();

	}

}
