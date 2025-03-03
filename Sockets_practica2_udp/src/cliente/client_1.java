package cliente;

public class client_1 {

	public static void main(String[] args) {

		String ip = "127.0.0.2";

		ClientManager cm = new ClientManager(ip, 7000);

		cm.connect();

	}
}
