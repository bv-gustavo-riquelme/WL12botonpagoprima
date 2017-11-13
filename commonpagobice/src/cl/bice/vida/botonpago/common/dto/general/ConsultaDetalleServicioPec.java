package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

import oracle.xml.parser.v2.XMLDocument;


public class ConsultaDetalleServicioPec  implements Serializable {   
    /**Map servicioPEC <-> bicevida.datamodel.ServicioPEC
     * @associates <{bicevida.datamodel.ServicioPEC}>
     */
    private ServicioPec servicioPEC;
    
    private Long idNavegacion;
    private Integer entrada;
    private Integer codPagina;
    private Date fechaHora;
    private Integer montoTransaccion;
    
    private XMLDocument  xmldetalle;

    public ConsultaDetalleServicioPec() {
        super();
        this.servicioPEC = new ServicioPec();
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

    public void setXmldetalle(XMLDocument xmldetalle) {
        this.xmldetalle = xmldetalle;
    }

    public XMLDocument getXmldetalle() {
        return xmldetalle;
    }

    public void setServicioPEC(ServicioPec servicioPEC) {
        this.servicioPEC = servicioPEC;
    }

    public ServicioPec getServicioPEC() {
        return this.servicioPEC;
    }
}
