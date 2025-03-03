package server;

import util.util;
import java.io.*;
import java.net.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.*;

import javax.crypto.spec.SecretKeySpec;

public class ServerManager {
    private int puerto;
    private boolean fin = false;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    
    private KeyPair keyPair;

    public ServerManager(int puerto) throws Exception {
        this.puerto = puerto;
        this.clients = new ArrayList<ClientHandler>();
        
        this.keyPair = util.generarParDeClaves();
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
        private PublicKey clavePublica;
        private PrivateKey clavePrivada;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.clavePublica = keyPair.getPublic();
            this.clavePrivada = keyPair.getPrivate();
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                // Enviamos la clave pública al cliente
                byte[] clavePublicaBytes = clavePublica.getEncoded();
                out.writeInt(clavePublicaBytes.length);
                out.write(clavePublicaBytes);
                out.flush();


                while (true) {
                	String mensajeCifradoBase64 = in.readUTF();
                	byte[] mensajeCifradoBytes = Base64.getDecoder().decode(mensajeCifradoBase64);
                    String mensajeDescifrado = util.descifrar(mensajeCifradoBytes, clavePrivada);
                	
                    System.out.println("Cliente [" + clientSocket.getPort() + "]: " + mensajeDescifrado);

                    // Reenviar el mensaje a todos los demás clientes
                    synchronized(clients) {
                        for (ClientHandler client : clients) {
                            if (client != this) {
                            	String mensajeInformativo = "Cliente [" + clientSocket.getPort() + "]: " + mensajeDescifrado;
                            	byte[] mensajeInformativoCifradoBytes = util.cifrar(mensajeInformativo, this.clavePrivada);
                            	String mensajeInformativoCifradoBase64 = Base64.getEncoder().encodeToString(mensajeInformativoCifradoBytes);
                            	client.out.writeUTF(mensajeInformativoCifradoBase64);
                            }
                        }
                    }

                    if (mensajeDescifrado.equalsIgnoreCase("fin")) {
                        System.out.println("Cliente [" + clientSocket.getPort() + "] se ha desconectado.");
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al manejar el cliente: " + e.getMessage());
            } finally {
                try {
                    if (clientSocket != null)
                        clientSocket.close();
                    clients.remove(this);
                } catch (IOException e) {
                    System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
                }
            }
        }

    }
}
