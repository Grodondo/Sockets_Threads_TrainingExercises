package server;

import util.util;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import javax.crypto.spec.SecretKeySpec;

public class ServerManager {
    private int puerto;
    private boolean fin = false;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    
    private int acceptedMessageSize;
    private SecretKeySpec key;

    public ServerManager(int puerto, String key) {
        this.puerto = puerto;
        this.clients = new ArrayList<ClientHandler>();
        
        this.acceptedMessageSize = 32;
        byte[] keyBytes = Arrays.copyOf(key.getBytes(), acceptedMessageSize);
        this.key = new SecretKeySpec(keyBytes, "AES");
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor iniciado en el puerto: " + puerto);

            while (!fin) {
            	if (clients == null) {
            		System.out.println("Esperando clientes...");
            	}
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                
                ClientHandler client = new ClientHandler(clientSocket);

                new Thread(client).start();
                clients.add(client);
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        } finally {
            stop();
        }
    }

    public void stop() {
        fin = true;
        try {
            if (serverSocket != null) serverSocket.close();
            System.out.println("Servidor detenido.");
        } catch (IOException e) {
            System.err.println("Error al detener el servidor: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            // Recibimos el mensaje del cliente y lo reenviamos a todos los clientes
        	try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                byte[] criptedMessage;
                while ((criptedMessage = in.readNBytes(acceptedMessageSize)) != null) {
                	String message = new String(util.descifrar(criptedMessage, key));;
                	
                    System.out.println("Cliente [" + clientSocket.getPort() + "]: " + message);

                    synchronized(clients) {
						for (ClientHandler client : clients) {
							if (client != this) {
								byte[] criptedMessageToSend = util.cifrar(message, key);
								client.out.writeUTF("Cliente [" + clientSocket.getPort() + "]: ");
								client.out.write(criptedMessageToSend);;
							}
						}
                    }

                    if (message.equalsIgnoreCase("fin")) {
                        System.out.println("Cliente [" + clientSocket.getPort() + "] se ha desconectado.");
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al manejar el cliente: " + e.getMessage());
            } finally {
                try {
                    if (clientSocket != null) clientSocket.close();
                    clients.remove(this);
                } catch (IOException e) {
                    System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
                }
            }
        }
    }
}
