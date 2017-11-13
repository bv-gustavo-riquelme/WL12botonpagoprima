package cl.bice.vida.botonpago.modelo.thread;

import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.modelo.dao.DAOFactory;
import cl.bice.vida.botonpago.modelo.util.DateUtils;
import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;
import cl.bice.vida.botonpago.services.notificacion.RecaudacionServiceClient;
import cl.bice.vida.botonpago.services.notificacion.types.InformarRecaudacionIn;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;


public class GenerarPagoThread {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(GenerarPagoThread.class);    


    private SPActualizarTransaccionDto dto;
    
    public GenerarPagoThread(SPActualizarTransaccionDto dto) {
      this.dto = dto;
    }
    
    /**
     *  Ejecucion Asincrona
     */
    public void pagar() {
        try {
            long start = System.currentTimeMillis();
            BpiTraTransaccionesTbl transaccion = null;
            System.out.println("EJECUTANDO PAGO EN SISTEMA DE CAJAS");
            Boolean out = DAOFactory.getPersistenciaGeneralDao().actualizaTransaccionSP(dto);
            System.out.println("RESUTALDO EJECUTANDO PAGO EN SISTEMA DE CAJAS:" + out);
            if (out.booleanValue() == true) {
                System.out.println("Iniciando envio a sistema AR con WS");
                //SI LE FUE BIEN EN EL PAGO DE LA TRANSACCION SE REALIZA LA NOTIFIACION AL AR
                RecaudacionServiceClient myPort;
                try {
                    //CONSULTA LA TRANSACCION PARA REALIZAR EL CARGO DE PAGO
                    System.out.println("--> Buscando transaccion si es Bice Vida para informar AR");
                    transaccion = DAOFactory.getConsultasDao().findTransaccionCodigoEstadoByNumTransaccion(dto.getIdTrx());     
                    //if (transaccion != null && transaccion.getCodigoEmpresa().equalsIgnoreCase("BV")) {
                    if (transaccion != null) {
                        
                        System.out.println("--> Transaccion es Bice Vida o Hipotecaria, verificando que exista numero de folio caja");
                        if (transaccion.getNumeroFolioCaja() != null && transaccion.getNumeroFolioCaja().longValue() > 0) {
                            System.out.println("-->Todo en orden informado operacion de caja:"+transaccion.getNumeroFolioCaja()+" a AR");
                            myPort = new RecaudacionServiceClient();
                            myPort.setEndpoint(ResourceBundleUtil.getProperty("webservice.recaudacion.endpoint"));
                            logger.info("=====> ENDPOINT SERVICIO: "+ myPort.getEndpoint() +" <========");
                            InformarRecaudacionIn in = new InformarRecaudacionIn();
                            in.setCallerSystem(ResourceBundleUtil.getProperty("integracion.webservice.notificacion.recaudacion.callersystem"));
                            in.setCallerUser(ResourceBundleUtil.getProperty("integracion.webservice.notificacion.recaudacion.calleruser"));
                          
                            //INFORMA SOLO CON EL NUMERO DE TURNO ABIERTO PARA INTERNET
                            Long foliocajadiario = null;
                            java.util.Date fechaTurno = DAOFactory.getPersistenciaGeneralDao().getFechaYHoraServidorOracle();
                            java.util.Date hoyantes2pm = FechaUtil.getFecha("dd/MM/yyyy HH:mm:ss", FechaUtil.getFechaFormateoCustom(fechaTurno,"dd/MM/yyyy") + " 14:00:00");
                            
                            System.out.println("-->Fecha de Base de Datos:"+ FechaUtil.getFechaFormateoCustom(fechaTurno, "dd/MM/yyyy HH:mm:ss"));
                            System.out.println("-->Fecha de Limite Notificacion:"+ FechaUtil.getFechaFormateoCustom(hoyantes2pm, "dd/MM/yyyy HH:mm:ss"));
                            
                            if (fechaTurno.getTime() <= hoyantes2pm.getTime()) {
                                System.out.println("-->Iniciando proceso de notificacion WS Integracion....");
                                foliocajadiario = DAOFactory.getPersistenciaGeneralDao().getFolioCajaByTurno(fechaTurno);
                                Long idTurnoInternet = DAOFactory.getPersistenciaGeneralDao().getIdTurnoOpenForInternet();
                                
                                if (foliocajadiario != null && foliocajadiario.longValue() > 0) {
                                    if (idTurnoInternet != null && idTurnoInternet.longValue() > 0) {
                                    
                                        logger.info(">>----> FOLIO CAJA DIARIA: " + foliocajadiario);
                                        logger.info(">>----> ID TURNO INTERNET: " + idTurnoInternet);
                                        logger.info(">>----> FECHA TURNO      : " + fechaTurno);
                                        
                                        in.setFolioCajaDiario(foliocajadiario);//SELECT
                                        in.setFolioPago(transaccion.getNumeroFolioCaja());
                                        Calendar cale = new GregorianCalendar();
                                        cale.setTime(fechaTurno);
                                        in.setTurno(DateUtils.calendarToXMLGregorianCalendar(cale));
                                        
                                        // RECUPERA EL TURNO DE I
                                        in.setIdTurno(idTurnoInternet);
                                        in.setUsuarioCaja(ResourceBundleUtil.getProperty("integracion.webservice.notificacion.recaudacion.usuariocaja"));
                                        
                                        //LLAMA A PAGAR EL FOLIO INDICADO
                                        myPort.informarRecaudacion(in);
                                        System.out.println("--> Envio existoso a sistema AR para informar el pago");
                                        logger.info("--> Envio existoso a sistema AR para informar el pago");
                                    } else {
                                     System.out.println("--> NOTA: Aun no hay turno de caja abierto para usuario INTERNET ---");
                                     logger.info("--> NOTA: Aun no hay turno de caja abierto para usuario INTERNET ---");                                 
                                    }
                                } else {
                                    System.out.println("--> NOTA: Aun no hay folio de caja diaria abierto se interaran en las siguientes transacciones de pago ---");
                                    logger.info("--> NOTA: Aun no hay folio de caja diaria abierto se interaran en las siguientes transacciones de pago ---");
                                }
                            } else {
                                System.out.println("--> NOTA: El horario de notificacion es mayor a las 14:00 horas, se procedera a informar en el siguiente turno ---");
                                logger.info("--> NOTA: El horario de notificacion es mayor a las 14:00 horas, se procedera a informar en el siguiente turno ---");
                            }
                        }
                    }
                } catch (Exception e) {
                   e.printStackTrace();
                }
                
            }
            long end = System.currentTimeMillis();        
            logger.info("actualizaTransaccionSP() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
