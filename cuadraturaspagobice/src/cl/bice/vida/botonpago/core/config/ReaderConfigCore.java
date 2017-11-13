package cl.bice.vida.botonpago.core.config;

import cl.bice.vida.botonpago.core.vo.EmpresaVO;
import cl.bice.vida.botonpago.core.vo.FieldStructureVo;
import cl.bice.vida.botonpago.core.vo.FileStructureVo;

import cl.bice.vida.botonpago.core.vo.MedioPagoVO;

import java.io.File;

import java.util.Hashtable;

import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ReaderConfigCore {

    public static Hashtable config;
    public static Hashtable empresas;
    
    
    /**
     * Lee Configuracion
     * de procesamiento de archivos
     */
    public static void loadConfig(String fileXml) {
     try {
             //Si ya esta cargo se devuelve
             config = new Hashtable();
             empresas = new Hashtable();
             
             //lee archivo
             File file = new File(fileXml);
             DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
             DocumentBuilder db = dbf.newDocumentBuilder();
             Document doc = db.parse(file);
             doc.getDocumentElement().normalize();

            //Empresas             
             NodeList nodeEmp = doc.getElementsByTagName("empresa");            
             for (int s = 0; s < nodeEmp.getLength(); s++) {
                 Node fstNode = nodeEmp.item(s);
                 NamedNodeMap atributos =  fstNode.getAttributes();
                 EmpresaVO emp = getAtributesEmpresa(atributos);              
                 //Recupera los campos con sus atributos
                 if (fstNode.getNodeType() == Node.ELEMENT_NODE) {    
                     Element fstElmnt = (Element) fstNode;
                     NodeList camposLts = fstElmnt.getElementsByTagName("comercio");
                     for (int x = 0; x < camposLts.getLength(); x++) {
                         Node campoNode = camposLts.item(x);
                         NamedNodeMap atrNode =  campoNode.getAttributes();
                         MedioPagoVO campo = getAtributesComercioNode(atrNode);
                         emp.addField(campo.getArchivoId(), campo);
                     }                 
                     //Agrega Configuracion de Archivo
                     empresas.put(emp.getCodigo(), emp);                     
                 }                    
             }
             
             
             //Archivos
             NodeList nodeLst = doc.getElementsByTagName("archivo");        
             for (int s = 0; s < nodeLst.getLength(); s++) {
                Node fstNode = nodeLst.item(s);
                NamedNodeMap atributos =  fstNode.getAttributes();
                FileStructureVo fileStruct = getAtributesFileNode(atributos);              
                //Recupera los campos con sus atributos
                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {      
                    Element fstElmnt = (Element) fstNode;
                    NodeList camposLts = fstElmnt.getElementsByTagName("campo");
                    for (int x = 0; x < camposLts.getLength(); x++) {
                        Node campoNode = camposLts.item(x);
                        NamedNodeMap atrNode =  campoNode.getAttributes();
                        FieldStructureVo campo = getAtributesNode(atrNode);
                        fileStruct.addField(campo.getId(), campo);
                    }
                   //Agrega Configuracion de Archivo
                   config.put(fileStruct.getNombre(), fileStruct);                    
               }        
             }
         } catch (Exception e) {
           e.printStackTrace();
         }
    }    
    
    /**
     * Recupera los atributos de la empresa
     * @param atributos
     * @return
     */
    private static EmpresaVO getAtributesEmpresa(NamedNodeMap atributos) {
        String databasecode = "0";
        String code         = "";
        String descripcion  = "";
        
        if (atributos.getNamedItem("databasecode") != null ) databasecode = atributos.getNamedItem("databasecode").getNodeValue();
        if (atributos.getNamedItem("code") != null ) code = atributos.getNamedItem("code").getNodeValue();
        if (atributos.getNamedItem("descripcion") != null ) descripcion = atributos.getNamedItem("descripcion").getNodeValue();

        //Setea Archivo
        EmpresaVO fileStruct = new EmpresaVO();
        fileStruct.setCodigo(code);
        fileStruct.setDescripcion(descripcion);               
        fileStruct.setIdDataBase(new Integer(databasecode));
        return fileStruct;
    }    
    
    /**
     * Recupera los atributos del nodo padre
     * @param atributos
     * @return
     */
    private static FileStructureVo getAtributesFileNode(NamedNodeMap atributos) {
        String databasecode = "0";
        String nombre      = "";
        String tipo        = "";
        String descripcion = "";
        String delimitador = null;
        String tagrow      = null;
        String finaInicio  = "1";
        String usoUltimoRg = "true";
        String upload      = "false"; 
        String estadovalue = null;
        
        if (atributos.getNamedItem("upload") != null ) upload = atributos.getNamedItem("upload").getNodeValue();
        if (atributos.getNamedItem("databasecode") != null ) databasecode = atributos.getNamedItem("databasecode").getNodeValue();
        if (atributos.getNamedItem("nombre") != null ) nombre = atributos.getNamedItem("nombre").getNodeValue();
        if (atributos.getNamedItem("tipo") != null ) tipo = atributos.getNamedItem("tipo").getNodeValue();
        if (atributos.getNamedItem("delimitador") != null ) delimitador = atributos.getNamedItem("delimitador").getNodeValue();
        if (atributos.getNamedItem("tagRow") != null ) tagrow = atributos.getNamedItem("tagRow").getNodeValue();
        if (atributos.getNamedItem("descripcion") != null ) descripcion = atributos.getNamedItem("descripcion").getNodeValue();
        if (atributos.getNamedItem("registroInicio") != null ) finaInicio = atributos.getNamedItem("registroInicio").getNodeValue();
        if (atributos.getNamedItem("usarUltimoRegistro") != null ) usoUltimoRg = atributos.getNamedItem("usarUltimoRegistro").getNodeValue();
        if (atributos.getNamedItem("estadovalue") != null ) estadovalue = atributos.getNamedItem("estadovalue").getNodeValue();

        //Setea Archivo
        FileStructureVo fileStruct = new FileStructureVo();
        fileStruct.setDatabasecode(new Integer(databasecode));
        fileStruct.setUpload(Boolean.parseBoolean(upload));
        fileStruct.setNombre(nombre);
        fileStruct.setTipoArchivo(tipo);               
        fileStruct.setDelimitador(delimitador);
        fileStruct.setTagRow(tagrow);        
        fileStruct.setDescripcion(descripcion);
        fileStruct.setFilaInicio(Integer.parseInt(finaInicio));
        fileStruct.setUsoUltimoRegistro(Boolean.parseBoolean(usoUltimoRg));
        fileStruct.setEstadoOkValue(estadovalue);
        return fileStruct;
    }
    
    /**
     * Recupera los atributos de un nodo
     * @param nodo
     * @return
     */
    private static FieldStructureVo getAtributesNode(NamedNodeMap atributos) {
        //Setea Structura
        String id = null;
        String largo = "0";
        String inicio = "0";
        String type = null;                       
        String tagName = null;                           
        String formato = null;    
        String index = "0";
        String substructura = null;
        String decimales = "true";
            
        if (atributos.getNamedItem("id") != null ) id = atributos.getNamedItem("id").getNodeValue();
        if (atributos.getNamedItem("largo") != null ) largo = atributos.getNamedItem("largo").getNodeValue();
        if (atributos.getNamedItem("inicio") != null ) inicio = atributos.getNamedItem("inicio").getNodeValue();
        if (atributos.getNamedItem("type") != null ) type = atributos.getNamedItem("type").getNodeValue();                       
        if (atributos.getNamedItem("tagName") != null ) tagName = atributos.getNamedItem("tagName").getNodeValue();            
        if (atributos.getNamedItem("formato") != null ) formato = atributos.getNamedItem("formato").getNodeValue();            
        if (atributos.getNamedItem("indexTab") != null ) index = atributos.getNamedItem("indexTab").getNodeValue();         
        if (atributos.getNamedItem("subarchivo") != null ) substructura = atributos.getNamedItem("subarchivo").getNodeValue();  
        if (atributos.getNamedItem("decimales") != null ) decimales = atributos.getNamedItem("decimales").getNodeValue();         
        
        FieldStructureVo field = new FieldStructureVo();
        field.setId(id);
        field.setLargo(Integer.parseInt(largo));
        field.setNombre(id);
        field.setPosicionInicial(Integer.parseInt(inicio));
        field.setTagNombre(tagName);
        field.setTipo(type);     
        field.setFormato(formato);
        field.setIndexTab(Integer.parseInt(index));
        field.setSubstructura(substructura);
        field.setDecimales(new Boolean(decimales));
        return field;
    }
    
    /**
     * Recupera los atributos de un nodo
     * @param nodo
     * @return
     */
    private static MedioPagoVO getAtributesComercioNode(NamedNodeMap atributos) {
        //Setea Structura
        String idcomercio = "";
        String archivo = "";
            
        if (atributos.getNamedItem("archivo") != null ) archivo = atributos.getNamedItem("archivo").getNodeValue();
        if (atributos.getNamedItem("idcomercio") != null ) idcomercio = atributos.getNamedItem("idcomercio").getNodeValue();
        
        MedioPagoVO field = new MedioPagoVO();
        field.setArchivoId(archivo);
        field.setIdComercio(idcomercio);
        return field;
    }
    
    /**
     * Testde prueba
     * @param argv
     */
    public static void main(String argv[]) {
        ReaderConfigCore test = new ReaderConfigCore();
        test.loadConfig("D:\\Java\\wks\\jdeveloper\\botonlast\\src\\webpagobice\\public_html\\WEB-INF\\config\\cuadraturas.xml");
    }    
}
