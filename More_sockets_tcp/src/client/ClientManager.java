package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientManager {

	private int id;
	private String ip;
	private int puerto;
    private boolean fin;
    
    private int numReserves;
    private int maxNumReserves;
	
	public ClientManager(String ip, int puerto, int maxNumReserves, int id) {
		this.ip = ip;
		this.puerto = puerto;
		this.fin = false;
		this.id = id;
		
		this.maxNumReserves = maxNumReserves;
		this.numReserves = 0;
	}
	
	public void connect() {
		Scanner sc = new Scanner(System.in);
		Socket socket = null;

		String mensaje = "";
		try {
			socket = new Socket(this.ip, this.puerto);

			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			System.out.println("Restaurante " + id + " conectado con la central de reservas");
			
			while (!fin) {  	
				
				int personasReserva = in.readInt();
	            if (personasReserva == -1) {
	            	fin = true;
	            	break;
	            }
		        int reservasActuales = this.numReserves + personasReserva;
		        out.writeInt(reservasActuales);
				out.writeInt(this.maxNumReserves);
				
				if (reservasActuales <= this.maxNumReserves) {
					this.numReserves += personasReserva;
				}
				
				String message = in.readUTF();
	            System.out.println(message);
			 }

			in.close();
			
			System.out.println("Comunicación finalizada");

		}catch (Exception e) {
			System.err.println(e.getMessage());
		} 
		finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
	
	}
	
}

