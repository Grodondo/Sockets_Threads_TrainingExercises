import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BMPToByteArrayStream {
	public static void main(String[] args) {
		try {
			// Archivo BMP a leer
			File file = new File("imagen.bmp");
			// Crear flujo de entrada
			FileInputStream fis = new FileInputStream(file);
			
			// Leer todo el archivo en un array de bytes
			byte[] byteArray = new byte[(int) file.length()];
			fis.read(byteArray);
			fis.close();
			
			// Mostrar el tamaño del array
			System.out.println("Tamaño del array de bytes: " + byteArray.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}