package client;

public class mainClient {

	public static void main(String[] args) {

		String ip = "192.168.56.1";
		
		ClientManager cm = new ClientManager(ip, 7000, 10, 1);
		
		cm.connect();
		

	}

}
