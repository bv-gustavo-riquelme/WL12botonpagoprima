package cl.bice.vida.botonpago.modelo.dao;

import cl.bice.vida.botonpago.common.dto.general.AperturaCaja;
import cl.bice.vida.botonpago.common.dto.general.Comprobantes;
import cl.bice.vida.botonpago.common.dto.general.ConsultaDetalleServicioPec;
import cl.bice.vida.botonpago.common.dto.general.DetalleTransaccionByEmp;
import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.dto.general.ServicioPec;
import cl.bice.vida.botonpago.common.dto.general.Transacciones;
import cl.bice.vida.botonpago.common.dto.parametros.ValorUf;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosOfflineVw;
import cl.bice.vida.botonpago.common.dto.vistas.BhDividendosVw;
import cl.bice.vida.botonpago.common.dto.vistas.IndPrimaNormalOfflineVw;
import cl.bice.vida.botonpago.common.dto.vistas.IndPrimaNormalVw;
import cl.bice.vida.botonpago.common.util.FechaUtil;
import cl.bice.vida.botonpago.common.util.NumeroUtil;
import cl.bice.vida.botonpago.modelo.jdbc.DataSourceBice;
import cl.bice.vida.botonpago.modelo.util.ResourceBundleUtil;

import java.io.StringWriter;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import noNamespace.ConfirmacionDocument;
import noNamespace.ConsultaPagosPendientesDocument;
import noNamespace.DetalleCargos;
import noNamespace.Infocajas;
import noNamespace.InformePagoPendienteDocument;
import noNamespace.InformePagoServipagDocument;
import noNamespace.Producto;
import noNamespace.RespuestaPagosPendientesDocument;
import noNamespace.RespuestaServipagHipotecariaDocument;
import noNamespace.RespuestaServipagVidaDocument;

import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLElement;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;


public class LogicaPecDAOImpl extends DataSourceBice implements LogicaPecDAO {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(LogicaPecDAOImpl.class);

    
    public LogicaPecDAOImpl() {
    }
    
    public String crearRespuestaPagosPendientes(String xml) {
        String medio = "Ninguno";
        int cod_medio = 0;
        int empresa = 0;
        boolean pasaje= false;
        
        String resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
        
        logger.info("crearRespuestaPagosPendientes() - Buscando codigo Empresa, si es Bicevida");
        if(xml.contains("<Empresa>1")){
           empresa = 1;
            pasaje= true;
           logger.info("crearRespuestaPagosPendientes() - codigo Empresa BiceVida: "+empresa);
        }else if(xml.contains("1</Empresa>")){
           empresa = 1;
            pasaje= true;
           logger.info("crearRespuestaPagosPendientes() - codigo Empresa BiceVida: "+empresa);
        }else{
           empresa = 0;
            pasaje= false;
           logger.info("crearRespuestaPagosPendientes() - codigo Empresa no encontrada: "+empresa);
        }
        
        if(!pasaje){
            logger.info("crearRespuestaPagosPendientes() - Buscando codigo Empresa, si es BiceHipotecaria");
            if(xml.contains("<Empresa>2")){
                empresa = 2;
                logger.info("crearRespuestaPagosPendientes() - codigo Empresa BiceHipotecaria: "+empresa);
            }else if(xml.contains("2</Empresa>")){
                empresa = 2;
                logger.info("crearRespuestaPagosPendientes() - codigo Empresa BiceHipotecaria: "+empresa);
            }else{
                empresa = 0;
                logger.info("crearRespuestaPagosPendientes() - codigo Empresa no encontrada: "+empresa);
            }
        }
            
        
        logger.info("crearRespuestaPagosPendientes() - Buscando codigo medio");
        if(xml.contains("SANTAN")){
            medio = "SANTAN";
            cod_medio= Integer.parseInt(ResourceBundleUtil.getProperty(medio)); 
            logger.info("crearRespuestaPagosPendientes() - codigo medio Santander: "+cod_medio);
        }else if(xml.contains("SERVIP")){
            medio = "SERVIP";
            cod_medio= Integer.parseInt(ResourceBundleUtil.getProperty(medio));            
            logger.info("crearRespuestaPagosPendientes() - codigo medio Servipag: "+cod_medio);
        }else if(xml.contains("BECAJV")){
            medio = "BECAJV";
            cod_medio= Integer.parseInt(ResourceBundleUtil.getProperty(medio));            
            logger.info("crearRespuestaPagosPendientes() - codigo medio Servipag: "+cod_medio);
        }else if(xml.contains("BESERV")){
            medio = "BESERV";
            cod_medio= Integer.parseInt(ResourceBundleUtil.getProperty(medio));            
            logger.info("crearRespuestaPagosPendientes() - codigo medio Servipag: "+cod_medio);
        }else if(xml.contains("BEINT")){
            medio = "BEINT";
            cod_medio= Integer.parseInt(ResourceBundleUtil.getProperty(medio));            
            logger.info("crearRespuestaPagosPendientes() - codigo medio Servipag: "+cod_medio);
        }else{
            cod_medio=0;  
            logger.info("crearRespuestaPagosPendientes() - codigo medio no encontrada: "+cod_medio);
        }
        
        resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
        
        ConsultaPagosPendientesDocument consulta;  
        
        if(cod_medio!=0 && empresa!=0){

            try {
            
                consulta = ConsultaPagosPendientesDocument.Factory.parse(xml);
                //String medio1 = consulta.getConsultaPagosPendientes().getIdentificacionMensaje().getDe().toString();
                //cod_medio = Integer.parseInt(ResourceBundleUtil.getProperty(medio1));
                empresa = consulta.getConsultaPagosPendientes().getConsulta().getEmpresa();
                int rut = consulta.getConsultaPagosPendientes().getConsulta().getCliente();
                String nombre_persona = consulta.getConsultaPagosPendientes().getConsulta().getNombreCliente();
                
                if(cod_medio == 3 || cod_medio == 16) {                    
                    /**
                     * SERVIPAG
                     */
                    return resumenxml = crearXMLRespuestaServipag(cod_medio, medio, empresa, rut, nombre_persona, xml);                      
                    
                } else if(cod_medio == 5 || cod_medio == 6 || cod_medio == 7){
                    /**
                     * BANCO ESTADO
                     */                                          
                    return resumenxml = crearXMLRespuestaPagosPendientesBcoEstado(cod_medio, medio, empresa, rut, nombre_persona, xml);
                                        
                }
                else {
                    return resumenxml = crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));                   
                }                
                
            } catch (XmlException e) {
                logger.error("crearRespuestaPagosPendientes() - No se pudo parsear archivo de entrada: " + e.getMessage(), e);
                resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.entradaenviada")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.entradaenviada"));
                e.printStackTrace();
            }catch (NumberFormatException e){
                logger.error("crearRespuestaPagosPendientes() - Medio de pago no encontrado en archivo properties. Nombre medio: " + medio, e);
                resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.mediopago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.mediopago"));
            }catch (Exception e){
                logger.error("crearRespuestaPagosPendientes() - Se produjo un excepción :" + e.getMessage(), e);
                resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " +e.getMessage());
            }finally{
                logger.info("crearRespuestaPagosPendientes() - termino");
                return resumenxml;
            }
        } else{
            logger.error("crearRespuestaPagosPendientes() - La empresa o medio no son correctos");
            return resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.empresamedio")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.empresamedio"));
        }
    }

    public String procesarInformePagoPendientes(String xml) {
        logger.info("procesarInformePagoPendientes() - inicio"); 
        int cod_medio=0;
        String resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
        
        
        logger.info("procesarInformePagoPendientes() - Buscando codigo medio");
        if(xml.contains("SANTAN")){
             cod_medio=1;  
             logger.info("procesarInformePagoPendientes() - codigo medio Santander: "+cod_medio);
        }else if(xml.contains("SERVIP")){
             cod_medio=3;  
             logger.info("procesarInformePagoPendientes() - codigo medio Servipag: "+cod_medio);
        }else if(xml.contains("BECAJV")){
             cod_medio=5;  
             logger.info("procesarInformePagoPendientes() - codigo medio Banco Estado: "+cod_medio);
        }else if(xml.contains("BESERV")){
             cod_medio=6;  
             logger.info("procesarInformePagoPendientes() - codigo medio Banco Estado: "+cod_medio);
        }else if(xml.contains("BEINT")){
             cod_medio=7;  
             logger.info("procesarInformePagoPendientes() - codigo medio Banco Estado: "+cod_medio);
        }else{
            cod_medio=0;  
            logger.info("crearRespuestaPagosPendientes() - codigo medio no encontrada: "+cod_medio);
        }
        
        resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
        
        XmlObject xmlrecibido;

        if (cod_medio != 0 ){
           try {
               logger.info("procesarInformePagoPendientes() - parseo xml recibido"); 
               xmlrecibido = XmlObject.Factory.parse(xml);
               
               logger.info("procesarInformePagoPendientes() - chequeo tipo del xml recibido"); 
               //proceso InformePagoPendienteDocument
               if(xmlrecibido instanceof noNamespace.InformePagoPendienteDocument){
                   logger.info("procesarInformePagoPendientes() - xml recibido de tipo InformePagoPendienteDocument"); 
                   InformePagoPendienteDocument informe = (InformePagoPendienteDocument)xmlrecibido;
                   logger.info("procesarInformePagoPendientes() - valido xml recibido"); 
                   String errores = "";//validateXML(informe);
                    if(errores == ""){
                        logger.info("procesarInformePagoPendientes() - llamando a procesarInformePago"); 
                        //resumenxml = procesarInformePago(xml, informe, cod_medio);procesarInformePagoBcoEstado
                         resumenxml = procesarInformePagoBcoEstado(xml, informe, cod_medio);
                    }
                    else{
                        logger.error("XML recibido con errores: "+errores);
                        //resumenxml="NOK|Xml con errores"+errores;
                        return resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.entradaenviada")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.entradaenviada"));
                    }
               } //fin de InformePagoPendienteDocument
               //proceso InformePagoServipagDocument
               else if(xmlrecibido instanceof noNamespace.InformePagoServipagDocument){
                   logger.info("procesarInformePagoPendientes() - zml ercibido de tipo InformePagoServipagDocument"); 
                   InformePagoServipagDocument informe = (InformePagoServipagDocument)xmlrecibido;
                   logger.info("procesarInformePagoPendientes() - valido xml recibido"); 
                   String errores = validateXML(informe);
                    if(errores == ""){
                        logger.info("procesarInformePagoPendientes() - llamando a procesarInformePagoServipag"); 
                        return resumenxml = procesarInformePagoPendientesServipag(xml, informe, cod_medio);
                    }
                    else{    
                        logger.error("Xml recibido con errores: "+errores);
                        return resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.entradaenviada")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.entradaenviada"));
                    }                
               }
               else{
                   logger.error("procesarInformePagoPendientes() - Se esperaba otro tipo de xml: " + xmlrecibido.getClass().toString());
                   return resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.entradaenviada")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.entradaenviada"));
               }
           } catch (Throwable e) {
               logger.error("procesarInformePagoPendientes() - Ha ocurrido una excepción: " + e.getMessage(),e);
               return resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.entradaenviada")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.entradaenviada"));
           } finally{
               logger.info("procesarInformePagoPendientes() - termino retornado xml : " + resumenxml); 
               return resumenxml;
           } 
        }else{
           return resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.mediopago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.mediopago"));
        }
    }

    public String crearXMLRespuestaPagosPendientes(String medio, int cod_medio, 
                                                   int empresa, int rut, 
                                                   String nombre_persona, 
                                                   ServicioPec servicio) {
        return null;
    }

    public String crearXMLRespuestaServipag(int cod_medio, String medio, 
                                            int empresa, int rut, 
                                            String nombre_persona, 
                                            String xmlsource) {
        String resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
        AperturaCaja turno = DAOFactory.getPersistenciaGeneralDao().findCajaAbierta();
        
        if(turno != null){
            if(empresa==1) {//bicevida
                logger.info("crearXMLRespuestaServipag() - llamando a setPolizasServipag");
                resumenxml = setPolizasServipag(rut,cod_medio,medio,nombre_persona,turno, xmlsource);
            }else if (empresa==2) { //bicehipotecaria
             logger.info("crearXMLRespuestaServipag() - llamando a setDividendosServipag");
                resumenxml = setDividendosServipag(rut,cod_medio,medio,nombre_persona,turno, xmlsource);
            }else{
                logger.error("crearXMLRespuestaServipag() - Empresa o medio no coinciden");
                resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.empresamedio")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.empresamedio"));
            }
        }else{
            resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
        }
        
        return resumenxml;
        
    }
    
    /**
     * Crea respuesta a consulta pagos pendientes, cuando el medio es Banco Estado
     * @param medio
     * @param cod_medio
     * @param empresa
     * @param rut
     * @param nombre_persona
     * @return String,  xml con deudas pendientes
     */
    public String crearXMLRespuestaPagosPendientesBcoEstado(int cod_medio, String medio, 
                                            int empresa, int rut, 
                                            String nombre_persona, 
                                            String xmlsource) {


        String resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));

        ServicioPec servicio = DAOFactory.getConsultasPecDao().newServicioPec(rut,cod_medio);
        DAOFactory.getConsultasPecDao().createDetalleServPec(servicio.getIdNavegacion(),1,6,0,xmlsource);                 
        
        if(servicio != null){
            RespuestaPagosPendientesDocument respuesta = RespuestaPagosPendientesDocument.Factory.newInstance();
            ConfirmacionDocument conf=ConfirmacionDocument.Factory.newInstance(); 
            
            logger.info("crearXMLRespuestaPagosPendientes() - llamando a generaXmlRespuestaPagosPendientes");
            Transacciones transaccion = generaXmlRespuestaPagosPendientesBcoEstado(respuesta,rut,empresa,cod_medio,medio,nombre_persona,servicio,conf);
            if(transaccion != null){
                logger.info("crearXMLRespuestaPagosPendientes() - validando ");
                String resp = respuesta.toString();            
                try{
                    logger.info("crearXMLRespuestaPagosPendientes() - parseando xml a string");
                    resumenxml=respuesta.xmlText();
                                
                    //guardo xml de respuesta en tabla dpe
                    logger.info("crearXMLRespuestaPagosPendientes() - guardo xml de respuesta en tabla dpe");
                    int monto = 0;
                    if(respuesta.getRespuestaPagosPendientes().getCuota()!=null)
                        //monto=respuesta.getRespuestaPagosPendientes().getCuota().getEnPesos().intValue();
                        
                     logger.info("crearXMLRespuestaPagosPendientes() - creando detalle consultasBD.createDetalleServPec()");
                     DAOFactory.getConsultasPecDao().createDetalleServPec(servicio.getIdNavegacion(),2,7,monto,resumenxml);
                     
                     //valido xml de confirmacion y lo almaceno en la bd solo si hay algo que pagar
                     String confirmar = conf.toString(); 
                     logger.info("crearXMLRespuestaPagosPendientes() - valido xml de confirmacion y lo almaceno en la bd solo si hay algo que pagar");
                     //if(monto!=0){
                         
                         
                         logger.info("crearXMLRespuestaPagosPendientes() - creando detalle servicios pec consultasBD.createDetalleServPec()");
                         String confirmacionxml=conf.xmlText();
                         DAOFactory.getConsultasPecDao().createDetalleServPec(servicio.getIdNavegacion(),3,3,respuesta.getRespuestaPagosPendientes().getCuota().getEnPesos().intValue(),confirmacionxml);
                  
                          //creo detalle de la transacion por empresa
                          logger.info("crearXMLRespuestaPagosPendientes() - creo detalle de la transacion por empresa");
                          List detalles = new ArrayList(1);
                          DetalleTransaccionByEmp detalleTransaccion = new DetalleTransaccionByEmp();
                          detalleTransaccion.setCodEmpresa(empresa);
                          detalleTransaccion.setFechahora(conf.getConfirmacion().getFechaUF().getTime());
                          detalleTransaccion.setIdTransaccion(transaccion.getIdTransaccion().longValue());
                          detalleTransaccion.setMontoTotal(respuesta.getRespuestaPagosPendientes().getCuota().getEnPesos().intValue());
                          detalleTransaccion.setCodMedio(cod_medio);
                          detalles.add(detalleTransaccion);
                          
                          //creo detalle de la transaccion
                          logger.info("crearXMLRespuestaPagosPendientes() - creo detalle de la transaccion");
                          List comprobantes = new ArrayList(1);
                          Comprobantes comprobante = new Comprobantes();
                          comprobante.setCodProducto(respuesta.getRespuestaPagosPendientes().getProducto().getCodigoProducto().intValue());
                          if(empresa == 1){                  
                             comprobante.setCuota(conf.getConfirmacion().getProductosPorPagar().getEntradaArray(0).getInfoCajas().getPolizas().getFolio().intValue());
                         }
                          else comprobante.setCuota(conf.getConfirmacion().getProductosPorPagar().getEntradaArray(0).getInfoCajas().getCreditos().getNumDividendo().intValue());
                          
                          comprobante.setIdTransaccion(transaccion.getIdTransaccion().longValue());
                          comprobante.setMontoBase(respuesta.getRespuestaPagosPendientes().getCuota().getEnPesos().intValue());
                          comprobante.setMontoExcedente(0);
                          comprobante.setMontoTotal(respuesta.getRespuestaPagosPendientes().getCuota().getEnPesos().intValue());
                          comprobante.setNumProducto(respuesta.getRespuestaPagosPendientes().getProducto().getNumProducto().longValue());
                          comprobantes.add(comprobante);
                          
                          //actualizo el detalle de la transaccion
                          logger.info("crearXMLRespuestaPagosPendientes() - actualizo el detalle de la transaccion ejecutando updateDetalleTransaccion()");
                          //DAOFactory.getConsultasPecDao().updateDetalleTransaccion(transaccion,detalles,comprobantes);
                     //}
                }
                catch (Exception e) {
                    logger.error("crearXMLRespuestaPagosPendientes() - Error al grabar en la base de datos: " + e.getMessage(), e);
                    resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());  
                }
                
                return resumenxml;
            }else{
                return resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.sinpagos")), ResourceBundleUtil.getProperty("mensaje.error.servipag.sinpagos.poliza"));                                        
            }
            
        }else{
            logger.error("crearRespuestaPagosPendientes() - Problemas al guardar xml recibido");
            return resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));                                    
        }
        
    }
    
    /**
     * Este metodo se usa para determinar que metodo se debe llamar para
     * obtener el producto a pagar. Retorna el mismo dato creado por estos metodos
     * 
     * @param respuesta Objeto con el xml que se esta creando con la respuesta
     * @param rut Rut de la persona consultada
     * @param empresa Codigo de la empresa consultada: 1 BiceVida, 2 BiceHipotecaria
     * @param cod_medio Codigo unico del medio de pago
     * @param medio Nombre del medio de pago
     * @param nombre_persona Nombre de la persona
     * @param servicio Registro de la consulta realizada
     * @param conf Objeto con el xml de confirmacion asociado a la transaccion que se va a crear
     * @return transaccion creada por los metodos accedidos
     */
    public Transacciones generaXmlRespuestaPagosPendientesBcoEstado(RespuestaPagosPendientesDocument respuesta, int rut, 
                                                                               int empresa, int cod_medio, String medio, String nombre_persona,ServicioPec servicio,ConfirmacionDocument conf) {
        
        logger.info("generaXmlRespuestaPagosPendientes() - inicio");
        //primero debo buscar si la persona tiene pagos pendientes para la empresa seleccionada
        
        logger.info("generaXmlRespuestaPagosPendientes() - busco el turno");
        AperturaCaja turno = DAOFactory.getPersistenciaGeneralDao().findCajaAbierta();
       
        if(turno != null){
            logger.info("generaXmlRespuestaPagosPendientes() -  termina - vida - termina ejecutando setPolizas()");
            return setPolizasBcoEstado(respuesta, rut, cod_medio, medio, nombre_persona, servicio, turno,conf);
        }else{
            logger.info("generaXmlRespuestaPagosPendientes() -  termina - vida - termina ejecutando setPolizas()");
            return null;
        }
    }

    public String validateXML(XmlObject xml) {
        logger.info("validateXML() - inicio");
        String txtMsg = "";
        Collection errorList = new ArrayList();
        XmlOptions xo = new XmlOptions();
        xo.setErrorListener(errorList);
        if (!xml.validate(xo)) {
            logger.info(xml.toString());
            StringBuffer sb = new StringBuffer();
            for (Iterator it = errorList.iterator(); it.hasNext(); ) {
                sb.append(it.next() + "\n");
            }
            txtMsg = "XML con errores:" + sb.toString();     
            logger.info("validateXML() - XML "+xml.getClass()+" con errores :" + txtMsg);
        }
        logger.info("validateXML() - termino");
        return txtMsg;
    }

    public Transacciones generaXmlRespuestaPagosPendientes(RespuestaPagosPendientesDocument respuesta, 
                                                           int rut, 
                                                           int empresa, 
                                                           int cod_medio, 
                                                           String medio, 
                                                           String nombre_persona, 
                                                           ServicioPec servicio, 
                                                           ConfirmacionDocument conf) {
        return null;
    }

    public Transacciones setDividendos(RespuestaPagosPendientesDocument respuesta, 
                                       int rut, int cod_medio, String medio, 
                                       String nombre_persona, 
                                       ServicioPec servicio, 
                                       AperturaCaja turno, 
                                       ConfirmacionDocument conf) {
        return null;
    }

    public String setDividendosServipag(int rut, int cod_medio, String medio, 
                                        String nombre_persona, 
                                        AperturaCaja turno, String xmlorigen) {        
        logger.info("setDividendos() - inicio");
        //String resumenxml = "NOK|Error inesperado";
        List dividendos = null;
        Date fecha_turno = null;
        Integer empresa = new Integer(ResourceBundleUtil.getProperty("codigo.hipotecaria.dividendo"));
        String resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.noesperado")), ResourceBundleUtil.getProperty("mensaje.error.noesperado"));
        if (turno != null) {
            logger.info("setDividendosServipag() - consultando dividendos por rut consultasBD.findOlderDividendoByRut()");
            dividendos = DAOFactory.getConsultasPecDao().findOlderDividendoByRut(Integer.toString(rut));
            fecha_turno = turno.getTurno();
        } else {
            logger.info("setDividendosServipag() - consultando dividendos offline por rut consultasBD.findOlderDividendosOfflineByRut");
            dividendos = DAOFactory.getConsultasPecDao().findOlderDividendosOfflineByRut(Integer.toString(rut));
        }     
        
        logger.info("setDividendosServipag() - creo nueva instancia de respuesta");
        
        /**
         * SETEA DOCUMENTO DE RESPUESTA PRA HIPOTECARIA SERVIPAG
         */
        RespuestaServipagHipotecariaDocument respuesta = RespuestaServipagHipotecariaDocument.Factory.newInstance();        
        RespuestaServipagHipotecariaDocument.RespuestaServipagHipotecaria newRespuesta = respuesta.addNewRespuestaServipagHipotecaria();
        
        /**
         * EVALUA SI HAY DIVIDENDOS
         */
        if (dividendos == null || dividendos.size() == 0) {
            
            /**
             * NO DIVIDENDOS
             */
            logger.error("setDividendosServipag() - error del dividendo en null, seteando error forzado 'codigo.error.sinpagos'");            
            resumenxml=crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.sinpagos")), ResourceBundleUtil.getProperty("mensaje.error.servipag.sinpagos.dividendo"));
            return resumenxml;
        } else {
        
            /**
             * SI HAY DIVIDENDOS PARA PAGAR
             */
            //obtengo el valor de la uf para calcular el monto a pagar
            logger.info("setDividendosServipag() - obtengo el valor de la uf para calcular el monto a pagar");            
            double valor_uf = setValorUF();
            if(valor_uf==0) {
                logger.error("setDividendosServipag() - error de la uf =0 , seteando error forzado 'codigo.error.bd'");
                resumenxml=crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));                    
                return resumenxml;
            }            
            
            
            /**
             * SETEA DATOS ADICIONALES DE CONFIRMACION DE LA OPERACION
             */
            RespuestaServipagHipotecariaDocument.RespuestaServipagHipotecaria.ListaProductos listaprod = newRespuesta.addNewListaProductos();                            
                
            /**
             * ITERA LOS DIVIDENDOS Y LOS REGISTRA
             * PARA SER PROCESADOS COMO XML DE RESPUESTA
             */
            for(int i=0;i<dividendos.size();i++){                
                ServicioPec servicioNav;
                try {
                    servicioNav = DAOFactory.getConsultasPecDao().newServicioPec(rut,cod_medio);
                } catch (Exception e) {
                    logger.error("setDividendosServipag() - Error al generar Id de naveacion: " + e.getMessage(), e);
                    resumenxml=crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());  
                    return resumenxml;
                }
                RespuestaServipagHipotecariaDocument respuestaOnly = RespuestaServipagHipotecariaDocument.Factory.newInstance();        
                RespuestaServipagHipotecariaDocument.RespuestaServipagHipotecaria newRespuestaOnly = respuestaOnly.addNewRespuestaServipagHipotecaria();    
                RespuestaServipagHipotecariaDocument.RespuestaServipagHipotecaria.ListaProductos listaprodOnly = newRespuestaOnly.addNewListaProductos();  
                RespuestaServipagHipotecariaDocument.RespuestaServipagHipotecaria.ListaProductos.Entrada entradaOnly = listaprodOnly.addNewEntrada();
            
                /**
                 * CREA ID DE TRANSACCION PARA LA OPERACION A REALIZAR 
                 */
                logger.info("setDividendosServipag() - creando transaccion crearTransaccion()");
                Transacciones transaccion = crearTransaccion("BH",rut, cod_medio, nombre_persona, servicioNav, fecha_turno, 0);
                if (transaccion != null) {
                
                    /**
                     * CREAMOS XML PARA NAVEGACION HISTORICA
                     */
                    ConfirmacionDocument conf = ConfirmacionDocument.Factory.newInstance();
                    ConfirmacionDocument.Confirmacion newConfirmacion=conf.addNewConfirmacion();
                    crearEncabezadoConfirmacion(new BigInteger(transaccion.getIdTransaccion().toString()),rut, nombre_persona, valor_uf, newConfirmacion, medio);
                    ConfirmacionDocument.Confirmacion.ProductosPorPagar productos=newConfirmacion.addNewProductosPorPagar();
                    productos.addNewTotalPorPagar();
                    productos.getTotalPorPagar().setEnPesos(new BigInteger("0"));
                    productos.getTotalPorPagar().setEnUF(new BigDecimal("0"));
                    newConfirmacion.addNewConvenio();
                    newConfirmacion.getConvenio().setCodigo(new BigInteger(Integer.toString(cod_medio)));
                    newConfirmacion.getConvenio().setDescripcion(medio);
                
                
                    BhDividendosVw dividendo = null;
                    if(dividendos.get(i) instanceof BhDividendosVw){
                        logger.info("setDividendosServipag() - recuperando dividendos de vista BhDividendosVw");
                        dividendo = (BhDividendosVw)dividendos.get(i);
                    } else{
                        logger.info("setDividendosServipag() - recuperando dividendos de vista BhDividendosOfflineVw");
                        dividendo = ((BhDividendosOfflineVw)dividendos.get(i)).toBhDividendosVw(); 
                    }
                    
                    /**
                     * SETEA UNA NUEVA ENTRADA
                     */
                    RespuestaServipagHipotecariaDocument.RespuestaServipagHipotecaria.ListaProductos.Entrada entrada = listaprod.addNewEntrada();
                    entrada.setIdTransaccion(new BigInteger(transaccion.getIdTransaccion().toString()));
                    entrada.setContador(new BigInteger(Integer.toString(i+1)));
                    int monto = (int)Math.round(dividendo.getTotMda()*valor_uf);                     
                    String descripcion_prod = dividendo.getProducto()+" "+dividendo.getNumDiv()+"/"+dividendo.getPlazo_mes();                    
                    logger.info("setDividendosServipag() - crando nuevo producto newRespuesta.addNewProducto()");
                    
                    /**
                     * REPLICA ONLY
                     */
                    entradaOnly.setIdTransaccion(entrada.getIdTransaccion());
                    entradaOnly.setContador(entrada.getContador());
                    
                    
                    /**
                     * SETEA PRODUCTO
                     */
                    Producto producto = entrada.addNewProducto();
                    setearProducto(producto,dividendo.getCodProducto().toString(),descripcion_prod,dividendo.getNumOpe().toString(),2);                    
                    logger.info("setDividendosServipag() - crando nuevo cuota newRespuesta.addNewCuota()");
                    
                    /**
                     * REPLICA ONLY
                     */
                     Producto productoOnly = entradaOnly.addNewProducto();
                    setearProducto(productoOnly,dividendo.getCodProducto().toString(),descripcion_prod,dividendo.getNumOpe().toString(),2);   
                    
                    
                    /**
                     * SETEA CUOTA
                     */
                    RespuestaServipagHipotecariaDocument.RespuestaServipagHipotecaria.ListaProductos.Entrada.Cuota cuota = entrada.addNewCuota();                    
                    logger.info("setDividendosServipag() - info de la fecha de vencimiento de la cuota y del detalle");          
                    Calendar fv = Calendar.getInstance();
                    fv.setTime(dividendo.getFecVencimiento());
                    cuota.setFechaVencimiento(fv);
                    cuota.setNumDividendo(new BigInteger(dividendo.getNumDiv().toString()));
                    cuota.addNewMontoCuota();
                    cuota.getMontoCuota().setEnPesos(new BigInteger(Integer.toString(monto)));
                    cuota.getMontoCuota().setEnUF(new BigDecimal(dividendo.getTotMda().toString()));                    
                    /**
                     * REPLICA ONLY
                     */
                     RespuestaServipagHipotecariaDocument.RespuestaServipagHipotecaria.ListaProductos.Entrada.Cuota cuotaOnly = entradaOnly.addNewCuota();
                    cuotaOnly.setFechaVencimiento(cuota.getFechaVencimiento());
                    cuotaOnly.setNumDividendo(cuota.getNumDividendo());
                    cuotaOnly.addNewMontoCuota();
                    cuotaOnly.getMontoCuota().setEnPesos(cuota.getMontoCuota().getEnPesos());
                    cuotaOnly.getMontoCuota().setEnUF(cuota.getMontoCuota().getEnUF());
                    

                    
                    //completo confirmacion
                    //agrego entrada
                    logger.info("setDividendosServipag() - creando xml de confirmacion entrada.addNewInfoCajas().addNewCreditos()");
                    ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entradaConfirmacion;
                    entradaConfirmacion = agregarEntradaConfirmacion(productos,i+1,producto.getCodigoProducto(),descripcion_prod,producto.getNumProducto(),dividendo.getCodEmpresa(),cuota.getMontoCuota().getEnPesos(),cuota.getMontoCuota().getEnUF());
                    Infocajas.Creditos cred = entradaConfirmacion.addNewInfoCajas().addNewCreditos();   
                    cred.setNumDividendo(cuota.getNumDividendo());        
                    logger.info("setDividendosServipag() - info de la fecha de vencimiento de la cuota y del detalle"); 
                    entradaConfirmacion.getCuota().setFechaVencimiento(fv);
                     
                    /**
                     * SETEA DETALLE DEL LA CUOTA A PAGAR
                     */
                    logger.info("setDividendosServipag() - creando nuevo detalle entrada.getCuota().getMonto().addNewDetalle()"); 
                    DetalleCargos detalle = entradaConfirmacion.getCuota().getMonto().addNewDetalle();
                    
                    DetalleCargos.Cargo cargo;
                    List detalleDividendo = dividendo.getDetalleDividendo();
                    for(int j=0;j<detalleDividendo.size();j++){
                        Object[] det = (Object[])detalleDividendo.get(j);
                        cargo = detalle.addNewCargo();
                        cargo.setDescripcion(det[0].toString());
                        cargo.setBigDecimalValue(new BigDecimal(det[1].toString())); 
                    }
                    
                    
                    /**
                     * GRABA NAVEGACION POR CADA DIVIDENDO A PAGAR
                     */
                    try {
                        String resumenxmltmp = respuestaOnly.xmlText(); 
                        String xmlconfirmaciontmp = conf.xmlText();
                        logger.info("setDividendosServipag() - grabo xml de respuesta en naveacion en DB");
                        DAOFactory.getConsultasPecDao().createDetalleServPec(servicioNav.getIdNavegacion(),1,6,0,xmlorigen);
                        DAOFactory.getConsultasPecDao().createDetalleServPec(servicioNav.getIdNavegacion(),2,7,0,resumenxmltmp);
                        DAOFactory.getConsultasPecDao().createDetalleServPec(servicioNav.getIdNavegacion(),3,3,0,xmlconfirmaciontmp);
                        
                    } catch (Exception e) {
                         logger.error("setDividendosServipag() - Error al grabar en la base de datos: " + e.getMessage(), e);
                        resumenxml=crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());  
                    }                    
                
                } else {
                    logger.error("setDividendosServipag() - No se ha podido crear la transaccion"); 
                    resumenxml=crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));                                    
                    return resumenxml;
                }
                
            }//fin de la iteracion, xmls creados
            
             /**
              * PARSEO XML COMPLETO
              */
             resumenxml=respuesta.xmlText(); 
            
        }
        return resumenxml; 
    }

    public Transacciones setPolizas(RespuestaPagosPendientesDocument respuesta, 
                                    int rut, int cod_medio, String medio, 
                                    String nombre_persona, 
                                    ServicioPec servicio, AperturaCaja turno, 
                                    ConfirmacionDocument conf) {
        return null;
    }

    public String setPolizasServipag(int rut, int cod_medio, String medio, 
                                     String nombre_persona, AperturaCaja turno, 
                                     String xmlorigen) {
        logger.info("setPolizasServipag() - inicio");
        List polizas = null;
        Date fecha_turno = null;
        
        Integer empresa = new Integer(ResourceBundleUtil.getProperty("codigo.vida.poliza"));
        String resumenxml=crearError(cod_medio,empresa,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
        
        if (turno != null) {
            logger.info("setPolizasServipag() - turno consultado por consultasBD.findOlderPolizasByRut()");          
            polizas = DAOFactory.getConsultasPecDao().findOlderPolizasByRut(rut);
            fecha_turno = turno.getTurno();
        } else {
            logger.info("setPolizasServipag() - turno es nulo consultado por consultasBD.findOlderPolizasOfflineByRut()");          
            polizas = DAOFactory.getConsultasPecDao().findOlderPolizasOfflineByRut(rut);
        }
        
        /**
         * SE VERIFICA SI HAY POLIZAS PARA PAGAR
         */
        if (polizas == null || polizas.size() == 0) {//no hay polizas por pagar
            logger.error("setPolizasServipag() - creando error de logica de proceso, con valor 'codigo.error.sinpagos'");          
            resumenxml = crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.sinpagos")), ResourceBundleUtil.getProperty("mensaje.error.servipag.sinpagos.poliza"));
            return resumenxml;
        } else {
            /**
             * RECUPERA VALOR DE UF
             */
            logger.info("setPolizasServipag() - obtengo el valor de la uf para calcular el monto a pagar"); 
            double valor_uf = setValorUFPolizas();
            if(valor_uf==0) {
                logger.error("setPolizasServipag() - creando error de logica de proceso , con valor 'codigo.error.bd'"); 
                resumenxml = crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));                    
               return resumenxml;
            }          
            
            /**
             * CONFECCIONO TAG RAIZ DE POLIZAS COMO RESULTADO FINAL
             */
            RespuestaServipagVidaDocument respuesta = RespuestaServipagVidaDocument.Factory.newInstance();
            RespuestaServipagVidaDocument.RespuestaServipagVida newRespuesta = respuesta.addNewRespuestaServipagVida();
            RespuestaServipagVidaDocument.RespuestaServipagVida.ListaProductos listaprod = newRespuesta.addNewListaProductos();  
            
            /**
             * ITERA LAS POLIZAS
             */
             for(int i=0;i<polizas.size();i++){
             
                 /**
                  * GENERO NUEVO SERVICIO DE NAVEGACION HISTORICA
                  */
                 ServicioPec servicioNav;
                 try {
                     servicioNav = DAOFactory.getConsultasPecDao().newServicioPec(rut, cod_medio);
                 } catch (Exception e) {
                     logger.error("setPolizasServipag() - Error al generar Id de navegacion: " + e.getMessage(), e);
                     resumenxml=crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());  
                     return resumenxml;
                 }
                 
                 if(servicioNav != null){
                     /**
                     * RECUPERA INFORMACION DE POLIZA
                     */
                     logger.info("setPolizasServipag() - RECUPERA INFORMACION DE POLIZA"); 
                     IndPrimaNormalVw poliza;
                     if(polizas.get(i) instanceof IndPrimaNormalVw){
                       poliza = (IndPrimaNormalVw)polizas.get(i);
                     }
                     else{
                       poliza = ((IndPrimaNormalOfflineVw)polizas.get(i)).toIndPrimaNormalVw();
                     }
                     
                     /**
                      * SETEO MONTO A PAGAR
                      */
                     int monto = (int)Math.round(poliza.getPrimaBrutaUfRecibo()*valor_uf);   
                        
                     /**
                      * SETEA Y GENERA NUEVO ID DE TRANSACCION Para Cada Poliza
                      */
                     logger.info("setPolizasServipag() - creando transaccion crearTransaccion()");                  
                     Transacciones transaccion = crearTransaccion("BV",rut, cod_medio, nombre_persona, servicioNav, fecha_turno, monto);
                     logger.info("setPolizasServipag() - 2 parte creando transaccion crearTransaccion()"); 
                     if(transaccion!=null){  
                         logger.info("setPolizasServipag() - transaccion no es null()");     
                        /**
                        * SETEO XML PARA REGISTRO INDIVIDUAL
                        */
                        logger.info("setPolizasServipag() - SETEO XML PARA REGISTRO INDIVIDUAL");     
                        RespuestaServipagVidaDocument respuestaOnly = RespuestaServipagVidaDocument.Factory.newInstance();
                        RespuestaServipagVidaDocument.RespuestaServipagVida newRespuestaOnly = respuestaOnly.addNewRespuestaServipagVida();
                        RespuestaServipagVidaDocument.RespuestaServipagVida.ListaProductos listaprodOnly = newRespuestaOnly.addNewListaProductos();
                        
                        /**
                        * SETEA DATA DE CONFIRMACION
                        */
                        logger.info("setPolizasServipag() - SETEA DATA DE CONFIRMACION"); 
                        ConfirmacionDocument conf = ConfirmacionDocument.Factory.newInstance();
                        ConfirmacionDocument.Confirmacion newConfirmacion=conf.addNewConfirmacion();
                        crearEncabezadoConfirmacion(new BigInteger(transaccion.getIdTransaccion().toString()),rut, nombre_persona, valor_uf, newConfirmacion, medio);
                        ConfirmacionDocument.Confirmacion.ProductosPorPagar productos=newConfirmacion.addNewProductosPorPagar();
                        productos.addNewTotalPorPagar();
                        productos.getTotalPorPagar().setEnPesos(new BigInteger(Integer.toString(monto)));
                        productos.getTotalPorPagar().setEnUF(new BigDecimal(poliza.getPrimaBrutaUfRecibo().toString()));
                        newConfirmacion.addNewConvenio();
                        newConfirmacion.getConvenio().setCodigo(new BigInteger(Integer.toString(cod_medio)));
                        newConfirmacion.getConvenio().setDescripcion(medio);
                                          
                        /**
                         * CREA NUEVA ENTRADA
                         */
                         logger.info("setPolizasServipag() - CREA NUEVA ENTRADA"); 
                         RespuestaServipagVidaDocument.RespuestaServipagVida.ListaProductos.Entrada entrada = listaprod.addNewEntrada();                                                   
                         entrada.setContador(new BigInteger(Integer.toString(i+1)));                     
                         Producto producto = entrada.addNewProducto();
                         setearProducto(producto,poliza.getCodProducto().toString(),poliza.getNombre(),poliza.getPolizaPol().toString(),1);
                         entrada.setIdTransaccion(new BigInteger(transaccion.getIdTransaccion().toString()));
                         
                        /**
                         * REPLICA
                         */
                         logger.info("setPolizasServipag() - REPLICA"); 
                         RespuestaServipagVidaDocument.RespuestaServipagVida.ListaProductos.Entrada entradaOnly = listaprodOnly.addNewEntrada();   
                         entradaOnly.setContador(entrada.getContador());
                         Producto productoOnly = entradaOnly.addNewProducto();
                         setearProducto(productoOnly, poliza.getCodProducto().toString(), poliza.getNombre(), poliza.getPolizaPol().toString(),1);
                         entradaOnly.setIdTransaccion(entrada.getIdTransaccion());
                          
                        
                         /**
                          * AGREGA NUEVA CUOTA
                          */
                          logger.info("setPolizasServipag() - AGREGA NUEVA CUOTA"); 
                         RespuestaServipagVidaDocument.RespuestaServipagVida.ListaProductos.Entrada.Cuota cuota = entrada.addNewCuota();
                         Calendar fv = Calendar.getInstance();
                         fv.setTime(poliza.getFTerminoRecibo());
                         cuota.setFechaRecibo(fv);
                         cuota.setFolio(new BigInteger(poliza.getFolioRecibo().toString()));
                         cuota.setRamo(new BigInteger(poliza.getRamo().toString()));
                         cuota.addNewMontoCuota();
                         cuota.getMontoCuota().setEnPesos(new BigInteger(Integer.toString(monto)));
                         cuota.getMontoCuota().setEnUF(new BigDecimal(poliza.getPrimaBrutaUfRecibo().toString()));

                        /**
                         * REPLICA
                         */
                         logger.info("setPolizasServipag() - REPLICA 2"); 
                         RespuestaServipagVidaDocument.RespuestaServipagVida.ListaProductos.Entrada.Cuota cuotaOnly = entradaOnly.addNewCuota();
                         cuotaOnly.setFechaRecibo(cuota.getFechaRecibo());
                         cuotaOnly.setFolio(cuota.getFolio());
                         cuotaOnly.setRamo(cuota.getRamo());
                         cuotaOnly.addNewMontoCuota();
                         cuotaOnly.getMontoCuota().setEnPesos(cuota.getMontoCuota().getEnPesos());
                         cuotaOnly.getMontoCuota().setEnUF(cuota.getMontoCuota().getEnUF());
                         
                         
                         
                         
                         /**
                          * SE COMPLETA INFORMACION DE CONFIRMACION
                          */
                          //agrego entrada
                          logger.info("setPolizasServipag() - creando xml de confirmacion entrada.addNewInfoCajas().addNewCreditos()");
                          ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entradaConfirmacion;
                          entradaConfirmacion = agregarEntradaConfirmacion(productos,i+1,producto.getCodigoProducto(),producto.getDescripcionProducto(),producto.getNumProducto(),producto.getCodEmpresa(),cuota.getMontoCuota().getEnPesos(),cuota.getMontoCuota().getEnUF());
                          Infocajas.Polizas pol = entradaConfirmacion.addNewInfoCajas().addNewPolizas();   
                          pol.setFolio(new BigInteger(poliza.getFolioRecibo().toString()));
                          pol.setRamo(new BigInteger(poliza.getRamo().toString()));
                          entradaConfirmacion.getCuota().setFechaVencimiento(fv);
                          DetalleCargos detalle = entradaConfirmacion.getCuota().getMonto().addNewDetalle();
                          DetalleCargos.Cargo cargo = detalle.addNewCargo();
                          cargo.setDescripcion("Prima Neta");
                          cargo.setBigDecimalValue(new BigDecimal(poliza.getPrimaNetaUfRecibo().toString()));
                          detalle.setCargoArray(0, cargo);
                          cargo = detalle.addNewCargo();
                          cargo.setDescripcion("IVA");
                          cargo.setBigDecimalValue(new BigDecimal(poliza.getIvaUfRecibo().toString()));
                          detalle.setCargoArray(1, cargo);     
                                               
                         
                         /**
                          * SETEO XML Y GRABO REGISTRO Y NAVEGACION
                          */
                         try {
                             String resumenxmltmp = respuestaOnly.xmlText(); 
                             String xmlconfirmaciontmp = conf.xmlText();
                             logger.info("setPolizasServipag() - grabo xml de respuesta en naveacion en DB");       
                             DAOFactory.getConsultasPecDao().createDetalleServPec(servicioNav.getIdNavegacion(),1,6,0,xmlorigen);
                             DAOFactory.getConsultasPecDao().createDetalleServPec(servicioNav.getIdNavegacion(),2,7,0,resumenxmltmp);
                             DAOFactory.getConsultasPecDao().createDetalleServPec(servicioNav.getIdNavegacion(),3,3,monto,xmlconfirmaciontmp);
                         } catch (Exception e) {
                              logger.error("setPolizasServipag() - Error al grabar en la base de datos: " + e.getMessage(), e);
                              resumenxml=crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());  
                              return resumenxml;
                         }
                         
                     }  else {
                        logger.error("setPolizasServipag() - No se ha podido crear la transaccion"); 
                        resumenxml=crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));                                    
                        return resumenxml;
                     }    
                 }else{
                     logger.error("setPolizasServipag() - No se ha podido crear la transaccion"); 
                     resumenxml=crearError(cod_medio,empresa.intValue(),Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));                                    
                     return resumenxml;
                 }
             } // FIN CICLO FOR               
             
             /**
              * SETEA DATA FINAL CON XML COMPLETO
              */
             resumenxml = respuesta.xmlText();
            
        }
        
        return resumenxml;
    }
    
    /**
     * Busca en la base de datos polizas pendientes por pagar por parte del cliente.
     * En caso de encontrar datos crea la transaccion en la base de datos y setea la 
     * informacion necesaria en el xml de respuesta y en el de confirmacion
     * En caso de no encontrar datos, crea mensaje de error en el xml de respuesta
     * 
     * @param respuesta Objeto con el xml que se esta creando con la respuesta
     * @param rut Rut de la persona consultada
     * @param cod_medio Codigo unico del medio de pago
     * @param medio Nombre del medio de pago
     * @param nombre_persona Nombre de la persona
     * @param servicio Registro de la consulta realizada
     * @param turno Informacion del turno con la cual se creo la transaccion, 
     *              este dato indicara que tabla se debe acceder
     * @param conf Objeto con el xml de confirmacion asociado a la transaccion que se va a crear
     * @return transaccion creada con el producto posible a pagar
     */
    private Transacciones setPolizasBcoEstado(RespuestaPagosPendientesDocument respuesta, 
                            int rut, int cod_medio, String medio, String nombre_persona, 
                            ServicioPec servicio, AperturaCaja turno,ConfirmacionDocument conf) {
        logger.info("setPolizas() - inicio"); 
        List polizas = null;
        Date fecha_turno = null;
        
        
        if (turno != null) {
            logger.info("setPolizas() - turno consultado por consultasBD.findOlderPolizasByRut()");          
            polizas = DAOFactory.getPersistenciaGeneralDao().findPolizasByRut(rut);
            fecha_turno = turno.getTurno();
        } 

        if (polizas == null || polizas.size() == 0) {
            //creo mesnaje de error
             logger.info("setPolizas() - creando error de logica de proceso, con valor 'codigo.error.sinpagos'");          
            crearError(respuesta,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.sinpagos")), ResourceBundleUtil.getProperty("mensaje.error.servipag.sinpagos.poliza"));            
            return null;
        } else {
             //creo el xml
             //obtengo el valor de la uf para calcular el monto a pagar
             logger.info("setPolizas() - obtengo el valor de la uf para calcular el monto a pagar"); 
             int monto=0;                
             double valor_uf = setValorUFPolizas();
             if(valor_uf==0) {
                 logger.info("setPolizas() - creando error de logica de proceso , con valor 'codigo.error.bd'"); 
                 crearError(respuesta,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.bd")), ResourceBundleUtil.getProperty("mensaje.error.bd.valoruf"));                    
                return null;
             }
             
            Iterator result = polizas.iterator();
            Double primabruta = new Double("0"); 
            Double primaneta = new Double("0"); 
            IndPrimaNormalVw poliza =  new IndPrimaNormalVw();
            while(result.hasNext()) {
                IndPrimaNormalVw polizatemp = (IndPrimaNormalVw)result.next();
                primabruta = primabruta + polizatemp.getPrimaBrutaUfRecibo();
                primaneta = primaneta + polizatemp.getPrimaNetaUfRecibo();
                
                monto = monto + (int)Math.round(polizatemp.getPrimaBrutaUfRecibo()*valor_uf);
                
                //poliza.setCodEmpresa(polizatemp.getCodEmpresa());
                //poliza.setCodMecanismo(polizatemp.getCodMecanismo());
                poliza.setCodProducto(polizatemp.getCodProducto());
                //poliza.setDescTipoRecibo(polizatemp.getDescTipoRecibo());
                //poliza.setDvRutContratante(polizatemp.getDvRutContratante());
                //poliza.setFInicioRecibo(polizatemp.getFInicioRecibo());
                //poliza.setFolioRecibo(polizatemp.getFolioRecibo());
                //poliza.setFpago(polizatemp.getFpago());
                //poliza.setFTerminoRecibo(polizatemp.getFTerminoRecibo());
                //poliza.setIdTipoRecibo(polizatemp.getIdTipoRecibo());
                //poliza.setIvaUfRecibo(polizatemp.getIvaUfRecibo());
                //poliza.setNombre(polizatemp.getNombre());
                poliza.setPolizaPol(polizatemp.getPolizaPol());
                poliza.setPrimaBrutaUfRecibo(primabruta);
                poliza.setPrimaNetaUfRecibo(primaneta);
                //poliza.setPropuestaPol(polizatemp.getPropuestaPol());
                //poliza.setRamo(polizatemp.getRamo());
                //poliza.setRutContratante(polizatemp.getRutContratante());
                //poliza.setViapago(polizatemp.getViapago());
                            
            }
            
            poliza.setNombre("Seguros Bice Vida"); 
             
             
             logger.info("setPolizas() - creando transaccion crearTransaccion()"); 
             Transacciones transaccion = crearTransaccion("BV",rut, cod_medio, nombre_persona, servicio, fecha_turno, monto);
             
             if(transaccion!=null){     
                 RespuestaPagosPendientesDocument.RespuestaPagosPendientes newRespuesta = respuesta.addNewRespuestaPagosPendientes();                
                 newRespuesta.setIdTransaccion(new BigInteger(transaccion.getIdTransaccion().toString()));
                                 
                 logger.info("setPolizas() - agregando nuevo producto newRespuesta.addNewProducto()"); 
                 Producto producto = newRespuesta.addNewProducto();
                 setearProducto(producto,poliza.getCodProducto().toString(),poliza.getNombre(),poliza.getPolizaPol().toString(),1);
                 
                 RespuestaPagosPendientesDocument.RespuestaPagosPendientes.Cuota cuota = newRespuesta.addNewCuota();
                 cuota.setEnPesos(new BigInteger(Integer.toString(monto)));
                 cuota.setEnUF(new BigDecimal(poliza.getPrimaBrutaUfRecibo().toString()));
                 
                 //creo xml de confirmacion
                  logger.info("setPolizas() - agregando nueva confirmacion conf.addNewConfirmacion()"); 
                 ConfirmacionDocument.Confirmacion newConfirmacion=conf.addNewConfirmacion();
                  
                 crearXmlConfirmacionAllProducts(new BigInteger(transaccion.getIdTransaccion().toString()), polizas, rut, nombre_persona, valor_uf, newConfirmacion, poliza.getPrimaBrutaUfRecibo(), monto, medio);
                 //completo datos con la ifnormacion de la poliza
                 
                  
                 ConfirmacionDocument.Confirmacion.ProductosPorPagar productosporpagar =  newConfirmacion.getProductosPorPagar();
                 
                 Iterator polizasitr = polizas.iterator();
                 int contador = 0;
                 while(polizasitr.hasNext()){
                     IndPrimaNormalVw polizaind = (IndPrimaNormalVw)polizasitr.next();
                     ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entrada = newConfirmacion.getProductosPorPagar().getEntradaArray(contador);
                     Infocajas.Polizas pol = entrada.addNewInfoCajas().addNewPolizas();   
                     pol.setFolio(new BigInteger(polizaind.getFolioRecibo().toString()));
                     pol.setRamo(new BigInteger(polizaind.getRamo().toString()));
                                     
                     //info de la fecha de vencimiento de la cuota y del detalle
                     logger.info("setPolizas() - info de la fecha de vencimiento de la cuota y del detalle");
                     Calendar fv = Calendar.getInstance();
                     fv.setTime(polizaind.getFTerminoRecibo());
                     entrada.getCuota().setFechaVencimiento(fv);
                     
                     DetalleCargos detalle = entrada.getCuota().getMonto().addNewDetalle();
                     DetalleCargos.Cargo cargo = detalle.addNewCargo();
                     cargo.setDescripcion("Prima Neta");
                     cargo.setBigDecimalValue(new BigDecimal(polizaind.getPrimaNetaUfRecibo().toString()));
                     detalle.setCargoArray(0, cargo);
                     cargo = detalle.addNewCargo();
                     cargo.setDescripcion("IVA");
                     cargo.setBigDecimalValue(new BigDecimal(polizaind.getIvaUfRecibo().toString()));
                     detalle.setCargoArray(1, cargo);
                     contador++;
                 }
                 
                 logger.info("setPolizas() - agregando nuevo newConfirmacion.addNewConvenio()");          
                 newConfirmacion.addNewConvenio();
                 newConfirmacion.getConvenio().setCodigo(new BigInteger(Integer.toString(cod_medio)));
                 newConfirmacion.getConvenio().setDescripcion(medio);
                 
                 /*ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entrada = newConfirmacion.getProductosPorPagar().getEntradaArray(0);
                 Infocajas.Polizas pol = entrada.addNewInfoCajas().addNewPolizas();   
                 pol.setFolio(new BigInteger(poliza.getFolioRecibo().toString()));
                 pol.setRamo(new BigInteger(poliza.getRamo().toString()));
                                 
                 //info de la fecha de vencimiento de la cuota y del detalle
                logger.info("setPolizas() - info de la fecha de vencimiento de la cuota y del detalle");          
                 Calendar fv = Calendar.getInstance();
                 fv.setTime(poliza.getFTerminoRecibo());
                 entrada.getCuota().setFechaVencimiento(fv);
                 
                 DetalleCargos detalle = entrada.getCuota().getMonto().addNewDetalle();
                 DetalleCargos.Cargo cargo = detalle.addNewCargo();
                 cargo.setDescripcion("Prima Neta");
                 cargo.setBigDecimalValue(new BigDecimal(poliza.getPrimaNetaUfRecibo().toString()));
                 detalle.setCargoArray(0, cargo);
                 cargo = detalle.addNewCargo();
                 cargo.setDescripcion("IVA");
                 cargo.setBigDecimalValue(new BigDecimal(poliza.getIvaUfRecibo().toString()));
                 detalle.setCargoArray(1, cargo);
                 logger.info("setPolizas() - agregando nuevo newConfirmacion.addNewConvenio()");          
                 newConfirmacion.addNewConvenio();
                 newConfirmacion.getConvenio().setCodigo(new BigInteger(Integer.toString(cod_medio)));
                 newConfirmacion.getConvenio().setDescripcion(medio);
                 logger.info("setPolizas() - termina - con exito retornado la transaccion");          */
                 return transaccion;
             }  else {
                logger.error("crearTransaccion() - No se ha podido crear la transaccion"); 
                crearError(respuesta,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.bd")), ResourceBundleUtil.getProperty("mensaje.error.bd.transaccion"));                                    
                return null;
            }
        }
    }
        
    /**
     * Con la informacion proporcionada por el xml de la respuesta se crea el 
     * xml de confirmacion
     * 
     * @param idtransaccion
     * @param polizas
     * @param rut
     * @param nombre_persona
     * @param valor_uf
     * @param newConfirmacion
     * @param uf
     * @param pesos
     */
    public void crearXmlConfirmacionAllProducts(BigInteger idtransaccion,List polizas, 
                                      int rut, String nombre_persona, 
                                      double valor_uf, 
                                      ConfirmacionDocument.Confirmacion newConfirmacion, Double uf, int pesos, String medio) {
        logger.info("crearXmlConfirmacion() - inicio"); 
        //Copia IdentificacionMensaje
        crearEncabezadoConfirmacion(idtransaccion,rut, nombre_persona, valor_uf, newConfirmacion, medio);
        
        //ProductosPorPagar
        logger.info("crearXmlConfirmacion() - ProductosPorPagar"); 
        ConfirmacionDocument.Confirmacion.ProductosPorPagar productos=newConfirmacion.addNewProductosPorPagar();
        
        
        Iterator pol = polizas.iterator();
        int contador = 1;
        while(pol.hasNext()){
            IndPrimaNormalVw poliza = (IndPrimaNormalVw)pol.next();
            
            int valor = ((int)Math.round(poliza.getPrimaBrutaUfRecibo()*valor_uf));
            BigInteger monto = new BigInteger(Integer.toString(valor));
            
            //ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada agregarEntradaConfirmacion(ConfirmacionDocument.Confirmacion.ProductosPorPagar productos, 
              //              Integer contador, BigInteger codigoProducto, String descripcionProducto,BigInteger numProducto,int codEmpresa, BigInteger enPesos, BigDecimal enUF) {
            agregarEntradaConfirmacion(productos, contador, new BigInteger(poliza.getCodProducto().toString()) , poliza.getNombre(), new BigInteger(poliza.getPolizaPol().toString()), 
                                        poliza.getCodEmpresa().intValue(), monto, new BigDecimal(poliza.getPrimaBrutaUfRecibo().toString()) );
            contador++;
        }
        
        productos.addNewTotalPorPagar();
        productos.getTotalPorPagar().setEnPesos(new BigInteger(Integer.toString(pesos)));
        productos.getTotalPorPagar().setEnUF(new BigDecimal(uf.toString()));
        logger.info("crearXmlConfirmacion() - termino"); 
    }

    public void crearXmlConfirmacion(RespuestaPagosPendientesDocument.RespuestaPagosPendientes newRespuesta, 
                                     int rut, String nombre_persona, 
                                     double valor_uf, 
                                     ConfirmacionDocument.Confirmacion newConfirmacion, String medio) {
        logger.info("crearXmlConfirmacion() - inicio"); 
        //Copia IdentificacionMensaje
        crearEncabezadoConfirmacion(newRespuesta.getIdTransaccion(),rut, nombre_persona, valor_uf, newConfirmacion, medio);
        
        //ProductosPorPagar
        logger.info("crearXmlConfirmacion() - ProductosPorPagar"); 
        ConfirmacionDocument.Confirmacion.ProductosPorPagar productos=newConfirmacion.addNewProductosPorPagar();
        agregarEntradaConfirmacion(productos, 1, newRespuesta.getProducto().getCodigoProducto(), 
                newRespuesta.getProducto().getDescripcionProducto(),newRespuesta.getProducto().getNumProducto(),newRespuesta.getProducto().getCodEmpresa(),newRespuesta.getCuota().getEnPesos(),newRespuesta.getCuota().getEnUF());
        
        productos.addNewTotalPorPagar();
        productos.getTotalPorPagar().setEnPesos(newRespuesta.getCuota().getEnPesos());
        productos.getTotalPorPagar().setEnUF(newRespuesta.getCuota().getEnUF());
        logger.info("crearXmlConfirmacion() - termino"); 
    }

    public void crearEncabezadoConfirmacion(BigInteger idTransaccion, int rut, 
                                            String nombre_persona, 
                                            double valor_uf, 
                                            ConfirmacionDocument.Confirmacion newConfirmacion, String medio) {
        ConfirmacionDocument.Confirmacion.IdentificacionMensaje idm = newConfirmacion.addNewIdentificacionMensaje();
        idm.setCodigo("CONFIR");
        idm.setVersion("1.0");
        idm.setDe(medio);
        idm.setFechaCreacion(java.util.Calendar.getInstance());
        idm.setAccion("CONFIRMAR");
         
         
        newConfirmacion.setIdTransaccion(idTransaccion);
        newConfirmacion.setRutCliente(rut);
        newConfirmacion.setNombreCliente(nombre_persona);
        newConfirmacion.setFechaConsulta(Calendar.getInstance());
        newConfirmacion.setFechaUF(Calendar.getInstance());
        newConfirmacion.setValorUF(new BigDecimal(Double.toString(valor_uf)));
    }

    public ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada agregarEntradaConfirmacion(ConfirmacionDocument.Confirmacion.ProductosPorPagar productos, 
                                                                                                  Integer contador, 
                                                                                                  BigInteger codigoProducto, 
                                                                                                  String descripcionProducto, 
                                                                                                  BigInteger numProducto, 
                                                                                                  int codEmpresa, 
                                                                                                  BigInteger enPesos, 
                                                                                                  BigDecimal enUF) {
        ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada entrada = productos.addNewEntrada();
        entrada.setContador(new BigInteger(contador.toString()));
        ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Producto prod = entrada.addNewProducto();
        prod.setCodigoProducto(codigoProducto);
        prod.setDescripcionProducto(descripcionProducto);
        prod.setNumProducto(numProducto);
        prod.setCodEmpresa(codEmpresa);
        
        ConfirmacionDocument.Confirmacion.ProductosPorPagar.Entrada.Cuota cuota = entrada.addNewCuota();
        cuota.addNewMonto();
        cuota.getMonto().setEnPesos(enPesos);
        cuota.getMonto().setEnUF(enUF);
        return entrada;
    }

    public void setearProducto(Producto producto, String cod_producto, 
                               String descripcion_prod, String num_producto, 
                               int cod_empresa) {
        logger.info("setearProducto() - inicio"); 
        producto.setCodigoProducto(new BigInteger(cod_producto));
        producto.setDescripcionProducto(descripcion_prod);
        producto.setNumProducto(new BigInteger(num_producto));
        producto.setCodEmpresa(cod_empresa);
        logger.info("setearProducto() - termino"); 
    }

    public Transacciones crearTransaccion(String empresa, int rut, int cod_medio, 
                                          String nombre_persona, 
                                          ServicioPec servicio, 
                                          Date fecha_turno, int monto) {
        logger.info("crearTransaccion() - inicio"); 
        Transacciones transaccion = null;
        try{
            logger.info("crearTransaccion() - ejecutando  consultasBD.newTransaccion()"); 
            transaccion =  DAOFactory.getPersistenciaGeneralDao().newTransaccion("P",empresa, cod_medio,fecha_turno,monto,null,rut,nombre_persona);
            if(transaccion != null) {
               //actualizo la transaccion en el registro de la bd
                logger.info("crearTransaccion() - actualizo la transaccion en el registro de la bd consultasBD.updateTransaccionInServicioPec()"); 
                DAOFactory.getConsultasPecDao().updateTransaccionInServicioPec(servicio,transaccion.getIdTransaccion(),monto);
            }
        }
        catch(Exception e){
            logger.error("crearTransaccion() - Exception ocurrida al crear transaccion: " + e.getMessage(), e); 
        } finally{ 
            logger.info("crearTransaccion() - termino con exito la transaccion"); 
            return transaccion; 
        }     
    }

    public String crearError(RespuestaPagosPendientesDocument respuesta, 
                             Integer cod_error, String error) {
        return null;
    }

    public String crearError(int medio, int empresa, Integer cod_error, 
                             String error) {
        String mensaje;
        if(medio == 3 || medio == 16) //SERVIPAG
        {
            if (empresa == 1) { //bicevida
                RespuestaServipagVidaDocument respuesta = RespuestaServipagVidaDocument.Factory.newInstance();
                RespuestaServipagVidaDocument.RespuestaServipagVida newRespuesta = respuesta.addNewRespuestaServipagVida();
                newRespuesta.addNewError();
                newRespuesta.getError().setCodError(new BigInteger(cod_error.toString()));
                newRespuesta.getError().setDescripcion(error);            
                mensaje = respuesta.toString();
            }
            else{ //bicehipotecaria
                 RespuestaServipagHipotecariaDocument respuesta = RespuestaServipagHipotecariaDocument.Factory.newInstance();
                 RespuestaServipagHipotecariaDocument.RespuestaServipagHipotecaria newRespuesta = respuesta.addNewRespuestaServipagHipotecaria();
                 newRespuesta.addNewError();
                 newRespuesta.getError().setCodError(new BigInteger(cod_error.toString()));
                 newRespuesta.getError().setDescripcion(error);            
                 mensaje = respuesta.toString();
            }
        }else{
            RespuestaPagosPendientesDocument respuesta = RespuestaPagosPendientesDocument.Factory.newInstance();
            RespuestaPagosPendientesDocument.RespuestaPagosPendientes newRespuesta = respuesta.addNewRespuestaPagosPendientes();
            newRespuesta.addNewError();
            newRespuesta.getError().setCodError(new BigInteger(cod_error.toString()));
            newRespuesta.getError().setDescripcion(error);            
            mensaje = respuesta.toString();
        }
        return mensaje;
    }

    public String crearErrorProcesarInformePagoPendientes(int medio, 
                                                          Integer cod_error, 
                                                          String error) {
        String cabecera = "ConfirmacionPago";
        String dataresponde;
        String resultado;
        
        if (cod_error == 0){
            resultado = "OK";
        }else {
             resultado = "NOK";
        }
        
        if (medio == 3 || medio == 16) cabecera = "RespuestaServipag"; //SERVIP
        // XML de respuesta
         dataresponde = 
            "<" + cabecera +">" 
          + " <Error>"
          + "  <CodError>" + cod_error + "</CodError>"
          + "  <Descripcion>" + error + "</Descripcion>"
          + " </Error>"
          + " <Resultado>" + resultado + "</Resultado>"
          + "</" + cabecera + ">";
        

         return dataresponde;
    }

    public double setValorUF() {
        Calendar fecha = Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");        
        String fechastr = formato.format(fecha.getTime());
        Date fecha_uf;;
        
        try {
            fecha_uf = formato.parse(fechastr);

            ValorUf valor_uf = DAOFactory.getPersistenciaGeneralDao().findValorUfByDate(fecha_uf);
            if (valor_uf != null) {
                Double tmp = Double.parseDouble(NumeroUtil.redondear(valor_uf.getValorUf().doubleValue(),2));
                double val = tmp.doubleValue();                
                logger.info("setValorUF() - termino retornando valor de UF :" + val);
                return val;
            } else
                logger.info("setValorUF() - termino retornando valor cero 0 de la UF");
                return 0;
        } catch (ParseException e) {
            logger.info("setValorUF() - termino retornando valor cero 0"); 
            return 0;
        }
        
    }
    
    public double setValorUFPolizas() {
        logger.info("setValorUF() - inicio"); 
        Calendar fecha = Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat formatonew = new SimpleDateFormat("yyyy-MM-dd");
        String fechas = formato.format(fecha.getTime());
        fechas = fechas + "-05";
        Date fecha_uf;

        try {
            fecha_uf = formatonew.parse(fechas);

            ValorUf valor_uf = DAOFactory.getPersistenciaGeneralDao().findValorUfByDate(fecha_uf);
            if (valor_uf != null) {
                Double tmp = Double.parseDouble(NumeroUtil.redondear(valor_uf.getValorUf().doubleValue(),2));
                double val = tmp.doubleValue();                
                logger.info("setValorUF() - termino retornando valor de UF :" + val);
                return val;
            } else
                logger.info("setValorUF() - termino retornando valor cero 0 de la UF");
                return 0;
        } catch (ParseException e) {
            logger.info("setValorUF() - termino retornando valor cero 0"); 
            return 0;
        }
    }

    public String procesarInformePago(String xml, 
                                      InformePagoPendienteDocument informe, 
                                      int cod_medio) {
        String resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
        
        long idtransaccion = informe.getInformePagoPendiente().getIdTransaccion().longValue();
        logger.info("procesarInformePago() - ejecutando consultasBD.findServicioPecByIdtransaccion()");
        ServicioPec servicio = DAOFactory.getConsultasPecDao().findServicioPecByIdtransaccion(idtransaccion);        
        //if(servicio.getMontoTransaccion().intValue() == informe.getInformePagoPendiente().getMontoPagado().intValue()){
            if(servicio!=null){
                 logger.info("procesarInformePago() - ejecutando consultasBD.createDetalleServPec()");
                try {
                    if(informe.getInformePagoPendiente().getIdentificacionMensaje().getAccion().equals("OK")){
                        DAOFactory.getConsultasPecDao().createDetalleServPec(servicio.getIdNavegacion(),4,8,servicio.getMontoTransaccion(),xml);
                        resumenxml=cerrarTransaccion(informe.getInformePagoPendiente().getIdentificacionMensaje().getAccion(), 
                                        informe.getInformePagoPendiente().getNumTransaccionMedio().longValue(), idtransaccion, 0, cod_medio);   
                    }else{
                        logger.info("procesarInformePago() - Accion NOK: actualizando transaccion consultasBD.updateTransaccion()"); 
                        DAOFactory.getConsultasPecDao().updateTransaccion(idtransaccion,4,informe.getInformePagoPendiente().getNumTransaccionMedio().longValue());
                        resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.exito")),"");
                    }
                    
                } catch (Exception e) {
                     logger.error("procesarInformePago() - Exception error ocurrido : " + e.getMessage(), e); 
                     //resumenxml="NOK|" + ResourceProperties.getProperty("mensaje.error.bd.guardardetalle");
                    resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());
                } finally {
                    return resumenxml;                       
                }
             }
            else{
                logger.info("procesarInformePago() - No se ha encontrado registro del servicio pec");
                //resumenxml="NOK|" + ResourceProperties.getProperty("mensaje.error.bd.servicio");
                resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
            }
        //}else{
        //    logger.info("procesarInformePago() - Los montos no coinciden");
        //    //resumenxml="NOK|" + ResourceProperties.getProperty("mensaje.error.bd.servicio");
        //    resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.montoapagar")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.montoapagar"));
        //}
        
        return resumenxml;
    }

    public String procesarInformePagoPendientesServipag(String xml, 
                                              InformePagoServipagDocument informe, 
                                              int cod_medio) {
        String resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));        
        long idtransaccion = informe.getInformePagoServipag().getIdTransaccion().longValue(); 
            
        ServicioPec servicio = DAOFactory.getConsultasPecDao().findServicioPecByIdtransaccion(idtransaccion);
        if(servicio!=null){
            /*
             * comparo montos de la base de datos con lo informado por servipag 
             */
            if(servicio.getMontoTransaccion().intValue() == informe.getInformePagoServipag().getMontoTotalPagado().intValue()){
                logger.info("procesarInformePagoServipag() - ejecutando consultasBD.createDetalleServPec()");
                try {
                    /*
                     * Almaceno XML del informe de pago de Servipag
                     */
                    DAOFactory.getConsultasPecDao().createDetalleServPec(servicio.getIdNavegacion(),4,8,informe.getInformePagoServipag().getMontoTotalPagado().intValue(),xml);
                    if(informe.getInformePagoServipag().getIdentificacionMensaje().getAccion().equals("OK")){
                        /*
                         * Si el pago es OK sigo con el proceso
                         */
                        logger.info("procesarInformePagoServipag() - Accion OK"); 
                        resumenxml = procesarInformePagoServipagOK(informe, idtransaccion, informe.getInformePagoServipag().getNumTransaccionServipag().longValue(), cod_medio);
                    }else{
                        /*
                         * Si el pago no es OK lo dejo en estado de rechazado
                         */
                        logger.info("procesarInformePagoServipag() - Accion NOK: actualizando transaccion consultasBD.updateTransaccion() a pago rechazado"); 
                        DAOFactory.getConsultasPecDao().updateTransaccion(idtransaccion,4,informe.getInformePagoServipag().getNumTransaccionServipag().longValue());
                        resumenxml = crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.exito")),"");
                    }
                } catch (Exception e) {
                     logger.error("procesarInformePagoServipag() - Exception error ocurrido : " + e.getMessage(), e);                      
                     resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());
                } finally {
                    return resumenxml;                       
                }
            }else{
                logger.info("procesarInformePago() - Los montos no coinciden");
                //resumenxml="NOK|" + ResourceProperties.getProperty("mensaje.error.bd.servicio");
                resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.montoapagar")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.montoapagar"));
            }
        } else{
            logger.error("procesarInformePagoServipag() - No se ha encontrado registro del servicio pec");
            //resumenxml="NOK|" + ResourceProperties.getProperty("mensaje.error.bd.servicio"); 
            resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
        }
        return resumenxml;
    }

    public String procesarInformePagoServipagOK(InformePagoServipagDocument informe, 
                                                long idtransaccion, 
                                                long numtransaccionmedio, 
                                                int cod_medio) {
        String resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));                
        /*
         * Busco xml de confirmación asociado a la transacción
         */
        logger.info("procesarInformePagoServipagOK() - ejecutando consultasBD.findDetalleServicioPECByIdTransaccionPagina()");
        ConsultaDetalleServicioPec respuesta = DAOFactory.getConsultasPecDao().findDetalleServicioPECByIdTransaccionPagina(idtransaccion, new Long(3));
        if (respuesta != null) {
            XMLDocument xmlrespuesta = respuesta.getXmldetalle();
            if (xmlrespuesta.getDocumentElement().getTagName().equals("Confirmacion")) {
                /*
                 * Creacion de arreglo para pagar la entrada seleccionada
                 */
                logger.info("procesarInformePagoServipagOK() - creando arreglo de entradas seleccionadas");
                BigInteger[] entradassel = 
                    informe.getInformePagoServipag().getProductosPagados().getEntradaArray();
                ArrayList seleccionados = 
                    new ArrayList(entradassel.length);
                for (int i = 0; i < entradassel.length; i++) {
                    seleccionados.add(entradassel[i].toString());
                }

                /*
                 * Obteniendo lista de entradas del xml de confirmación
                 */
                logger.info("procesarInformePagoServipagOK() - obteniendo lista de entradas");
                org.w3c.dom.Node ppp = 
                    xmlrespuesta.getDocumentElement().getElementsByTagName("ProductosPorPagar").item(0);
                NodeList entradas = 
                    xmlrespuesta.getDocumentElement().getElementsByTagName("Entrada");
                           
                /*
                 * Obteniendo fecha de pago
                 */
                logger.info("procesarInformePagoServipagOK() - obteniendo fecha de consulta");
                XMLElement fechaconsultaelement = (XMLElement)xmlrespuesta.getDocumentElement().getElementsByTagName("FechaConsulta").item(0);
                FechaUtil format = new FechaUtil();                        
                Date fechaconsulta = format.getFecha("yyyy-MM-dd-HH:mm",fechaconsultaelement.getText());
                  
                int cont_seleccionados = 0;     
                int cod_empresa=0;                
                int totalenpesos = 0;
                double totalenuf = 0;
                boolean entrada_flag = false;                
                ArrayList comprobantes = new ArrayList();
                /*
                 * Recorro las entradas
                 */
                for (int i = 0; i < entradas.getLength(); i++) {
                    org.w3c.dom.Node entrada = entradas.item(i);
                    NamedNodeMap atributos = entrada.getAttributes();
                    String contador = 
                        atributos.getNamedItem("contador").getNodeValue(); 
                    /*
                     * Verifico que la entrada existe removiendola
                     */
                    if (seleccionados.remove(contador)) {
                        logger.info("procesarInformePagoServipagOK() - entrada "+contador+" ha sido seleccionado");
                        entrada_flag = true;
                        ++cont_seleccionados;
                        
                        /*
                         * Obtengo el total a pagar en pesos y e nUF
                         */
                        logger.info("procesarInformePagoServipagOK() - sumando al monto total");
                        org.w3c.dom.Node monto = 
                            entrada.getChildNodes().item(2).getChildNodes().item(1);
                        NodeList montolist = monto.getChildNodes();
                        XMLElement enpesos = 
                            (XMLElement)montolist.item(0);
                        totalenpesos += 
                                Integer.parseInt(enpesos.getText());
                        XMLElement enuf = 
                            (XMLElement)montolist.item(1);
                        totalenuf += 
                                Double.parseDouble(enuf.getText());
                                
                        logger.info("procesarInformePagoServipagOK() - obteniendo datos del producto");
                        org.w3c.dom.Node producto = entrada.getChildNodes().item(0);                                                   
                        NodeList productolist = producto.getChildNodes();
                        
                        /*
                         * obtengo empresa (Bicevida o Hipotecaria)
                         */
                        XMLElement empresa = (XMLElement)productolist.item(3);
                        cod_empresa = Integer.parseInt(empresa.getText()); 
                        
                        /*
                         * Obtengo el codigo del producto (poliza o numero de producto hipotecario)
                         */
                        XMLElement codproductoelement = (XMLElement)productolist.item(0);
                        int cod_producto = Integer.parseInt(codproductoelement.getText());
                        
                        /*
                         * Obtengo el numero del producto (folio o dividendo)
                         */
                        XMLElement numproductoelement = (XMLElement)productolist.item(2);
                        long num_producto = Long.parseLong(numproductoelement.getText());
                           
                        /*
                         * Generar comprobantes
                         */
                        logger.info("procesarInformePagoServipagOK() - se crea el comprobante");
                        Comprobantes comprobante = new Comprobantes();
                        comprobante.setCodProducto(cod_producto);
                        if(cod_empresa == 1){
                            XMLElement folioele = (XMLElement)entrada.getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(0);
                            logger.info("procesarInformePagoServipagOK() - folioele " + folioele.getText().trim());
                            Integer folio = new Integer(folioele.getText().trim());                                             
                            comprobante.setCuota(folio);
                            logger.info("procesarInformePagoServipagOK() - folio " + folio);
                        }
                        else if(cod_empresa == 2){
                            XMLElement numdividendo = (XMLElement)entrada.getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(0);
                            int num_dividendo = Integer.parseInt(numdividendo.getText().trim());
                            comprobante.setCuota(num_dividendo);
                            logger.info("procesarInformePagoServipagOK() - num_dividendo " + num_dividendo);
                        }
                        
                        comprobante.setIdTransaccion(idtransaccion);
                        comprobante.setMontoBase(Integer.parseInt(enpesos.getText()));
                        comprobante.setMontoExcedente(0);
                        comprobante.setMontoTotal(Integer.parseInt(enpesos.getText()));
                        comprobante.setNumProducto(num_producto);
                        comprobantes.add(comprobante);                                
                    } 
                    else {
                        /*
                         * La entrada no existe
                         */
                        logger.info("procesarInformePagoServipagOK() - entrada "+contador+" no se ha eliminado de confirmacion.xml, por lo que la entrada no existe");
                        //eliminar entrada del xml de confirmacion
                        ppp.removeChild(entrada);
                    }
                }  //fin for
                
                if (entrada_flag){
                    //Comparacion totales a pagar                    
                    if( totalenpesos == informe.getInformePagoServipag().getMontoTotalPagado().intValue()){
                        
                        //obtengo informacion del total por pagar
                        logger.info("procesarInformePagoServipagOK() - cargo informacion del total por pagar");
                        
                        /*NodeList totalpplist = 
                            xmlrespuesta.getDocumentElement().getElementsByTagName("TotalPorPagar");
                        org.w3c.dom.Node totalpp = totalpplist.item(0);
                        NodeList totalppchild = totalpp.getChildNodes();
                        XMLElement enpesos = (XMLElement)totalppchild.item(0);
                        XMLElement enuf = (XMLElement)totalppchild.item(1);
                        enpesos.setTextContent(Integer.toString(totalenpesos));
                        enuf.setTextContent(NumeroUtil.redondear(totalenuf, 4));*/
                        
                        //debo agregar info al detalle de la transaccion
                        logger.info("procesarInformePagoServipagOK() - se crea el detalle por empresa");
                        DetalleTransaccionByEmp detalleTransaccion = new DetalleTransaccionByEmp();
                        detalleTransaccion.setCodEmpresa(cod_empresa);
                        detalleTransaccion.setFechahora(fechaconsulta);
                        detalleTransaccion.setIdTransaccion(idtransaccion);
                        detalleTransaccion.setMontoTotal(totalenpesos);
                        detalleTransaccion.setCodMedio(cod_medio);
                        detalleTransaccion.setNumTransaccionMedio(numtransaccionmedio);
                        
                        ArrayList detallesbyemp = new ArrayList();
                        detallesbyemp.add(detalleTransaccion);
                        
                        //TODO validar xml creado
                        //actualizo la bd
                        StringWriter sw = new StringWriter();
                        try {
                            /*
                            xmlrespuesta.print(new PrintWriter(sw));
                            
                            //NUEVO ?? o actualizar?
                            DetalleServicioPec detalle = new DetalleServicioPec();
                            detalle.setEntrada(respuesta.getEntrada());
                            detalle.setIdNavegacion(respuesta.getIdNavegacion());
                            detalle.setCodPagina(respuesta.getCodPagina());
                            detalle.setFechaHora(respuesta.getFechaHora());
                            detalle.setMontoTransaccion(new Integer(totalenpesos));
                                                
                            //consultasBD.SQLUpdateXML(detalle, sw.toString());
                            DAOFactory.getConsultasPecDao().updateDetNavegacionXML(detalle, sw.toString().trim());
                            //consultasBD.SQLUpdateXMLSuper(detalle, sw.toString());
                            */
                            
                            
                            /*
                             * Actualizao Transacción
                             */
                            logger.info("procesarInformePagoServipagOK() - busco transaccion");
                            Transacciones transaccion  = DAOFactory.getPersistenciaGeneralDao().findTransaccionById((int)idtransaccion);
                            transaccion.setMontoTotal(totalenpesos);
                            transaccion.setCod_estado(2);
                            transaccion.setNumtransaccionmedio(numtransaccionmedio);                            
                            logger.info("procesarInformePagoServipagOK() - actualizo detalle transaccion");                            
                            DAOFactory.getConsultasPecDao().updateDetalleTransaccion(transaccion,detallesbyemp,comprobantes);
                            
                            
                            /*
                             * Reliza el pago
                             */
                            SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();
                            dto.setIdTrx(idtransaccion);
                            dto.setCodRet(0);
                            dto.setMontoPago(transaccion.getMontoTotal());
                            dto.setNropPagos(cont_seleccionados);
                            
                            logger.info("procesarInformePagoServipagOK() - actualizando transaccion consultasBD.actualizaTransaccionSP(dto)"); 
                            DAOFactory.getConsultasPecDao().actualizaTransaccionSP(dto);
                            
                            /*
                             * Genera respuesta afirmativa de pago
                             */
                            resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.exito")),"");                            
                            logger.info("procesarInformePagoServipagOK() - termino");
                                                                                 
                        } catch (Exception e) {
                             logger.error("procesarInformePagoServipagOK() - error probable al guardar en la bd: "+e.getMessage(),e);
                             //resumenxml="NOK|" + ResourceProperties.getProperty("mensaje.error.bd.actualizartransaccion");
                            resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());
                        }
                    }else {
                        logger.error("procesarInformePagoServipagOK() - monto no concuerda con lo esperado. Monto recibido: " + 
                                           informe.getInformePagoServipag().getMontoTotalPagado().intValue()
                                           + ". Monto esperado: " + totalenpesos );                        
                        resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.montoapagar")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.montoapagar"));
                    } 
                }else{
                    logger.error("procesarInformePagoServipagOK() - Xml con errores: Entradas a pagar no concuerdan");
                    resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.entradaenviada")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.entradaenviada"));                   
                }
            } else {
                logger.error("procesarInformePagoServipagOK() - Xml encontrado en la BD no es del tipo esperado " + 
                                   xmlrespuesta.getDocumentElement().getTagName());                
                resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));         
            }
        } else {
            logger.error("procesarInformePagoServipagOK() - No se ha encontrado xml de confirmacion para transaccion " + 
                               idtransaccion);            
            resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));         
                    
        }
        return resumenxml;
    }

    public String cerrarTransaccion(String accion, long numtransaccionmedio, 
                                    long idtransaccion, int nropagos, 
                                    int cod_medio) {
        String resultado=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.noesperado")), ResourceBundleUtil.getProperty("mensaje.error.noesperado"));
        
        try{    
            if(accion.equals("OK")) {
                logger.info("procesarInformePagoPendientes() - ejecutando updateTransaccion()"); 
                nropagos = 1;
                Transacciones transaccion = DAOFactory.getConsultasPecDao().updateTransaccion(idtransaccion,2,numtransaccionmedio);                                           
                SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();
                dto.setIdTrx(idtransaccion);
                dto.setCodRet(0);
                dto.setMontoPago(transaccion.getMontoTotal());
                dto.setNropPagos(nropagos);
                dto.setIdTrxBanco(""+numtransaccionmedio);
                
                logger.info("procesarInformePagoPendientes() - actualizando transaccion consultasBD.actualizaTransaccionSP(dto)"); 
                DAOFactory.getConsultasPecDao().actualizaTransaccionSP(dto);                
                //resultado="OK";
                resultado=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.exito")),"");
            }
            else{
                 logger.info("procesarInformePagoPendientes() - actualizando transaccion consultasBD.updateTransaccion()"); 
                 DAOFactory.getConsultasPecDao().updateTransaccion(idtransaccion,4,numtransaccionmedio);
                 //resultado="OK";
                  resultado=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.exito")),"");
            }
        } catch (Exception e) {
             logger.error("procesarInformePagoPendientes() - Exception error ocurrido : " + e.getMessage(), e); 
             //resultado="NOK|" + ResourceProperties.getProperty("mensaje.error.bd.actualizartransaccion");            
            resultado=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());
        }finally{             
             return resultado;
        }     
    }
    
    /**********************************/
    
    
     public String procesarInformePagoBcoEstado(String xml, 
                                               InformePagoPendienteDocument informe, 
                                               int cod_medio) {
         logger.error("procesarInformePagoBcoEstado() - Inicio");
         String resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));        
         long idtransaccion = informe.getInformePagoPendiente().getIdTransaccion().longValue();
             
         ServicioPec servicio = DAOFactory.getConsultasPecDao().findServicioPecByIdtransaccion(idtransaccion);
         if(servicio!=null){
             /*
              * comparo montos de la base de datos con lo informado por servipag 
              */
             //if(servicio.getMontoTransaccion().intValue() == informe.getInformePagoPendiente().getMontoPagado().intValue()){
                 logger.info("procesarInformePagoBcoEstado() - ejecutando consultasBD.createDetalleServPec()");
                 try {
                     /*
                      * Almaceno XML del informe de pago de Servipag
                      */
                     //DAOFactory.getConsultasPecDao().createDetalleServPec(servicio.getIdNavegacion(),4,8,informe.getInformePagoPendiente().getMontoPagado().intValue(),xml);
                     if(informe.getInformePagoPendiente().getIdentificacionMensaje().getAccion().equals("OK")){
                         /*
                          * Si el pago es OK sigo con el proceso
                          */
                         logger.info("procesarInformePagoBcoEstado() - Accion OK"); 
             //            resumenxml = procesarInformePagoBcoEstadoOK(informe, idtransaccion, informe.getInformePagoPendiente().getNumTransaccionMedio().longValue(), cod_medio);
                     }else{
                         /*
                          * Si el pago no es OK lo dejo en estado de rechazado
                          */
                         logger.info("procesarInformePagoBcoEstado() - Accion NOK: actualizando transaccion consultasBD.updateTransaccion() a pago rechazado"); 
                         DAOFactory.getConsultasPecDao().updateTransaccion(idtransaccion,4,informe.getInformePagoPendiente().getNumTransaccionMedio().longValue());
                         resumenxml = crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.exito")),"");
                     }
                 } catch (Exception e) {
                      logger.error("procesarInformePagoBcoEstado() - Exception error ocurrido : " + e.getMessage(), e);                      
                      resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());
                 } finally {
                     return resumenxml;                       
                 }
             //}else{
             //    logger.info("procesarInformePagoBcoEstado() - Los montos no coinciden");
             //    //resumenxml="NOK|" + ResourceProperties.getProperty("mensaje.error.bd.servicio");
              //   resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.montoapagar")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.montoapagar"));
             //}
         } else{
             logger.error("procesarInformePagoBcoEstado() - No se ha encontrado registro del servicio pec");
             //resumenxml="NOK|" + ResourceProperties.getProperty("mensaje.error.bd.servicio"); 
             resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));
         }
         
         logger.error("procesarInformePagoBcoEstado() - Termino");
         return resumenxml;
     }
     
    public String procesarInformePagoBcoEstadoOK(InformePagoPendienteDocument informe, 
                                                long idtransaccion, 
                                                long numtransaccionmedio, 
                                                int cod_medio) {
        String resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));                
        /*
         * Busco xml de confirmación asociado a la transacción
         */
        logger.info("procesarInformePagoBcoEstadoOK() - ejecutando consultasBD.findDetalleServicioPECByIdTransaccionPagina() - obtiene xml de confirmacion");
        ConsultaDetalleServicioPec respuesta = DAOFactory.getConsultasPecDao().findDetalleServicioPECByIdTransaccionPagina(idtransaccion, new Long(3));
        if (respuesta != null) {
            XMLDocument xmlrespuesta = respuesta.getXmldetalle();
            if (xmlrespuesta.getDocumentElement().getTagName().equals("Confirmacion")) {                
                /*
                 * Obteniendo lista de entradas del xml de confirmación
                 */
                logger.info("procesarInformePagoBcoEstadoOK() - obteniendo lista de entradas");
                org.w3c.dom.Node ppp = 
                    xmlrespuesta.getDocumentElement().getElementsByTagName("ProductosPorPagar").item(0);
                NodeList entradas = 
                    xmlrespuesta.getDocumentElement().getElementsByTagName("Entrada");
                           
                /*
                 * Obteniendo fecha de pago
                 */
                logger.info("procesarInformePagoBcoEstadoOK() - obteniendo fecha de consulta");
                XMLElement fechaconsultaelement = (XMLElement)xmlrespuesta.getDocumentElement().getElementsByTagName("FechaConsulta").item(0);
                FechaUtil format = new FechaUtil();                        
                Date fechaconsulta = format.getFecha("yyyy-MM-dd-HH:mm",fechaconsultaelement.getText());
                  
                int cont_seleccionados = 0;     
                int cod_empresa=0;                
                int totalenpesos = 0;
                double totalenuf = 0;
                boolean entrada_flag = false;                
                ArrayList comprobantes = new ArrayList();
                /*
                 * Recorro las entradas
                 */
                for (int i = 0; i < entradas.getLength(); i++) {                                  
                    org.w3c.dom.Node entrada = entradas.item(i);
                    
                    NamedNodeMap atributos = entrada.getAttributes();
                    String contador = 
                        atributos.getNamedItem("contador").getNodeValue(); 
                   
                    logger.info("procesarInformePagoBcoEstadoOK() - entrada "+contador+" ha sido seleccionado");
                    entrada_flag = true;
                    ++cont_seleccionados;
                    
                    /*
                     * Obtengo el total a pagar en pesos y e nUF
                     */
                    logger.info("procesarInformePagoBcoEstadoOK() - sumando al monto total");                    
                    org.w3c.dom.Node monto = 
                        entrada.getChildNodes().item(2).getChildNodes().item(1);
                    NodeList montolist = monto.getChildNodes();
                    XMLElement enpesos = 
                        (XMLElement)montolist.item(0);
                    totalenpesos += 
                            Integer.parseInt(enpesos.getText());
                    XMLElement enuf = 
                        (XMLElement)montolist.item(1);
                    totalenuf += 
                            Double.parseDouble(enuf.getText());
                            
                    logger.info("procesarInformePagoServipagOK() - obteniendo datos del producto");
                    org.w3c.dom.Node producto = entrada.getChildNodes().item(0);                                                   
                    NodeList productolist = producto.getChildNodes();
                    
                    /*
                     * obtengo empresa (Bicevida o Hipotecaria)
                     */
                    XMLElement empresa = (XMLElement)productolist.item(3);
                    cod_empresa = Integer.parseInt(empresa.getText()); 
                    
                    /*
                     * Obtengo el codigo del producto (poliza o numero de producto hipotecario)
                     */
                    XMLElement codproductoelement = (XMLElement)productolist.item(0);
                    int cod_producto = Integer.parseInt(codproductoelement.getText());
                    
                    /*
                     * Obtengo el numero del producto (folio o dividendo)
                     */
                    XMLElement numproductoelement = (XMLElement)productolist.item(2);
                    long num_producto = Long.parseLong(numproductoelement.getText());
                       
                    /*
                     * Generar comprobantes
                     */
                    logger.info("procesarInformePagoServipagOK() - se crea el comprobante");
                    Comprobantes comprobante = new Comprobantes();
                    comprobante.setCodProducto(cod_producto);
                    if(cod_empresa == 1){
                        XMLElement folioele = (XMLElement)entrada.getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(0);
                        logger.info("procesarInformePagoServipagOK() - folioele " + folioele.getText().trim());
                        Integer folio = new Integer(folioele.getText().trim());                                             
                        comprobante.setCuota(folio);
                        logger.info("procesarInformePagoServipagOK() - folio " + folio);
                    }
                    else if(cod_empresa == 2){
                        XMLElement numdividendo = (XMLElement)entrada.getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(0);
                        int num_dividendo = Integer.parseInt(numdividendo.getText().trim());
                        comprobante.setCuota(num_dividendo);
                        logger.info("procesarInformePagoServipagOK() - num_dividendo " + num_dividendo);
                    }
                    
                    comprobante.setIdTransaccion(idtransaccion);
                    comprobante.setMontoBase(Integer.parseInt(enpesos.getText()));
                    comprobante.setMontoExcedente(0);
                    comprobante.setMontoTotal(Integer.parseInt(enpesos.getText()));
                    comprobante.setNumProducto(num_producto);
                    comprobantes.add(comprobante);                                
                   
                }  //fin for
                
                if (entrada_flag){
                    //Comparacion totales a pagar                    
                    //if( totalenpesos == informe.getInformePagoPendiente().getMontoPagado().intValue()){
                        
                        //obtengo informacion del total por pagar
                        logger.info("procesarInformePagoServipagOK() - cargo informacion del total por pagar");
                        
                        /*NodeList totalpplist = 
                            xmlrespuesta.getDocumentElement().getElementsByTagName("TotalPorPagar");
                        org.w3c.dom.Node totalpp = totalpplist.item(0);
                        NodeList totalppchild = totalpp.getChildNodes();
                        XMLElement enpesos = (XMLElement)totalppchild.item(0);
                        XMLElement enuf = (XMLElement)totalppchild.item(1);
                        enpesos.setTextContent(Integer.toString(totalenpesos));
                        enuf.setTextContent(NumeroUtil.redondear(totalenuf, 4));*/
                        
                        //debo agregar info al detalle de la transaccion
                        logger.info("procesarInformePagoServipagOK() - se crea el detalle por empresa");
                        DetalleTransaccionByEmp detalleTransaccion = new DetalleTransaccionByEmp();
                        detalleTransaccion.setCodEmpresa(cod_empresa);
                        detalleTransaccion.setFechahora(fechaconsulta);
                        detalleTransaccion.setIdTransaccion(idtransaccion);
                        detalleTransaccion.setMontoTotal(totalenpesos);
                        detalleTransaccion.setCodMedio(cod_medio);
                        detalleTransaccion.setNumTransaccionMedio(numtransaccionmedio);
                        
                        ArrayList detallesbyemp = new ArrayList();
                        detallesbyemp.add(detalleTransaccion);
                        
                        //TODO validar xml creado
                        //actualizo la bd
                        StringWriter sw = new StringWriter();
                        try {
                            /*
                            xmlrespuesta.print(new PrintWriter(sw));
                            
                            //NUEVO ?? o actualizar?
                            DetalleServicioPec detalle = new DetalleServicioPec();
                            detalle.setEntrada(respuesta.getEntrada());
                            detalle.setIdNavegacion(respuesta.getIdNavegacion());
                            detalle.setCodPagina(respuesta.getCodPagina());
                            detalle.setFechaHora(respuesta.getFechaHora());
                            detalle.setMontoTransaccion(new Integer(totalenpesos));
                                                
                            //consultasBD.SQLUpdateXML(detalle, sw.toString());
                            DAOFactory.getConsultasPecDao().updateDetNavegacionXML(detalle, sw.toString().trim());
                            //consultasBD.SQLUpdateXMLSuper(detalle, sw.toString());
                            */
                            
                            
                            /*
                             * Actualizao Transacción
                             */
                            logger.info("procesarInformePagoServipagOK() - busco transaccion");
                            Transacciones transaccion  = DAOFactory.getPersistenciaGeneralDao().findTransaccionById((int)idtransaccion);
                            transaccion.setMontoTotal(totalenpesos);
                            transaccion.setCod_estado(2);
                            transaccion.setNumtransaccionmedio(numtransaccionmedio);                            
                            logger.info("procesarInformePagoServipagOK() - actualizo detalle transaccion");                            
                            DAOFactory.getConsultasPecDao().updateDetalleTransaccion(transaccion,detallesbyemp,comprobantes);
                            
                            
                            /*
                             * Reliza el pago
                             */
                            SPActualizarTransaccionDto dto = new SPActualizarTransaccionDto();
                            dto.setIdTrx(idtransaccion);
                            dto.setCodRet(0);
                            dto.setMontoPago(transaccion.getMontoTotal());
                            dto.setNropPagos(cont_seleccionados);
                            
                            logger.info("procesarInformePagoServipagOK() - actualizando transaccion consultasBD.actualizaTransaccionSP(dto)"); 
                            DAOFactory.getConsultasPecDao().actualizaTransaccionSP(dto);
                            
                            /*
                             * Genera respuesta afirmativa de pago
                             */
                            resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.exito")),"");                            
                            logger.info("procesarInformePagoServipagOK() - termino");
                                                                                 
                        } catch (Exception e) {
                             logger.error("procesarInformePagoServipagOK() - error probable al guardar en la bd: "+e.getMessage(),e);
                             //resumenxml="NOK|" + ResourceProperties.getProperty("mensaje.error.bd.actualizartransaccion");
                            resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.excepcion")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.excepcion") + " " + e.getMessage());
                        }
                    //}else {
                    //    logger.error("procesarInformePagoServipagOK() - monto no concuerda con lo esperado. Monto recibido: " + 
                    //                       informe.getInformePagoPendiente().getMontoPagado().intValue()
                    //                       + ". Monto esperado: " + totalenpesos );                        
                    //    resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.montoapagar")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.montoapagar"));
                    //} 
                }else{
                    logger.error("procesarInformePagoServipagOK() - Xml con errores: Entradas a pagar no concuerdan");
                    resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.entradaenviada")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.entradaenviada"));                   
                }
            } else {
                logger.error("procesarInformePagoServipagOK() - Xml encontrado en la BD no es del tipo esperado " + 
                                   xmlrespuesta.getDocumentElement().getTagName());                
                resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));         
            }
        } else {
            logger.error("procesarInformePagoServipagOK() - No se ha encontrado xml de confirmacion para transaccion " + 
                               idtransaccion);            
            resumenxml=crearErrorProcesarInformePagoPendientes(cod_medio,Integer.parseInt(ResourceBundleUtil.getProperty("codigo.error.servipag.pago")), ResourceBundleUtil.getProperty("mensaje.error.servipag.problema.pago"));         
                    
        }
        return resumenxml;
    }
}
