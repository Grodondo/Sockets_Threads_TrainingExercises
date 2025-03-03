package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

public class ServerManager {

    private int puerto;
    private int cont;
    DatagramSocket socket = null;
    Random rand = null;

    public ServerManager(int puerto) throws SocketException {
        this.puerto = puerto;
        this.cont = 0;
        this.socket = new DatagramSocket(this.puerto);
        this.rand = new Random();
    }

    public void start() {
        System.out.println("Comienza el control de acceso");
        Thread t = new Thread(() -> sendResponse());
        t.start();
        
        while(true) {
        	int random = rand.nextInt(3);
        	
        	try {
				Thread.sleep(1500);
	        	this.cont += random;
	        	System.out.println("Personas contabilizadas: " + this.cont);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
        }
        
    }

	private void sendResponse() {
        try {
            while (true) {
                byte[] receiveData = new byte[255];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                socket.receive(receivePacket);

                String mensaje = new String(receivePacket.getData(), 0, receivePacket.getLength());
                
                String respuesta = "";
                switch(mensaje) {
				case "1":
					respuesta = this.cont + "";
					System.out.println("**Personas?**");
					break;
				case "2":
					this.cont = 0;
					respuesta = "Contador reseteado";
					System.out.println("**Resetear contador**");
					break;
                }

                byte[] sendData = respuesta.getBytes();
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);

                socket.send(sendPacket);

            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }

	}

}

