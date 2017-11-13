package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

import oracle.xml.parser.v2.XMLDocument;


public class DetalleServicioPec  implements Serializable {

    private Long idNavegacion;
    private Integer entrada;
    private Integer codPagina;
    private Date fechaHora;
    private Integer montoTransaccion;
    private String detallePagina;
    
    private XMLDocument  xmldetalle;

    public DetalleServicioPec() {
        super();
    }

    public void setIdNavegacion(Long idNavegacion) {
        this.idNavegacion = idNavegacion;
    }

    public Long getIdNavegacion() {
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

    public void setDetallePagina(String detallePagina) {
        this.detallePagina = detallePagina;
    }

    public String getDetallePagina() {
        return detallePagina;
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
               "montoTransaccion :" + montoTransaccion + "\n" +
               "detallePagina :" + detallePagina;
    }    
}
