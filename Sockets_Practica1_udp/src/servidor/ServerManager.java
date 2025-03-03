package servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerManager {

    private int puerto;

    public ServerManager(int puerto) {
        this.puerto = puerto;
    }

    public void listen() {
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(this.puerto);

            System.out.println("Servidor UDP escuchando en el puerto: " + this.puerto);

            boolean fin = false;

            while (!fin) {
                byte[] receiveData = new byte[255];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                System.out.println("Esperando mensaje del cliente...");

                socket.receive(receivePacket);

                String mensaje = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Mensaje recibido: " + mensaje);

                String respuesta = mensaje.toUpperCase();

                byte[] sendData = respuesta.getBytes();
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);

                socket.send(sendPacket);
                System.out.println("Se ha contestado el mensaje en mayúsculas.");

                if (mensaje.equalsIgnoreCase("fin")) {
                    fin = true;
                    System.out.println("Finalizando comunicación...");
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }

        System.out.println("Comunicación finalizada.");
    }
}
