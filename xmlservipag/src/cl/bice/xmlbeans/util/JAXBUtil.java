package cl.bice.xmlbeans.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

public class JAXBUtil {
    private static final Logger LOGGER = Logger.getLogger(JAXBUtil.class);
    
    private JAXBUtil() {
        
    }

    public static String convertObjectTOXmlString(Object clazz) {
        String xml = "";
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { clazz.getClass() });
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(clazz, sw);
            xml = sw.toString();
            LOGGER.info("Object to XML -> " + xml);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return xml;
    }
}
