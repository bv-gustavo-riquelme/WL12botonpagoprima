package cl.bice.vida.botonpago.common.comparator;

import cl.bice.vida.botonpago.common.dto.general.BpiDitribFondosAPVTbl;

import java.util.Comparator;


public class BpiDitribFondosAPVTblComparator implements Comparator {

     /**
     * Compara dos objetos con el objetivo
     * de determinar su orden en una lista
     * @param t1
     * @param t2
     * @return
     */
    public int compare(Object t1, Object t2) {
        BpiDitribFondosAPVTbl o1 = (BpiDitribFondosAPVTbl) t1; 
        BpiDitribFondosAPVTbl o2 = (BpiDitribFondosAPVTbl) t2;
        if( o1.getOrdenPresentacion() < o2.getOrdenPresentacion() ) {
                    return -1;
        } else {
            if( o1.getOrdenPresentacion() > o2.getOrdenPresentacion() ) {
                    return 1;
            } else {  
                    return 0;
            }
        }
    }
}