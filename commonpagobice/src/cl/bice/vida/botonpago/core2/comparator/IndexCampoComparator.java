package cl.bice.vida.botonpago.core2.comparator;

import cl.bice.vida.botonpago.core2.vo.FieldStructureVo;

import java.util.Comparator;


public class IndexCampoComparator implements Comparator<FieldStructureVo>  {
    /**
     * Realiza comparacion
     * @param o1
     * @param o2 
     * @return
     */
    public int compare(FieldStructureVo o1, FieldStructureVo o2) {
        return o1.getIndexadd() - o2.getIndexadd();
    }
}
