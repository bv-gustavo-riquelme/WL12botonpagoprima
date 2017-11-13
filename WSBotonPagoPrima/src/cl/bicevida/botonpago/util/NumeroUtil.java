package cl.bicevida.botonpago.util;

import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.Locale;


/**
 * Clase para utilidades numericas
 * @author mmaldonado
 *
 */
public class NumeroUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(NumeroUtil.class);

	private static DecimalFormat df00 = null;
	private static DecimalFormat df000 = null;
	private static DecimalFormatSymbols unusualSymbols =  new DecimalFormatSymbols( new Locale("ES","","") );
	
	static {
		
		unusualSymbols.setDecimalSeparator(',');
		unusualSymbols.setGroupingSeparator('.');
		
		df00 = new DecimalFormat( ResourceMessageUtil.getProperty("jsf.pattern.number.decimal")  ,  unusualSymbols );
		df000 = new DecimalFormat( ResourceMessageUtil.getProperty("jsf.pattern.number.miles") ,  unusualSymbols );
		
	}
	
	
	
	/**
	 * Valida que un string contenga un numero
	 * @param valor
	 * @return boolean
	 */
	public static boolean isNumero(String valor) {
		logger.info("isNumero(String valor=" + valor + ") - iniciando");

		try {
			Long.parseLong(valor);

			logger.info("isNumero(String valor=" + valor + ") - termina");
			return true;
		} catch (NumberFormatException e) {
			logger.error("isNumero(String) - catch (error)", e);

			logger.info("isNumero(String valor=" + valor + ") - termina");
			return false;
		}			
	}
	/**
	 * Retorna un numero en formato decimal
	 * @param number
	 * @return String
	 */
	public static String formatMilesSiDecimal( Object number ){
		logger.info("formatMilesSiDecimal(Object number=" + number + ") - iniciando");

		String returnString = df00.format(number);
		logger.info("formatMilesSiDecimal(Object number=" + number + ") - termina");
		return returnString;
	}
	/**
	 * Retorna un numero en formato de puntos para miles
	 * @param number
	 * @return String
	 */
	public static String formatMilesNoDecimal( Object number ){
		logger.info("formatMilesNoDecimal(Object number=" + number + ") - iniciando");

		String returnString = df000.format(number);
		logger.info("formatMilesNoDecimal(Object number=" + number + ") - termina");
		return returnString;
	}


	/**
	 * Funcion para reemplazar separadores de miles
	 * @param numero
	 * @return String
	 */
	public static String removeSeparadoresDeMiles(String numero) {
		logger.info("removeSeparadoresDeMiles(String numero=" + numero + ") - iniciando");

		String formateado = RutUtil.replaceCaracter(numero,".","");

		logger.info("removeSeparadoresDeMiles(String numero=" + numero + ") - termina");
		return formateado;
	}

	
}
