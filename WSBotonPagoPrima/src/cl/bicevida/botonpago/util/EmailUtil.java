package cl.bicevida.botonpago.util;


/*import cl.bice.vida.botonpago.servicios.sendmail.SendMailEJBClient;
import cl.bice.vida.botonpago.servicios.sendmail.types.Encabezado;
import cl.bice.vida.botonpago.servicios.sendmail.types.InSendMail;
import cl.bice.vida.botonpago.servicios.sendmail.types.ListOfRegistro;
import cl.bice.vida.botonpago.servicios.sendmail.types.ListOfString;
import cl.bice.vida.botonpago.servicios.sendmail.types.Registro;*/

import cl.bice.vida.botonpago.servicios.sendmail.SendMailEJBClient;
import cl.bice.vida.botonpago.servicios.sendmail.SendMailEJBV1;
import cl.bice.vida.botonpago.servicios.sendmail.SendMailEJBWebService;
import cl.bice.vida.botonpago.servicios.sendmail.types.Encabezado;
import cl.bice.vida.botonpago.servicios.sendmail.types.InSendMail;
import cl.bice.vida.botonpago.servicios.sendmail.types.ListOfRegistro;
import cl.bice.vida.botonpago.servicios.sendmail.types.ListOfString;
import cl.bice.vida.botonpago.servicios.sendmail.types.Registro;

import java.io.File;

import java.net.URL;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;


/**
 *
 * @author bicevida
 *
 */
public class EmailUtil {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(EmailUtil.class);
    
     /**
      * Envia correo electronico via WS
      * @param to
      * @param from
      * @param subject
      * @param mensaje
      * @param atachados
      */
     public static void enviarEmailWS(boolean pagado, String to, 
                                     String from, String cc, 
                                     String cco, String subject, 
                                     String nombrecliente,
                                     String fecha,
                                     String polizas,
                                     String pathArchivosAtachados) {
                 logger.info("enviarEmailWS(String to=" + to + ", String from=" + from + ", String cc=" + cc + ", String cco=" + cco + ", String subject=" + subject + ", String nombrecliente=" + nombrecliente + ", String fecha=" + fecha
                                 + ", String pathArchivosAtachados=" + pathArchivosAtachados + ",String polizas= )" + polizas + " - iniciando");

         
         try {            
             //SendMailEJBClient myPort2 = new SendMailEJBClient();
             URL url = new URL(ResourcePropertiesUtil.getProperty("bice.webservices.client.email.endpoint")+"?wsdl");
             SendMailEJBV1 sendMailEJBV1 = new SendMailEJBV1(url);
             
             Map<String, Object> requestContext = ((BindingProvider)sendMailEJBV1.getSendMailEJB()).getRequestContext();
             requestContext.put("javax.xml.ws.client.connectionTimeout", 300000);
             requestContext.put("javax.xml.ws.client.receiveTimeout", 300000);    
             
             
             //myPort.setEndpoint(ResourcePropertiesUtil.getProperty("bice.webservices.client.email.endpoint"));*/
             
             
             //logger.info("=====> ENDPOINT SERVICIO: "+ myPort.getEndpoint() +" <========");
             //Envio en duro de cuentas de correo
             boolean desarrolloTo = Boolean.parseBoolean(ResourcePropertiesUtil.getProperty("bice.webservices.client.email.send.only.account"));
             if (desarrolloTo) to = ResourcePropertiesUtil.getProperty("bice.webservices.client.email.send.only.account.email");
             //Data para envio
             InSendMail in = new InSendMail();
             Encabezado encabezado = new Encabezado();
             ListOfRegistro reg = new ListOfRegistro();
             ListOfString variables = new ListOfString();
             ListOfString valores = new ListOfString();

             //Encabezado
             if (pagado==false) encabezado.setNombrePlantilla(ResourcePropertiesUtil.getProperty("bice.webservices.client.email.template.enespera"));
             if (pagado==true)  encabezado.setNombrePlantilla(ResourcePropertiesUtil.getProperty("bice.webservices.client.email.template.pagado"));
             
             encabezado.setSubject(MimeUtility.encodeText(subject));
             encabezado.setFrom(from);
             encabezado.setCc(cc);
             encabezado.setCco(cco);
             String[] valoresa = new String[3];
             valoresa[0]="nombrecompleto";
             valoresa[1]="fecha"; 
             valoresa[2]="numeropoliza";   
             variables.getItem().addAll(Arrays.asList(valoresa) );
             encabezado.setVariables(variables);
             

             //Registro
             Registro[] registro = new Registro[1];
             String[] valoresx = new String[3];
             valoresx[0] = StringUtil.replaceBadCharsHtml(nombrecliente);
             valoresx[1]=fecha;
             valoresx[2]=StringUtil.replaceBadCharsHtml(polizas);
             valores.getItem().addAll(Arrays.asList(valoresx) );
             registro[0] = new Registro();
             registro[0].setValores(valores);
             registro[0].setTo(to);
             registro[0].setAttachment(pathArchivosAtachados);            
             reg.getItem().addAll(Arrays.asList(registro) );
             
             //Setea Data
             in.setEncabezado(encabezado);
             in.setRegistros(reg);
             
             //Envia correo
             //myPort.sendMail(in);
             logger.info("in " + in);
             sendMailEJBV1.getSendMailEJB().sendMail(in);
         } catch (Exception e) {
             logger.error("enviarEmailWS(String, String, String, String, String, String, String, String) - catch (error)", e);
             e.printStackTrace();
         }

            logger.info("enviarEmailWS(String to=" + to + ", String from=" + from + ", String cc=" + cc + ", String cco=" + cco + ", String subject=" + subject + ", String nombrecliente=" + nombrecliente + ", String fecha=" + fecha
                + ", String pathArchivosAtachados=" + pathArchivosAtachados + ") - termina");
     }

    /**
     * Metodo que permite enviar un correo electronico, con o sin archivo adjunto
     * Como ejemplo de envio de email, ver main de esta clase.
     * @param filename  Nombre del archivo adjunto (que debe estar en el directorio FILES)
     * @param dto       DTO que contiene los datos a enviar.
     * @return boolean
     */
    public static boolean enviarEmail(String to, String from, String subject, String mensaje, List atachados){
    	logger.info("enviarEmail() - inicio");
        String host = ResourcePropertiesUtil.getProperty("bice.ip_host_sendmail");
        logger.info("enviarEmail() - obteniendo host mail: |"+ host +"|");
        
        /*
         * Confeccion de parametros de envio del email
         */
        logger.info("enviarEmail() - recuperando informacion generica para confeccion de email");

        
        
        /*
         * Confecciona objetos que enviaran el mensaje
         */
        logger.info("enviarEmail() - recuperando propiedades para confeccion de mensaje smtp");
        Properties props = System.getProperties();
        String smtp = ResourcePropertiesUtil.getProperty("bice.smtp_host");
        logger.info("enviarEmail() - obteniendo smtp mail: |"+ smtp +"|");
        props.put(smtp, host);
        Session session = Session.getInstance(props, null);
        try {
        	logger.info("enviarEmail() - confeccionado cuerpo del mensaje");
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = {new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			MimeBodyPart mbpbody = new MimeBodyPart();
			mbpbody.setContent(mensaje, "text/html; charset=iso-8859-1");
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbpbody);
			
			/*
			 * Itera los archivos adjuntados y los adjunta al envio del email
			 */
			logger.info("enviarEmail() - iterando archivos a atachar en evio");
			Iterator files = atachados.iterator();
			while (files.hasNext()) {
				String file = (String) files.next();
			    File archivo=new File(file);
			    if (archivo.exists()) {	               
			    	logger.info("enviarEmail() - atachando archivo :" + archivo.getName());
			        MimeBodyPart mbpfileattach = new MimeBodyPart();
			        FileDataSource fds = new FileDataSource(file);
			        mbpfileattach.setDataHandler(new DataHandler(fds));
			        mbpfileattach.setFileName(fds.getName());
			        mp.addBodyPart(mbpfileattach);
			    }  
			}
			logger.info("enviarEmail() - enviando contenido del email");
			logger.info("enviarEmail() - to: "+to);
			logger.info("enviarEmail() - from: "+from);
			logger.info("enviarEmail() - subject: "+subject);
			logger.info("enviarEmail() - mensaje: "+mensaje);
			logger.info("enviarEmail() - host: "+host);
			logger.info("enviarEmail() - smtp: "+smtp);
			logger.info("enviarEmail() - address: "+address);
			logger.info("enviarEmail() - msg: "+msg);
			
			msg.setContent(mp);   
			msg.setSentDate(new Date());
			
			
			
			Transport.send(msg);
			
		} catch (AddressException e) {
			logger.error("enviarEmail() - AddressException ocurrida:"+e.getMessage(), e);
		} catch (MessagingException e) {
			logger.error("enviarEmail() - MessagingException ocurrida:"+e.getMessage(), e);
		}
		logger.info("enviarEmail() - termino");
        return true;
    }
    
    /**
    * Validate the form of an email address.
    *
    * <P>Return <tt>true</tt> only if 
    *<ul> 
    * <li> <tt>aEmailAddress</tt> can successfully construct an 
    * {@link javax.mail.internet.InternetAddress} 
    * <li> when parsed with "@" as delimiter, <tt>aEmailAddress</tt> contains 
    * two tokens which satisfy {@link hirondelle.web4j.util.Util#textHasContent}.
    *</ul>
    *
    *<P> The second condition arises since local email addresses, simply of the form
    * "<tt>albert</tt>", for example, are valid for 
    * {@link javax.mail.internet.InternetAddress}, but almost always undesired.
    */
    public static boolean isValidEmailAddress(String aEmailAddress){
      if (aEmailAddress == null) return false;
      boolean result = true;
      try {
        InternetAddress emailAddr = new InternetAddress(aEmailAddress);
        if ( ! hasNameAndDomain(aEmailAddress) ) {
          result = false;
        }
      }
      catch (AddressException ex){
        result = false;
      }
      return result;
    }

    private static boolean hasNameAndDomain(String aEmailAddress){
      String[] tokens = aEmailAddress.split("@");
      return  tokens.length == 2?true:false ;
    }

}
