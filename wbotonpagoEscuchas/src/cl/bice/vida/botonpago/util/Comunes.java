package cl.bice.vida.botonpago.util;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import java.util.StringTokenizer;

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

public class Comunes {
    private static final Logger logger = Logger.getLogger(Comunes.class);

    public static String getPathServer() {
        boolean windows = isWindowsPlatform();
        if (windows) {
            return ResourcePropertiesUtil.getProperty("bice.ServerWin"); //$NON-NLS-1$
        } else {
            return ResourcePropertiesUtil.getProperty("bice.ServerLinux"); //$NON-NLS-1$
        }
    }

    /**
     * Retorna el path de los archivos segun el sistema operativo
     * @return String
     */
    public static String getPathFiles() {
        boolean windows = isWindowsPlatform();
        if (windows) {
            return ResourcePropertiesUtil.getProperty("bice.FilesWin"); //$NON-NLS-1$
        } else {
            return ResourcePropertiesUtil.getProperty("bice.FilesLinux"); //$NON-NLS-1$

        }
    }

    /**
     * Retorna true si es plataforma windows
     * @return boolean
     */
    public static boolean isWindowsPlatform() {
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        if (os != null && os.startsWith(WIN_ID))
            return true;
        else
            return false;
    }

    private static final String WIN_ID = "Windows"; //$NON-NLS-1$

    /**
     * Convert document to string for display
     * @param document org.w3c.dom.Document
     * @return String
     */
    public static String documentToString(XMLDocument document) {
        if (logger.isDebugEnabled()) {
            logger.info("documentToString(XMLDocument) - start");
        }

        org.w3c.dom.Document doc = document;
        // Create dom source for the document
        DOMSource domSource = new DOMSource(doc);

        // Create a string writer
        StringWriter stringWriter = new StringWriter();

        // Create the result stream for the transform
        StreamResult result = new StreamResult(stringWriter);

        // Create a Transformer to serialize the document
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            //      Transform the document to the result stream
            try {
                transformer.transform(domSource, result);
            } catch (TransformerException e) {
                logger.error("documentToString(XMLDocument) - TransformerException " + e.getMessage(), e);
            }
        } catch (TransformerConfigurationException e) {
            logger.error("documentToString(XMLDocument) - TransformerConfigurationException " + e.getMessage(), e);
            e.printStackTrace();
        }
        String returnString = stringWriter.toString();
        if (logger.isDebugEnabled()) {
            logger.info("documentToString(XMLDocument) - end");
        }
        return returnString;
    }

    /**
     * Trandforma un Double a Integer
     * @param numero
     * @return Integer
     */
    public static Integer DoubleToInteger(Double numero) {
        return new Integer(String.valueOf(numero.intValue()));
    }

    /**
     * Extrae la parte numerica de un rut en formato XXXXXXX-X
     * @param run
     * @return String
     */
    public static String extractRut(String run) {
        return run.length() < 3 ? null : run.substring(0, run.length() - 2);
    }

    /**
     * Transforma a Html, el resultado de un xml y un xslt
     * @param xmlOrigen
     * @param xsltFileName
     * @return String
     */
    public static String transformarXmltoHtml(String xmlOrigen, String xsltFileName) {
        if (logger.isDebugEnabled()) {
            logger.info("transformarXmltoHtml(String, String) - start");
        }

        String xslOrigenFile = ""; //xmlbase.xsl";
        String separator = "";

        if (isWindowsPlatform()) {
            separator = "\\";
        } else {
            separator = "/";
        }

        xslOrigenFile = Comunes.getPathServer() + separator + "xsl" + separator + xsltFileName;
        Source xmlSource = new StreamSource(new StringReader(xmlOrigen));
        Source xsltSource = new StreamSource(new File(xslOrigenFile));

        StringWriter cadenaSalida = new StringWriter();

        Result bufferResultado = new StreamResult(cadenaSalida);

        TransformerFactory factoriaTrans = TransformerFactory.newInstance();
        Transformer transformador = null;
        try {
            transformador = factoriaTrans.newTransformer(xsltSource);
        } catch (TransformerConfigurationException e) {
            logger.error("transformarXmltoHtml(String, String) - TransformerConfigurationException " + e.getMessage(),
                         e);
        }

        try {
            transformador.transform(xmlSource, bufferResultado);
        } catch (TransformerException e) {
            logger.error("transformarXmltoHtml(String, String) - TransformerException " + e.getMessage(), e);
        }

        String returnString = cadenaSalida.toString();
        if (logger.isDebugEnabled()) {
            logger.info("transformarXmltoHtml(String, String) - end");
        }
        return returnString;

    }

    /**
     * Reemplaza los caracteres de fin de linea de un String por un String nuevo
     * blanco
     * @param texto String
     * @param newText String
     * @return String
     */
    public static String ReplaceEndLine(String texto, String newText) {
        String aux = ""; //$NON-NLS-1$
        int i = 0;
        for (; i < texto.length(); i++) {
            //|| texto.charAt(i) != '\t'
            if (texto.charAt(i) != '\n') { //se va creando una cadena con cada
                // linea
                aux += texto.charAt(i);
            } else if (texto.charAt(i) != '\\' && texto.charAt(i + 1) != 'n') {
                aux = aux.substring(0, aux.length()) + newText;
            } else { //se encuentra un "fin de linea"
                aux = aux.substring(0, aux.length()) + newText;
            }
        }
        aux = aux.substring(0, aux.length()) + newText;
        return aux;
    }

    /**
     * calculaDv: Calcula el digito verificador modulo 11 de un rut
     *
     * @param rutSinDv
     *            String
     * @return String el digito verificador en modulo 11
     */
    public static String calculaDv(String rutSinDv) {
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

        return Integer.toString(resta);
    }

    /**
     * Recibe rut en formato XXXXXXXXX-X y lo pasa a XX.XXX.XXX-X
     * @param run
     * @return String
     */
    public static String formateaRut(String run) {
        if (run != null && run.length() > 3) {
            String runNumber = run.substring(0, run.length() - 2);
            String dv = run.substring(run.length() - 2, run.length());
            int cantNumber = runNumber.length();
            String newRut = dv;
            if (cantNumber > 3) {
                String parte1 = "." + runNumber.substring(cantNumber - 3, cantNumber);
                newRut = parte1 + newRut;
                String resto = runNumber.substring(0, cantNumber - 3);
                cantNumber = resto.length();
                if (cantNumber > 3) {
                    String parte2 = "." + resto.substring(cantNumber - 3, cantNumber);
                    newRut = parte2 + newRut;
                    String parte3 = resto.substring(0, cantNumber - 3);
                    return parte3 + newRut;
                } else {
                    return resto + newRut;
                }
            } else {
                return run;
            }
        } else {
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
    public static String replaceCaracter(String texto, String caracterToReplace, String newText) {
        String aux = "";
        int i = 0;
        for (; i < texto.length(); i++) {
            if (texto.charAt(i) == '.') { //se va creando una cadena con cada linea
                aux += newText;
            } else {
                aux += texto.charAt(i);
            }
        }
        return aux;
    }

    /**
     * Reemplaza los caracteres de fin de linea de un String por un String nuevo
     * blanco
     * @param texto String
     * @param caracterToReplace
     * @param newText String
     * @return String
     */
    public static String replaceCaracterInString(String texto, String caracterToReplace, String newText) {
        if (texto != null) {
            String aux = "";
            int i = 0;
            for (; i < texto.length(); i++) {
                if (String.valueOf(texto.charAt(i)).equals(caracterToReplace)) { //se va creando una cadena con cada linea
                    aux += newText;
                } else {
                    aux += texto.charAt(i);
                }
            }
            return aux;
        }
        return null;
    }

    /**
     * Desformatea rut, pasa de XX.XXX.XXX-X y lo pasa a XXXXXXXXX-X
     * @param run
     * @return String
     */
    public static String desformateaRut(String run) {
        run = run.trim();
        return Comunes.replaceCaracter(run, ".", "");
    }

    /**
     * Verifica que rut venga con guien, sino, se lo agrega
     * @param run
     * @return String
     */
    public static String validaRutConGuion(String run) {
        run = run.trim();
        if (run.length() > 3 && run.indexOf("-") < 0) {
            return run.substring(0, run.length() - 1) + "-" + run.substring(run.length() - 1, run.length());
        } else {
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
    public static String replaceString(String buffer, String strIni, String strMod) {
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
        return out;
    }

    public static String toTitleCase(String str) {
        StringBuffer sb = new StringBuffer();
        str = str.toLowerCase();
        StringTokenizer strTitleCase = new StringTokenizer(str);
        while (strTitleCase.hasMoreTokens()) {
            String s = strTitleCase.nextToken();
            sb.append(s.replaceFirst(s.substring(0, 1), s.substring(0, 1).toUpperCase()) + " ");
        }

        return sb.toString();
    }

    public static String cutStringforColumns(String data, Integer largopermitido) {
        int largo = data.length();
        String datasalida = "";
        try {
            if (largo > largopermitido.intValue()) {
                while (largo > largopermitido.intValue()) {
                    datasalida = datasalida + data.substring(0, largopermitido.intValue());
                    data = data.substring(largopermitido.intValue(), data.length());
                    largo = data.length();
                    if (largo > largopermitido.intValue()) {
                        datasalida = datasalida + " ";
                    } else {
                        datasalida = datasalida + " " + data;
                    }
                }
            } else {
                datasalida = data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datasalida;
    }

    public static String replaceChartoHtmlChar(String data) {
        int lench;
        int lenchhtml;

        String[] character = new String[] {
            " ", "À", "Á", "Â", "Ã", "Ä", "È", "É", "Ê", "Ë", "Ì", "Í", "Î", "Ï", "Ñ", "Ò", "Ó", "Ô", "Õ", "Ö", "Ù",
            "Ú", "Û", "Ü", "à", "á", "â", "ã", "ä", "è", "é", "ê", "ë", "ì", "í", "î", "ï", "ñ", "ò", "ó", "ô", "õ",
            "ö", "ù", "ú", "û", "ü"
        };
        String[] characterhtml = new String[] {
            "&nbsp;", "&Agrave;", "&Aacute;", "&Acirc;", "&Atilde;", "&Auml;", "&Egrave;", "&Eacute;", "&Ecirc;",
            "&Euml;", "&Igrave;", "&Iacute;", "&Icirc;", "&Iuml;", "&Ntilde;", "&Ograve;", "&Oacute;", "&Ocirc;",
            "&Otilde;", "&Ouml;", "&Ugrave;", "&Uacute;", "&Ucirc;", "&Uuml;", "&agrave;", "&aacute;", "&acirc;",
            "&atilde;", "&auml;", "&egrave;", "&eacute;", "&ecirc;", "&euml;", "&igrave;", "&iacute;", "&icirc;",
            "&iuml;", "&ntilde;", "&ograve;", "&oacute;", "&ocirc;", "&otilde;", "&ouml;", "&ugrave;", "&uacute;",
            "&ucirc;", "&uuml;"
        };

        lench = character.length;


        for (int i = 0; i < character.length; i++) {
            data = replaceString(data, character[i], characterhtml[i]);
        }

        return data;
    }

    public static boolean deleteFile(String fileName) {
        boolean borrado = false;
        try {

            File target = new File(fileName);

            if (!target.exists()) {
                logger.error("Archivo a borrar " + fileName + ", no existe");
                return borrado;
            }

            // Quick, now, delete it immediately:
            if (target.delete()) {
                logger.info("Archivo a borrar " + fileName + ", borrado");
                borrado = true;
            } else {
                logger.error("Archivo a borrar " + fileName + ", no pudo ser borrado");
            }
        } catch (SecurityException e) {
            logger.error("Archivo a borrar " + fileName + ", no pudo ser borrado", e);
        }

        return borrado;
    }
}
