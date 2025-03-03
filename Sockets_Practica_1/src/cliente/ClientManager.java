package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
		Socket socket = null;

		String mensaje = "";
		try {
			socket = new Socket(this.ip, this.puerto);

			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			// "Hola desde el servidor"
			System.out.println(in.readUTF());
			
			do {
				System.out.println("Escribe el mensaje a enviar");

				mensaje = sc.nextLine();

				out.writeUTF(mensaje);
				
				System.out.println("Mensaje enviado");
				System.out.println("Mensaje recibido del servidor: " + in.readUTF() + "\n");

			} while (!mensaje.equals("fin"));
			
			out.close();
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
