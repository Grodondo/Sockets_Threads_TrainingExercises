package cliente;

import java.io.IOException;

public class mainCliente {

    public static void main(String[] args) {
        // Server IP and port
        String serverIP = "localhost";
        int serverPort = 7000;

        ClientManager client = new ClientManager(serverIP, serverPort);
        client.start();
    }
}

class mainCliente1 {

	public static void main(String[] args) {

		String serverIP = "192.168.56.1";
		int serverPort = 7000;

        ClientManager client = new ClientManager(serverIP, serverPort);
        client.start();

	}
}

class mainCliente2 {

	public static void main(String[] args) {

		String serverIP = "192.168.56.1";
		int serverPort = 7000;

        ClientManager client = new ClientManager(serverIP, serverPort);
        client.start();

	}
}

class mainCliente3 {

	public static void main(String[] args) {

		String serverIP = "192.168.56.1";
		int serverPort = 7000;

        ClientManager client = new ClientManager(serverIP, serverPort);
        client.start();

	}
}
