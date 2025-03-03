package client;

public class mainClient {

	public static void main(String[] args) {

		String ip = "localhost";
		
		ClientManager cm = new ClientManager(ip, 7000);
		
		cm.connect();
		

	}

}
