package client;

import util.util;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ClientManager {
    private String serverIP;
    private int serverPort;
    private boolean isRunning;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scanner;
    
    private SecretKey key;
    private int messageSize;

    public ClientManager(String serverIP, int serverPort, String key) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.isRunning = true;
        this.scanner = new Scanner(System.in);
        
        this.messageSize = 32;
        byte[] keyBytes = Arrays.copyOf(key.getBytes(), messageSize);
        this.key = new SecretKeySpec(keyBytes, "AES");
        
    }

    public void start() {
        try {
            // Conexion al servidor
            socket = new Socket(serverIP, serverPort);
            System.out.println("Conectado al servidor: " + serverIP + ":" + serverPort);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
            // Crea un Thread para escuchar mensajes del servidor
            Thread listenThread = new Thread(() -> this.listen());
//			Thread listenThread = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					listen();
//				}
//			});
            listenThread.start();

            // Loop principal para enviar mensajes al servidor
            while (isRunning) {
                String message = scanner.nextLine();
                byte[] messageCifrado = util.cifrar(message, key);
                out.write(messageCifrado);; 

                if (message.equalsIgnoreCase("fin")) {
                    isRunning = false;
                }
            }

            // Espera a que termine el hilo de escucha para cerrar los recursos
            listenThread.join();
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Error en el hilo de escucha: " + e.getMessage());
        } catch (Exception e) {
        	System.out.println("Error al cifrar el mensaje: " + e.getMessage());
		} finally {
            close();
        }
    }

    private void listen() {
        try {
            while (isRunning) {
                String clientName = in.readUTF();
                System.out.print(clientName);
                
                byte[] messageCifrado = in.readNBytes(messageSize);
                String message = new String(util.descifrar(messageCifrado, key));
                
                System.out.println(message);
//                if (message.equalsIgnoreCase("fin")) {
//                    isRunning = false;
//                }
            }
        } catch (IOException e) {
            System.err.println("Error al escuchar mensajes del servidor: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al descifrar el mensaje: " + e.getMessage());
		}
    }

    private void close() {
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
            if (scanner != null) scanner.close();
            System.out.println("Conexión cerrada.");
        } catch (IOException e) {
            System.err.println("Error al cerrar recursos del cliente: " + e.getMessage());
        }
    }
}
