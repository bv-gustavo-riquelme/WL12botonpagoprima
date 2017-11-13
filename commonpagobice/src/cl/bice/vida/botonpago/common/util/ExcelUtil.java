package cl.bice.vida.botonpago.common.util;

import java.text.SimpleDateFormat;

public class ExcelUtil {
    private static String[] simbols = new String[11];
    private static String[] charspr = new String[11];        
    
    static  {
        simbols[0]="a"; charspr[0]="á";
        simbols[1]="e"; charspr[1]="é";
        simbols[2]="i"; charspr[2]="í";
        simbols[3]="o"; charspr[3]="ó";
        simbols[4]="u"; charspr[4]="ú";
        simbols[5]="A"; charspr[5]="Á";
        simbols[6]="E"; charspr[6]="É";
        simbols[7]="I"; charspr[7]="Í";
        simbols[8]="O"; charspr[8]="Ó";
        simbols[9]="U"; charspr[9]="Ú";        
        simbols[10]=""; charspr[10]=" ";  
    }
    /**
     * Recupera fecha formateada para Excel
     * @param fecha
     * @return
     */
    public static String getDateToExcel(java.util.Date fecha) {
        String returnString = "";
        if (fecha != null) {
           SimpleDateFormat formt = new SimpleDateFormat("dd-MM-yyyy");
           returnString = formt.format(fecha);
        }
       return returnString;
    }    
    
    /**
     * Recupera y pinta en caso de ser null nada en blanco
     * @param data
     * @return
     */
    public static String getNoNullExcel(Integer data) {
        String valor = "";
        if (data != null) valor = Integer.toString(data);
        return valor;
    }
    
    /**
     * Recupera y pinta en caso de ser null nada en blanco
     * @param data
     * @return
     */
    public static String getNoNullExcel(Long data) {
        String valor = "";
        if (data != null) valor = Long.toString(data);
        return valor;
    }    
    
    
    /**
     * Recupera y pinta en caso de ser null nada en blanco
     * @param data
     * @return
     */
    public static String getNoNullExcel(Double data) {
        String valor = "";
        if (data != null) valor = Double.toString(data);
        return valor;
    }        

    /**
     * Recupera y pinta en caso de ser null nada en blanco
     * @param data
     * @return
     */
    public static String getNoNullExcel(String data) {
        String valor = "";
        if (data != null) valor = data;
        return valor;
    }
    
    /**
     * Remover Acentuaciones de titulos
     * @param data
     * @return
     */
    public static String removeAcentuacion(String texto) {
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
}
