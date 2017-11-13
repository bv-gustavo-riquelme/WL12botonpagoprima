package cl.bice.vida.botonpago.core.vo;

import java.io.Serializable;

public class FieldStructureVo implements Serializable {
    private String id;           //Identificador en el sistema
    private String nombre;       //Nombre del campo
    private int posicionInicial; //Posicion inicial de lectura en caso de largo fijo
    private int largo;           //Largo del campo
    private String tagNombre;    //Nombre de Tag XML a leer
    private String tipo;         //Tipo de campo a convertir
    private String formato;      //En caso de fechas con formato de lectura
    private int indexTab;        //Indice de tabulacion para recuperacion del campo delimitado
    private String substructura; //Estructura adicial para un campo que no esta descompuesto aun
    private boolean decimales = false;
    
    public FieldStructureVo() {
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setPosicionInicial(int posicionInicial) {
        this.posicionInicial = posicionInicial;
    }

    public int getPosicionInicial() {
        return posicionInicial;
    }

    public void setLargo(int largo) {
        this.largo = largo;
    }

    public int getLargo() {
        return largo;
    }

    public void setTagNombre(String tagNombre) {
        this.tagNombre = tagNombre;
    }

    public String getTagNombre() {
        return tagNombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getFormato() {
        return formato;
    }

    public void setIndexTab(int indexTab) {
        this.indexTab = indexTab;
    }

    public int getIndexTab() {
        return indexTab;
    }

    public void setSubstructura(String substructura) {
        this.substructura = substructura;
    }

    public String getSubstructura() {
        return substructura;
    }

    public void setDecimales(boolean decimales) {
        this.decimales = decimales;
    }

    public boolean isDecimales() {
        return decimales;
    }
}
