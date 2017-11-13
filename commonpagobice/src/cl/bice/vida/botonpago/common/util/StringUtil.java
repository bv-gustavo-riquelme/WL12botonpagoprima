package cl.bice.vida.botonpago.common.util;

import java.io.BufferedReader;
import java.io.IOException;

import java.sql.Clob;
import java.sql.SQLException;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;


public class StringUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StringUtil.class);
        private static String[] simbols = new String[61];
        private static String[] charspr = new String[61];        
        private static String[] simbolsHtml = new String[10];
        private static String[] charsprHtml = new String[10];   
      
        
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
            
        static  {
            charsprHtml[0]="�"; simbolsHtml[0]="&aacute;";
            charsprHtml[1]="�"; simbolsHtml[1]="&Aacute;";
            charsprHtml[2]="�"; simbolsHtml[2]="&eacute;";
            charsprHtml[3]="�"; simbolsHtml[3]="&Eacute;";
            charsprHtml[4]="�"; simbolsHtml[4]="&iacute;";
            charsprHtml[5]="�"; simbolsHtml[5]="&Iacute;";
            charsprHtml[6]="�"; simbolsHtml[6]="&oacute;";
            charsprHtml[7]="�"; simbolsHtml[7]="&Oacute;";
            charsprHtml[8]="�"; simbolsHtml[8]="&uacute;";
            charsprHtml[9]="�"; simbolsHtml[9]="&Uacute;";
        
        } 
        
    private static String[] simbolstitle = new String[12];    
    static  {
        simbolstitle[0]="(";
        simbolstitle[1]=")";
        simbolstitle[2]="$";
        simbolstitle[3]="\\";
        simbolstitle[4]="?";
        simbolstitle[5]="�";
        simbolstitle[6]="+";
        simbolstitle[7]="*";
        simbolstitle[8]="{";
        simbolstitle[9]="}";
        simbolstitle[10]="[";
        simbolstitle[11]="]";
        
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
     * Reemplazador de caracteres
     * @param buffer
     * @param strIni
     * @param strMod
     * @return
     */
    public static String replaceString(String buffer, String strIni, String strMod) {
        logger.debug("replaceString(String buffer, String strIni=" + strIni + ", String strMod=" + strMod + ") - iniciando");

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
        logger.debug("replaceString(String buffer, String strIni=" + strIni + ", String strMod=" + strMod + ") - termina");
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
     * Reemplaza valores y caracteres
     * especiales para la impresion correcta
     * de un Html --> Email
     * @param texto
     * @return
     */
    public static String replaceBadCharsHtml(String texto) {
        if (texto != null) {
            for (int x = 0; x < simbolsHtml.length; x++) {
                if (texto.indexOf(charsprHtml[x]) != -1) {
                   texto = replaceString(texto, charsprHtml[x],simbolsHtml[x] );
                }
            }
        }
        return texto;
    }    
    

     
     /**
     * Quita acentuaciones
     * @param texto
     * @return
     */
      public static String quitarAcentos(String texto) {
          StringBuilder filtered = new StringBuilder(texto.length());
           for (int i = 0; i < texto.length(); i++) {
               char current = texto.charAt(i);
               if (current != ' ') {
                   boolean bueno = false;
                   //nUMEROS
                   if (current >= 48 && current <= 57) {
                       filtered.append(current);
                       bueno = true;
                   }
                   //LETRAS MAYUSCULAS
                   if (current >= 65 && current <= 90) {
                       filtered.append(current);
                       bueno = true;
                   }
                   //LETRAS MINUSCULAS
                   if (current >= 97 && current <= 122) {
                       filtered.append(current);
                       bueno = true;
                   }
               } else {
                   filtered.append(current);
               }
           }
           return filtered.toString();
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
    
    public static String toTitleCase(String str, String delimiters) {
        try{
            StringBuffer sb = new StringBuffer();
            str = str.toLowerCase();
            StringTokenizer strTitleCase = new StringTokenizer(str, delimiters);
                while (strTitleCase.hasMoreTokens()) {
                     String s = strTitleCase.nextToken();      
                     s = s.trim();
                     if(s.equalsIgnoreCase("APV") || s.equalsIgnoreCase("apv") || s.equalsIgnoreCase("Apv")){
                         sb.append(s.toUpperCase() + " ");
                     }else{
                         if(validateStringtoTitleCase(s.substring(0, 1))){
                             sb.append(s + " ");
                         }else{
                             sb.append(s.replaceFirst(s.substring(0, 1), 
                                                      s.substring(0, 1).toUpperCase()) + " ");
                         }
                     }                
                }

            return sb.toString().trim();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }        
    }
    
    private static boolean validateStringtoTitleCase(String texto){
        boolean ret = false;
        for (int x = 0; x < simbolstitle.length; x++) {
            if (texto.indexOf(simbolstitle[x]) != -1) {
               ret = true; 
               break;               
            }
        }
        return ret;
    }
    
    /**
     * Genera alta y bajas
     * @param texto
     * @return
     */
    public static String textoAltaBajas(String texto, boolean incluyeallspaces) {
        if (texto != null) {
            
            if (incluyeallspaces == true) {
                StringTokenizer textfind = new StringTokenizer(texto, " ");
                String fullText = "";
                while (textfind.hasMoreTokens()) {
                    String textoR = textfind.nextToken();
                    fullText = fullText + textoAltaBajas(textoR, false) + " ";
                }
                texto = fullText.trim();
            } else {    
                texto = texto.toUpperCase();
                if (texto.length() > 1) {
                    String parteUno = texto.substring(0,1);
                    String parteDos = texto.substring(1);
                    parteDos = parteDos.toLowerCase();
                    texto = parteUno+parteDos;
                }
            }
        }
        if (texto != null) {
            texto = replaceString(texto," De ", " de ");
            texto = replaceString(texto," Y ", " y ");
            texto = replaceString(texto," O ", " o ");
            texto = replaceString(texto," Del ", " del ");
            texto = replaceString(texto," La ", " la ");
            texto = replaceString(texto," El ", " el ");
            texto = replaceString(texto," Los ", " los ");
            texto = replaceString(texto," Las ", " las ");
            texto = replaceString(texto," En ", " en ");
            texto = replaceString(texto," Un ", " un ");
            texto = replaceString(texto," Unos ", " unos ");
            texto = replaceString(texto," Unas ", " unas ");
            texto = replaceString(texto,"Linea", "L�nea");
            texto = replaceString(texto,"Credito", "Cr�dito");
        }
        return texto;
    }
    
    
  
}
