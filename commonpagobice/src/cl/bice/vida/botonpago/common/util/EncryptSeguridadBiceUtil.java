package cl.bice.vida.botonpago.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import weblogic.servlet.utils.Base64;


/**
 * @author MMaldonado.ex
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EncryptSeguridadBiceUtil {

    private static final Logger logger = Logger.getLogger(EncryptSeguridadBiceUtil.class);


    /**
     *
     * @param args
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     * @throws SignatureException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     */
    public static void main2(String[] args) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException,
                                                   NoSuchProviderException, InvalidKeySpecException, IOException {
        String clavePublica =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWabqyO2mGGr93sHCmjOHgiXxnUqZKGv4A4usXEGPMEliVF+bD+Qd/a2eR8cdUym6BJmzLl+cltQahOyUgMmZqseoElYQilHaVNg3uSsFdFuaxIfSsFtWe3XXln2a3C8mbjjj5+tede5WjmK5KgN83VNCpqWD6ilskp9gqFwlslQIDAQAB";
        String clavePrivada =
            "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJZpurI7aYYav3ewcKaM4eCJfGdS" +
            "pkoa/gDi6xcQY8wSWJUX5sP5B39rZ5Hxx1TKboEmbMuX5yW1BqE7JSAyZmqx6gSVhCKUdpU2De5K" +
            "wV0W5rEh9KwW1Z7ddeWfZrcLyZuOOPn61517laOYrkqA3zdU0KmpYPqKWySn2CoXCWyVAgMBAAEC" +
            "gYBAQ8JJ3oqSiqEd4VvwPSdRCU0G1FBKP0ghVtK9EvqguYHxibnGJ4NllJ02xQe2mZsnQSSgxDE1" +
            "cif4AU42eHI7HJnXq+zWno+ernAKiJS41o2VYsHjj8wlKF6CBBPkYAA5yR1Dcnr7Sn4FYD9M22eD" +
            "pr9ufXUA2kT4utXmL7vHwQJBAP5NGKVwU/ESNuEq1Esc8QvCv2lkLj8ge8Gy3RcnnsalKLh9qDxW" +
            "gyVPYRUO+P31YJvWIbCk0AjCdXL3Qr1mm2UCQQCXavbf2D9tDkoSO0EMwBVhAyjf7f1D0heJ10om" +
            "5cuqcf0DEa8Ewr+TJH9mpLrWBlg7t1Al+hXn1sV2E8p0NrFxAkAOSoDfEz/0ZUSmRMTnb7nwIkKl" +
            "hpa+v3GVAhtAqN5YZYGzVIR215nJJ/vZthyyEO8b8t2z0p9KTrtQ+apLgI+tAkBLq7kUxQ1tguah" +
            "lds8dAI7KUkUH+uiKSx6Ro1YQx81XyIClqPQzULQVj0VHt4Ad8tAjKwRTpoD8uFb0SgsS+kBAkAP" +
            "DcHZrG8eCqFK42HBJjaC/E2ziQBeVlGDw3LM2OVqjfmajp+Un6geFfcGIXlR6NRMITznUMUIYMiR" + "GlaVVRTT";

        //Data a firmar
        String texto = "8383672;7878787;20060910;135000";

        //Inicializa algoritmo de encriptacion
        Signature sig = Signature.getInstance("MD5WithRSA");

        //Construye claves a partir de base 64
        PublicKey keyPubl = getClavePublicaByBase64(clavePublica);
        PrivateKey keyPriv = getClavePrivadaByBase64(clavePrivada);

        //Firma los datos en base 64
        String firmado64 = firmarDataBase64(sig, keyPriv, texto.getBytes());
        byte[] firmadecode = base64decode(firmado64);
        System.out.println("Data Firmada Base64 : " + firmado64 + "\n");


        //Verifica con clave publica seteada		
        //boolean resultado = verisingDataSignature(sig, keyPubl, texto.getBytes(), firmadecode);

    }

    /**
     * Confecciona claves privada y publica
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void crearClaves() throws NoSuchAlgorithmException, IOException {
        //Confecciona clave
        KeyPair clave = generateClaves();
        String keyPublica = getClavePublicaBase64(clave);
        String keyPrivada = getClavePrivadaBase64(clave);
        logger.info("crearClaves() - Clave Publica Base64 : " + keyPublica + "\n");
        logger.info("crearClaves() - Clave Privada Base64 : " + keyPrivada + "\n");

    }

    /**
     * Crea claves
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static KeyPair generateClaves() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair clave = keyGen.generateKeyPair();
        return clave;
    }

    /**
     * Recupera la clave publica en base 64
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getClavePublicaBase64(KeyPair claves) throws NoSuchAlgorithmException, IOException {
        return base64encode(claves.getPublic().getEncoded());
    }

    /**
     * Recupera la clave privada
     * @param claves
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getClavePrivadaBase64(KeyPair claves) throws NoSuchAlgorithmException, IOException {
        return base64encode(claves.getPrivate().getEncoded());
    }


    /**
     * Recupera la clave publica segun base 64
     * @param clavePublica
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getClavePublicaByBase64(String clavePublica) throws IOException, NoSuchAlgorithmException,
                                                                                InvalidKeySpecException {
        byte[] clavePub = base64decode(clavePublica);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(clavePub);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(pubKeySpec);
    }

    /**
     * Recupera la clave privada segun base 64
     * @param clavePrivada
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getClavePrivadaByBase64(String clavePrivada) throws IOException, NoSuchAlgorithmException,
                                                                                 InvalidKeySpecException {
        java.security.Security.addProvider(
            new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        byte[] clavePub = base64decode(clavePrivada);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        KeySpec ks = new PKCS8EncodedKeySpec(clavePub);
        return kf.generatePrivate(ks);
    }
    
    public static  PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException, IOException {
        java.security.Security.addProvider(
            new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        byte[] clear = base64decode(key64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
    }
    
    public static void main(String[] args) {
        String xmlFinal = "<MPINISecure><MPINIComer><?xml version='1.0' ?><MPINI><IDCOM>9665641001</IDCOM><IDTRX>9665641001000001</IDTRX><TOTAL>14234</TOTAL><NROPAGOS>1</NROPAGOS><DETALLE><SRVREC>005218</SRVREC><MONTO>14234</MONTO><GLOSA>Pago XX</GLOSA><CANTIDAD>1</CANTIDAD><PRECIO>14234</PRECIO><DATOADIC>BCEL00048217630063512443</DATOADIC></DETALLE></MPINI></MPINIComer><MPINIFirma>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</MPINIFirma></MPINISecure>";

        try {
            System.out.println(base64encode(xmlFinal.getBytes("UTF8")));
            
            System.out.println("=========================");
                
            System.out.println(Base64.encode(xmlFinal.getBytes("UTF8")));
            
            
        } catch (UnsupportedEncodingException e) {
        }

    }

    public static void mainOld(String[] args) {
        String privateKey = "MIICXAIBAAKBgQDUJCv49Q5M5vVEU0yjYlZMaOP8mG/sg6R4+LxlmnBaXl16AmwLFbzggpE892ms6wFwRuuxjc71ssbpKked8UzswpTdWMR/LTs8hgLg7i9r85uJGTZ9+m94Fe0xBePxpXfH9uP4AWZUvb62dzUlAG/eZwHe3+1FOMCBpX34d7Rv3wIDAQABAoGAElZiGq4MI2efcd2FdWYNsaMree0vV5F1iSNvHZVA+v7HM8G71yV04y9GIK/+SX0y7aKjwwBQEHr4qLUH5cYy8OgDE8G3PRvmWAoMJKfbF39hQ/iJRCLDEOShXVC7Y5LT4w2ENONMYhFX/zAPqaFmLsFVompRTFhYlCMMgocTJKkCQQDpKXlhDdY2rWaPnnT1vbo+/BXaE8FnqVsuK9Igi9WIol///2/VctBKxCPAdfCMGXqDEaz1xGbM+ym3YXZJ/E2VAkEA6Ouas6hu41PzUXD/eEwgIMXM6X2JpKjvl/vZQ/CQ9qwyzZPZlLBZTiVvJZqpCo6CR5v4+k4qZqr53KON/exiowJAcGx/Cj0RvOt/OGC/JH6G/zetkalU6BbQ0EJw3yCNpYQlpdoUombp6CMK2hIEGolueSRcsWQx7URtqO5liucMwQJAfOYyO9q7M63F0Us9wv1XURugaSiHUcPBTwefFv5+TbhDOdfpmgECspQCSPDpap7Gn6200kxuEkXm3Hjb4GkM7wJBAIp6LpSlg5d7jyVa5sChY5fYd44XlcAZbR/KJMzK05i+th3P9js+3kdLzT1h/DkhQLlD15z1VK+xphbLxEDi9rA=";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUJCv49Q5M5vVEU0yjYlZMaOP8mG/sg6R4+LxlmnBaXl16AmwLFbzggpE892ms6wFwRuuxjc71ssbpKked8UzswpTdWMR/LTs8hgLg7i9r85uJGTZ9+m94Fe0xBePxpXfH9uP4AWZUvb62dzUlAG/eZwHe3+1FOMCBpX34d7Rv3wIDAQAB";
        try {
            String datafirmar = "525201608311047115397332677166153973301104720160910";
            //String datafirmar = "525201608311047";
            
            
            PublicKey keyPubl = EncryptSeguridadBiceUtil.getClavePublicaByBase64(publicKey);
            PrivateKey keyPriv = EncryptSeguridadBiceUtil.getClavePrivadaByBase64(privateKey);
            
            
            Signature sig = Signature.getInstance("MD5WithRSA");
            
            String firmado64 = firmarDataBase64(sig, keyPriv, datafirmar.getBytes());
            System.out.println("firmado64 -> " +firmado64);
            
            
            byte[] firmadecode = base64decode(firmado64);
            boolean resultado = verisingDataSignature(sig, keyPubl, datafirmar.getBytes(), firmadecode);
            System.out.println("=====================================================");
            
            
            System.out.println("resultado -> " + resultado);
            
            System.out.println("=====================================================");
            //System.out.println("decrypt -> " + verfiySignature(keyPubl, firmado64, datafirmar) );
            
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    

    /**
     * Firma los datos con las claves
     * publicas y privadas mas el texto
     * y entregla
     * @param algoritmoFirma
     * @param clave
     * @param textoFirmar
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String firmarDataBase64(Signature algoritmoFirma, PrivateKey clavePrivada,
                                          byte[] textoFirmar) throws NoSuchAlgorithmException, InvalidKeyException,
                                                                     SignatureException {
        algoritmoFirma.initSign(clavePrivada);
        algoritmoFirma.update(textoFirmar);
        byte[] signature = algoritmoFirma.sign();
        System.out.println(algoritmoFirma.getProvider().getInfo());
        String data = base64encode(signature);
        System.out.println("Datos Firmados y en Base64 : " + data);
        return data;
    }

    /**
     * Firma los datos con las claves
     * privadas y entrega la firma sin codificar
     * @param algoritmoFirma
     * @param clavePrivada
     * @param textoFirmar
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static byte[] firmarData(Signature algoritmoFirma, PrivateKey clavePrivada,
                                    byte[] textoFirmar) throws NoSuchAlgorithmException, InvalidKeyException,
                                                               SignatureException {
        algoritmoFirma.initSign(clavePrivada);
        algoritmoFirma.update(textoFirmar);
        byte[] signature = algoritmoFirma.sign();
        return signature;
    }

    /**
     * Verifica los datos segun una clave
     * publica, el texto original y el texto firmado.
     * @param firma
     * @param clavePublica
     * @param texto
     * @param firmado
     * @return
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verisingDataSignature(Signature firma, PublicKey clavePublica, byte[] texto,
                                                byte[] firmado) throws InvalidKeyException, SignatureException {
        firma.initVerify(clavePublica);
        firma.update(texto);
        if (firma.verify(firmado)) {
            System.out.println("Verificación positiva");
            return true;
        } else {
            System.out.println("Fallo la verificacion");
            return false;
        }
    }

    /**
     * Verifica los datos segun una clave publica
     * en formato de texto en base64
     * @param firma
     * @param clavePublica
     * @param texto
     * @param firmado
     * @return
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean verificarDatosFirmaPublica(Signature firma, String clavePublicaBase64, byte[] texto,
                                                     byte[] firmado) throws InvalidKeyException, SignatureException,
                                                                            IOException, NoSuchAlgorithmException,
                                                                            NoSuchProviderException,
                                                                            InvalidKeySpecException {
        byte[] clavePub = base64decode(clavePublicaBase64);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(clavePub);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        //Verifica firma
        firma.initVerify(pubKey);
        firma.update(texto);
        if (firma.verify(firmado)) {
            System.out.println("Verificación positiva");
            return true;
        } else {
            System.out.println("Fallo la verificacion");
            return false;
        }
    }

    /**
     * Encodifica a
     * Convierte a Base 64
     * @param enc
     * @return
     */
    public static String base64encode(byte[] enc) {
        return new sun.misc.BASE64Encoder().encode(enc);
    }

    /**
     * Decodifica
     * @param decode
     * @return
     * @throws IOException
     */
    public static byte[] base64decode(String decode) throws IOException {
        return new sun.misc.BASE64Decoder().decodeBuffer(decode);
    }

}

