package servidor;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ServerManager {
    private int puerto;
    private boolean fin = false;
    private ServerSocket serverSocket;
    private ExecutorService clientThreadPool;
    private List<ClientHandler> clients;

    public ServerManager(int puerto) {
        this.puerto = puerto;
        this.clientThreadPool = Executors.newCachedThreadPool(); 
        this.clients = new ArrayList<ClientHandler>();
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

				for (ClientHandler client : clients) {
					client.out.writeUTF("Nuevo cliente conectado desde: " + clientSocket.getInetAddress() + ":"
							+ clientSocket.getPort());
				}
                
                ClientHandler client = new ClientHandler(clientSocket);
                //clientThreadPool.submit(client);  // new Thread(client).start(); soy como Pilato
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
            clientThreadPool.shutdown();
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

                String message;
                while ((message = in.readUTF()) != null) {
                    System.out.println("Cliente [" + clientSocket.getPort() + "]: " + message);

                    synchronized(clients) {
						for (ClientHandler client : clients) {
							if (client != this) {
								client.out.writeUTF("Cliente [" + clientSocket.getPort() + "]: " + message);
							}
						}
                    }

                    if (message.equalsIgnoreCase("fin")) {
                        System.out.println("Cliente [" + clientSocket.getPort() + "] se ha desconectado.");
                        break;
                    }
                }
            } catch (IOException e) {
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
