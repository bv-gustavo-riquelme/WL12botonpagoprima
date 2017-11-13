package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

import oracle.xml.parser.v2.XMLDocument;


public class DetalleServicioPecFace  implements Serializable {

    private Integer idNavegacion;
    private Integer entrada;
    private Integer codPagina;
    private Date fechaHora;
    private Integer montoTransaccion;    
    private XMLDocument  xmldetalle;

    public DetalleServicioPecFace() {
        super();
    }

    public void setIdNavegacion(Integer idNavegacion) {
        this.idNavegacion = idNavegacion;
    }

    public Integer getIdNavegacion() {
        return idNavegacion;
    }

    public void setEntrada(Integer entrada) {
        this.entrada = entrada;
    }

    public Integer getEntrada() {
        return entrada;
    }

    public void setCodPagina(Integer codPagina) {
        this.codPagina = codPagina;
    }

    public Integer getCodPagina() {
        return codPagina;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = (fechaHora==null)?new Date(System.currentTimeMillis()):fechaHora;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setMontoTransaccion(Integer montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public Integer getMontoTransaccion() {
        return montoTransaccion;
    }
    

    public void setXmldetalle(XMLDocument xmldetalle) {
        this.xmldetalle = xmldetalle;
    }

    public XMLDocument getXmldetalle() {
        return xmldetalle;
    }
    
    public String toString() {            
        return "idNavegacion :" + idNavegacion + "\n" +
               "entrada :" + entrada + "\n" +
               "codPagina :" + codPagina + "\n" +
               "fechaHora :" + fechaHora + "\n" +
               "xmldetalle :" + xmldetalle + "\n" +
               "montoTransaccion :" + montoTransaccion;
    }    
}
