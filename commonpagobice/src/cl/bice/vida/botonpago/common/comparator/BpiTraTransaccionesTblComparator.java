package cl.bice.vida.botonpago.common.comparator;

import cl.bice.vida.botonpago.common.dto.general.BpiDnaDetnavegTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;

import java.util.Comparator;

/**
 * Clase Comparator para odern de Listas en Java
 */
public class BpiTraTransaccionesTblComparator implements Comparator {

     /**
     * Compara dos objetos con el objetivo
     * de determinar su orden en una lista
     * @param t1
     * @param t2
     * @return
     */
    public int compare(Object t1, Object t2) {
        BpiDnaDetnavegTbl o1 = (BpiDnaDetnavegTbl) t1; 
        BpiDnaDetnavegTbl o2 = (BpiDnaDetnavegTbl) t2;
        if( o1.getFechaHora().getTime() > o2.getFechaHora().getTime() ) {
                    return -1;
        } else {
            if( o1.getFechaHora().getTime() < o2.getFechaHora().getTime() ) {
                    return 1;
            } else {  
                    return 0;
            }
        }
    }
}