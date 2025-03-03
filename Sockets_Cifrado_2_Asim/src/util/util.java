package util;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class util {
    
    // Generar un par de claves RSA (pública y privada)
    public static KeyPair generarParDeClaves() throws Exception {
        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048); // Tamaño de clave recomendado
        return generador.generateKeyPair();
    }
    
    // Cifrar un mensaje con la clave pública
    public static byte[] cifrar(String mensaje, PublicKey clavePublica) throws Exception {
        Cipher cifrador = Cipher.getInstance("RSA");
        cifrador.init(Cipher.ENCRYPT_MODE, clavePublica);
        return cifrador.doFinal(mensaje.getBytes());
    }
    
    // Cifrar un mensaje con la clave privada
    public static byte[] cifrar(String mensaje, PrivateKey clavePrivada) throws Exception {
        Cipher cifrador = Cipher.getInstance("RSA");
        cifrador.init(Cipher.ENCRYPT_MODE, clavePrivada);
        return cifrador.doFinal(mensaje.getBytes());
    }
    
    
    // Descifrar un mensaje con la clave privada
    public static String descifrar(byte[] mensajeCifrado, PrivateKey clavePrivada) throws Exception {
        Cipher cifrador = Cipher.getInstance("RSA");
        cifrador.init(Cipher.DECRYPT_MODE, clavePrivada);
        return new String(cifrador.doFinal(mensajeCifrado));
    }
    
    // Descifrar un mensaje con la clave publica
    public static String descifrar(byte[] mensajeCifrado, PublicKey clavePublica) throws Exception {
        Cipher cifrador = Cipher.getInstance("RSA");
        cifrador.init(Cipher.DECRYPT_MODE, clavePublica);
        return new String(cifrador.doFinal(mensajeCifrado));
    }
    
    public static void main(String[] args) {
        try {
            // Generar claves RSA
            KeyPair parDeClaves = generarParDeClaves();
            PublicKey clavePublica = parDeClaves.getPublic();
            PrivateKey clavePrivada = parDeClaves.getPrivate();
            
            String mensajeOriginal = "Este es un mensaje secreto";
            
            // Cifrado
            byte[] mensajeCifrado = cifrar(mensajeOriginal, clavePublica);
            String mensajeCifradoBase64 = Base64.getEncoder().encodeToString(mensajeCifrado);
            System.out.println("Mensaje cifrado (Base64): " + mensajeCifradoBase64);
            
            // Descifrado
            byte[] mensajeCifradoBytes = Base64.getDecoder().decode(mensajeCifradoBase64);
            String mensajeDescifrado = descifrar(mensajeCifradoBytes, clavePrivada);
            System.out.println("Mensaje descifrado: " + mensajeDescifrado);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
