package client;

public class mainClient2 {

	public static void main(String[] args) {

		String ip = "192.168.56.1";
		
		ClientManager cm = new ClientManager(ip, 7000, 7, 2);
		
		cm.connect();
		

	}

}
