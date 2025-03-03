package servidor;

import java.net.*;
import java.io.*;

public class ServerManager {

	private int puerto;
	
	public ServerManager(int puerto) {
		this.puerto = puerto;
	}
	
	public void listen() {
		ServerSocket socket = null;

		try {
			socket = new ServerSocket(this.puerto);
			
			System.out.println("Esperando a que alguien conecte a través del puerto: " + this.puerto);

			Socket socket_cli = socket.accept();

			DataOutputStream out = new DataOutputStream(socket_cli.getOutputStream());
			DataInputStream in = new DataInputStream(socket_cli.getInputStream());
			boolean fin = false;

			out.writeUTF("Hola desde el servidor\n");
			System.out.println("Ha conectado " + socket_cli.getInetAddress() + ":" + socket_cli.getPort() + "\n");
			
			do {
				System.out.println("Esperando recibir mensaje");

				String mensaje = in.readUTF();
				System.out.println("Mensaje recibido: " + mensaje);
				
				// Esperar 3 segundos antes de mandar el mensaje de vuelta
				Thread.sleep(3000);

				out.writeUTF(mensaje.toUpperCase());
				System.out.println("Se ha contestado el mismo mensaje en mayúsculas\n");
				
				if (mensaje.equalsIgnoreCase("fin")) {
					fin = true;
					socket_cli.close();

				}

			} while (!fin);
			
			out.close();
			in.close();

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
