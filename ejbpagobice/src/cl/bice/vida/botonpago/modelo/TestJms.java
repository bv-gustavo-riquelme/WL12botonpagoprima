package cl.bice.vida.botonpago.modelo;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestJms {
    
        public final static String WEBLOGIC_JNDI_FACTORY_NAME = "weblogic.jndi.WLInitialContextFactory"; // Weblogic JNDI
        private static final String CONNECTION_FACTORY_JNDI_NAME = "jms/BotonPagoTopicFactory"; // Weblogic ConnectionFactory JNDI
        private static final String QUEUE_JNDI_NAME = "jms/BotonPagoTopic"; // Weblogic Queue JNDI
     
     
        //parametros desarrollo
        //private static final String WEBLOGIC_JMS_URL = "t3://desa-wlogic01:9151"; // Weblogic JMS URL
        //private static final String SECURITY_PRINCIPAL = "jose.arias";
        //private static final String SECURITY_CREDENTIALS = "bicevida1";
        
        //parametros locales
        private static final String WEBLOGIC_JMS_URL = "t3://localhost:7101"; // Weblogic JMS URL
        private static final String SECURITY_PRINCIPAL = "weblogic";
        private static final String SECURITY_CREDENTIALS = "weblogic1";
     
        // variables 
        private Hashtable<String, String> wlsEnvParamHashTbl = null;
        private static InitialContext initialContext = null;
        private static QueueConnectionFactory queueConnectionFactory = null;
        private static QueueConnection queueConnection = null;
        private static QueueSession queueSession = null;
        private static Queue queue = null;
        private static QueueSender queueSender = null;
        private static TextMessage textMessage = null;
        private static ObjectMessage objectMessage = null;
     
        /**
         * default constructor
         */
        public TestJms() {
     
            wlsEnvParamHashTbl = new Hashtable<String, String>();
            wlsEnvParamHashTbl.put(Context.PROVIDER_URL, WEBLOGIC_JMS_URL); // set Weblogic JMS URL
            wlsEnvParamHashTbl.put(Context.INITIAL_CONTEXT_FACTORY, WEBLOGIC_JNDI_FACTORY_NAME); // set Weblogic JNDI
            wlsEnvParamHashTbl.put(Context.SECURITY_PRINCIPAL, SECURITY_PRINCIPAL); // set Weblogic UserName
            wlsEnvParamHashTbl.put(Context.SECURITY_CREDENTIALS,SECURITY_CREDENTIALS ); // set Weblogic PassWord
        }
     
        /**
         * This method initializes all necessary connection parameters for establishing JMS Queue communication
         * 
         * @throws NamingException
         * @throws JMSException
         */
        public void initializeConnParams() throws NamingException, JMSException {
     
            initialContext = new InitialContext(wlsEnvParamHashTbl); // set InitialContext 
            queueConnectionFactory = (QueueConnectionFactory) initialContext.lookup(CONNECTION_FACTORY_JNDI_NAME); // lookup using initial context
            queueConnection = queueConnectionFactory.createQueueConnection(); // create ConnectionFactory
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE); // create QueueSession
            queue = (Queue) initialContext.lookup(QUEUE_JNDI_NAME); // lookup Queue JNDI using initial context created above
            queueSender = queueSession.createSender(queue); // create Sender using Queue JNDI as arguments
            textMessage = queueSession.createTextMessage(); // create TextMessage for Queue
            objectMessage = queueSession.createObjectMessage();
            queueConnection.start(); // start Queue connection
        }
     
        /**
         * This is the actual method to SEND messages to JMS Queues
         * 
         * @param message
         * @throws JMSException
         */
        public void send(String message) throws JMSException {
     
            textMessage.setText(message);
            queueSender.send(textMessage);
        }
        
        public void sendObject(String message) throws JMSException {
        
            objectMessage.setObject(message);
            queueSender.send(objectMessage);
        }
     
        /**
         * This method closes all connections
         * 
         * @throws JMSException
         */
        public void closeConnParams() throws JMSException {
     
            queueSender.close();
            queueSession.close();
            queueConnection.close();
        }
     
        /**
         * This is main() method to initiate all necessary calls
         * 
         * @param args
         */
        public static void main(String[] args) {
     
            // create an object to invoke initialize(), send() and close() methods
            TestJms jmsQueueProducer = new TestJms();
            String enqueueMessage = "JMS Queue testing";
     
            try {
                jmsQueueProducer.initializeConnParams(); // invokes initialize method to all necessary connections
                jmsQueueProducer.sendObject(enqueueMessage); // invokes send method with actual message
                
                jmsQueueProducer.closeConnParams(); // invokes to close all connections
     
                System.out.println("JMSQueueProducer: Text message \"" + enqueueMessage + "\" enqueued to JMS Queue 'TestQueue' successfully ");
            } 
            catch (NamingException nex) {
                nex.printStackTrace();
            } 
            catch (JMSException jmsex) {
                jmsex.printStackTrace();
            }
        }
}
