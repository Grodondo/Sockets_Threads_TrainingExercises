package servidor;

public class server_1 {
	
	public static void main(String[] args) {

		System.out.println("mainServer Practica -1-");

		ServerManager sm = new ServerManager(7000);

		sm.start();

	}
}
