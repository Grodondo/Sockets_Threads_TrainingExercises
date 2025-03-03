package server;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ServerManager {

	private int puerto;
	private Scanner scInt;
	private Scanner scStr;
	
	public ServerManager(int puerto) {
		this.puerto = puerto;
		scInt = new Scanner(System.in);
		scStr = new Scanner(System.in);
	}
	
	public void start() {
		ServerSocket socket = null;

		try {
			socket = new ServerSocket(this.puerto);
			
			System.out.println("**Central de reservas**");
			System.out.println("Esperando a que los restaurantes conecten");

			Socket socket_cli1 = socket.accept();
			DataInputStream inC1 = new DataInputStream(socket_cli1.getInputStream());
			DataOutputStream outC1 = new DataOutputStream(socket_cli1.getOutputStream());
			System.out.println("Restaurante 1 conectado");
			
			Socket socket_cli2 = socket.accept();
			DataInputStream inC2 = new DataInputStream(socket_cli2.getInputStream());
			DataOutputStream outC2 = new DataOutputStream(socket_cli2.getOutputStream());
			System.out.println("Restaurante 2 conectado");

			boolean fin = false;
			
			System.out.println("\nComienza el proceso de tomar reservas\n");
			
			do {
				System.out.println("Numero de personas?");
				int nPersonas = scInt.nextInt();
				
				if (nPersonas == -1) {
					outC1.writeInt(-1);
					outC2.writeInt(-1);
					socket_cli1.close();
					socket_cli2.close();
					System.out.println("cComunicacion finalizada");
					break;
				}
				
				System.out.println("A nombre de?");
				String nombre = scStr.nextLine();
				
				System.out.println("Restaurante (1 ó 2)?");
				int restaurante = scInt.nextInt();				
				
				// Enviar mensaje al restaurante correspondiente
				switch(restaurante) {
				case 1:
					outC1.writeInt(nPersonas);
					int numTotalReservas = inC1.readInt();
					int maxNumReservas = inC1.readInt();
					
					//System.out.println("Reservas actuales: " + numTotalReservas + " de " + maxNumReservas);
					
					if (numTotalReservas <= maxNumReservas) {
						System.out.println("Reserva admitida\n");
						outC1.writeUTF("Admitida reserva de " + nPersonas + " personas a nombre de " + nombre);
						continue;
					}
					else {
						int reservasRestantes = maxNumReservas - (numTotalReservas - nPersonas);
						System.out.println("Reserva no admitida. Solo quedan" + reservasRestantes + "\n");
						outC1.writeUTF("Rechazada reserva de " + nPersonas + " personas");
						
					}
					
					break;
				case 2:
					outC2.writeInt(nPersonas);
					int numTotalReservas2 = inC2.readInt();
					int maxNumReservas2 = inC2.readInt();
					
					//System.out.println("Reservas actuales: " + numTotalReservas2 + " de " + maxNumReservas2);
					
					if (numTotalReservas2 <= maxNumReservas2) {
						System.out.println("Reserva admitida\n");
						outC2.writeUTF("Admitida reserva de " + nPersonas + " personas a nombre de " + nombre);
						continue;
					}
					else {
						int reservasRestantes2 = maxNumReservas2 - (numTotalReservas2 - nPersonas);
						System.out.println("Reserva no admitida. Solo quedan " + reservasRestantes2 + "\n");
						outC2.writeUTF("Rechazada reserva de " + nPersonas + " personas");
						
					}
					break;
				}

			} while (!fin);
			
			outC1.close();
			outC2.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		finally {
			try {
				if(socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Comunicación finalizada\n");
	}
	

}
