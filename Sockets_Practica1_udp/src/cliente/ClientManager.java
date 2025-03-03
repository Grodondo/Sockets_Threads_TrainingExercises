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

	private String ip;
	private int puerto;
	
	public ClientManager(String ip, int puerto) {
		this.ip = ip;
		this.puerto = puerto;
	}
	
	
	public void connect() {
		Scanner sc = new Scanner(System.in);
		DatagramSocket socket = null;

		try {
			socket = new DatagramSocket();
			InetAddress serverAddress = InetAddress.getByName(this.ip);
			
			String mensaje = "";
			do {
				System.out.println("Escribe el mensaje a enviar");
				mensaje = sc.nextLine();

				byte[] buffer = mensaje.getBytes();
				DatagramPacket paqueteEnviado = new DatagramPacket(buffer, buffer.length, serverAddress, this.puerto);
				socket.send(paqueteEnviado);
				
				System.out.println("Mensaje enviado");
				//----------------------------------------------------------------------------------
				byte[] receiveData = new byte[255];
                DatagramPacket paqueteRecibido = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(paqueteRecibido);
                
                String mensajeRecibido = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
                System.out.println("Mensaje recibido del servidor: " + mensajeRecibido + "\n");

			} while (!mensaje.equals("fin"));
			
			System.out.println("Comunicación finalizada");

		}catch (Exception e) {
			System.err.println(e.getMessage());
		} 
		finally {
			if (socket != null && !socket.isClosed()) {
                socket.close();
            }
		}
			
	
	}
	
}
