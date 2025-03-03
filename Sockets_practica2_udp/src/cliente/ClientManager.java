package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientManager {

	private String severIp;
	private InetAddress serverAddress;
	
	private int serverPort;
	private DatagramSocket socket;
	private Scanner input;
	
	private boolean endConnection;

	
	public ClientManager(String serverIp, int serverPort) {
		this.severIp = serverIp;
		this.serverPort = serverPort;
		this.endConnection = false;
	}
	
	
	public void connect() {
		this.input = new Scanner(System.in);
		
		try {
			System.out.println("Conectando al servidor: " + this.severIp + ":" + this.serverPort);
			
			socket = new DatagramSocket();
			serverAddress = InetAddress.getByName(this.severIp);
			
            // Crea un Thread para recibir mensajes del servidor
            Thread receiveThread = new Thread(() -> {
                while (!endConnection) {
                    receive(255);
                }
            });
            receiveThread.start();

            // Envia mensajes al servidor
            while (!endConnection) {
                send();
            }
			
            receiveThread.join();
			System.out.println("Comunicación finalizada");

		}catch (Exception e) {
			System.err.println(e.getMessage());
		} 
		finally {
			if (socket != null && !socket.isClosed()) {
                socket.close();
            }
			this.endConnection = false;
		}
			
	
	}
	
	private void receive(int packetSize) {
		byte[] receiveData = new byte[packetSize];
		try {
	        DatagramPacket paqueteRecibido = new DatagramPacket(receiveData, receiveData.length);
			this.socket.receive(paqueteRecibido);

	        String mensajeRecibido = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
	        
			if (mensajeRecibido.equalsIgnoreCase("end")) 
				this.endConnection = true;
			else
				System.out.println("Mensaje recibido del servidor: " + mensajeRecibido + "\n");
        } 
        catch (IOException e) {
			System.out.println("Error al recibir el paquete: " + e.getMessage());
		}
	}
	
	private void send() {
		String mensaje = input.nextLine();
		if (mensaje.equalsIgnoreCase("end")) {
			this.endConnection = true;
			return;
		}
		
		byte[] buffer = mensaje.getBytes();
		DatagramPacket paqueteEnviado = new DatagramPacket(buffer, buffer.length, serverAddress, this.serverPort);
		try {
			socket.send(paqueteEnviado);
		} catch (IOException e) {
			System.out.println("Error al enviar el paquete: " + e.getMessage());
		}
	}
	
}
