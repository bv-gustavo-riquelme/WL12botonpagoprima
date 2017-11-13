package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import java.util.Date;

import oracle.xml.parser.v2.XMLDocument;

/**
 * Tabla : BICEVIDA.BPI_DNA_DETNAVEG_TBL
 */
public class DetalleNavegacion  implements Serializable {

    private Integer codpagina;          //COD_PAGINA
    private Long idNavegacion;          //ID_NAVEGACION
    private Date fechaHora;             //FECHA_HORA
    private Integer montoTransaccion;   //MONTO_TRANSACCION
    private String detallePagina;       //DETALLE_PAGINA
    private Integer entrada;            //ENTRADA
    private Integer idCanal;
    private XMLDocument  xmldetalle;

    public DetalleNavegacion() {
    
        super();
    }

    public String getDetallePagina() {
        return this.detallePagina;
    }

    public Integer getEntrada() {
        return this.entrada;
    }

    public Date getFechaHora() {
        return this.fechaHora;
    }

    public Long getIdNavegacion() {
        return this.idNavegacion;
    }

    public Integer getMontoTransaccion() {
        return this.montoTransaccion;
    }
    
    public void setDetallePagina(String detallePagina) {
        this.detallePagina = detallePagina;
    }

    public void setEntrada(Integer entrada) {
        this.entrada = entrada;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = (fechaHora==null)?new Date(System.currentTimeMillis()):fechaHora;
    }

    public void setIdNavegacion(Long idNavegacion) {
        this.idNavegacion = idNavegacion;
    }

    public void setMontoTransaccion(Integer montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public void setCodpagina(Integer codpagina) {
        this.codpagina = codpagina;
    }

    public Integer getCodpagina() {
        return codpagina;
    }

    public void setXmldetalle(XMLDocument xmldetalle) {
        this.xmldetalle = xmldetalle;
    }

    public XMLDocument getXmldetalle() {
        return xmldetalle;
    }

    public void setIdCanal(Integer idCanal) {
        this.idCanal = idCanal;
    }

    public Integer getIdCanal() {
        return idCanal;
    }
}
