package cl.bice.vida.botonpago.services.notificacion;

import cl.bice.vida.botonpago.modelo.util.DateUtils;
import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;
import cl.bice.vida.botonpago.services.notificacion.types.InformarRecaudacionIn;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

public class RecaudacionServiceClient {

    private static final Logger LOGGER = Logger.getLogger(RecaudacionServiceClient.class);

    private static RecaudacionServiceWebServiceService recaudacionServiceWebServiceService;
    private RecaudacionServiceWebService _port;

    static {
        try {
            recaudacionServiceWebServiceService = new RecaudacionServiceWebServiceService();
        } catch (Exception e) {
            LOGGER.error("Error al crear instancia RecaudacionServiceClient", e);
        }

    }

    public RecaudacionServiceClient() {
        _port = recaudacionServiceWebServiceService.getRecaudacionService();
    }

    public static void main(String[] args) {
        RecaudacionServiceClient client = new RecaudacionServiceClient();
        // Add your code to call the desired methods.
        InformarRecaudacionIn in = new InformarRecaudacionIn();
        in.setCallerSystem(ResourceBundleUtil.getProperty("integracion.webservice.notificacion.recaudacion.callersystem"));
        in.setCallerUser(ResourceBundleUtil.getProperty("integracion.webservice.notificacion.recaudacion.calleruser"));

        in.setFolioCajaDiario(9432L); //SELECT
        in.setFolioPago(8003610861L);
        Calendar cale = new GregorianCalendar();
        cale.setTime(new Date());
        in.setTurno(DateUtils.calendarToXMLGregorianCalendar(cale));

        // RECUPERA EL TURNO DE I
        in.setIdTurno(69214L);
        in.setUsuarioCaja(ResourceBundleUtil.getProperty("integracion.webservice.notificacion.recaudacion.usuariocaja"));

        //LLAMA A PAGAR EL FOLIO INDICADO

        try {
            client.informarRecaudacion(in);
        } catch (ApplicationModelException e) {
            e.printStackTrace();
        }

    }


    public void informarRecaudacion(InformarRecaudacionIn in) throws ApplicationModelException {
        _port.informarRecaudacion(in);

    }

    public void setEndpoint(String endpoint) {
        cargaEnPoint(endpoint);
    }

    public String getEndpoint() {
        BindingProvider bp = (BindingProvider) _port;

        return bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY) + "";
    }

    public void cargaEnPoint(String endpoint) {
        BindingProvider bp = (BindingProvider) _port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
    }
}
