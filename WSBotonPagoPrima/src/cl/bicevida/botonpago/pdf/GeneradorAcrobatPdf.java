package cl.bicevida.botonpago.pdf;

import cl.bicevida.botonpago.util.FechaUtil;
import cl.bicevida.botonpago.util.ImagenesUtil;
import cl.bicevida.botonpago.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import java.net.URL;

import java.util.Date;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.messaging.MessageHandler;
import org.apache.fop.fonts.apps.TTFReader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class GeneradorAcrobatPdf {
    /**
     * Logger for this class
     */
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GeneradorAcrobatPdf.class);

    private File xslt = null;
    private File diroutput = null;
    private String filenamepdfinitial = null;
    private String xml = null;
    private File xmlFile = null;
    private String pdfAbsolutePath = null;
    private String pdfFilename = null;
    private String contextPathImages = null;

    
    /**
     * General el PDF
     * @return
     */
    public boolean generatePDF(boolean incluyetimbre) {
        logger.info("generatePDF(boolean incluyetimbre=" + incluyetimbre + ") - iniciando");
        boolean resultado = false;
        File outdir = getDiroutput();
        if (!outdir.exists()) outdir.mkdirs();
        String idgen = ""+System.currentTimeMillis();
        Date timbreFecha = new Date();        
        String nametimbre = "timbre" + FechaUtil.getFechaFormateoNumeros(timbreFecha) + ".png";
        File pngfile = new File(getContextPathImages(), nametimbre);
        File jpgfile = new File(getContextPathImages(), "transaccion_pagada_90grados.jpg");
        File xmlfile = new File(outdir, getFilenamepdfinitial()+idgen+".xml");
        File fofile  = new File(outdir, getFilenamepdfinitial()+idgen+".fo");
        File pdffile = new File(outdir, getFilenamepdfinitial()+idgen+".pdf");
                
        try {
            //Reemplaza todas las imagenes por un Path de Contexto
            String xslt = FileUtils.readFileToString(this.getXslt());
            xslt = StringUtil.replaceString(xslt,"url(","url("+getContextPathImages());
            
            //Reemplazamos el Timbre
            if (incluyetimbre) {
                ImagenesUtil.generarTimbrePagado(FechaUtil.getFechaFormateoStandar(timbreFecha),"PNG", new URL("file:"+jpgfile.getAbsolutePath()), pngfile.getAbsolutePath());
                xslt = StringUtil.replaceString(xslt,"${timbre}", nametimbre);             
            }
            
            //Crea el archivo XML  
            if (getXmlFile()==null) {
                FileUtils.writeStringToFile(xmlfile, getXml());
            } else {
                xmlfile = getXmlFile(); 
            }
            
            //General de XML+XSL ==> XLS-FO
            convertXML2FO(xmlfile, xslt , fofile);
            
            //General de XSL-FO ==> PDF
            convertFO2PDF(fofile, pdffile);
            
            //Eliminar
            if (getXmlFile()==null) xmlfile.delete();
            //if (pngfile.exists()) pngfile.delete();
            fofile.delete();
            
             
            //Setea Ruta de PDF
            setPdfAbsolutePath(pdffile.getAbsolutePath());
            setPdfFilename(getFilenamepdfinitial()+idgen+".pdf");
            resultado = true;
        } catch (Exception e) {
            logger.error("generatePDF(boolean) - catch (error)", e);
            setPdfAbsolutePath(null);
            setPdfFilename(null);
            e.printStackTrace();
        }
        logger.info("generatePDF(boolean incluyetimbre=" + incluyetimbre + ") - termina");
        return resultado;
    }
    
    
    /**
     * Converts an FO file to a PDF file using FOP
     * @param fo the FO file
     * @param pdf the target PDF file
     * @throws FactoryConfigurationError In case of a problem with the JAXP factory configuration
     * @throws ParserConfigurationException In case of a problem with the parser configuration
     * @throws SAXException In case of a problem during XML processing
     * @throws IOException In case of an I/O problem
     */
    public void convertFO2PDF(File fo, File pdf) throws IOException, FOPException  {
	logger.info("convertFO2PDF() - iniciando");
       
        //Construct driver
        Driver driver = new Driver();
        
        //Setup logger
        Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
        driver.setLogger(logger);
        MessageHandler.setScreenLogger(logger);
    
        //Setup Renderer (output format)        
        driver.setRenderer(Driver.RENDER_PDF);
        
        //Setup output
        OutputStream out = new java.io.FileOutputStream(pdf);
        try {
            driver.setOutputStream(out);

            //Setup input
            InputStream in = new java.io.FileInputStream(fo);
            try {
                driver.setInputSource(new InputSource(in));
            
                //Process FO
                driver.run();
            } finally {
                in.close();
            }
        } finally {
            out.close();
        }               
	logger.info("convertFO2PDF() - termina");
    }

    
    
    /**
     * Converts an XML file to an XSL-FO file using JAXP (XSLT).
     * @param xml the XML file
     * @param xslt the stylesheet file
     * @param fo the target XSL-FO file
     * @throws IOException In case of an I/O problem
     * @throws TransformerException In case of a XSL transformation problem
     */
    public void convertXML2FO(File xml, String xslt, File fo) throws IOException, TransformerException {
        logger.info("convertXML2FO() - iniciando");
        OutputStream out = new java.io.FileOutputStream(fo);
        try {
            //Setup XSLT
            //TTFReader.main(new String[] {"/var/root/java/wks10g/bicevida/2013/LastBotonPago/src/webpagobicepublico/public_html/font/arial.ttf","/var/root/java/wks10g/bicevida/2013/LastBotonPago/src/webpagobicepublico/public_html/font/arial.xml"});
             
            //Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(new StringReader(xslt)));
            Source src = new StreamSource(xml);
            Result res = new StreamResult(out);
            transformer.transform(src, res);
        } finally {
        out.close();
        }
        logger.info("convertXML2FO() - termina");
    }    

    public void setXslt(File xslt) {
        this.xslt = xslt;
    }

    public File getXslt() {
        return xslt;
    }

    public void setDiroutput(File diroutput) {
        this.diroutput = diroutput;
    }

    public File getDiroutput() {
        return diroutput;
    }

    public void setFilenamepdfinitial(String filenamepdfinitial) {
        this.filenamepdfinitial = filenamepdfinitial;
    }

    public String getFilenamepdfinitial() {
        return filenamepdfinitial;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getXml() {
        return xml;
    }

    public void setPdfAbsolutePath(String pdfAbsolutePath) {
        this.pdfAbsolutePath = pdfAbsolutePath;
    }

    public String getPdfAbsolutePath() {
        return pdfAbsolutePath;
    }

    public void setPdfFilename(String pdfFilename) {
        this.pdfFilename = pdfFilename;
    }

    public String getPdfFilename() {
        return pdfFilename;
    }

    public void setContextPathImages(String contextPathImages) {
        this.contextPathImages = contextPathImages;
    }

    public String getContextPathImages() {
        return contextPathImages;
    }
    
    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
   }

    public File getXmlFile() {
        return xmlFile;
    }
    
    /**
     * Main de pruebas
     * @param args
     */
    public static void main(String[] args) {
        GeneradorAcrobatPdf pdf = new GeneradorAcrobatPdf();
        String pathroot = "/var/root/java/wksJDev10.3.5/BotonPago/src/wpagobicepublico/public_html";
        pdf.setXslt(new File(pathroot, "xslt" + File.separator + "comprobante-pagado-fo.xsl"));
        //pdf.setXslt(new File(pathroot, "xslt" + File.separator + "comprobante-enespera-fo.xsl"));
        pdf.setXmlFile(new File(pathroot, "xml" + File.separator + "comprobante.xml"));        
        pdf.setDiroutput(new File(pathroot , "pdf"));
        pdf.setFilenamepdfinitial("testpagado");
        pdf.setContextPathImages(pathroot+"/images/styles/");
        if ( pdf.generatePDF(true)) {
            System.out.println("PDF Generado :" + pdf.getPdfFilename());
        }
    }

 
}
