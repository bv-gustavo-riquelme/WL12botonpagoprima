package cl.bice.vida.botonpago.utils;

import java.io.BufferedReader;
import java.io.IOException;

import java.sql.Clob;
import java.sql.SQLException;

import java.util.StringTokenizer;

public class StringUtil {
	/**
	 * Logger for this class
	 */
        private static String[] simbols = new String[61];
        private static String[] charspr = new String[61];        
        
        static  {
            simbols[0]="&#xC0;"; charspr[0]="�";
            simbols[1]="&#xC1;"; charspr[1]="�";
            simbols[2]="&#xC2;"; charspr[2]="�";
            simbols[3]="&#xC3;"; charspr[3]="�";
            simbols[4]="&#xC4;"; charspr[4]="�";
            simbols[5]="&#xC5;"; charspr[5]="�";
            simbols[6]="&#xC8;"; charspr[6]="�";
            simbols[7]="&#xC9;"; charspr[7]="�";
            simbols[8]="&#xCA;"; charspr[8]="�";
            simbols[9]="&#xCB;"; charspr[9]="�";
            simbols[10]="&#xCC;"; charspr[10]="�";
            simbols[11]="&#xCD;"; charspr[11]="�";
            simbols[12]="&#xCE;"; charspr[12]="�";
            simbols[13]="&#xCF;"; charspr[13]="�";
            simbols[14]="&#xD2;"; charspr[14]="�";
            simbols[15]="&#xD3;"; charspr[15]="�";
            simbols[16]="&#xD4;"; charspr[16]="�";
            simbols[17]="&#xD5;"; charspr[17]="�";
            simbols[18]="&#xD6;"; charspr[18]="�";
            simbols[19]="&#xD9;"; charspr[19]="�";
            simbols[20]="&#xDA;"; charspr[20]="�";
            simbols[21]="&#xDB;"; charspr[21]="�";
            simbols[22]="&#xDC;"; charspr[22]="�";
            simbols[23]="&#xDD;"; charspr[23]="�";
            simbols[24]="&#xE0;"; charspr[24]="�";
            simbols[25]="&#xE1;"; charspr[25]="�";
            simbols[26]="&#xE2;"; charspr[26]="�";
            simbols[27]="&#xE3;"; charspr[27]="�";
            simbols[28]="&#xE4;"; charspr[28]="�";
            simbols[29]="&#xE5;"; charspr[29]="�";
            simbols[30]="&#xE8;"; charspr[30]="�";
            simbols[31]="&#xE9;"; charspr[31]="�";
            simbols[32]="&#xEA;"; charspr[32]="�";
            simbols[33]="&#xEB;"; charspr[33]="�";
            simbols[34]="&#xEC;"; charspr[34]="�";
            simbols[35]="&#xED;"; charspr[35]="�";
            simbols[36]="&#xEE;"; charspr[36]="�";
            simbols[37]="&#xEF;"; charspr[37]="�";
            simbols[38]="&#xF1;"; charspr[38]="�";
            simbols[39]="&#xF2;"; charspr[39]="�";
            simbols[40]="&#xF3;"; charspr[40]="�";
            simbols[41]="&#xF4;"; charspr[41]="�";
            simbols[42]="&#xF5;"; charspr[42]="�";
            simbols[43]="&#xF6;"; charspr[43]="�";
            simbols[44]="&#xF9;"; charspr[44]="�";
            simbols[45]="&#xFA;"; charspr[45]="�";
            simbols[46]="&#xFB;"; charspr[46]="�";
            simbols[47]="&#xFC;"; charspr[47]="�";
            simbols[48]="&#xFD;"; charspr[48]="�";
            simbols[49]="&#xFF;"; charspr[49]="�";
            simbols[50]="&#x0160;"; charspr[50]="�";
            simbols[51]="&#x0161;"; charspr[51]="�";
            simbols[52]="&#x0178;"; charspr[52]="�";
            simbols[53]="&#x017D;"; charspr[53]="�";
            simbols[54]="&#x017E;"; charspr[54]="�";
            simbols[55]="&#x2018;"; charspr[55]="�";
            simbols[56]="&#x2019;"; charspr[56]="�";
            simbols[57]="&#x201A;"; charspr[57]="�";
            simbols[58]="&#x201C;"; charspr[58]="�";
            simbols[59]="&#x201D;"; charspr[59]="�";
            simbols[60]="&#xD1;"; charspr[60]="�";            
        }

    /**
     * Recupera el primer caracter del String
     * @param text
     * @return
     */
    public static Character getFirstCharacter(String text) {
        if (text != null) return text.charAt(0);
        return null;
    }
    
    
    /**
     * Quita caracter al comienzo de la data
     * @param texto
     * @param caracter
     * @return
     */
    public static String quitarCaracteresIzquierza(String texto, char caracter) {
        String retorno = "";
        boolean yaPaso = false;
        if (texto != null) {
            for (int x=0; x < texto.length(); x++) {
                char poschar = texto.charAt(x);
                if (poschar != caracter) {
                    retorno = retorno + poschar;
                    yaPaso = true;
                } else {
                    if (yaPaso == true) {
                        retorno = retorno + poschar;
                    }
                }
            }
        }
        return retorno;
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
    
    /**
     * Reemplaza valores y caracteres
     * especiales para la impresion correcta
     * de un FO --> PDF
     * @param texto
     * @return
     */
    public static String replaceBadCharsFOPDF(String texto) {
        if (texto != null) {
            for (int x = 0; x < simbols.length; x++) {
                if (texto.indexOf(charspr[x]) != -1) {
                   texto = replaceString(texto, charspr[x],simbols[x] );
                }
            }
        }
        return texto;
    }
    
    /**
     * Funcion para transformar
     * un objeto CLOB en un String
     * @param cl
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static String clobToString(Clob cl) throws IOException, SQLException {
          if (cl == null) return  "";              
          StringBuffer strOut = new StringBuffer();
          String aux;
          BufferedReader br = new BufferedReader(cl.getCharacterStream());
          while ((aux=br.readLine())!=null) strOut.append(aux);
          return strOut.toString();
    }     
    
    /**
     * Rellena con ceros
     * @param texto
     * @param largo
     * @param relleno
     * @return
     */
    public static String rellenarTextoIzquierda(String texto, int largo, String relleno) {        
        if (texto.length() < largo)  {
            int rell = largo-texto.length();
            for (int p = 0; p < rell; p++) {
                texto = relleno + texto;
            }
        }
        return texto;
    }    
    
    /**
     * 
     * @param texto
     * @param token
     * @return
     */
    public static String getUltimoToken(String texto, String token) {
        String ultimovalor = "";
        if (texto != null && token != null) {
            StringTokenizer list = new StringTokenizer(texto, token);
            while (list.hasMoreTokens()) {
               ultimovalor = list.nextToken();
            }
        }
        return ultimovalor;
    }
}
