package cl.bicevida.botonpago.util;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import oracle.xml.parser.v2.XMLDocument;

import org.apache.log4j.Logger;


/**
 * Clase de utilidad con metodos genericos
 * @author rhicil
 *
 */
public class RutUtil {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(RutUtil.class);


    /**
     * Extrae la parte numerica de un rut en formato XXXXXXX-X
     * @param run
     * @return String
     */
    public static String extractRut(String run) {
		logger.info("extractRut(String run=" + run + ") - iniciando");

		String returnString = run.length() < 3 ? "0" : run.substring(0, run.length() - 2);
		logger.info("extractRut(String run=" + run + ") - termina");
        return returnString;
    }

    /**
     * Calcula digito verificador
     * @param rutSinDv
     * @return
     */
    public static String calculaDv(Integer rutSinDv) {
        return calculaDv(Integer.toString(rutSinDv) );
    }

    /**
     * calculaDv: Calcula el digito verificador modulo 11 de un rut
     *
     * @param rutSinDv
     *            String
     * @return String el digito verificador en modulo 11
     */
    public static String calculaDv(String rutSinDv) {
        logger.info("calculaDv(String rutSinDv=" + rutSinDv + ") - iniciando");

        String vRut = String.valueOf(Long.parseLong(rutSinDv));
        int i = vRut.length() - 1, j = 2, suma = 0;

        while (i >= 0) {
            if (j > 7)
                j = 2;

            suma += Integer.parseInt(vRut.substring(i, i + 1)) * j;
            j++;
            i--;
        }

        int modulo = suma % 11;
        int resta = 11 - modulo;
        if (resta == 10)
            return "K"; //$NON-NLS-1$

        if (resta == 11)
            return "0"; //$NON-NLS-1$

		String returnString = Integer.toString(resta);
		logger.info("calculaDv(String rutSinDv=" + rutSinDv + ") - termina");
        return returnString;
    }

    /**
		 * Recibe rut en formato XXXXXXXXX-X y lo pasa a XX.XXX.XXX-X
		 * @param run
		 * @return String
		 */
    public static String formateaRut(String run) {
		logger.info("formateaRut(String run=" + run + ") - iniciando");

        if (run != null && run.length() > 3) {
            String runNumber = run.substring(0, run.length() - 2);
            String dv = run.substring(run.length() - 2, run.length());
            int cantNumber = runNumber.length();
            String newRut = dv;
            if (cantNumber > 3) {
                String parte1 = 
                    "." + runNumber.substring(cantNumber - 3, cantNumber);
                newRut = parte1 + newRut;
                String resto = runNumber.substring(0, cantNumber - 3);
                cantNumber = resto.length();
                if (cantNumber > 3) {
                    String parte2 = 
                        "." + resto.substring(cantNumber - 3, cantNumber);
                    newRut = parte2 + newRut;
                    String parte3 = resto.substring(0, cantNumber - 3);
					String returnString = parte3 + newRut;
					logger.info("formateaRut(String run=" + run + ") - termina");
                    return returnString;
                } else {
					String returnString = resto + newRut;
					logger.info("formateaRut(String run=" + run + ") - termina");
                    return returnString;
                }
            } else {
				logger.info("formateaRut(String run=" + run + ") - termina");
                return run;
            }
        } else {
			logger.info("formateaRut(String run=" + run + ") - termina");
            return run;
        }
    }

    /**
		  * Reemplaza los caracteres de fin de linea de un String por un String nuevo
		  * blanco
		  * @param texto String
		  * @param caracterToReplace
		  * @param newText String
		  * @return String
		  */
    public static String replaceCaracter(String texto, 
                                         String caracterToReplace, 
                                         String newText) {
		logger.info("replaceCaracter(String texto=" + texto + ", String caracterToReplace=" + caracterToReplace + ", String newText=" + newText + ") - iniciando");

        String aux = "";
        int i = 0;
        for (; i < texto.length(); i++) {
            if (texto.charAt(i) == 
                '.') { //se va creando una cadena con cada linea    
                aux += newText;
            } else {
                aux += texto.charAt(i);
            }
        }

		logger.info("replaceCaracter(String texto=" + texto + ", String caracterToReplace=" + caracterToReplace + ", String newText=" + newText + ") - termina");
        return aux;
    }


    /**
		  * Desformatea rut, pasa de XX.XXX.XXX-X y lo pasa a XXXXXXXXX-X
		  * @param run
		  * @return String
		  */
    public static String desformateaRut(String run) {
		logger.info("desformateaRut(String run=" + run + ") - iniciando");

		String returnString = RutUtil.replaceCaracter(run, ".", "");
		logger.info("desformateaRut(String run=" + run + ") - termina");
        return returnString;
    }

    /**
		  * Verifica que rut venga con guien, sino, se lo agrega
		  * @param run
		  * @return String
		  */
    public static String validaRutConGuion(String run) {
		logger.info("validaRutConGuion(String run=" + run + ") - iniciando");

        if (run.length() > 3 && run.indexOf("-") < 0) {
			String returnString = run.substring(0, run.length() - 1) + "-" + run.substring(run.length() - 1, run.length());
			logger.info("validaRutConGuion(String run=" + run + ") - termina");
            return returnString;
        } else {
			logger.info("validaRutConGuion(String run=" + run + ") - termina");
            return run;
        }
    }

    /**
	 * Reemplazador de caracteres
	 * @param buffer
	 * @param strIni
	 * @param strMod
	 * @return
	 */
    public static String replaceString(String buffer, String strIni, 
                                       String strMod) {
		logger.info("replaceString(String buffer=" + buffer + ", String strIni=" + strIni + ", String strMod=" + strMod + ") - iniciando");

        String out = ""; //$NON-NLS-1$
        String aux = ""; //$NON-NLS-1$
        String aux2 = buffer;
        int ind, ind2;
        ind = aux2.indexOf(strIni);
        ind2 = ind + strIni.length();
        while (ind != -1) {
            aux = aux2.substring(0, ind) + strMod;
            out += aux;
            if (aux2.length() > ind2) {
                aux2 = aux2.substring(ind2, aux2.length());
            } else {
                aux2 = ""; //$NON-NLS-1$
            }
            ind = aux2.indexOf(strIni);
            ind2 = ind + strIni.length();
        }
        out += aux2;

		logger.info("replaceString(String buffer=" + buffer + ", String strIni=" + strIni + ", String strMod=" + strMod + ") - termina");
        return out;
    }


}
