package cl.bice.vida.botonpago;

import cl.bice.vida.botonpago.modelo.util.DateUtils;
import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;
import cl.bice.vida.botonpago.services.notificacion.ApplicationModelException;
import cl.bice.vida.botonpago.services.notificacion.RecaudacionServiceClient;
import cl.bice.vida.botonpago.services.notificacion.types.InformarRecaudacionIn;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestWs {
    public TestWs() {
        super();
    }
    
    public static void main(String[] args) {
            RecaudacionServiceClient recaudacionServiceWebService = new RecaudacionServiceClient();
            // Add your code to call the desired methods.
            InformarRecaudacionIn in = new InformarRecaudacionIn();
            in.setCallerSystem(ResourceBundleUtil.getProperty("integracion.webservice.notificacion.recaudacion.callersystem"));
            in.setCallerUser(ResourceBundleUtil.getProperty("integracion.webservice.notificacion.recaudacion.calleruser"));
            
            in.setFolioCajaDiario(9432L);//SELECT
            in.setFolioPago(8003610861L);
            Calendar cale = new GregorianCalendar();
            cale.setTime(new Date());
            System.out.println(DateUtils.calendarToXMLGregorianCalendar(cale));
            in.setTurno(DateUtils.calendarToXMLGregorianCalendar(cale));
            
            // RECUPERA EL TURNO DE I
            in.setIdTurno(69214L);
            in.setUsuarioCaja(ResourceBundleUtil.getProperty("integracion.webservice.notificacion.recaudacion.usuariocaja"));
            
            //LLAMA A PAGAR EL FOLIO INDICADO
            try {
                recaudacionServiceWebService.informarRecaudacion(in);
            } catch (ApplicationModelException e) {
                e.printStackTrace();
            }

        }
}
