package client;

import util.util;
import java.io.*;
import java.net.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
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
    
    private PublicKey serverPublicKey;

    public ClientManager(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.isRunning = true;
        this.scanner = new Scanner(System.in);
        
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

            // Leer la clave pública del servidor
            int serverPublicKeyLength = in.readInt();
            byte[] serverPublicKeyBytes = new byte[serverPublicKeyLength];
            in.readFully(serverPublicKeyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(serverPublicKeyBytes);
            this.serverPublicKey = keyFactory.generatePublic(keySpec);

            
            listenThread.start();

            // Loop principal para enviar mensajes al servidor
            while (isRunning) {
                String message = scanner.nextLine();
                byte[] messageCifrado = util.cifrar(message, this.serverPublicKey);
                
                String mensajeCifradoBase64 = Base64.getEncoder().encodeToString(messageCifrado);
                System.out.println("Mensaje cifrado (Base64): " + mensajeCifradoBase64);
                out.writeUTF(mensajeCifradoBase64);

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
            	String mensajeCifradoBase64 = in.readUTF();
            	byte[] mensajeCifradoBytes = Base64.getDecoder().decode(mensajeCifradoBase64);
                String mensajeDescifrado = util.descifrar(mensajeCifradoBytes, this.serverPublicKey);
            	
                
                System.out.println(mensajeDescifrado);
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
