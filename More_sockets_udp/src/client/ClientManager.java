package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
			
			while(true) {
				System.out.println("Ver personas entrada (1), resetear (2)?");
				String tipo = sc.nextLine();

				byte[] buffer = tipo.getBytes();
				DatagramPacket paqueteEnviado = new DatagramPacket(buffer, buffer.length, serverAddress, this.puerto);
				socket.send(paqueteEnviado);
				
				//System.out.println("Mensaje enviado");
				//----------------------------------------------------------------------------------
				byte[] receiveData = new byte[255];
                DatagramPacket paqueteRecibido = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(paqueteRecibido);
                
                String mensajeRecibido = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
                System.out.println(mensajeRecibido + "\n");

			}
			
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

