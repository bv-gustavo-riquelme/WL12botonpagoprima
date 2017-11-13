package cl.bice.vida.botonpago.common.dto.general;

import java.io.Serializable;

import oracle.xml.parser.v2.XMLDocument;

public class BpiCppComprobpagopolizaTbl  implements Serializable {

    private Long idComprobpago;
    private Long rutCliente;
    private Long folioOperacion;
    private String lugarPago;
    private XMLDocument comprobante;
    private String nombreCliente;
    private String instrPago;

    public BpiCppComprobpagopolizaTbl() {
        super();
    }

    public XMLDocument getComprobante() {
        return this.comprobante;
    }

    public Long getFolioOperacion() {
        return this.folioOperacion;
    }

    public Long getIdComprobpago() {
        return this.idComprobpago;
    }

    public String getInstrPago() {
        return this.instrPago;
    }

    public String getLugarPago() {
        return this.lugarPago;
    }

    public String getNombreCliente() {
        return this.nombreCliente;
    }

    public Long getRutCliente() {
        return this.rutCliente;
    }

    public void setComprobante(XMLDocument comprobante) {
        this.comprobante = comprobante;
    }

    public void setFolioOperacion(Long folioOperacion) {
        this.folioOperacion = folioOperacion;
    }

    public void setIdComprobpago(Long idComprobpago) {
        this.idComprobpago = idComprobpago;
    }

    public void setInstrPago(String instrPago) {
        this.instrPago = instrPago;
    }

    public void setLugarPago(String lugarPago) {
        this.lugarPago = lugarPago;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setRutCliente(Long rutCliente) {
        this.rutCliente = rutCliente;
    }

}
