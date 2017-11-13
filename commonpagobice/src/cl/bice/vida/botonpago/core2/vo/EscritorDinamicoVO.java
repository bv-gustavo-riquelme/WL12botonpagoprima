package cl.bice.vida.botonpago.core2.vo;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;


public class EscritorDinamicoVO {
    private Hashtable valores;
    private List<RegistroValoresVO> valorlst;
    
    public EscritorDinamicoVO() {
    }

    /**
     * Agrega valor a la lista
     * @param nombre
     * @param valor
     */
    public void addField(String nombre, Object valor) {
        if (valores==null) {
            valores = new Hashtable();
            valorlst = new ArrayList();
        }
        RegistroValoresVO vo = new RegistroValoresVO(nombre, valor);
        valores.put(nombre, vo);
        valorlst.add(vo);
    }

    /**
     * Recupera valor de la lista
     * @param nombre
     * @return
     */
    public Object getField(String nombre) {
        if (valores==null) {
            valores = new Hashtable();
            valorlst = new ArrayList();
        }
        RegistroValoresVO valor = (RegistroValoresVO) valores.get(nombre);
        //VERIFICA SI EL VALOR SE ENCONTRO, DE NO SER ASI HABRA QUE BUSCAR POR EL COMIENZO
        //DEL NOMBRE DE CAMPO YA QUE PODRIA SER UN CAMPO QUE ES REPETIDO Y TIENE UNA ITERACION
        if (valor == null) {
            Iterator en = valorlst.iterator();
            while (en.hasNext()) {
               RegistroValoresVO vo = (RegistroValoresVO) en.next();
               if (nombre.startsWith(vo.getKey())) {
                   valor = new RegistroValoresVO(nombre, vo.getValor());
                   break;
               }
            }
        }
        return valor!=null?valor.getValor():null;
    }

    public Hashtable getValores() {
        return valores;
    }
}


