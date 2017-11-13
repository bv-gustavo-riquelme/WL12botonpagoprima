/*
 * Created on 21-09-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.bicevida.botonpago.util;

import org.apache.log4j.Logger;

import java.io.IOException;

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

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * @author MMaldonado.ex
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EncriptacionUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EncriptacionUtil.class);


	/**
	 * Confecciona claves privada y publica
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static void crearClaves() throws NoSuchAlgorithmException, IOException {
		logger.info("crearClaves() - iniciando");

		//Confecciona clave
		KeyPair clave = generateClaves();
		String keyPublica = getClavePublicaBase64(clave);
		String keyPrivada = getClavePrivadaBase64(clave);
		System.out.println("Clave Publica Base64 : " + keyPublica + "\n");
		System.out.println("Clave Privada Base64 : " + keyPrivada + "\n");
		
logger.info("crearClaves() - termina");
	}
	
	/**
	 * Crea claves
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static KeyPair generateClaves() throws NoSuchAlgorithmException, IOException {
		logger.info("generateClaves() - iniciando");

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair clave = keyGen.generateKeyPair();

		logger.info("generateClaves() - termina");
		return clave;
	}
	
	/**
	 * Recupera la clave publica en base 64
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getClavePublicaBase64(KeyPair claves) throws NoSuchAlgorithmException, IOException {
		logger.info("getClavePublicaBase64(KeyPair claves=" + claves + ") - iniciando");

String returnString = base64encode(claves.getPublic().getEncoded());
		logger.info("getClavePublicaBase64(KeyPair claves=" + claves + ") - termina");
		return returnString;
	}	

	/**
	 * Recupera la clave privada
	 * @param claves
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getClavePrivadaBase64(KeyPair claves) throws NoSuchAlgorithmException, IOException {
		logger.info("getClavePrivadaBase64(KeyPair claves=" + claves + ") - iniciando");

String returnString = base64encode(claves.getPrivate().getEncoded());
		logger.info("getClavePrivadaBase64(KeyPair claves=" + claves + ") - termina");
		return returnString;
	}	
	

	/**
	 * Recupera la clave publica segun base 64
	 * @param clavePublica
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getClavePublicaByBase64(String clavePublica) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		logger.info("getClavePublicaByBase64(String clavePublica=" + clavePublica + ") - iniciando");

		byte[] clavePub = base64decode(clavePublica);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(clavePub);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey returnPublicKey = keyFactory.generatePublic(pubKeySpec);
		logger.info("getClavePublicaByBase64(String clavePublica=" + clavePublica + ") - termina");
        return returnPublicKey;
	}
	
	/**
	 * Recupera la clave privada segun base 64
	 * @param clavePrivada
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getClavePrivadaByBase64(String clavePrivada) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		logger.info("getClavePrivadaByBase64(String clavePrivada=" + clavePrivada + ") - iniciando");

		 byte[] clavePub = base64decode(clavePrivada);
		 KeyFactory kf = KeyFactory.getInstance("RSA");
		 KeySpec ks = new PKCS8EncodedKeySpec(clavePub);
		PrivateKey returnPrivateKey = kf.generatePrivate(ks);
		logger.info("getClavePrivadaByBase64(String clavePrivada=" + clavePrivada + ") - termina");
		 return returnPrivateKey;		
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
	public static String firmarDataBase64(Signature algoritmoFirma, PrivateKey clavePrivada, byte[] textoFirmar) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		logger.info("firmarDataBase64(Signature algoritmoFirma=" + algoritmoFirma + ", PrivateKey clavePrivada=" + clavePrivada + ", byte[] textoFirmar=" + textoFirmar + ") - iniciando");

		algoritmoFirma.initSign(clavePrivada);
		algoritmoFirma.update(textoFirmar);		
		byte[] signature = algoritmoFirma.sign();
		System.out.println(algoritmoFirma.getProvider().getInfo() );
		String data = base64encode(signature);
		System.out.println("Datos Firmados y en Base64 : " + data );

		logger.info("firmarDataBase64(Signature algoritmoFirma=" + algoritmoFirma + ", PrivateKey clavePrivada=" + clavePrivada + ", byte[] textoFirmar=" + textoFirmar + ") - termina");
		return data;
	}
	
	/**
	 * Firma los datos con las claves
	 * privadas y entrega la firma sin codificar
	 * @param algoritmoFirma
	 * @param clave
	 * @param textoFirmar
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static byte[] firmarData(Signature algoritmoFirma, PrivateKey clavePrivada, byte[] textoFirmar) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		logger.info("firmarData(Signature algoritmoFirma=" + algoritmoFirma + ", PrivateKey clavePrivada=" + clavePrivada + ", byte[] textoFirmar=" + textoFirmar + ") - iniciando");

		algoritmoFirma.initSign(clavePrivada);
		algoritmoFirma.update(textoFirmar);		
		byte[] signature = algoritmoFirma.sign();

		logger.info("firmarData(Signature algoritmoFirma=" + algoritmoFirma + ", PrivateKey clavePrivada=" + clavePrivada + ", byte[] textoFirmar=" + textoFirmar + ") - termina");
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
	public static boolean verisingDataSignature(Signature firma, PublicKey clavePublica, byte[] texto, byte[] firmado) throws InvalidKeyException, SignatureException {
		logger.info("verisingDataSignature(Signature firma=" + firma + ", PublicKey clavePublica=" + clavePublica + ", byte[] texto=" + texto + ", byte[] firmado=" + firmado + ") - iniciando");

		firma.initVerify(clavePublica);
		firma.update(texto);	
		if (firma.verify(firmado)) { 
			//System.out.println( "Verificaci�n positiva" );

			logger.info("verisingDataSignature(Signature firma=" + firma + ", PublicKey clavePublica=" + clavePublica + ", byte[] texto=" + texto + ", byte[] firmado=" + firmado + ") - termina");
			return true;
		} else {
			//System.out.println( "Fallo la verificacion" );

			logger.info("verisingDataSignature(Signature firma=" + firma + ", PublicKey clavePublica=" + clavePublica + ", byte[] texto=" + texto + ", byte[] firmado=" + firmado + ") - termina");
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
	public static boolean verificarDatosFirmaPublica(Signature firma, String clavePublicaBase64, byte[] texto, byte[] firmado) throws InvalidKeyException, SignatureException, IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		logger.info("verificarDatosFirmaPublica(Signature firma=" + firma + ", String clavePublicaBase64=" + clavePublicaBase64 + ", byte[] texto=" + texto + ", byte[] firmado=" + firmado + ") - iniciando");

		byte[] clavePub = base64decode(clavePublicaBase64);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(clavePub);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        //Verifica firma
		firma.initVerify(pubKey);
		firma.update(texto);	
		if (firma.verify(firmado)) { 
			System.out.println( "Verificaci�n positiva" );

			logger.info("verificarDatosFirmaPublica(Signature firma=" + firma + ", String clavePublicaBase64=" + clavePublicaBase64 + ", byte[] texto=" + texto + ", byte[] firmado=" + firmado + ") - termina");
			return true;
		} else {
			System.out.println( "Fallo la verificacion" );

			logger.info("verificarDatosFirmaPublica(Signature firma=" + firma + ", String clavePublicaBase64=" + clavePublicaBase64 + ", byte[] texto=" + texto + ", byte[] firmado=" + firmado + ") - termina");
			return false;
		}			
	}	
	
	/**
	 * Encodifica a
	 * Convierte a Base 64
	 * @param enc
	 * @return
	 */
	public static String base64encode(byte[] enc)  {
		logger.info("base64encode(byte[] enc=" + enc + ") - iniciando");
                String returnString = new BASE64Encoder().encode(enc);
		logger.info("base64encode(byte[] enc=" + enc + ") - termina");
		return returnString;
	}
	
	/**
	 * Decodifica
	 * @param decode
	 * @return
	 * @throws IOException
	 */
	public static byte[] base64decode(String decode) throws IOException  {
		logger.info("base64decode(String decode=" + decode + ") - iniciando");

byte[] returnbyteArray = new BASE64Decoder().decodeBuffer(decode);
		logger.info("base64decode(String decode=" + decode + ") - termina");
		return returnbyteArray;
	}
	
}

