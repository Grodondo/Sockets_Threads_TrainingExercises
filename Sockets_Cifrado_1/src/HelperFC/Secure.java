package HelperFC;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

public class Secure {

	public static SecretKey key;
	
	// Generar un IV aleatorio de 16 bytes
    public static byte[] generarIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    
    // Cifrar un texto con AES-256 en modo CBC y adjuntar el IV al mensaje cifrado
    public static byte[] cifrar(String mensaje, SecretKey clave) throws Exception {
        byte[] iv = generarIV(); // Generar IV aleatorio
        Cipher cifrador = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cifrador.init(Cipher.ENCRYPT_MODE, clave, new IvParameterSpec(iv));
        byte[] textoCifrado = cifrador.doFinal(mensaje.getBytes());
        
        // Concatenar IV y texto cifrado
        byte[] ivYTextoCifrado = new byte[iv.length + textoCifrado.length];
        System.arraycopy(iv, 0, ivYTextoCifrado, 0, iv.length);
        System.arraycopy(textoCifrado, 0, ivYTextoCifrado, iv.length, textoCifrado.length);
        
        return ivYTextoCifrado;
    }
    
    // Descifrar un texto con AES-256 en modo CBC extrayendo el IV del mensaje cifrado
    public static String descifrar(byte[] ivYTextoCifrado, SecretKey clave) throws Exception {
        byte[] iv = Arrays.copyOfRange(ivYTextoCifrado, 0, 16);
        byte[] textoCifrado = Arrays.copyOfRange(ivYTextoCifrado, 16, ivYTextoCifrado.length);
        
        Cipher cifrador = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cifrador.init(Cipher.DECRYPT_MODE, clave, new IvParameterSpec(iv));
        return new String(cifrador.doFinal(textoCifrado));
    }
	
}
