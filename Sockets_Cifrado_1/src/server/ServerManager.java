package server;

import HelperFC.Secure;
import java.net.*;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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

				// Recibir mensaje cifrado
				int n_bytes = in.readInt();
				System.out.println("Numero de bytes en el mensaje recibido cifrado: " + n_bytes);
				byte[] mensaje = new byte[32];
				in.readFully(mensaje);
				System.out.println("Mensaje recibido encriptado: " + mensaje);
				
				// Crear la llave con la que se descifrará el mensaje
				String mensajeClave = "Dodogama";
	            byte[] claveBytes = Arrays.copyOf(mensajeClave.getBytes(), 32);
	            SecretKey clave = new SecretKeySpec(claveBytes, "AES");
				
	            // Descifrar mensaje
				String mensajeDescifrado = Secure.descifrar(mensaje, clave);
				System.out.println("Mensaje descifrado: " + mensajeDescifrado);
				
				// Cifrar mensaje modificado con AES-256
				byte[] mensajeCifrado = Secure.cifrar(mensajeDescifrado.toUpperCase(), clave);
				
				// Enviar mensaje cifrado
				out.write(mensajeCifrado);
				out.flush();
				System.out.println("Se ha contestado el mismo mensaje en mayúsculas\n");
				
				if (mensajeDescifrado.equalsIgnoreCase("fin")) {
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
