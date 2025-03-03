package client;

import java.io.IOException;

public class mainClient {

    public static void main(String[] args) {
        // Server IP and port
        String serverIP = "localhost";
        int serverPort = 7000;

        ClientManager client = new ClientManager(serverIP, serverPort, "mega_key");
        client.start();
    }
}

