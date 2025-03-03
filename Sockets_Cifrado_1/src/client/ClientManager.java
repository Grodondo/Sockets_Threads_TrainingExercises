package client;

import HelperFC.Secure;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
				
				// Crear la llave con la que se cifrará el mensaje
				String mensajeClave = "Dodogama";
	            byte[] claveBytes = Arrays.copyOf(mensajeClave.getBytes(), 32);
	            SecretKey clave = new SecretKeySpec(claveBytes, "AES");
	            
	            // Cifrar mensaje con AES-256
				byte[] mensajeCifrado = Secure.cifrar(mensaje, clave);
				
				out.writeInt(mensajeCifrado.length);
				out.write(mensajeCifrado);
				out.flush();
				
				System.out.println("Mensaje enviado");
				
				// Recibir mensaje cifrado
				byte[] mensajeRecibido = new byte[32];
				in.read(mensajeRecibido);
				System.out.println("Mensaje recibido del servidor: " + Secure.descifrar(mensajeRecibido, clave) + "\n");

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
