package cl.bice.vida.botonpago.modelo.ejb;

import cl.bice.vida.botonpago.common.dto.general.SPActualizarTransaccionDto;
import cl.bice.vida.botonpago.common.dto.general.Transacciones;
import cl.bice.vida.botonpago.modelo.dao.DAOFactory;
import cl.bice.vida.botonpago.modelo.dao.JmsBalancedDAO;
import cl.bice.vida.botonpago.modelo.thread.GenerarPagoThread;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;


//@MessageDriven(mappedName = "weblogic.wsee.DefaultQueue")
@MessageDriven(mappedName="jms/BotonPagoTopic", 
    activationConfig =  {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    })
public class PagosAsyncMDBEJBBean implements MessageListener {


    /**
     * Logger for this class
     */
     private static final Logger logger = Logger.getLogger(PagosAsyncMDBEJBBean.class);    
     private String nombreServer;
    
     public PagosAsyncMDBEJBBean() {
          logger.info("(JMS) MDB asincrono creado.");
          cargaNombreServer();
      }
     
     private void cargaNombreServer() {
        logger.info("Cargando nombre servidor"); 
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
            this.nombreServer = address.getHostName();    
        } catch (UnknownHostException e) {
            logger.error("Error obteniendo nombre servidor", e);
        }
         logger.info("Nombre servidor [" + nombreServer + "]Cargado");
     }
      
      public void onMessage(Message msg) {
          logger.info("(JMS) Nuevo Mensaje asincrono ha llegado:");
          if (msg instanceof ObjectMessage) {
              JmsBalancedDAO jmsDAO = DAOFactory.getJmsBalancedDAO();
              String mensajeJMS = "Iniciando procesamiento de cola JMS";
              
                  
                  ObjectMessage tm = (ObjectMessage) msg;
                  try {
                     if (tm.getObject() != null && tm.getObject() instanceof SPActualizarTransaccionDto) {
                         SPActualizarTransaccionDto dtoPago = (SPActualizarTransaccionDto) tm.getObject();
                         if(!jmsDAO.existProcesoEnEjecucion("P", dtoPago.getIdTrx())) {
                             
                             jmsDAO.guardaEjecucion(nombreServer, "P", mensajeJMS, dtoPago.getIdTrx());        
                              //IDENTIFICACION
                              
                              Integer idTrx = new Integer(""+dtoPago.getIdTrx());
                              boolean yaUsado = false;
                              logger.info("(JMS) Procesando nuevo mensaje:" + msg.getJMSMessageID() +" en base de datos...");
                              Transacciones trx = DAOFactory.getPersistenciaGeneralDao().findTransaccionById(idTrx);
                              if (trx != null) {
                              
                                  //VERIFICA QUE NO ESTE USADO
                                  if (trx.getTrxUsadaporMDB() != null) {
                                      logger.info("(JMS) Transaccion con uso antererior detectado verificado si es reintento de mensaje JMS...");
                                     //VERIFICA QUE LOS ID SEAN INGUALES SIGNIFICARIA QUES ES UN REINTENTO
                                     if( trx.getTrxUsadaporMDB().trim().equalsIgnoreCase(""+msg.getJMSMessageID())) {
                                        logger.info("(JMS) Es un reintento verificando si el pago de caja cuenta con folio");
                                        if (trx.getFolioCaja() != null && trx.getFolioCaja().longValue() > 0) {
                                            logger.info("(JMS) Pago ya posee folio de caja nº :" + trx.getFolioCaja()+ ", imposible enviar nuevamente a pago!");
                                            yaUsado = true;
                                        }
                                     } else {
                                        logger.info("(JMS) Ya esta usado por otro mensaje ('" + trx.getTrxUsadaporMDB() + "') la transaccion, el actual es :" + msg.getJMSMessageID());
                                        yaUsado = true;
                                     }
                                  }
                                 
                                  //PASA
                                  if (yaUsado == false) {
                                      //MARCAR
                                      logger.info("(JMS) Procesando Transaccion de Pago : " + dtoPago.getIdTrx());
                                      DAOFactory.getPersistenciaGeneralDao().updateTransaccion(idTrx, ""+msg.getJMSMessageID());
                                      logger.info("(JMS) Macarado para uso de MDB : " + dtoPago.getIdTrx());
                                      GenerarPagoThread pago = new GenerarPagoThread(dtoPago); //TODO:OJO YA NO ESTRA THREAD FUE EN SU MOMENTO PERO NO LO DEBE SER
                                      logger.info("(JMS) Pagando en Caja... : " + dtoPago.getIdTrx());
                                      pago.pagar();
                                      logger.info("(JMS) Transaccion de pago realizado."); 
                                  } else {
                                      logger.info("(JMS) Ya existe Mensaje JMS en curso, no se procesara este mensaje en caja."); 
                                  }
                              } else {
                                  logger.info("(JMS) Numero de transaccion no se encontro en la base de datos"); 
                              }
                         }
                         jmsDAO.actualizaEjecucion(nombreServer, "P", "F", "Finaliza OK");
                     }
                      
                  } catch (JMSException e) {
                      logger.error(e);
                      jmsDAO.actualizaEjecucion(nombreServer, "P", "E", e.getMessage());
                  } 
          }
          logger.info("(JMS) Mensaje asincrono terminado de procesar.");
      }
      
      
      @PreDestroy
      public void remove() {
          logger.info("(JMS) MDB asincrono de pago terminado.");
      }
}
