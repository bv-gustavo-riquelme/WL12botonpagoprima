package cl.bicevida.botonpago.util;

import org.apache.log4j.Logger;

import java.sql.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Clase de utilidades para fechas y horas
 * @author mmaldonado
 *
 */
public class FechaUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FechaUtil.class);

    /**
	 * convierte un string a fecha
	 * @param fecha
	 * @param formato
	 * @return Date
	 * @throws ParseException
	 */
    public static java.util.Date toDate(String fecha, String formato) {
		logger.info("toDate(String fecha=" + fecha + ", String formato=" + formato + ") - iniciando");

        SimpleDateFormat form = new SimpleDateFormat(formato);
        Date fechatmp = null;
        try {
            fechatmp = form.parse(fecha);
        } catch (ParseException e) {
			logger.error("toDate(String, String) - catch (error)", e);

            fechatmp = new Date(System.currentTimeMillis());
        }

		logger.info("toDate(String fecha=" + fecha + ", String formato=" + formato + ") - termina");
        return fechatmp;
    }

    /**
	 * Recupera el primer dia del mes en curso
	 * y entrega un valor long que sirve para
	 * construir un objeto Date (util.Date, sql.Date)
	 * @return long
	 */
    public static long getPrimerDiadelMesEnCurso() {
		logger.info("getPrimerDiadelMesEnCurso() - iniciando");

        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setLenient(true);
        calendar1.set(java.util.Calendar.DAY_OF_MONTH, 1);
		long returnlong = calendar1.getTimeInMillis();
		logger.info("getPrimerDiadelMesEnCurso() - termina");
        return returnlong;
    }

    /**
	 * Recupera el primer dia del mes en curso
	 * y entrega un valor long que sirve para
	 * construir un objeto Date (util.Date, sql.Date)
	 * @return long
	 */
    public static long getUltimoDiaHabilNow() {
		logger.info("getUltimoDiaHabilNow() - iniciando");

        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setLenient(true);
        int dayOfWeek = calendar1.get(calendar1.DAY_OF_WEEK);
        /**
		 * Evalua si el dia es domingo o sabado
		 */
        if (dayOfWeek == 1 || dayOfWeek == 7) {
            if (dayOfWeek == 7)
                calendar1.add(java.util.Calendar.DATE, -1);
            if (dayOfWeek == 1)
                calendar1.add(java.util.Calendar.DATE, -2);
        }
		long returnlong = calendar1.getTimeInMillis();
		logger.info("getUltimoDiaHabilNow() - termina");
        return returnlong;
    }

    /**
	 * Recupera la fecha tope desde los
	 * 13 ultimos meses
	 * @return long
	 */
    public static long getUltimo13MesesNow() {
		logger.info("getUltimo13MesesNow() - iniciando");

        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setLenient(true);
        /**
		 * resta 13 meses a la fecha actual
		 */
        calendar1.add(java.util.Calendar.MONTH, -13);
		long returnlong = calendar1.getTimeInMillis();
		logger.info("getUltimo13MesesNow() - termina");
        return returnlong;
    }

    /**
	 * Retorna el a�o de una fecha
	 * @param fecha
	 * @return Integer
	 */
    public static Integer getAnoFecha(Object fecha) {
		logger.info("getAnoFecha(Object fecha=" + fecha + ") - iniciando");

        Integer agno = new Integer(0);
        if (fecha instanceof Calendar) {
            agno = ((Calendar)fecha).YEAR;
        } else if (fecha instanceof Date) {
            agno = ((Date)fecha).getYear() + 1900;
        }

		logger.info("getAnoFecha(Object fecha=" + fecha + ") - termina");
        return agno;
    }

    /**
     * Retorna el nombre del mes de una fecha
     * @param fecha
     * @return String
     */
    public static Integer getMesFecha(Object fecha) {
		logger.info("getMesFecha(Object fecha=" + fecha + ") - iniciando");

        int mes = 0;
        if (fecha instanceof Calendar) {
            mes = ((Calendar)fecha).MONTH;
        } else if (fecha instanceof Date) {
            mes = ((Date)fecha).getMonth() + 1;
        }
		Integer returnInteger = new Integer(mes);
		logger.info("getMesFecha(Object fecha=" + fecha + ") - termina");
        return returnInteger;
    }

    /**
     * Retorna el nombre del mes de una fecha
     * @param fecha
     * @return String
     */
    public static String getNombreMesFecha(Object fecha) {
		logger.info("getNombreMesFecha(Object fecha=" + fecha + ") - iniciando");

		String returnString = FechaUtil.getNombreMes(getMesFecha(fecha));
		logger.info("getNombreMesFecha(Object fecha=" + fecha + ") - termina");
        return returnString;
    }

    /**
     * Retorna la hora de una fecha
     * @param fecha
     * @return String
     */
    public static

    String getHoraFecha(Object fecha) {
		logger.info("getHoraFecha(Object fecha=" + fecha + ") - iniciando");

        String hora = "00:00";
        if (fecha instanceof Calendar) {
            hora = ((Calendar)fecha).HOUR_OF_DAY + "";
        } else if (fecha instanceof Date) {
            hora = ((Date)fecha).getHours() + ":" + ((Date)fecha).getMinutes();
        }

		logger.info("getHoraFecha(Object fecha=" + fecha + ") - termina");
        return hora;
    }

    /**
     * Retorna el nombre del dia de una fecha y el numero (Martes 23)
     * @param fecha
     * @return String
     */
    public static String getNombreDiaFecha(Object fecha) {
		logger.info("getNombreDiaFecha(Object fecha=" + fecha + ") - iniciando");

        String fechaOut = "";
        if (fecha instanceof Calendar) {
            fechaOut = 
                    FechaUtil.getNombreDia(((Calendar)fecha).DAY_OF_WEEK) + " " + 
                    ((Calendar)fecha).DAY_OF_WEEK;
        } else if (fecha instanceof Date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date)fecha);
            fechaOut = 
                    FechaUtil.getNombreDia(calendar.get(Calendar.DAY_OF_WEEK)) + 
                    " " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        }

		logger.info("getNombreDiaFecha(Object fecha=" + fecha + ") - termina");
        return fechaOut;
    }

    /**
         * Retorna el nombre del mes
         * @param mes numero de mes
         * @return nombre del mes
         */
    public static String getNombreMes(int mes) {
		logger.info("getNombreMes(int mes=" + mes + ") - iniciando");

        switch (mes) {
        case 1:
            return "Enero";
        case 2:
            return "Febrero";
        case 3:
            return "Marzo";
        case 4:
            return "Abril";
        case 5:
            return "Mayo";
        case 6:
            return "Junio";
        case 7:
            return "Julio";
        case 8:
            return "Agosto";
        case 9:
            return "Septiembre";
        case 10:
            return "Octubre";
        case 11:
            return "Noviembre";
        case 12:
            return "Diciembre";
        default:
            return "";
        }
    }

    /**
	     * Retorna el nombre del dia
	     * @param diaSemana numero de dia de la semana
	     * @return nombre del dia
	     */
    public static String getNombreDia(int diaSemana) {
		logger.info("getNombreDia(int diaSemana=" + diaSemana + ") - iniciando");

        switch (diaSemana) {
        case 1:
            return "Lunes";
        case 2:
            return "Martes";
        case 3:
            return "Miercoles";
        case 4:
            return "Jueves";
        case 5:
            return "Viernes";
        case 6:
            return "Sabado";
        case 7:
            return "Domingo";
        default:
            return "";
        }
    }

    /**
         * Retorna una la fecha del primer dia del mes actual
         * @return Date
         */
    public static Date getFistDayOfMonth() {
		logger.info("getFistDayOfMonth() - iniciando");

		Date returnDate = new Date(getPrimerDiadelMesEnCurso());
		logger.info("getFistDayOfMonth() - termina");
        return returnDate;
    }

    /**
         * Retorna una la fecha del primer dia del mes actual
         * @return Timestamp
         */
    public static Timestamp getFisrtDayOfMonth() {
		logger.info("getFisrtDayOfMonth() - iniciando");

        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setLenient(true);
        calendar1.set(java.util.Calendar.DAY_OF_MONTH, 0);
		Timestamp returnTimestamp = new Timestamp(calendar1.getTimeInMillis());
		logger.info("getFisrtDayOfMonth() - termina");
        return returnTimestamp;
    }

    /**
         * Convierte una fecha tipo Date a Timestamp
         * @param fecha
         * @return Timestamp
         */
    public static Timestamp DateToTimestamp(Date fecha) {
		logger.info("DateToTimestamp(Date fecha=" + fecha + ") - iniciando");

		Timestamp returnTimestamp = new java.sql.Timestamp(fecha.getTime());
		logger.info("DateToTimestamp(Date fecha=" + fecha + ") - termina");
        return returnTimestamp;
    }

    /**
    	 * Devuelve la fecha en formato DD/MM/YYYY
    	 * @param fecha
    	 * @return String
    	 */
    public static String getFechaNormal(Timestamp fecha) {
            logger.info("getFechaNormal(Timestamp fecha=" + fecha + ") - iniciando");
            java.text.SimpleDateFormat formato = 
            new java.text.SimpleDateFormat("dd/MM/yyyy");
		String returnString = formato.format(fecha);
		logger.info("getFechaNormal(Timestamp fecha=" + fecha + ") - termina");
        return returnString;
    }

    /**
    	 * Devuelve la fecha en formato DD/MM/YYYY
    	 *
    	 * @return String
    	 */
    public static String getFechaFormateoStandar(Date fecha) {
        if (fecha == null) fecha = new Date();
        Locale locale = new Locale("ES");
        java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy",locale);
        return formato.format(fecha);
    }
    
    /**
         * Devuelve la fecha en formato DD/MM/YYYY
         *
         * @return String
         */
    public static String getFechaFormateoCustom(Date fecha, String formato) {
        if (fecha == null) fecha = new Date();
        Locale locale = new Locale("ES");
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(formato, locale);
        String formatook = format.format(fecha);        
        String primeraletra = formatook.substring(0,1).toUpperCase();
        return primeraletra + formatook.substring(1);
    }    
    
    /**
     * Formateo
     * @param fecha
     * @return
     */
    public static String getFechaFormateoNumeros(Date fecha) {
                logger.info("getFechaFormateoNumeros(Date fecha=" + fecha + ") - iniciando");
        java.text.SimpleDateFormat formato = 
            new java.text.SimpleDateFormat("yyyyMMdd");
                String returnString = formato.format(fecha);
                logger.info("getFechaFormateoNumeros(Date fecha=" + fecha + ") - termina");
        return returnString;
    }    

    /**
    	 * Devuelve la fecha actual formateada
    	 * @return String
    	 */
    public static String getFecha() {
		logger.info("getFecha() - iniciando");

        java.text.SimpleDateFormat formato = 
            new java.text.SimpleDateFormat("dd/MM/yyyy"); //$NON-NLS-1$ 
		String returnString = formato.format(new java.util.Date());
		logger.info("getFecha() - termina");
        return returnString;
    }

    /**
    	 * Valida si la primera fecha es menor o igual que la segunda fecha
    	 * @param fecha1
    	 * @param fecha2
    	 * @return boolean
    	 */
    public static boolean validaFechas(Timestamp fecha1, Timestamp fecha2) {
		logger.info("validaFechas(Timestamp fecha1=" + fecha1 + ", Timestamp fecha2=" + fecha2 + ") - iniciando");

        int result = fecha1.compareTo(fecha2);
        if (result <= 0) {
			logger.info("validaFechas(Timestamp fecha1=" + fecha1 + ", Timestamp fecha2=" + fecha2 + ") - termina");
            return true;
        } else {
			logger.info("validaFechas(Timestamp fecha1=" + fecha1 + ", Timestamp fecha2=" + fecha2 + ") - termina");
            return false;
        }
    }

    /**
    	 * Genera un java.sql.Date a partir de un formato
    	 * de fecha y hora especifico.
    	 *
    	 * Ejemplo:
    	 *   Date sqldate = getFecha("dd/MM/yyyy HH:mm:ss", "12-10-2005 10:50:43");
    	 * @param format
    	 * @param value
    	 * @return Date
    	 */
    public static Date getFecha(String format, String value) {
		logger.info("getFecha(String format=" + format + ", String value=" + value + ") - iniciando");

        long fechahora = 0;
        SimpleDateFormat formato = new SimpleDateFormat(format);
        try {
            fechahora = formato.parse(value).getTime();
        } catch (ParseException e) {
			logger.error("getFecha(String, String) - catch (error)", e);
        }
		Date returnDate = new Date(fechahora);
		logger.info("getFecha(String format=" + format + ", String value=" + value + ") - termina");
        return returnDate;
    }


    /**
    	 * Recupera en formato fecha hora
    	 * @param format
    	 * @param value
    	 * @return
    	 */
    public static Timestamp getTimestamp(String format, String value) {
		logger.info("getTimestamp(String format=" + format + ", String value=" + value + ") - iniciando");

        long fechahora = 0;
        SimpleDateFormat formato = new SimpleDateFormat(format);
        try {
            fechahora = formato.parse(value).getTime();
        } catch (ParseException e) {
			logger.error("getTimestamp(String, String) - catch (error)", e);
        }
		Timestamp returnTimestamp = new Timestamp(fechahora);
		logger.info("getTimestamp(String format=" + format + ", String value=" + value + ") - termina");
        return returnTimestamp;
    }

    /**
    	 * A partir de una fecha String de formato formatIn entrega la fecha con el formato
    	 * pasado como out
    	 * @param fechaIn fecha a convertir
    	 * @param formatoIn  formato de entrada
    	 * @param formatoOut formato de salida
    	 * @return la fecha transformada
    	 */
    public static String formatearFecha(String fechaIn, String formatoIn, 
                                        String formatoOut) {
		logger.info("formatearFecha(String fechaIn=" + fechaIn + ", String formatoIn=" + formatoIn + ", String formatoOut=" + formatoOut + ") - iniciando");

        String formateado;
        try {
            if (!fechaIn.equals("")) {
                Date dateSql = getFecha(formatoIn, fechaIn);
                SimpleDateFormat df1 = 
                    new java.text.SimpleDateFormat(formatoOut);
                formateado = df1.format(dateSql);
            } else {
                formateado = "";
            }

        } catch (Exception ex) {
			logger.error("formatearFecha(String, String, String) - catch (error)", ex);

            formateado = "";
        }

		logger.info("formatearFecha(String fechaIn=" + fechaIn + ", String formatoIn=" + formatoIn + ", String formatoOut=" + formatoOut + ") - termina");
        return formateado;
    }

}
