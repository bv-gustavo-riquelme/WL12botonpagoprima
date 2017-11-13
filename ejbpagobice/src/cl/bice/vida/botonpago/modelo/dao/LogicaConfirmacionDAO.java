package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.ResumenRequest;

import noNamespace.ConfirmacionDocument;
import noNamespace.ResumenConHeaderDocument;

import org.apache.xmlbeans.XmlObject;


public interface LogicaConfirmacionDAO {

    public void validateXML(XmlObject xml) throws Exception;
    public String crearConfirmacion(String xmlResumenPagos, ResumenRequest req);
    
    public String crearConfirmacionConXmlSeleccionado(String origenTransaccion, String xmlResumenSeleccionado, int idCanal, String email);
    public String crearConfirmacionConXmlSeleccionado(String origenTransaccion, String xmlResumenSeleccionado, int idCanal, int tipoTransaccion, String email);
    public String crearConfirmacionConXmlSeleccionadoAPT(String origenTransaccion, String xmlResumenSeleccionado, int idCanal, int tipoTransaccion, int cargoExtra, String email);
 
    public String generaXmlResumenPagosConProductosSeleccionados(String xmlResumenPagos, ResumenRequest req, int idCanal);  
    public String generaXmlResumenPagosConProductosSeleccionados(String xmlResumenPagos, ResumenRequest req, int idCanal, String email);  
    public String generaXmlResumenPagosConProductosSeleccionadosAPVAPT(String xmlResumenPagos, ResumenRequest req, int idCanal, int cargoTarjetaCredito);
    public void generaXmlResumenPagosConProductosSeleccionados(ResumenConHeaderDocument res,ResumenRequest req);          
    public ConfirmacionDocument generaXmlConfirmacion(ResumenConHeaderDocument res, long idTransaccion) throws Exception;

    //==== PRIMERA PRIMA ====
    public String generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima(String xmlResumenPagos, ResumenRequest req, int idCanal);
    public String crearConfirmacionConXmlSeleccionadoPrimeraPrima(String origenTransaccion, String xmlResumenSeleccionado, int idCanal, String email);
}

