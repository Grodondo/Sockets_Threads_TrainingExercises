import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Signature;

public class FirmarBMP {
    public static void main(String[] args) {
        try {
            File file = new File("imagen.bmp");
            FileInputStream fis = new FileInputStream(file);
            byte[] bmpBytes = new byte[(int) file.length()];
            fis.read(bmpBytes);
            fis.close();
            
            System.out.println("Tamaño del array de bytes de la imagen: " + bmpBytes.length);
            
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            
            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(privateKey);
            signer.update(bmpBytes);
            byte[] digitalSignature = signer.sign();
            
            System.out.println("Tamaño de la firma: " + digitalSignature.length);
            
            byte[] finalArray = new byte[bmpBytes.length + digitalSignature.length];
            System.arraycopy(bmpBytes, 0, finalArray, 0, bmpBytes.length);
            System.arraycopy(digitalSignature, 0, finalArray, bmpBytes.length, digitalSignature.length);
            
            FileOutputStream fos = new FileOutputStream("imagen_firmada.bmp");
            fos.write(finalArray);
            fos.close();
            
            System.out.println("La imagen se ha firmado correctamente y se ha guardado en 'imagen_firmada.bmp'.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
