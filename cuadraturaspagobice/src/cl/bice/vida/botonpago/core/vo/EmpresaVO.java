package cl.bice.vida.botonpago.core.vo;

import java.io.Serializable;

import java.util.Hashtable;
import java.util.List;

public class EmpresaVO implements Serializable {
    private String descripcion;
    private String codigo;
    private Integer idDataBase;
    private Hashtable medios;
    
    public void addField(String pk, MedioPagoVO field) {
        if (getMedios()==null) medios = new Hashtable();
        medios.put(pk, field);
    }    

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setIdDataBase(Integer idDataBase) {
        this.idDataBase = idDataBase;
    }

    public Integer getIdDataBase() {
        return idDataBase;
    }

    public void setMedios(Hashtable medios) {
        this.medios = medios;
    }

    public Hashtable getMedios() {
        return medios;
    }
}
