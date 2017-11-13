package cl.bice.vida.botonpago.core.comparator;

import cl.bice.vida.botonpago.core.vo.TransaccionRendicionVO;

import java.util.Comparator;


public class TransaccionRendicionVOComparator implements Comparator {

     /**
     * Compara dos objetos con el objetivo
     * de determinar su orden en una lista
     * @param t1
     * @param t2
     * @return
     */
    public int compare(Object t1, Object t2) {
        TransaccionRendicionVO o1 = (TransaccionRendicionVO) t1; 
        TransaccionRendicionVO o2 = (TransaccionRendicionVO) t2;
        if(  Long.parseLong(o1.getNumeroOrdenBice()) > Long.parseLong(o2.getNumeroOrdenBice()) ) {
                    return -1;
        } else {
            if( Long.parseLong(o1.getNumeroOrdenBice()) < Long.parseLong(o2.getNumeroOrdenBice()) ) {
                    return 1;
            } else {  
                    return 0;
            }
        }
    }
}