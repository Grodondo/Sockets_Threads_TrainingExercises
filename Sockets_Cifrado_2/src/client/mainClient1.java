package client;

import java.io.IOException;

class mainCliente1 {

	public static void main(String[] args) {

		String serverIP = "192.168.56.1";
		int serverPort = 7000;

        ClientManager client = new ClientManager(serverIP, serverPort, "mega_key");
        client.start();

	}
}