package cl.bice.vida.botonpago.utils;

import java.math.BigDecimal;

import java.sql.Date;
import java.sql.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Clase de utilidades para fechas y horas
 * @author mmaldonado
 *
 */
public class FechaUtil {

    /**
     * Formateo
     * @param fecha
     * @return
     */
    public static String getFechaFormateoNumeros(java.util.Date fecha) {
        java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyyyMMdd");
        String returnString = formato.format(fecha);
        return returnString;
    }    


    /**
     * Recupera el primer dia
     * del mes anterior
     * @return
     */
    public static java.util.Date getPrimerDiaMesAnterior() {
        Calendar calendar = Calendar.getInstance();        
        //mes anterior
        calendar.add(Calendar.MONTH,-1);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }
    
    /**
     * Recupera el primer dia
     * del mes anterior
     * @return
     */
    public static java.util.Date getPrimerDiaMesSiguiente() {
        Calendar calendar = Calendar.getInstance();        
        //mes anterior
        calendar.add(Calendar.MONTH,2);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);        
        return calendar.getTime();
    }
    
    /**
	 * convierte un string a fecha
	 * @param fecha
	 * @param formato
	 * @return Date
	 * @throws ParseException
	 */
    public static java.util.Date toDate(String fecha, String formato) {
        SimpleDateFormat form = new SimpleDateFormat(formato);
        java.util.Date fechatmp = null;
        try {
            fechatmp = form.parse(fecha);
        } catch (ParseException e) {
            fechatmp = new Date(System.currentTimeMillis());
        }
        return fechatmp;
    }
    
    /**
     * convierte una fecha de tipo
     * sql a una fecha de tipo util
     * @param fecha
     * @param formato
     * @return Date
     * @throws ParseException
     */
    public static java.util.Date toUtilDate(java.sql.Date fecha) {
        java.util.Date resp = null;
        if (fecha != null) resp = new java.util.Date(fecha.getTime());
        return resp;
    }
    
    /**
     * convierte una timestamp de tipo
     * sql a una fecha de tipo util
     * @param fecha
     * @param formato
     * @return Date
     * @throws ParseException
     */
    public static java.util.Date toUtilDate(java.sql.Timestamp timestamp) {
        java.util.Date resp = null;
        if (timestamp != null) resp = new java.util.Date(timestamp.getTime());
        return resp;
    }    
    

    /**
	 * Recupera el primer dia del mes en curso
	 * y entrega un valor long que sirve para
	 * construir un objeto Date (util.Date, sql.Date)
	 * @return long
	 */
    public static long getPrimerDiadelMesEnCurso() {
        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setLenient(true);
        calendar1.set(java.util.Calendar.DAY_OF_MONTH, 1);
        long returnlong = calendar1.getTimeInMillis();
        return returnlong;
    }

    /**
	 * Recupera el primer dia del mes en curso
	 * y entrega un valor long que sirve para
	 * construir un objeto Date (util.Date, sql.Date)
	 * @return long
	 */
    public static long getUltimoDiaHabilNow() {
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
        return returnlong;
    }
    
    /**
     * Ultimo dia del mes
     * @param fecha
     * @return
     */
    public static int getUltimoDiaHabilDelMesFecha(java.util.Date fecha) {
    
        Calendar calendar = Calendar.getInstance();        
        calendar.setTime(fecha);
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0); 
        
        java.util.Date fecha1erdiaNuevoMes = new java.util.Date(calendar.getTimeInMillis());
        
        
        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setTime(fecha1erdiaNuevoMes);
        calendar1.add(Calendar.DAY_OF_MONTH,-1);
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
        java.util.Date fechaGen = new java.util.Date(returnlong);
        String dia = getFechaFormateoCustom(fechaGen,"dd");
        return new Integer(dia);
    }

    /**
	 * Recupera la fecha tope desde los
	 * 13 ultimos meses
	 * @return long
	 */
    public static long getUltimo13MesesNow() {
        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setLenient(true);
        /**
		 * resta 13 meses a la fecha actual
		 */
        calendar1.add(java.util.Calendar.MONTH, -13);
        long returnlong = calendar1.getTimeInMillis();
        return returnlong;
    }

    /**
     * Retorna el año de una fecha
     * @param fecha
     * @return Integer
     */
    public static Integer getAnoFecha(Object fecha) {
        Integer agno = new Integer(0);
        if (fecha instanceof java.util.Calendar) {
            Calendar cal = (Calendar) fecha;
            agno = Integer.parseInt(getFechaFormateoCustom(new java.util.Date(cal.getTimeInMillis()),"yyyyy"));
        } else if (fecha instanceof java.util.Date) {
            java.util.Date cal = (java.util.Date) fecha;
            agno = Integer.parseInt(getFechaFormateoCustom(new java.util.Date(cal.getTime()),"yyyyy"));
        }
        return agno;
    }

    /**
     * Retorna el nombre del mes de una fecha
     * @param fecha
     * @return String
     */
    public static Integer getMesFecha(Object fecha) {
        int mes = 0;
        if (fecha instanceof Calendar) {
            mes = ((Calendar)fecha).MONTH;
        } else if (fecha instanceof java.util.Date) {
            SimpleDateFormat formato = new SimpleDateFormat("MM");
            String mesStr = formato.format((java.util.Date)fecha);
            mes =  new Integer(mesStr);
        }
        Integer returnInteger = new Integer(mes);
        return returnInteger;
    }

    /**
     * Recupera el dia de la fehca
     */
    public static Integer getDiaFecha(Object fecha) {
        int dia = 0;
        if (fecha instanceof Calendar) {
            dia = ((Calendar)fecha).DAY_OF_MONTH;
        } else if (fecha instanceof java.util.Date) {
            SimpleDateFormat formato = new SimpleDateFormat("dd");
            String mesStr = formato.format((java.util.Date)fecha);
            dia =  new Integer(mesStr);
        }
        Integer returnInteger = new Integer(dia);
        return returnInteger;
    }

    /**
     * Retorna el nombre del mes de una fecha
     * @param fecha
     * @return String
     */
    public static String getNombreMesFecha(Object fecha) {
        String returnString = FechaUtil.getNombreMes(getMesFecha(fecha));
        return returnString;
    }

    /**
     * Retorna la hora de una fecha
     * @param fecha
     * @return String
     */
    public static

    String getHoraFecha(Object fecha) {
        String hora = "00:00";
        if (fecha instanceof Calendar) {
            hora = ((Calendar)fecha).HOUR_OF_DAY + "";
        } else if (fecha instanceof Date) {
            SimpleDateFormat formato = new SimpleDateFormat("mm:ss");
            hora = formato.format((java.util.Date)fecha);        
        }
        return hora;
    }

    /**
     * Retorna el nombre del dia de una fecha y el numero (Martes 23)
     * @param fecha
     * @return String
     */
    public static String getNombreDiaFecha(Object fecha) {
        String fechaOut = "";
        if (fecha instanceof Calendar) {
            fechaOut = FechaUtil.getNombreDia(((Calendar)fecha).DAY_OF_WEEK) + " " + 
                    ((Calendar)fecha).DAY_OF_WEEK;
        } else if (fecha instanceof Date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date)fecha);
            fechaOut = FechaUtil.getNombreDia(calendar.get(Calendar.DAY_OF_WEEK)) + 
                    " " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        }
        return fechaOut;
    }

    /**
         * Retorna el nombre del mes
         * @param mes numero de mes
         * @return nombre del mes
         */
    public static String getNombreMes(int mes) {
        switch (mes) {
        case 1:            return "Enero";
        case 2:            return "Febrero";
        case 3:            return "Marzo";
        case 4:            return "Abril";
        case 5:            return "Mayo";
        case 6:            return "Junio";
        case 7:            return "Julio";
        case 8:            return "Agosto";
        case 9:            return "Septiembre";
        case 10:            return "Octubre";
        case 11:            return "Noviembre";
        case 12:            return "Diciembre";
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
        switch (diaSemana) {
        case 1:            return "Lunes";
        case 2:            return "Martes";
        case 3:            return "Miercoles";
        case 4:            return "Jueves";
        case 5:            return "Viernes";
        case 6:            return "Sabado";
        case 7:            return "Domingo";
        default:            return "";
        }
    }

    /**
         * Retorna una la fecha del primer dia del mes actual
         * @return Date
         */
    public static Date getFistDayOfMonth() {
        Date returnDate = new Date(getPrimerDiadelMesEnCurso());
        return returnDate;
    }

    /**
         * Retorna una la fecha del primer dia del mes actual
         * @return Timestamp
         */
    public static Timestamp getFisrtDayOfMonth() {
        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setLenient(true);
        calendar1.set(java.util.Calendar.DAY_OF_MONTH, 0);
        Timestamp returnTimestamp = new Timestamp(calendar1.getTimeInMillis());
        return returnTimestamp;
    }

    /**
         * Convierte una fecha tipo Date a Timestamp
         * @param fecha
         * @return Timestamp
         */
    public static Timestamp DateToTimestamp(Date fecha) {
		Timestamp returnTimestamp = new java.sql.Timestamp(fecha.getTime());
        return returnTimestamp;
    }

    /**
    	 * Devuelve la fecha en formato DD/MM/YYYY
    	 * @param fecha
    	 * @return String
    	 */
    public static String getFechaNormal(Timestamp fecha) {
            java.text.SimpleDateFormat formato = 
            new java.text.SimpleDateFormat("dd/MM/yyyy");
		String returnString = formato.format(fecha);
        return returnString;
    }

    /**
    	 * Devuelve la fecha en formato DD/MM/YYYY
    	 *
    	 * @return String
    	 */
    public static String getFechaFormateoStandar(java.util.Date fecha) {
        if (fecha == null) fecha = new java.util.Date();
        Locale locale = new Locale("ES");
        java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy",locale);
        return formato.format(fecha);
    }
    
    /**
         * Devuelve la fecha en formato DD/MM/YYYY
         *
         * @return String
         */
    public static String getFechaFormateoCustom(java.util.Date fecha, String formato) {
        if (fecha == null) fecha = new java.util.Date();
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
        java.text.SimpleDateFormat formato = 
            new java.text.SimpleDateFormat("yyyyMMdd");
                String returnString = formato.format(fecha);
        return returnString;
    }    

    /**
    	 * Devuelve la fecha actual formateada
    	 * @return String
    	 */
    public static String getFecha() {
        java.text.SimpleDateFormat formato = 
        new java.text.SimpleDateFormat("dd/MM/yyyy"); //$NON-NLS-1$ 
            String returnString = formato.format(new java.util.Date());
        return returnString;
    }

    /**
    	 * Valida si la primera fecha es menor o igual que la segunda fecha
    	 * @param fecha1
    	 * @param fecha2
    	 * @return boolean
    	 */
    public static boolean validaFechas(Timestamp fecha1, Timestamp fecha2) {
        int result = fecha1.compareTo(fecha2);
        if (result <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
    	 * Genera un java.util.Date a partir de un formato
    	 * de fecha y hora especifico.
    	 *
    	 * Ejemplo:
    	 *   java.util.Date utildate = getFecha("dd-MM-yyyy hh:mm:ss", "12-10-2005 10:50:43");
    	 * @param format
    	 * @param value
    	 * @return Date
    	 */
    public static java.util.Date getFecha(String format, String value) {
        java.util.Date fechahora = new java.util.Date();
        SimpleDateFormat formato = new SimpleDateFormat(format);
        try {
            fechahora = formato.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechahora;
    }
    
    

    /**
         * Genera un java.util.Date a partir de un formato
         * de fecha y hora especifico.
         *
         * Ejemplo:
         *   java.util.Date utildate = getFecha("dd-MM-yyyy hh:mm:ss", "12-10-2005 10:50:43");
         * @param format
         * @param value
         * @return Date
         */
    public static java.util.Date getFechaByError(String format, String value) throws Exception {
        java.util.Date fechahora = new java.util.Date();
        SimpleDateFormat formato = new SimpleDateFormat(format);
        try {
            //if (format.length() == value.length()) {
                fechahora = formato.parse(value);
            //} else {
            //    throw new Exception("Error formateo de fecha '" + format + "'");
           // }
        } catch (ParseException e) {
            throw new Exception("Error formateo de fecha '" + format + "'");
        }
        return fechahora;
    }

    /**
     * Genera fecha para SQL
     * @param format
     * @param value
     * @return
     */
    public static java.sql.Date getFechaSql(String format, String value) {
        long fechahora = 0;
        SimpleDateFormat formato = new SimpleDateFormat(format);
        try {
            fechahora = formato.parse(value).getTime();
        } catch (ParseException e) {
        }
        return new java.sql.Date(fechahora);
    }

    /**
    	 * Recupera en formato fecha hora
    	 * @param format
    	 * @param value
    	 * @return
    	 */
    public static Timestamp getTimestamp(String format, String value) {
        long fechahora = 0;
        SimpleDateFormat formato = new SimpleDateFormat(format);
        try {
            fechahora = formato.parse(value).getTime();
        } catch (ParseException e) {
        }
        Timestamp returnTimestamp = new Timestamp(fechahora);
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
    public static String formatearFecha(String fechaIn, String formatoIn, String formatoOut) {
        String formateado;
        try {
            if (!fechaIn.equals("")) {
                Date dateSql = getFechaSql(formatoIn, fechaIn);
                SimpleDateFormat df1 = new java.text.SimpleDateFormat(formatoOut);
                formateado = df1.format(dateSql);
            } else {
                formateado = "";
            }

        } catch (Exception ex) {
            formateado = "";
        }
        return formateado;
    }
    
    /**
     * Formatea una fecha de tipo string que ya estan
     * en un determinado formato a otro formato nuevo
     * @param fechaIn
     * @param formatoIn
     * @param formatoOut
     * @return
     */
    public static String formatearFechaconFormatoSalida(String fechaIn, String formatoIn, String formatoOut) {
        String formateado;
        try {
            if (!fechaIn.equals("")) {
                java.util.Date dateSql = getFecha(formatoIn, fechaIn);
                SimpleDateFormat df1 = new SimpleDateFormat(formatoOut, new Locale("ES"));
                formateado = df1.format(dateSql);
            } else {
                formateado = "";
            }
        } catch (Exception ex) {
            formateado = "";
        }
        return formateado;
    }

    /**
     * Primer dia habil del mes
     * @param fecha
     * @return
     */
    public static java.util.Date getPrimerDiaHabilMes(java.util.Date fecha) {
        //1 = DOMINGO
        //2 = LUNES
        //3 = MARTES
        //4 = MIERCOLES
        //5 = JUEVES
        //6 = VIERNES
        //7 = SABADO
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        System.out.println(calendar.getTime().toString());        
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) calendar.add(Calendar.DATE, 1); // AGREGA UN DIA
        if (weekday == 7) calendar.add(Calendar.DATE, 2); // AGREGA DOS DIA
        return calendar.getTime();
    }
    
    /**
     * Suma horas a una fecha
     * @param fecha
     * @param horas
     * @return
     */
    public static java.util.Date getSumaHorasFecha(java.util.Date fecha, Long horas) {
        Calendar ahora = Calendar.getInstance();
        ahora.setTime(fecha);
        ahora.add(Calendar.HOUR, new Integer(horas.toString()));
        return ahora.getTime();
    }
    
    
    /**
     * Recupera la fecha con el primer dia del mes
     * @param fecha
     * @return
     */
    public static java.util.Date getInicioMesByFecha(java.util.Date fecha) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(fecha);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();    
    }
    
    /**
     * Recupera la fecha con el ultimo dia del mes
     * @param fecha
     * @return
     */
    public static java.util.Date getTerminoMesByFecha(java.util.Date fecha) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(fecha);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1); //AGREGA UN MES
        calendar.add(Calendar.DATE, -1); //RESTA UN DIA
        return calendar.getTime();
    }
    
    
    /**
     * Verifica si la hora es mayor a las 14 horas
     * @param hora
     * @return
     */
    public static boolean isMayorHoras(java.util.Date hora, int horas24, int minutos) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(hora);
        int nowhora = calendar.get(Calendar.HOUR); 
        int nowminuto = calendar.get(Calendar.MINUTE); 
        if (nowhora > horas24) return true;
        if (nowhora < horas24) return false;
        if (nowhora == horas24) {
            if (nowminuto >= minutos) return true;
            if (nowminuto < minutos) return false;
        }
        return false;
    }
    
    /**
     * Posiciona en una fecha habil
     * @return long
     */
    public static java.util.Date getFechaPosicionadaEnDiaHabil(java.util.Date fecha) {
        Calendar calendar1 = GregorianCalendar.getInstance(new Locale("es"));
        calendar1.setTime(fecha);
        int dayOfWeek = calendar1.get(calendar1.DAY_OF_WEEK);
        if (dayOfWeek == 1 || dayOfWeek == 7) {
            if (dayOfWeek == 7) calendar1.add(java.util.Calendar.DATE, 2);
            if (dayOfWeek == 1) calendar1.add(java.util.Calendar.DATE, 1);
        }
        return calendar1.getTime();
    }
    
    /**
     * Suma dias a una fecha
     * @param fecha
     * @param dias
     * @return
     */
    public static java.util.Date sumarDiasFecha(java.util.Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DATE, dias); // AGREGA DIAS / O QUITA (-)
        return calendar.getTime();
    }
    
    
    /**
     * Main de pruebas
     * @param args
     */
    public static void main(String[] args) {
        FechaUtil fec = new FechaUtil();
        System.out.println("Inicio :" + fec.getFechaPosicionadaEnDiaHabil(new java.util.Date(System.currentTimeMillis())));
    }

}
