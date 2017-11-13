package cl.bice.vida.botonpago.modelo.ejb;

import cl.bice.vida.botonpago.common.dto.general.BpiCarCartolasTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiDitribFondosAPVTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiDlcDetalleLogCuadrTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiDnaDetnavegTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiLgclogCuadraturaTbl;
import cl.bice.vida.botonpago.common.dto.general.BpiTraTransaccionesTbl;
import cl.bice.vida.botonpago.common.dto.general.ConsultaDetalleServicioPec;
import cl.bice.vida.botonpago.common.dto.general.DetalleAPV;
import cl.bice.vida.botonpago.common.dto.general.Navegacion;
import cl.bice.vida.botonpago.common.dto.general.PrcCuadraturaDto;
import cl.bice.vida.botonpago.common.dto.general.PrcProcesoGeneraFileDto;
import cl.bice.vida.botonpago.common.dto.general.PrcProcesoReadFileDto;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionByEmpMedioInPeriod;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.RecaudacionMedioByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.ReporteAgentePolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteGerentePolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteJefeSucursalPolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteJefeZonalPolizas;
import cl.bice.vida.botonpago.common.dto.general.ReporteSupervisorPolizas;
import cl.bice.vida.botonpago.common.dto.general.ResumenRequest;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.dto.general.TransaccionByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.TransaccionMedioByEmpresa;
import cl.bice.vida.botonpago.common.dto.general.TransaccionMedioByEmpresaByMedio2;
import cl.bice.vida.botonpago.common.dto.vistas.BpiDttDettraturnoVw;
import cl.bice.vida.botonpago.common.dto.vistas.BpiRtcRestrancuadrVw;
import cl.bice.vida.botonpago.common.dto.vistas.BpiVreRechazosVw;
import cl.bice.vida.botonpago.common.dto.vistas.IndPrimaNormalVw;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.modelo.dao.DAOFactory;
import cl.bice.vida.botonpago.modelo.dto.BpiPecConsultaTransaccionDto;
import cl.bice.vida.botonpago.modelo.dto.HomologacionConvenioDTO;
import cl.bice.vida.botonpago.modelo.dto.PersonaCotizadorDto;
import cl.bice.vida.botonpago.modelo.dto.RegimenTributarioDTO;
import cl.bice.vida.botonpago.modelo.rest.RESTfulCallUtil;
import cl.bice.vida.botonpago.modelo.rest.RESTfulParametroDto;
import cl.bice.vida.botonpago.modelo.util.DateUtils;
import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;
import cl.bice.vida.botonpago.services.notificacion.RecaudacionServiceClient;
import cl.bice.vida.botonpago.services.notificacion.types.InformarRecaudacionIn;
import cl.bice.vida.botonpago.servicios.cuentainversion.CuentaDeInversionClient;
import cl.bice.vida.botonpago.servicios.cuentainversion.types.ListOfRetornoInformacionRegimen;
import cl.bice.vida.botonpago.servicios.cuentainversion.types.ObtenerInformacionRegimen;
import cl.bice.vida.botonpago.servicios.cuentainversion.types.RetornoInformacionRegimen;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import oracle.xml.parser.v2.XMLDocument;

import org.apache.log4j.Logger;

@Stateless(name = "MedioPagoElectronicoEJBBean",mappedName = "MedioPagoElectronicoEJBBean")
@Local(MedioPagoElectronicoEJB.class)
public class MedioPagoElectronicoEJBBean implements MedioPagoElectronicoEJB {

   
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(MedioPagoElectronicoEJBBean.class);    


    //================ FUNCIONES DE TURNO ======================================
    /**
     * Apertura de caja del dia
     * @return
     */
    public String aperturaCaja(){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getPersistenciaGeneralDao().aperturaCaja();
        long end = System.currentTimeMillis();        
        logger.info("aperturaCaja() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;
    }
    
    /**
     * Cierra de caja del dia
     * @return
     */
    public String cierreCaja(){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getPersistenciaGeneralDao().cierreCaja();
        long end = System.currentTimeMillis();        
        logger.info("cierreCaja() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;
    }
    
    /**
     * Cuadratura 
     * @param cuadraturadto
     * @return
     */
    public String cuadratura(PrcCuadraturaDto cuadraturadto){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getPersistenciaGeneralDao().cuadratura(cuadraturadto);
        long end = System.currentTimeMillis();        
        logger.info("cuadratura() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;
    } 
    
    //================ FUNCIONES DE RESUMEN Y CUENTAS POR COBRAR ===============
    /**
     * Metodo de creacion de XML para resumen
     * y pagos de polizas desde medios de pago 
     * publico y boton perfilado
     * @param rut
     * @param nombre
     * @param confirmacion
     * @return string con el xml de resumen y pagos
     */
    public String crearResumenPagosPolizas(Integer rut, String nombre, String confirmacion, int idCanal){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaResumenPagosDao().crearResumenPagosPolizas(rut ,nombre ,confirmacion, idCanal);
        long end = System.currentTimeMillis();        
        logger.info("crearResumenPagosPolizas() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;
    }
    
    /**
     * Metodo de creacion de XML para resumen
     * y pagos de productos APV desde medios de pago 
     * publico y boton perfilado
     * @param rut
     * @param nombre
     * @param confirmacion
     * @return string con el xml de resumen y pagos
     */
    public String crearResumenPagosAPVAPT(Integer rut, String nombre, String confirmacion, int idCanal){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaResumenPagosDao().crearResumenPagosAPVAPT(rut ,nombre ,confirmacion, idCanal);
        long end = System.currentTimeMillis();        
        logger.info("crearResumenPagosPolizas() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;
    }

    /**
     * Metodo de creacion de XML para resumen
     * y pasos de dividendos desde medios de pago
     * publico y boton perfilado
     * @param rut
     * @param nombre
     * @param confirmacion
     * @return
     */
    public String crearResumenPagosDividendos(Integer rut, String nombre, String confirmacion, int idCanal){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaResumenPagosDao().crearResumenPagosDividendos(rut ,nombre ,confirmacion, idCanal);
        long end = System.currentTimeMillis();        
        logger.info("crearResumenPagosDividendos() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;    
    }
    

    //================ FUNCIONES GENERA XML RESUMEN CON PRODUCTOS SELECCIONADOS ===========
    /**
     * Genera el XML de resumen y pagos con productos seleccionados
     * @param xml con la data de resumen y pagos
     * @param req productos que se han seleccionado para grabacion
     * @return XMl con el resultad de grabacion
     */
    public String generaXmlResumenPagosConProductosSeleccionados(String xml, ResumenRequest req, int idCanal){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaConfirmacionDao().generaXmlResumenPagosConProductosSeleccionados(xml, req, idCanal);
        long end = System.currentTimeMillis();        
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;        
    } 
    
    /**
     * Genera el XML de resumen y pagos con productos seleccionados
     * @param xml con la data de resumen y pagos
     * @param req productos que se han seleccionado para grabacion
     * @return XMl con el resultad de grabacion
     */
    public String generaXmlResumenPagosConProductosSeleccionadosAPVAPT(String xml, ResumenRequest req, int idCanal, int cargoTarjetaCredito){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaConfirmacionDao().generaXmlResumenPagosConProductosSeleccionadosAPVAPT(xml, req, idCanal, cargoTarjetaCredito);
        long end = System.currentTimeMillis();        
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;        
    }   
    
    //================ FUNCIONES DE CONFIRMACION DE PAGO ELECTRONICO ===================
    
    /**
     * Creac la confirmacion con el XML seleccionado
     * @param xmlResumenSeleccionado
     * @return
     */
    public String crearConfirmacionConXmlSeleccionado(String xmlResumenSeleccionado, String email, int idCanal){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaConfirmacionDao().crearConfirmacionConXmlSeleccionado("B",xmlResumenSeleccionado, idCanal, email);
        long end = System.currentTimeMillis();        
        logger.info("crearConfirmacionConXmlSeleccionado() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;         
    }
    
    /**
     * Creac la confirmacion con el XML seleccionado
     * @param xmlResumenSeleccionado
     * @return
     */
    public String crearConfirmacionConXmlSeleccionado(String xmlResumenSeleccionado, int idCanal, int tipoTransaccion, String email){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaConfirmacionDao().crearConfirmacionConXmlSeleccionado("B",xmlResumenSeleccionado, idCanal, tipoTransaccion, email);
        long end = System.currentTimeMillis();        
        logger.info("crearConfirmacionConXmlSeleccionado() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;         
    }
    
    /**
     * Creac la confirmacion con el XML seleccionado para aportacion extraordinaria
     * @param xmlResumenSeleccionado
     * @return
     */
    public String crearConfirmacionConXmlSeleccionadoAPT(String xmlResumenSeleccionado, int idCanal, int tipoTransaccion, int cargoExtra, String email){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaConfirmacionDao().crearConfirmacionConXmlSeleccionadoAPT("A",xmlResumenSeleccionado, idCanal, tipoTransaccion, cargoExtra, email);
        long end = System.currentTimeMillis();        
        logger.info("crearConfirmacionConXmlSeleccionado() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;         
    }
    
    //================ FUNCIONES DE BUSQUEDA Y OTROS =======================
    
    /**
     * Consulta del ID de la transaccion
     * @param id
     * @return
     */
    public BpiDnaDetnavegTbl findComprobanteByTransaccion(Long id) {
        long start = System.currentTimeMillis();
        BpiDnaDetnavegTbl out = DAOFactory.getPersistenciaGeneralDao().findComprobanteByTransaccion(id);
        long end = System.currentTimeMillis();        
        logger.info("findComprobanteByTransaccion() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }
    
    /**
     * Busca Comprobante y 
     * retorna el XML del comprobante
     * segun otras llaves de busqueda
     * @param tipo
     * @param idcomprobante
     * @param num_ope
     * @param num_div
     * @return
     */
    public XMLDocument findComprobante(int tipo, Long idcomprobante, Long num_ope, Integer num_div){
        long start = System.currentTimeMillis();
        XMLDocument out = DAOFactory.getConsultasDao().findComprobante(tipo, idcomprobante, num_ope, num_div);
        long end = System.currentTimeMillis();        
        logger.info("findComprobante() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;  
    }    
    
    /**
     * Busca una transaccion segun su
     * numero de producto o dividendo
     * @param prod
     * @param div
     * @return
     */
    public Long findTransaccionByProdDiv(Long prod, Integer div) {
        long start = System.currentTimeMillis();
        Long out = DAOFactory.getConsultasDao().findTransaccionByProdDiv(prod, div);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionByProdDiv() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    /**
     * Busca transacicones segun su codigo de estado
     * y segun el numero de transaccion
     * @param numtrx
     * @return
     */
    public BpiTraTransaccionesTbl findTransaccionCodigoEstadoByNumTransaccion(Long numtrx) {
        long start = System.currentTimeMillis();
        BpiTraTransaccionesTbl out = DAOFactory.getConsultasDao().findTransaccionCodigoEstadoByNumTransaccion(numtrx);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionCodigoEstadoByNumTransaccion(" + numtrx + ") - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Ejecucion de Store Procedure
     * para la actualizacion de datos
     * @param dto
     * @return
     */
    public Boolean actualizaTransaccionSP(SPActualizarTransaccionDto dto) {
        long start = System.currentTimeMillis();
        Boolean out = DAOFactory.getPersistenciaGeneralDao().actualizaTransaccionSP(dto);
        if (out.booleanValue() == true) {
            System.out.println("Iniciando envio a sistema AR con WS");
            //SI LE FUE BIEN EN EL PAGO DE LA TRANSACCION SE REALIZA LA NOTIFIACION AL AR
            RecaudacionServiceClient myPort;
            try {
                //CONSULTA LA TRANSACCION PARA REALIZAR EL CARGO DE PAGO
                System.out.println("--> Buscando transaccion si es Bice Vida para informar AR");
                BpiTraTransaccionesTbl transaccion = DAOFactory.getConsultasDao().findTransaccionCodigoEstadoByNumTransaccion(dto.getIdTrx());     
                //if (transaccion != null && transaccion.getCodigoEmpresa().equalsIgnoreCase("BV")) {
                if (transaccion != null) {
                    
                    System.out.println("--> Transaccion es Bice Vida, verificando que exista numero de folio caja");
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
        return out;   
    }
    
    
    /**
     * Ejecucion de Store Procedure
     * para la actualizacion de datos
     * @param dto
     * @return
     */
    public BpiTraTransaccionesTbl actualizaTransaccionSPReturnDTO(SPActualizarTransaccionDto dto) {
        long start = System.currentTimeMillis();
        BpiTraTransaccionesTbl transaccion = null;
        Boolean out = DAOFactory.getPersistenciaGeneralDao().actualizaTransaccionSP(dto);
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
        return transaccion;   
    }
    
    
    private boolean actualizaTransaccionSPAsyncV2(SPActualizarTransaccionDto dto) {
        boolean isOK = false;
        QueueSender queueSender = null;
        Queue queue = null;
        QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        try {
            InitialContext ctx = new InitialContext();
            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx.lookup("jms/BotonPagoTopicFactory"); // lookup using initial context
            queueConnection = queueConnectionFactory.createQueueConnection(); // create ConnectionFactory
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE); // create QueueSession
            queue = (Queue) ctx.lookup("jms/BotonPagoTopic"); // lookup Queue JNDI using initial context created above
            queueSender = queueSession.createSender(queue); // create Sender using Queue JNDI as arguments
            ObjectMessage objectMessage = queueSession.createObjectMessage();
            queueConnection.start(); // start Queue connection
            
            objectMessage.setObject(dto);
            queueSender.send(objectMessage);
            
            isOK = true;
            
        } catch(NamingException e) {
            logger.error(e);
        } catch(JMSException e) {
            logger.error(e);
        } catch(Exception e) {
            logger.error(e);
        } finally {
            closeConnParams(queueConnection, queueSender, queueSession);
        }    
        
        return isOK;
    }
    
    private void closeConnParams(QueueConnection queueConnection, QueueSender queueSender, QueueSession queueSession ) {
        try {
            queueSender.close();
            queueSession.close();
            queueConnection.close();
        } catch(Exception e) {
            logger.error("Ocuyrrio un error al cerrar conexiones");
        }
    }
    
    /**
     * Ejecucion de Store Procedure
     * para la actualizacion de datos
     * @param dto
     * @return
     */
    public Boolean actualizaTransaccionSPAsync(SPActualizarTransaccionDto dto) {
    try {
              logger.info("INICIANDO TRANSACCION DE PAGO POR COLA JMS");
              InitialContext ctx = new InitialContext();

              // 1: Lookup connection factory        
              TopicConnectionFactory factory = (TopicConnectionFactory) ctx.lookup("jms/BotonPagoTopicFactory");
              
              // 2: Use connection factory to create JMS connection
              TopicConnection connection = factory.createTopicConnection();
              
              // 3: Use connection to create a session
              TopicSession session = connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
              
              // 4: Lookup destination 
              Topic topic = (Topic)ctx.lookup("jms/BotonPagoTopic");        
              
              // 5: Create a message publisher 
              TopicPublisher publisher = session.createPublisher(topic);

              // 6: Create and publish a message
              ObjectMessage msg = session.createObjectMessage();
              msg.setObject(dto);
              publisher.send(msg);
              
              // finish
              publisher.close();
              logger.info("MENSAJE DE PAGO ENVIADO. QUEDA EN LA COLA CUANDO SE EJECUTE.");
              
    } catch (Exception e) {
        if(actualizaTransaccionSPAsyncV2(dto)) {
             logger.info("MENSAJE DE PAGO ENVIADO. QUEDA EN LA COLA CUANDO SE EJECUTE.");
         } else {
            e.printStackTrace();
            logger.info("ERROR ENVIO JMS:"+e.getMessage());
            logger.error(e);       
        }
        
    }
        return Boolean.TRUE;
    }
    
    /**
     * Actualiza el estado de la transaccion
     * @param idtransaccion
     * @param cod_estado
     * @return
     */
    public Boolean updateEstadoTransaccion(Integer idtransaccion, Integer cod_estado){
        long start = System.currentTimeMillis();
        Boolean out = DAOFactory.getPersistenciaGeneralDao().updateEstadoTransaccion(idtransaccion, cod_estado);
        long end = System.currentTimeMillis();        
        logger.info("updateEstadoTransaccion() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }
    
    /**
     * Actualiza el estado de la transaccion
     * @param idtransaccion
     * @param cod_estado
     * @return
     */
    public Boolean updateEmpresaTransaccion(Integer idtransaccion, String empresa){
        long start = System.currentTimeMillis();
        Boolean out = DAOFactory.getPersistenciaGeneralDao().updateEmpresaTransaccion(idtransaccion, empresa);
        long end = System.currentTimeMillis();        
        logger.info("updateEmpresaTransaccion() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }
    
    /**
     * Actualiza el estado de la transaccion
     * @param idtransaccion
     * @param cod_banco
     * @return
     */
    public Boolean updateCodBancoTransaccion(Integer idtransaccion, Integer cod_banco){
        long start = System.currentTimeMillis();
        Boolean out = DAOFactory.getPersistenciaGeneralDao().updateCodBancoTransaccion(idtransaccion, cod_banco);
        long end = System.currentTimeMillis();        
        logger.info("updateCodBancoTransaccion() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }

    /**
     * Buscra una trasaccion segun
     * una empresa en especifico.
     * @param fechaini
     * @param fechafin
     * @return
     */
    public List<TransaccionByEmpresa> findTransaccionesByEmpresa(Date fechaini, Date fechafin){
        long start = System.currentTimeMillis();
        List out = DAOFactory.getPersistenciaGeneralDao().findTransaccionesByEmpresa(fechaini, fechafin);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesByEmpresa() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }

    /**
     * Consulta Las transacciones realizadas
     * por medios de pago segun la empresa solicitada
     * @param id
     * @param fechaini
     * @param fechafin
     * @return
     */
    public List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresa(Integer id, Date fechaini, Date fechafin){
        long start = System.currentTimeMillis();
        List out = DAOFactory.getPersistenciaGeneralDao().findTransaccionesMedioByEmpresa(id, fechaini, fechafin);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesMedioByEmpresa() - tiempo ejecucion : " +  (end-start) + " milesegundos.");        
        return out;   
    }

    /**
     * Busca la ultima lista de recudacion
     * segun la empresa que se consulta
     * @param fecha
     * @return
     */
    public List<RecaudacionByEmpresa> findLastRecaudacionByEmpresa(Date fecha){
        long start = System.currentTimeMillis();
        List out = DAOFactory.getPersistenciaGeneralDao().findLastRecaudacionByEmpresa(fecha);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionByEmpresa() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }

    /**
     * Consulta la ultima lista
     * de recudacion segun medio y empresa
     * @param id
     * @param fecha
     * @return
     */
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresa(Integer id, Date fecha){
        long start = System.currentTimeMillis();
        List out = DAOFactory.getPersistenciaGeneralDao().findLastRecaudacionMedioByEmpresa(id, fecha);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionMedioByEmpresa() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }

    /**
     * Busca el ultimo de cuadratura
     * @return
     */
    public Date findUltimoDiaCuadratura() {
        long start = System.currentTimeMillis();
        Date out = DAOFactory.getPersistenciaGeneralDao().findUltimoDiaCuadratura();
        long end = System.currentTimeMillis();        
        logger.info("findUltimoDiaCuadratura() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }
    
    /**
     * Genera reporte de generente
     * segun polizas
     * @return
     */
    public List<ReporteGerentePolizas> generateReporteGerentePolizas(){
        long start = System.currentTimeMillis();
        List<ReporteGerentePolizas> out = DAOFactory.getReportesDao().generateReporteGerentePolizas();
        long end = System.currentTimeMillis();        
        logger.info("generateReporteGerentePolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }

    /**
     * Genera Reporte de Jefe de Sucursal
     * segun polizas
     * @param num_jefe_sucursal
     * @return
     */
    public List<ReporteJefeSucursalPolizas> generateReporteJefeSucursalPolizas(Integer num_jefe_sucursal){
        long start = System.currentTimeMillis();
        List<ReporteJefeSucursalPolizas> out = DAOFactory.getReportesDao().generateReporteJefeSucursalPolizas(num_jefe_sucursal);
        long end = System.currentTimeMillis();        
        logger.info("generateReporteJefeSucursalPolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }

    /**
     * Genera reporte de supervisor segun
     * polizas
     * @param numero_supervisor
     * @return
     */
    public List<ReporteSupervisorPolizas> generateReporteSupervisorPolizas(Integer numero_supervisor){
        long start = System.currentTimeMillis();
        List<ReporteSupervisorPolizas> out = DAOFactory.getReportesDao().generateReporteSupervisorPolizas(numero_supervisor);
        long end = System.currentTimeMillis();        
        logger.info("generateReporteSupervisorPolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    /**
     * Generera reporte de agentes
     * por polizas
     * @param numero_agente
     * @param rowdesde
     * @param rowhasta
     * @return
     */
    public List<ReporteAgentePolizas> generateReporteAgentePolizas(Integer numero_agente, Integer rowdesde, Integer rowhasta){
        long start = System.currentTimeMillis();
        List<ReporteAgentePolizas> out = DAOFactory.getReportesDao().generateReporteAgentePolizas(numero_agente, rowdesde, rowhasta);
        long end = System.currentTimeMillis();        
        logger.info("generateReporteAgentePolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Genera reporte de Jefe Zona
     * segun polizas
     * @param num_jefe_zona
     * @return
     */
    public List<ReporteJefeZonalPolizas> generateReporteJefeZonalPolizas(Integer num_jefe_zona){
        long start = System.currentTimeMillis();
        List<ReporteJefeZonalPolizas> out = DAOFactory.getReportesDao().generateReporteJefeZonalPolizas(num_jefe_zona);
        long end = System.currentTimeMillis();        
        logger.info("generateReporteJefeZonalPolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Genera reporte de agentes segun
     * rut y polizas
     * @param rut_agente
     * @param rowdesde
     * @param rowhasta
     * @return
     */
    public List<ReporteAgentePolizas> generateReporteAgenteByRutPolizas(Integer rut_agente, Integer rowdesde, Integer rowhasta){
        long start = System.currentTimeMillis();
        List<ReporteAgentePolizas> out = DAOFactory.getReportesDao().generateReporteAgenteByRutPolizas(rut_agente, rowdesde, rowhasta);
        long end = System.currentTimeMillis();        
        logger.info("generateReporteAgenteByRutPolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Genera reporte de supervisor
     * segun rut de polizas
     * @param rut_supervisor
     * @return
     */
    public List<ReporteSupervisorPolizas> generateReporteSupervisorByRutPolizas(Integer rut_supervisor){
        long start = System.currentTimeMillis();
        List<ReporteSupervisorPolizas> out = DAOFactory.getReportesDao().generateReporteSupervisorByRutPolizas(rut_supervisor);
        long end = System.currentTimeMillis();        
        logger.info("generateReporteSupervisorByRutPolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Genera reporte de Jefe de Sucursal segun
     * rut y polizas
     * @param rut_jefe_sucursal
     * @return
     */
    public List<ReporteJefeSucursalPolizas> generateReporteJefeSucursalByRutPolizas(Integer rut_jefe_sucursal){
        long start = System.currentTimeMillis();
        List<ReporteJefeSucursalPolizas> out = DAOFactory.getReportesDao().generateReporteJefeSucursalByRutPolizas(rut_jefe_sucursal);
        long end = System.currentTimeMillis();        
        logger.info("generateReporteJefeSucursalByRutPolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    /**
     * Genera reporte de Jefe de Zona
     * segun rut de polizas
     * @param rut_jefe_zona
     * @return
     */
    public List<ReporteJefeZonalPolizas> generateReporteJefeZonalByRutPolizas(Integer rut_jefe_zona){
        long start = System.currentTimeMillis();
        List<ReporteJefeZonalPolizas> out = DAOFactory.getReportesDao().generateReporteJefeZonalByRutPolizas(rut_jefe_zona);
        long end = System.currentTimeMillis();        
        logger.info("generateReporteJefeZonalByRutPolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    
    /**
     * Crear una cartola para contact center
     * en consulta historica de transacciones
     * @param rut
     * @param nombre
     * @param email
     * @param rut_usuario
     * @param nombre_usuario
     * @param fechaini
     * @param fechafin
     * @param tipo
     * @return
     */
    public String crearCartolaPoliza(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin, int tipo){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaCartolasDao().crearCartolaPoliza(rut, nombre, email, rut_usuario, nombre_usuario, fechaini, fechafin, tipo);
        long end = System.currentTimeMillis();        
        logger.info("crearCartolaPoliza() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
                        
    /**
     * Crea cartola para contaccenter
     * en los dividendos historicos
     * @param rut
     * @param nombre
     * @param email
     * @param rut_usuario
     * @param nombre_usuario
     * @param fechaini
     * @param fechafin
     * @param tipo
     * @return
     */
    public String crearCartolaDividendo(int rut, String nombre, String email, int rut_usuario, String nombre_usuario, Date fechaini, Date fechafin, int tipo){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaCartolasDao().crearCartolaDividendo(rut, nombre, email, rut_usuario, nombre_usuario, fechaini, fechafin, tipo);
        long end = System.currentTimeMillis();        
        logger.info("crearCartolaDividendo() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Genera recuento de reporte
     * por agente segun polizas
     * @param numero_agente
     * @return
     */
    public ReporteAgentePolizas getCountFindReporteAgentePolizas(Integer numero_agente) {
        long start = System.currentTimeMillis();
        ReporteAgentePolizas out = DAOFactory.getReportesDao().getCountFindReporteAgentePolizas(numero_agente);
        long end = System.currentTimeMillis();        
        logger.info("getCountFindReporteAgentePolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Genera recuento de reporte
     * de agentes por Rut y Polizas
     * @param rut_agente
     * @return
     */
    public ReporteAgentePolizas getCountFindReporteAgenteByRutPolizas(Integer rut_agente) {
        long start = System.currentTimeMillis();
        ReporteAgentePolizas out = DAOFactory.getReportesDao().getCountFindReporteAgenteByRutPolizas(rut_agente);
        long end = System.currentTimeMillis();        
        logger.info("getCountFindReporteAgenteByRutPolizas() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Genera Archivo de Cuadratura
     * @param generafiledto
     * @return
     */
    public String GeneraArchivoCuadratura(PrcProcesoGeneraFileDto generafiledto) {
        long start = System.currentTimeMillis();
        String out = DAOFactory.getConsultasDao().GeneraArchivoCuadratura(generafiledto);
        long end = System.currentTimeMillis();        
        logger.info("GeneraArchivoCuadratura() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Lectura de archivo de cuadratura
     * una ves que este ha sido generado
     * @param lecturafiledto
     * @return
     */
    public String LecturaArchivoCuadratura(PrcProcesoReadFileDto lecturafiledto) {
        long start = System.currentTimeMillis();
        String out = DAOFactory.getConsultasDao().LecturaArchivoCuadratura(lecturafiledto);
        long end = System.currentTimeMillis();        
        logger.info("LecturaArchivoCuadratura() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
                                                       
    /**
     * Buscar la ultima lista de recaudacion
     * segun la empresa y un rango de datos
     * @param fechamenor
     * @param fechamayor
     * @return
     */
    public List<RecaudacionByEmpresa> findLastRecaudacionByEmpresabyRango(Date fechamenor, Date fechamayor){
        long start = System.currentTimeMillis();
        List<RecaudacionByEmpresa> out = DAOFactory.getConsultasDao().findLastRecaudacionByEmpresabyRango(fechamenor, fechamayor);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionByEmpresabyRango() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Consukta el ultimo periodo de recudacion
     * segun la empresa, medio de pago y un rango de fechas
     * @param cod_empresa
     * @param cod_medio
     * @param fechamenor
     * @param fechamayor
     * @return
     */
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyRango(Integer cod_empresa, Integer cod_medio, Date fechamenor, Date fechamayor){
        long start = System.currentTimeMillis();
        List<RecaudacionByEmpMedioInPeriod> out = DAOFactory.getConsultasDao().findLastRecaudacionByEmpresaMediobyRango(cod_empresa, cod_medio, fechamenor, fechamayor);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionByEmpresaMediobyRango() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    /**
     * Consulta el ultimo periodo de recudacion
     * segun la empresa, medio de pago y un rango de fechas
     * @param cod_empresa
     * @param cod_medio
     * @param fechamenor
     * @param fechamayor
     * @param cod_medio2
     * @return
     */
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyRangoByMedio2(Integer cod_empresa, Integer cod_medio, Date fechamenor, Date fechamayor, Integer cod_medio2){
        long start = System.currentTimeMillis();
        List<RecaudacionByEmpMedioInPeriod> out = DAOFactory.getConsultasDao().findLastRecaudacionByEmpresaMediobyRangoByMedio2(cod_empresa, cod_medio, fechamenor, fechamayor, cod_medio2);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionByEmpresaMediobyRango() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    /**
     * Busca la ultima lista de recudacion
     * por la empresa y un determinado periodo
     * @param fechamenor
     * @param fechamayor
     * @return
     */
    public List<BpiRtcRestrancuadrVw> findLastRecaudacionByEmpresainPeriodo(Date fechamenor, Date fechamayor, Long empresa){
        long start = System.currentTimeMillis();
        List<BpiRtcRestrancuadrVw> out = DAOFactory.getConsultasDao().findLastRecaudacionByEmpresainPeriodo(fechamenor, fechamayor, empresa);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionByEmpresainPeriodo() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Consulta la ultima recudacion segun 
     * medio de pago, empresa en un cierto
     * periodo establecido.
     * @param id
     * @param fechamenor
     * @param fechamayor
     * @return
     */
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainPeriodo(Integer id, Date fechamenor, Date fechamayor){
        long start = System.currentTimeMillis();
        List<RecaudacionMedioByEmpresa> out = DAOFactory.getConsultasDao().findLastRecaudacionMedioByEmpresainPeriodo(id, fechamenor, fechamayor);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionMedioByEmpresainPeriodo() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    /**
     * COnsulta la recaudación de un medio específico que paga por otros medios
     * @param empresa
     * @param fechamenor
     * @param fechamayor
     * @param cod_medio
     * @return
     */
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainPeriodoByMedio(Integer empresa, Date fechamenor, Date fechamayor, Integer cod_medio){
        long start = System.currentTimeMillis();
        List<RecaudacionMedioByEmpresa> out = DAOFactory.getConsultasDao().findLastRecaudacionMedioByEmpresainPeriodoByMedio(empresa, fechamenor, fechamayor, cod_medio);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionMedioByEmpresainPeriodoByMedio() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    

    /**
     * Consulta la lista de transacciones segun la
     * empresa a la cual corresponden y su correspondiente
     * numero de transaccion
     * @param trx
     * @return
     */
    public List<TransaccionByEmpresa> findTransaccionesByEmpresabyTransaccion(String trx){
        long start = System.currentTimeMillis();
        List<TransaccionByEmpresa> out = DAOFactory.getConsultasDao().findTransaccionesByEmpresabyTransaccion(trx);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesByEmpresabyTransaccion() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }


    /**
     * Consulta transacciones segun Medio
     * de pagos, empresa y la identificacion
     * de la transaccion.
     * @param id
     * @param trx
     * @return
     */
    public List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresaByTrx(Integer id, String trx){
        long start = System.currentTimeMillis();
        List<TransaccionMedioByEmpresa> out = DAOFactory.getConsultasDao().findTransaccionesMedioByEmpresaByTrx(id, trx);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesMedioByEmpresaByTrx() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    /**
     * Consulta lista de transaccion segun medios
     * de pagos, empresa y el rut del cual se consulta
     * @param id
     * @param rut
     * @return
     */
    public List<TransaccionMedioByEmpresa> findTransaccionesMedioByEmpresaByRut(Integer id, String rut){
        long start = System.currentTimeMillis();
        List<TransaccionMedioByEmpresa> out = DAOFactory.getConsultasDao().findTransaccionesMedioByEmpresaByRut(id, rut);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesMedioByEmpresaByRut() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;     
    }

    /**
     * Lista transacciones de empresa segun
     * medios de pago y la fecha de consulta
     * @param cod_empresa
     * @param cod_medio
     * @param fechaini
     * @param fechfin
     * @return
     */
    public List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByFecha(Integer cod_empresa, Integer cod_medio, Date fechaini, Date fechfin){
        long start = System.currentTimeMillis();
        List<BpiDttDettraturnoVw> out = DAOFactory.getConsultasDao().findTransaccionesByEmpresaMedioByFecha(cod_empresa, cod_medio, fechaini, fechfin);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesByEmpresaMedioByFecha() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;    
    }

    /**
     * Busca transacciones de empresa
     * segun medios de pago y numero de transaccion
     * @param cod_empresa
     * @param cod_medio
     * @param trx
     * @return
     */
    public List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByTrx(Integer cod_empresa, Integer cod_medio, Integer trx){
        long start = System.currentTimeMillis();
        List<BpiDttDettraturnoVw> out = DAOFactory.getConsultasDao().findTransaccionesByEmpresaMedioByTrx(cod_empresa, cod_medio, trx);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesByEmpresaMedioByTrx() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;    
    }

    /**
     * Busca lista de transacciones segun 
     * empresa, medio de pago y rut
     * @param cod_empresa
     * @param cod_medio
     * @param rut
     * @return
     */
    public List<BpiDttDettraturnoVw> findTransaccionesByEmpresaMedioByRut(Integer cod_empresa, Integer cod_medio, Integer rut){
        long start = System.currentTimeMillis();
        List<BpiDttDettraturnoVw> out = DAOFactory.getConsultasDao().findTransaccionesByEmpresaMedioByRut(cod_empresa, cod_medio, rut);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesByEmpresaMedioByRut() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }

    /**
     * Consulta el ultimo periodo de recudacion
     * segun medio de pago, empresa y un rut
     * @param id
     * @param cuadratura
     * @return
     */
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresaRut(Integer id, Integer cuadratura){
        long start = System.currentTimeMillis();
        List<RecaudacionMedioByEmpresa> out = DAOFactory.getConsultasDao().findLastRecaudacionMedioByEmpresaRut(id, cuadratura);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionMedioByEmpresaRut() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }
    
    /**
     * 
     * @param empresa
     * @param cuadratura
     * @param cod_medio
     * @return
     */
    public List<RecaudacionMedioByEmpresa> findLastRecaudacionMedioByEmpresainIdCudraturaByMedio(Integer empresa, Integer cuadratura, Integer cod_medio){
        long start = System.currentTimeMillis();
        List<RecaudacionMedioByEmpresa> out = DAOFactory.getConsultasDao().findLastRecaudacionMedioByEmpresainIdCudraturaByMedio(empresa, cuadratura, cod_medio);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionMedioByEmpresainIdCudraturaByMedio() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }

    /**
     * Consulta el ultimo periodo de recudacion
     * segun la empresa, medio de pago y la cuadratura
     * @param cod_empresa
     * @param cod_medio
     * @param cuadratura
     * @return
     */
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyCuad(Integer cod_empresa, Integer cod_medio, Long cuadratura){
        long start = System.currentTimeMillis();
        List<RecaudacionByEmpMedioInPeriod> out = DAOFactory.getConsultasDao().findLastRecaudacionByEmpresaMediobyCuad(cod_empresa, cod_medio, cuadratura);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionByEmpresaMediobyCuad() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }
    
    /**
     * Consulta el ultimo periodo de recudacion
     * segun la empresa, medio de pago y la cuadratura
     * @param cod_empresa
     * @param cod_medio
     * @param cuadratura
     * @return
     */
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMediobyCuadByMedio2(Integer cod_empresa, Integer cod_medio, Long cuadratura, Integer cod_medi2){
        long start = System.currentTimeMillis();
        List<RecaudacionByEmpMedioInPeriod> out = DAOFactory.getConsultasDao().findLastRecaudacionByEmpresaMediobyCuadByMedio2(cod_empresa, cod_medio, cuadratura, cod_medi2);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionByEmpresaMediobyCuadByMedio2() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }

    /**
     * Busca transacciones seun empresa
     * y rut
     * @param rut
     * @return
     */
    public List<TransaccionByEmpresa> findTransaccionesByEmpresabyRut(String rut){
        long start = System.currentTimeMillis();
        List<TransaccionByEmpresa> out = DAOFactory.getConsultasDao().findTransaccionesByEmpresabyRut(rut);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesByEmpresabyRut() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;  
    }  
  
    /**
     * Buscar ultima recuadacion segun
     * la empresa y cuadratura
     * @param cuadratura
     * @return
     */
    public List<BpiRtcRestrancuadrVw> findLastRecaudacionByEmpresaIdCuad(Long cuadratura, Long empresa){
        long start = System.currentTimeMillis();
        List<BpiRtcRestrancuadrVw> out = DAOFactory.getConsultasDao().findLastRecaudacionByEmpresaIdCuad(cuadratura, empresa);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionByEmpresaIdCuad() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;
    }    
    
    /**
     * Buscar ultima recudacion segun 
     * empresa y medio de pago
     * @param cod_empresa
     * @param cod_medio
     * @param fecha
     * @return
     */
    public List<RecaudacionByEmpMedioInPeriod> findLastRecaudacionByEmpresaMedio(Integer cod_empresa, Integer cod_medio, Date fecha){
        long start = System.currentTimeMillis();
        List<RecaudacionByEmpMedioInPeriod> out = DAOFactory.getConsultasDao().findLastRecaudacionByEmpresaMedio(cod_empresa, cod_medio, fecha);
        long end = System.currentTimeMillis();        
        logger.info("findLastRecaudacionByEmpresaMedio() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;
    }        
    
    /**
     * Consulta de Rechaza en un periodo determinado
     * @param fechaini
     * @param fechafin
     * @return
     */
    public List<BpiVreRechazosVw> findRechazosInPeriod(Date fechaini, Date fechafin) {
        long start = System.currentTimeMillis();
        List<BpiVreRechazosVw> out = DAOFactory.getConsultasDao().findRechazosInPeriod(fechaini, fechafin);
        long end = System.currentTimeMillis();        
        logger.info("findRechazosInPeriod() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;
    }
    
    /**
     * Consulta de comprobantes 
     * en un periodo
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param empresa
     * @return
     */
    public List<BpiDnaDetnavegTbl> findComprobanteByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa) {
        long start = System.currentTimeMillis();
        List<BpiDnaDetnavegTbl> out = DAOFactory.getConsultasDao().findComprobanteByRutInPeriod(rut, fechaini, fechafin, empresa);
        long end = System.currentTimeMillis();        
        logger.info("findComprobanteByRutInPeriod() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;
    }
    
    /**
     * Consulta la ultima navegacion
     * seun un rut de cliente
     * @param rut
     * @param empresa
     * @return
     */
    public List<BpiDnaDetnavegTbl> findLastNavegacionByRut(Integer rut, Integer empresa) {
        long start = System.currentTimeMillis();
        List<BpiDnaDetnavegTbl> out = DAOFactory.getConsultasDao().findLastNavegacionByRut(rut, empresa);
        long end = System.currentTimeMillis();        
        logger.info("findLastNavegacionByRut() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;
    }
    
    /**
     * Busca e detalle en base a un id de navegacion
     * @param idnavegacion
     * @return
     */
    public List<BpiDnaDetnavegTbl> findDetalleNavegacion(Integer idnavegacion) {
        long start = System.currentTimeMillis();
        List<BpiDnaDetnavegTbl> out = DAOFactory.getConsultasDao().findDetalleNavegacion(idnavegacion);
        long end = System.currentTimeMillis();        
        logger.info("findDetalleNavegacion() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;    
    }
    
    /**
     * 
     * @param rut
     * @param empresa
     * @return
     */
    public List<BpiCarCartolasTbl> findLastCartolaByRut(Integer rut, Integer empresa) {
        long start = System.currentTimeMillis();
        List<BpiCarCartolasTbl> out = DAOFactory.getConsultasDao().findLastCartolaByRut(rut, empresa);
        long end = System.currentTimeMillis();        
        logger.info("findLastCartolaByRut() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;  
    }
    
    /**
     * 
     * @param rut
     * @param fechadesde
     * @param fechahasta
     * @param empresa
     * @return
     */
    public List<Navegacion> getCountFindNavegacionByRutInPeriod(Integer rut, Date fechadesde, Date fechahasta, Integer empresa) {
        long start = System.currentTimeMillis();
        List<Navegacion> out = DAOFactory.getConsultasDao().getCountFindNavegacionByRutInPeriod(rut, fechadesde, fechahasta, empresa);
        long end = System.currentTimeMillis();        
        logger.info("getCountFindNavegacionByRutInPeriod() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;                                                                
    }                    

    /**
     * 
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param empresa
     * @return
     */
    public List<BpiCarCartolasTbl> getCountFindNavCartolasByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa) {
        long start = System.currentTimeMillis();
        List<BpiCarCartolasTbl> out = DAOFactory.getConsultasDao().getCountFindNavCartolasByRutInPeriod(rut, fechaini, fechafin, empresa);
        long end = System.currentTimeMillis();        
        logger.info("getCountFindNavCartolasByRutInPeriod() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;                                                    
    }                                                        

    /**
     * 
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param rowdesde
     * @param rowhasta
     * @param empresa
     * @return
     */
    public List<Navegacion> findNavegacionByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa) {
        long start = System.currentTimeMillis();
        List<Navegacion> out = DAOFactory.getConsultasDao().findNavegacionByRutInPeriod(rut, fechaini, fechafin, empresa);
        long end = System.currentTimeMillis();        
        logger.info("findNavegacionByRutInPeriod() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;
    }

    /**
     * Consulta de navegaciones segun rut y periodo
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param empresa
     * @return
     */
     public List<BpiCarCartolasTbl> findNavCartolasByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer rowdesde, Integer rowhasta, Integer empresa) {
        long start = System.currentTimeMillis();
        List<BpiCarCartolasTbl> out = DAOFactory.getConsultasDao().findNavCartolasByRutInPeriod(rut, fechaini, fechafin, rowdesde, rowhasta, empresa);
        long end = System.currentTimeMillis();        
        logger.info("findNavCartolasByRutInPeriod() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;    
    }
    
    /**
     * Consulta de rechazos
     * @param rut
     * @param fechaini
     * @param fechafin
     * @param empresa
     * @return
     */
    public List<BpiVreRechazosVw> findRechazosByRutInPeriod(Integer rut, Date fechaini, Date fechafin, Integer empresa) {    
        long start = System.currentTimeMillis();
        List<BpiVreRechazosVw> out = DAOFactory.getConsultasDao().findRechazosByRutInPeriod(rut, fechaini, fechafin, empresa);
        long end = System.currentTimeMillis();        
        logger.info("findRechazosByRutInPeriod() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;   
    }
 
    /**
     * Consulta del ID de la transaccion
     * @param id
     * @return
     */
    public BpiDnaDetnavegTbl findComprobanteByTransaccion(Long id, Integer codigoPagina) {
        long start = System.currentTimeMillis();
        BpiDnaDetnavegTbl out = DAOFactory.getPersistenciaGeneralDao().findComprobanteByTransaccion(id, codigoPagina);
        long end = System.currentTimeMillis();        
        logger.info("findComprobanteByTransaccion("+ id + "," + codigoPagina + ") - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    } 

    /**
     * Inserta una nueva cuadratura en la base de datos
     * @param idEmpresa
     * @param idMedioPago
     * @param fechaCuadratura
     * @return
     */
    public Long insertNewCuadratura(Integer idEmpresa, Integer idMedioPago, Date fechaCuadratura) {    
        long start = System.currentTimeMillis();
        Long out = DAOFactory.getCuadraturaDao().insertNewCuadratura(idEmpresa, idMedioPago, fechaCuadratura);
        long end = System.currentTimeMillis();        
        logger.info("insertNewCuadratura("+ idEmpresa + "," + idMedioPago + "," + fechaCuadratura + ") - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }    
    
    /**
     * Actualiza el total de un proceso
     * de cuadratura de transacciones
     * @param idCuadratura
     * @param montoTotal
     * @param montoInformado
     * @param numeroTransacciones
     * @return
     */
    public Boolean updateCuadratura(Long idCuadratura, Long montoTotal, Long montoInformado, Long numeroTransacciones) {
        long start = System.currentTimeMillis();
        Boolean out = DAOFactory.getCuadraturaDao().updateCuadratura(idCuadratura, montoTotal, montoInformado, numeroTransacciones);
        long end = System.currentTimeMillis();        
        logger.info("updateCuadratura("+ idCuadratura + "," + montoTotal + "," + montoInformado + "," + numeroTransacciones + ") - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }
    
    /**
     * Inserta la relacion de cuadratura versus transaccion
     * cuadrada para los reportes de gestion
     * @param idCuadratura
     * @param idTransaccion
     * @return
     */
    public Boolean insertCuadraturaTransaccion(Long idCuadratura, Long idTransaccion) {
        long start = System.currentTimeMillis();
        Boolean out = DAOFactory.getCuadraturaDao().insertCuadraturaTransaccion(idCuadratura, idTransaccion);
        long end = System.currentTimeMillis();        
        logger.info("insertCuadraturaTransaccion("+ idCuadratura + "," + idTransaccion + ") - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }
    

     /*------------------------------------------------------------------------------------*/
     /*-------------------METODOS PARA EL SERVICIO PEC  -----------------------------------*/
     /*------------------------------------------------------------------------------------*/

     /**
      * Metodo que procesa servicio CONPPC, usando string con el xml de consulta.
      * @param xmlconsulta String con xml de consulta
      * @return String con xml de respuesta a la consulta
      */
     public String crearRespuestaPagosPendientes(String xmlconsulta) {
         long start = System.currentTimeMillis();
         logger.info("crearRespuestaPagosPendientes() - inicio");   
         logger.info("crearRespuestaPagosPendientes() - xml consulta: " + xmlconsulta);            
         String xml = DAOFactory.getLogicaPecDao().crearRespuestaPagosPendientes(xmlconsulta);   
         long end = System.currentTimeMillis();    
         logger.info("crearRespuestaPagosPendientes() - tiempo ejecucion : " + (end-start) + " milesegundos.");
         logger.info("crearRespuestaPagosPendientes() - xml respuesta: " + xml);       
         return xml;
    }
     
    public String procesarInformePagoPendientes(String xml) {
         String resp = "";    
         long start = System.currentTimeMillis();
         logger.info("crearRespuestaPagosPendientes() - inicio");   
         logger.info("crearRespuestaPagosPendientes() - xml pagos: " + xml);          
         resp =  DAOFactory.getLogicaPecDao().procesarInformePagoPendientes(xml);
         long end = System.currentTimeMillis();    
         logger.info("crearRespuestaPagosPendientes() - tiempo ejecucion : " + (end-start) + " milesegundos.");
         logger.info("crearRespuestaPagosPendientes() - xml respuesta: " + resp); 
         return resp;
    }

    /**
     * Consulta el detalle de navegacion de servicio PEC
     * @param idtransaccion
     * @param cod_pagina
     * @return
     */
    public ConsultaDetalleServicioPec findDetalleServicioPECByIdTransaccionPagina(Long idtransaccion, Long cod_pagina) {
        long start = System.currentTimeMillis();
        ConsultaDetalleServicioPec out = DAOFactory.getConsultasPecDao().findDetalleServicioPECByIdTransaccionPagina(idtransaccion, cod_pagina);
        long end = System.currentTimeMillis();        
        logger.info("findDetalleServicioPECByIdTransaccionPagina("+ idtransaccion + "," + cod_pagina + ") - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }

    /**
     * Ejecucion de Store Procedure
     * para la actualizacion de datos
     * @param dto
     * @return
     */
    public Boolean actualizaTransaccionSPByFechaPago(java.util.Date fechaPago, SPActualizarTransaccionDto dto) {
        long start = System.currentTimeMillis();
        Boolean out = DAOFactory.getPersistenciaGeneralDao().actualizaTransaccionSPByFechaPago(fechaPago, dto);
        long end = System.currentTimeMillis();        
        logger.info("actualizaTransaccionSPByFechaPago() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }     
    
    /**
     * Inserta un log de cuadratura
     * @param dto
     * @return
     */
    public Long insertLogCuadratura(BpiLgclogCuadraturaTbl dto) {
        long start = System.currentTimeMillis();
        Long out = DAOFactory.getCuadraturaDao().insertLogCuadratura(dto);
        long end = System.currentTimeMillis();        
        logger.info("insertLogCuadratura() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }     
    
    /**
     * Inserta un detalle log de cuadratura
     * @param dto
     * @return
     */
    public Boolean insertLogDetalleCuadratura(BpiDlcDetalleLogCuadrTbl dto) {
        long start = System.currentTimeMillis();
        Boolean out = DAOFactory.getCuadraturaDao().insertLogDetalleCuadratura(dto);
        long end = System.currentTimeMillis();        
        logger.info("insertLogDetalleCuadratura() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;   
    }     

    /**
     * Busca transacicones segun su codigo de estado
     * y segun el numero de transaccion para cuadratura de pagos.
     * @param numtrx
     * @return
     */
    public BpiTraTransaccionesTbl findTransaccionCodigoEstadoByNumTransaccionForCuadratura(Long numtrx) {
        long start = System.currentTimeMillis();
        BpiTraTransaccionesTbl out = DAOFactory.getConsultasDao().findTransaccionCodigoEstadoByNumTransaccionForCuadratura(numtrx);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionCodigoEstadoByNumTransaccionForCuadratura() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }

    public List<TransaccionMedioByEmpresaByMedio2> findTransaccionesRecauByMedio2ByFecha(Timestamp fechaini, Timestamp fechafin, Integer empresa) {
        long start = System.currentTimeMillis();
        List<TransaccionMedioByEmpresaByMedio2> out = DAOFactory.getConsultasDao().findTransaccionesRecauByMedio2ByFecha(fechaini, fechafin, empresa);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesRecauByMedio2ByFecha() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    public List<TransaccionMedioByEmpresaByMedio2> findTransaccionesRecauByMedio2ByCuadratura(Integer idcuadratura, Integer empresa) {
        long start = System.currentTimeMillis();
        List<TransaccionMedioByEmpresaByMedio2> out = DAOFactory.getConsultasDao().findTransaccionesRecauByMedio2ByCuadratura(idcuadratura, empresa);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesRecauByMedio2ByCuadratura() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    public DetalleAPV getDetalleProductoAPV(String poliza) {
        long start = System.currentTimeMillis();
        DetalleAPV out = DAOFactory.getPersistenciaGeneralDao().getDetalleProductoAPV(poliza);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionesRecauByMedio2ByCuadratura() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    public boolean updateEstadoPolizaAPV(Long rut_trabajador, Long numero_poliza, Long ramo, Long numero_recibo, Long secuencia){
        long start = System.currentTimeMillis();
        boolean out = DAOFactory.getPersistenciaGeneralDao().updateEstadoPolizaAPV(rut_trabajador, numero_poliza, ramo, numero_recibo, secuencia);
        long end = System.currentTimeMillis();        
        logger.info("updateEstadoPolizaAPV() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    public int getValorMaximoAporteExtraAPV(){
        long start = System.currentTimeMillis();
        int out = DAOFactory.getPersistenciaGeneralDao().getValorMaximoAporteExtraAPV();
        long end = System.currentTimeMillis();        
        logger.info("getValorMaximoAporteExtraAPV() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    public double getPorcentajeCobroTarjetaAPVAPT(){
        long start = System.currentTimeMillis();
        double out = DAOFactory.getPersistenciaGeneralDao().getPorcentajeCobroTarjetaAPVAPT();
        long end = System.currentTimeMillis();        
        logger.info("getPorcentajeCobroTarjetaAPVAPT() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    public DetalleAPV generarIdAporteExtraordinarioAPV(DetalleAPV detalle){
        long start = System.currentTimeMillis();
        DetalleAPV out = DAOFactory.getPersistenciaGeneralDao().generarIdAporteExtraordinarioAPV(detalle);
        long end = System.currentTimeMillis();        
        logger.info("getPorcentajeCobroTarjetaAPVAPT() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    /**
     * APT
     * @param detalle
     * @return
     */
    public boolean updateIdAporteComprobantes(DetalleAPV detalle){
        long start = System.currentTimeMillis();
        boolean out = DAOFactory.getPersistenciaGeneralDao().updateIdAporteComprobantes(detalle);
        long end = System.currentTimeMillis();        
        logger.info("getPorcentajeCobroTarjetaAPVAPT() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    public boolean grabarDistribucionFondosAPVProducto(int idAporte, List<BpiDitribFondosAPVTbl> DistribfFondos, Long CodigoRegimen, String DescrpcionRegimen) {
        long start = System.currentTimeMillis();
        logger.info("LO QUE LLEGO AL EJB DE DISTRIBUCION:" +CodigoRegimen+"  - " + DescrpcionRegimen );   
        boolean out = DAOFactory.getDistribucionFondosDao().grabarDistribucionFondosAPVProducto(idAporte, DistribfFondos, CodigoRegimen, DescrpcionRegimen);
        long end = System.currentTimeMillis();        
        logger.info("grabarDistribucionFondosAPVProducto() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;         
    }
    
    public List<BpiDitribFondosAPVTbl> getDistribucionFondosAPVByTransaccion(int idTransaccion, int poliza, int ramo, long rut) {
        long start = System.currentTimeMillis();
        List<BpiDitribFondosAPVTbl> out = DAOFactory.getDistribucionFondosDao().getDistribucionFondosAPVByTransaccion(idTransaccion, poliza, ramo, rut);
        long end = System.currentTimeMillis();        
        logger.info("grabarDistribucionFondosAPVProducto() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;         
    }
    
    /**
     * APT
     * @param detalleapv
     * @return
     */
    public boolean updateTransaccionDistribucionFondosAPVProducto(DetalleAPV detalleapv) {
        long start = System.currentTimeMillis();
        boolean out = DAOFactory.getDistribucionFondosDao().updateTransaccionDistribucionFondosAPVProducto(detalleapv);
        long end = System.currentTimeMillis();        
        logger.info("updateTransaccionDistribucionFondosAPVProducto() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out;         
    }



    //=============== P R I M E R A S    P R I M A S =====================

    /**
     * Metodo que recupera la consulta de deudas de primeras primas
     * @param rut
     * @param nombre
     * @param confirmacion
     * @param idCanal
     * @param numeroPropuesta
     * @return
     */
    public String crearResumenPagosPrimerasPrimas(Integer rut, String nombre, String confirmacion, int idCanal, Integer numeroPropuesta) {
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaResumenPagosDao().crearResumenPagosPrimerasPrimas(rut ,nombre ,confirmacion, idCanal, numeroPropuesta);
        long end = System.currentTimeMillis();        
        logger.info("crearResumenPagosPrimerasPrimas() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;
    }
    
    /**
     * Genera el XML de resumen y pagos con productos seleccionados
     * @param xml con la data de resumen y pagos
     * @param req productos que se han seleccionado para grabacion
     * @return XMl con el resultad de grabacion
     */
    public String generaXmlResumenPagosConProductosSeleccionadosPrimerasPrimas(String xml, ResumenRequest req, int idCanal){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaConfirmacionDao().generaXmlResumenPagosConProductosSeleccionadosPrimeraPrima(xml, req, idCanal);
        long end = System.currentTimeMillis();        
        logger.info("generaXmlResumenPagosConProductosSeleccionadosPrimerasPrimas() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;        
    } 
    
    /**
     * Creac la confirmacion con el XML seleccionado
     * @param xmlResumenSeleccionado
     * @return
     */
    public String crearConfirmacionConXmlSeleccionadoPrimeraPrima(String xmlResumenSeleccionado, int idCanal, String email){
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaConfirmacionDao().crearConfirmacionConXmlSeleccionadoPrimeraPrima("E",xmlResumenSeleccionado, idCanal, email);
        long end = System.currentTimeMillis();        
        logger.info("crearConfirmacionConXmlSeleccionadoPrimeraPrima() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;         
    }
    
    /**
     * Verifica si un rut tiene propuesta para ser pagada
     * @param rut
     * @param numeroPropuesta
     * @return
     */
    public boolean tienePropuestaRutPrimeraPrima(Integer rut, Integer numeroPropuesta){
          List<IndPrimaNormalVw> resultado = DAOFactory.getPersistenciaGeneralDao().findPrimeraPrimaByNumppRut(numeroPropuesta, rut);
          return resultado != null && resultado.size() > 0? true: false;
    }

    /**
     * Busca los datos de un cliente el sistema cotizador
     * @param rut
     * @return
     */
    public PersonaCotizadorDto buscaClienteCotizador(Integer rut){
          return DAOFactory.getPersistenciaGeneralDao().findClienteCotizador(rut);
    }
    
    
    /**
     * Busca la lista de regimen de las polizas en desarrollo
     * @param rutCliente
     * @return
     */
    public List<RegimenTributarioDTO> obtenerListaRegimenTributarioPolizas(Long rutCliente) {
        List<RegimenTributarioDTO> retorno = new ArrayList<RegimenTributarioDTO>();
        CuentaDeInversionClient myPort;
        try {
            myPort = new CuentaDeInversionClient();
            myPort.setEndpoint(ResourceBundleUtil.getProperty("webservice.cuentainversion.endpoint"));
            ObtenerInformacionRegimen in = new ObtenerInformacionRegimen();
            in.setRut(rutCliente);
            ListOfRetornoInformacionRegimen data = myPort.obtenerInformacionRegimen(in);
            if (data != null) {
                List<RetornoInformacionRegimen> regimens  = data.getItem();
                if (regimens != null && !regimens.isEmpty()) {
                    for(RetornoInformacionRegimen reg: regimens) {
                        RegimenTributarioDTO dto = new RegimenTributarioDTO();
                        dto.setDescripcionRegimen(reg.getDescripcionRegimenTributario());
                        dto.setNumeroPoliza(reg.getNumeroPoliza());
                        dto.setNumeroRegimen(reg.getRegimenTributario());
                        retorno.add(dto);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
    
    
    /**
     * Regulariza las transacciones pendientes de WebPay
     * 
     * @param idEmpresa
     * @param rutCliente
     * @param fechaDesde
     * @param fechaHasta
     * @return
     */
    public List<BpiTraTransaccionesTbl> findTransaccionForRegularizacionWebPay(Long idEmpresa, Integer rutCliente, java.util.Date fechaDesde, java.util.Date fechaHasta) {
        long start = System.currentTimeMillis();
        List<BpiTraTransaccionesTbl> out = DAOFactory.getConsultasDao().findTransaccionesForRegularizacionWebPay(idEmpresa, rutCliente, fechaDesde, fechaHasta);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionForRegularizacionWebPay() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    /**
     * Regulariza las transacciones pendientes de WebPay
     * 
     * @param idEmpresa
     * @param rutCliente
     * @param fechaDesde
     * @param fechaHasta
     * @return
     */
    public List<BpiTraTransaccionesTbl> findTransaccionForRegularizacionWebPayPool(Long idEmpresa) {
        long start = System.currentTimeMillis();
        List<BpiTraTransaccionesTbl> out = DAOFactory.getConsultasDao().findTransaccionesForRegularizacionWebPayPool(idEmpresa, "S");
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionForRegularizacionWebPayPool() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    /**
     * Regulariza las transacciones pendientes de WebPay
     * 
     * @param idEmpresa
     * @param rutCliente
     * @param fechaDesde
     * @param fechaHasta
     * @return
     */
    public List<BpiTraTransaccionesTbl> findTransaccionForYaRegularizadasWebPayPool(Long idEmpresa, Integer rutCliente) {
        long start = System.currentTimeMillis();
        List<BpiTraTransaccionesTbl> out = DAOFactory.getConsultasDao().findTransaccionesYaRegularizadasWebPayPool(idEmpresa, rutCliente);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionForRegularizacionWebPayPool() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    
    /**
     * Recupera el ID COmercio
     * 
     * @param codEmpresa
     * @param codMedioPago
     * @return
     */
    public Long getIdComercioBICEByMedioEmpresa(Long codEmpresa, Long codMedioPago) {
        long start = System.currentTimeMillis();
        Long out = DAOFactory.getConsultasDao().getIDCOMByEmpresaMedioPago(codEmpresa, codMedioPago);
        long end = System.currentTimeMillis();        
        logger.info("findTransaccionForRegularizacionWebPayPool() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
    
    /**
     * Recupera el ID de Comercio
     * @param idTransaccion id transaccion bice vida
     * @return
     */
    public HomologacionConvenioDTO getHomologacionConvenioByIdTrx(Long idTransaccion) {
        long start = System.currentTimeMillis();
        HomologacionConvenioDTO out = DAOFactory.getConsultasDao().getHomologacionConvenioByIdTrx(idTransaccion);
        long end = System.currentTimeMillis();        
        logger.info("getHomologacionConvenioByIdTrx() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    /**
     * Recupera el ID de Comercio
     * @param idTransaccion id transaccion bice vida
     * @return
     */
    public HomologacionConvenioDTO getHomologacionConvenioByIdComercioTrx(String idComercioTrx) {
        long start = System.currentTimeMillis();
        HomologacionConvenioDTO out = DAOFactory.getConsultasDao().getHomologacionConvenioByIdComercioTrx(idComercioTrx);
        long end = System.currentTimeMillis();        
        logger.info("getHomologacionConvenioByIdComercioTrx() - tiempo ejecucion : " + (end-start) + " milesegundos.");        
        return out; 
    }
    
 
 
    /**
     * Metodo RESTful para acutualizar estado
     * @param idtransaccion
     * @param codEstado
     * @return
     */
    public boolean updateTransaccionRESTful(Long idtransaccion, Integer codEstado) {
        return DAOFactory.getPersistenciaGeneralDao().updateTransaccionRESTful(idtransaccion, codEstado);
    }
    
    /**
     * Metodo RESTful para verificar si una operacion es de tipo REST
     * @param idTransaccion
     * @return
     */
    public boolean isTransaccionRESTful(Long idTransaccion) {
        return DAOFactory.getPersistenciaGeneralDao().isTransaccionRESTful(idTransaccion);
    }
    
    /**
     * Metodo RESTful para notificar a servicio web movil que esta pagada la operacion 
     * @param idTransaccion
     * @return
     */
    public boolean notificarPagoTransaccionRESTful(Long idTransaccion) {
        //NOTIFICACION A EMPRESA DESARROLLADORA DE INTERFAZ MOVIL PARA PAGO
        String urlRESTnotifica = ResourceBundleUtil.getProperty("cl.bice.vida.restful.notifica.pago.url");
        
        String idOrdenCompraCanalSolicitante = DAOFactory.getPersistenciaGeneralDao().getOrdenCompraServicioRESTFulByTransaccion(idTransaccion);
        if (idOrdenCompraCanalSolicitante != null) {
            List<RESTfulParametroDto> parametros = new ArrayList<RESTfulParametroDto>();
            parametros = new ArrayList<RESTfulParametroDto>();
            parametros.add(new RESTfulParametroDto("idOrdenCanal",idOrdenCompraCanalSolicitante));
            parametros.add(new RESTfulParametroDto("idTransaccion",""+idTransaccion));
            parametros.add(new RESTfulParametroDto("tipoPago","ONLINE")); //ONLINE RENDICION
            parametros.add(new RESTfulParametroDto("estadoPago","PAGADO")); //PAGADO RECHAZADO
            parametros.add(new RESTfulParametroDto("claveFuenteOrigen","notengo"));
            parametros.add(new RESTfulParametroDto("checksum","sumarcheck"));
            String jsonnotif = RESTfulCallUtil.call(urlRESTnotifica, "POST",null,null, parametros);
            
            System.out.println("RESTfull resultado pago XML B:"+jsonnotif);
            return true; 
        }
        return false;
    }
    
    /**
    * Consulta el detalle de navegacion de servicio PEC V2
    * @param idtransaccion
    * @param cod_pagina
    * @return
    */
   public BpiPecConsultaTransaccionDto findDetalleServicioPECV2ByIdTransaccion(Long idtransaccion) {
       long start = System.currentTimeMillis();
       BpiPecConsultaTransaccionDto out = DAOFactory.getConsultasPecDao().findDetalleServicioPECV2ByIdTransaccion(idtransaccion);
       long end = System.currentTimeMillis();        
       logger.info("findDetalleServicioPECV2ByIdTransaccion("+ idtransaccion + ") - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
       return out;   
   }
   
    /**
     * Ejecucion de Store Procedure PAGAR PEC V2
     * para la actualizacion de datos
     * @param dto
     * @return
     */
    public Boolean actualizaTransaccionSPPecv2(SPActualizarTransaccionDto dto) {
    
        long start = System.currentTimeMillis();
        Boolean out = DAOFactory.getPersistenciaGeneralDao().pagarSPCajasPEC(dto);
        if (out.booleanValue() == true) {
            System.out.println("Iniciando envio a sistema AR con WS");
            //SI LE FUE BIEN EN EL PAGO DE LA TRANSACCION SE REALIZA LA NOTIFIACION AL AR
            RecaudacionServiceClient myPort;
            try {
                //CONSULTA LA TRANSACCION PARA REALIZAR EL CARGO DE PAGO
                System.out.println("--> Buscando transaccion si es Bice Vida para informar AR");
                BpiTraTransaccionesTbl transaccion = DAOFactory.getConsultasDao().findTransaccionCodigoEstadoByNumTransaccion(dto.getIdTrx());     
                //if (transaccion != null && transaccion.getCodigoEmpresa().equalsIgnoreCase("BV")) {
                if (transaccion != null) {
                    
                    System.out.println("--> Transaccion es Bice Vida, verificando que exista numero de folio caja");
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
        return out;
    }

    @Override
    public String generaXmlResumenPagosConProductosSeleccionados(String xml, ResumenRequest req, int idCanal,
                                                                 String email) {
        long start = System.currentTimeMillis();
        String out = DAOFactory.getLogicaConfirmacionDao().generaXmlResumenPagosConProductosSeleccionados(xml, req, idCanal, email);
        long end = System.currentTimeMillis();        
        logger.info("generaXmlResumenPagosConProductosSeleccionados() - tiempo ejecucion : " +  (end-start)+" milesegundos.");        
        return out;
    }

    @Override
    public String getNroBoletaByIdTrx(Long idTransaccion) {
        long start = System.currentTimeMillis();
        String nroBoleta = DAOFactory.getConsultasDao().getNroBoletaByIdTrx(idTransaccion);
        long end = System.currentTimeMillis();        
        logger.info("getNroBoletaByIdTrx() - tiempo ejecucion : " +  (end-start)+" milesegundos.");     
        
        return nroBoleta;
    }
}
