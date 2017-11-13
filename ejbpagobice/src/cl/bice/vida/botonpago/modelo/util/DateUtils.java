package cl.bice.vida.botonpago.modelo.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


public class DateUtils {
    private DateUtils() {
        
    }
    
    
    /**
     * Devuelve la fecha en formato deseado
     * @param fecha
     * @param format
     * @return String
     */
    public static String getFechaFormat(Date fecha, String format) {
        java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat(format);
        return formato.format(fecha);
    } 
    
    public static XMLGregorianCalendar calendarToXMLGregorianCalendar(Calendar cal) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(cal.getTimeInMillis());
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }
    
    public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar xmlCal) {
        GregorianCalendar cal = null;
        if(cal != null) {
            cal = xmlCal.toGregorianCalendar();
            
            return cal.getTime();
        }
        return null;
    }
}
