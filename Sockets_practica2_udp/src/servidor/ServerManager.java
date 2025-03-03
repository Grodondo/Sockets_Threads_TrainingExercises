package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerManager {

    private int puerto;
    private boolean endConnection = false;
    private DatagramSocket serverSocket;
    private HashMap<SocketAddress, String> clientEndpoints;

    public ServerManager(int puerto) {
        this.puerto = puerto;
        this.clientEndpoints = new HashMap<>();  // <adress, puerto>
    }
    
    public void start() {
    	try {
		
    		serverSocket = new DatagramSocket(this.puerto);
            System.out.println("Servidor UDP iniciado en el puerto: " + puerto);
            
            while (!endConnection) {
                receive(255);
            }
    		
		} catch (Exception e) {
			System.err.println("Error en el servidor: " + e.getMessage());
		} finally {
			stop();
    	}
    }
    
    private void receive(int bufferSize) {
        try {
	    	byte[] receiveData = new byte[bufferSize];
	        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	        serverSocket.receive(receivePacket);
	        
	        SocketAddress clientSocketAddress = receivePacket.getSocketAddress();
	        int clientPort = receivePacket.getPort();
	        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
	        
	        System.out.println("Mensaje recibido de " + clientSocketAddress + ":" + clientPort + " - " + message);
	        
	        // Si el cliente no está en la lista, se añade.
	        if (!clientEndpoints.containsKey(clientSocketAddress)) {
                clientEndpoints.put(clientSocketAddress, "Cliente-" + clientSocketAddress.hashCode());
            }
            
            // Si el mensaje es "end", se cierra la conexión.
            if (message.equalsIgnoreCase("end")) {
                endConnection = true;
                broadcast("El servidor se va a cerrar...", null);
            } else {
            	String senderId = clientEndpoints.get(clientSocketAddress);
                String messageToBroadcast = senderId + ": " + message;
                broadcast(messageToBroadcast, clientSocketAddress);
            }
        }
        catch (Exception e) {
            System.err.println("Error al recibir el mensaje: " + e.getMessage());
        }
    }
    
    private void broadcast(String message, SocketAddress excludeAddress) {
        try {
        	byte[] sendData = message.getBytes();
        	
            for (SocketAddress address : clientEndpoints.keySet()) {
            	
                if (address.equals(excludeAddress)) {
                    continue;
                }
            	
                InetSocketAddress socketAddress = (InetSocketAddress) address;
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, socketAddress.getAddress(), socketAddress.getPort());
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje: " + e.getMessage());
        }
    }
    
    private void stop() {
		endConnection = true;
		try {
			if (serverSocket != null)
				serverSocket.close();
			System.out.println("Servidor detenido.");
		} catch (Exception e) {
			System.err.println("Error al detener el servidor: " + e.getMessage());
		}
    }
        

}
