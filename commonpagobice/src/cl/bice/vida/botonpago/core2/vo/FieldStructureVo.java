package cl.bice.vida.botonpago.core2.vo;

import java.io.Serializable;


public class FieldStructureVo implements Serializable {
    private int indexadd;        //Indice de en que orden han sido agregados
  
    //LECTOR - ESCRITOR
    private String id;           //Identificador en el sistema
    private String nombre;       //Nombre del campo
    private int largo;           //Largo del campo
    private String tagNombre;    //Nombre de Tag XML a leer
    private String tipo;         //Tipo de campo a convertir
    private String formato;      //En caso de fechas con formato de lectura
    private String substructura; //Estructura adicial para un campo que no esta descompuesto aun
    private int columnaXLS;      //Numero de columna del Excel a utilizar
    private boolean simulateValue = false; //Simular valor
    private String valueSimulateVar; //Variable a simular
    private String hardvalue; //Valores duro
    
    //LECTOR
    private int posicionInicial; //Posicion inicial de lectura en caso de largo fijo
    private int indexTab;        //Indice de tabulacion para recuperacion del campo delimitado
    
    //ESCRITOR
    private int numdecimales; //Cantidad de decimales para salida
    private boolean sepdecimales = false; //Utilizara separador de decimales en la salida
    private String separadorDecimal; //Simbolo de separacion decimal
    private String rellenarIzquierda;
    private String rellenarDerecha;
    
    //DESCRIPCION
    private String descripcionCampo = null;
    
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

    public void setColumnaXLS(int columnaXLS) {
        this.columnaXLS = columnaXLS;
    }

    public int getColumnaXLS() {
        return columnaXLS;
    }

    public void setSimulateValue(boolean simulateValue) {
        this.simulateValue = simulateValue;
    }

    public boolean isSimulateValue() {
        return simulateValue;
    }

    public void setValueSimulateVar(String valueSimulateVar) {
        this.valueSimulateVar = valueSimulateVar;
    }

    public String getValueSimulateVar() {
        return valueSimulateVar;
    }

    public void setHardvalue(String hardvalue) {
        this.hardvalue = hardvalue;
    }

    public String getHardvalue() {
        return hardvalue;
    }

    public void setNumdecimales(int numdecimales) {
        this.numdecimales = numdecimales;
    }

    public int getNumdecimales() {
        return numdecimales;
    }

    public boolean isSepdecimales() {
        sepdecimales = false;
        if (separadorDecimal != null && separadorDecimal.trim().length() > 0) sepdecimales = true;
        return sepdecimales;
    }

    public void setRellenarIzquierda(String rellenarIzquierda) {
        this.rellenarIzquierda = rellenarIzquierda;
    }

    public String getRellenarIzquierda() {
        return rellenarIzquierda;
    }

    public void setRellenarDerecha(String rellenarDerecha) {
        this.rellenarDerecha = rellenarDerecha;
    }

    public String getRellenarDerecha() {
        return rellenarDerecha;
    }

    public void setIndexadd(int indexadd) {
        this.indexadd = indexadd;
    }

    public int getIndexadd() {
        return indexadd;
    }

    public void setSeparadorDecimal(String separadorDecimal) {
        this.separadorDecimal = separadorDecimal;
    }

    public String getSeparadorDecimal() {
        if (separadorDecimal == null || separadorDecimal.trim().length() == 0) separadorDecimal = ".";
        return separadorDecimal;
    }

    public void setDescripcionCampo(String descripcionCampo) {
        this.descripcionCampo = descripcionCampo;
    }

    public String getDescripcionCampo() {
        return descripcionCampo==null?nombre:descripcionCampo;
    }
}
