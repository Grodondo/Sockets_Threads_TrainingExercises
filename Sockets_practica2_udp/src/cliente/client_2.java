package cliente;

public class client_2 {

	public static void main(String[] args) {

		String ip = "localhost";

		ClientManager cm = new ClientManager(ip, 7000);

		cm.connect();

	}
	
}
