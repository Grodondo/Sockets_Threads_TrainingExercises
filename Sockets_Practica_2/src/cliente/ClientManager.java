package cliente;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientManager {
    private String serverIP;
    private int serverPort;
    private boolean isRunning;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scanner;

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
//			Thread listenThread = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					listen();
//				}
//			});
            listenThread.start();

            // Loop principal para enviar mensajes al servidor
            while (isRunning) {
//                System.out.print("Tu mensaje: ");
                String message = scanner.nextLine();
                out.writeUTF(message); // Send message to server

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
        } finally {
            close();
        }
    }

    private void listen() {
        try {
            while (isRunning) {
                String message = in.readUTF();

                // Wait for 3 seconds (3000 milliseconds) before processing the message
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                    System.err.println("Sleep interrupted: " + ie.getMessage());
                }
                
                System.out.println(message);

                if (message.equalsIgnoreCase("fin")) {
                    isRunning = false;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al escuchar mensajes del servidor: " + e.getMessage());
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
