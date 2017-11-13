package cl.bice.vida.botonpago.core2.lector.config;

import cl.bice.vida.botonpago.core2.vo.EntidadEstructuraVO;
import cl.bice.vida.botonpago.core2.vo.FieldDescripcionVO;
import cl.bice.vida.botonpago.core2.vo.FieldStructureVo;
import cl.bice.vida.botonpago.core2.vo.FileStructureVo;

import java.io.File;

import java.util.Hashtable;

import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ReaderConfigCore {

    public static Hashtable config;
    
    /**
     * Carga base sin definiciones
     * @param fileXml
     * @param isfile
     */
    public static void loadConfig(String fileXml, boolean isfile) {
        loadConfig(fileXml, isfile, null);
    }
    
    
    /**
     * Lee Configuracion
     * de procesamiento de archivos
     */
    public static void loadConfig(String fileXml, boolean isfile, List<FieldDescripcionVO> descripcionesAdicionalesCampos) {
     try {
             //Si ya esta cargo se devuelve
             config = new Hashtable();
                     
             //lee archivo
             File file = new File(fileXml);
             DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
             DocumentBuilder db = dbf.newDocumentBuilder();
             Document doc;

             //DETERMINA SI EL PARSEO ES POR ARCHIVO O UN STRING DE TIPO XML
             if (isfile) {
                doc = db.parse(file);
             } else {
                 org.xml.sax.InputSource inStream = new org.xml.sax.InputSource();
                 inStream.setCharacterStream(new java.io.StringReader(fileXml));
                 doc = db.parse(inStream);
             }
             doc.getDocumentElement().normalize();
             
             //En caso de existir definiciones adicionales las carga
             Hashtable descrip = null;
             if (descripcionesAdicionalesCampos != null && descripcionesAdicionalesCampos.size() > 0) {
                     descrip = new Hashtable();
                     Iterator items = descripcionesAdicionalesCampos.iterator();
                     while (items.hasNext()) {
                         FieldDescripcionVO vo = (FieldDescripcionVO)items.next();
                         descrip.put(vo.getKey(), vo);
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
                        //BUSCA DESCRIPCION ADICIONAL AL CAMPO
                        if (descrip != null) {
                            FieldDescripcionVO voDes = (FieldDescripcionVO) descrip.get(campo.getId());
                            if (voDes != null) campo.setDescripcionCampo(voDes.getDescripcion());
                        }
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
        
        if (atributos.getNamedItem("upload") != null ) upload = atributos.getNamedItem("upload").getNodeValue();
        if (atributos.getNamedItem("databasecode") != null ) databasecode = atributos.getNamedItem("databasecode").getNodeValue();
        if (atributos.getNamedItem("nombre") != null ) nombre = atributos.getNamedItem("nombre").getNodeValue();
        if (atributos.getNamedItem("tipo") != null ) tipo = atributos.getNamedItem("tipo").getNodeValue();
        if (atributos.getNamedItem("delimitador") != null ) delimitador = atributos.getNamedItem("delimitador").getNodeValue();
        if (atributos.getNamedItem("tagRow") != null ) tagrow = atributos.getNamedItem("tagRow").getNodeValue();
        if (atributos.getNamedItem("descripcion") != null ) descripcion = atributos.getNamedItem("descripcion").getNodeValue();
        if (atributos.getNamedItem("registroInicio") != null ) finaInicio = atributos.getNamedItem("registroInicio").getNodeValue();
        if (atributos.getNamedItem("usarUltimoRegistro") != null ) usoUltimoRg = atributos.getNamedItem("usarUltimoRegistro").getNodeValue();


        //Setea Archivo
        FileStructureVo fileStruct = new FileStructureVo();
        fileStruct.setDatabasecode(databasecode);
        fileStruct.setUpload(Boolean.parseBoolean(upload));
        fileStruct.setNombre(nombre);
        fileStruct.setTipoArchivo(tipo);               
        fileStruct.setDelimitador(delimitador);
        fileStruct.setTagRow(tagrow);        
        fileStruct.setDescripcion(descripcion);
        fileStruct.setFilaInicio(Integer.parseInt(finaInicio));
        fileStruct.setUsoUltimoRegistro(Boolean.parseBoolean(usoUltimoRg));
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
        String columna = "0";
        String simulate = "false";
        String valueVarSimulate = null;
        String hardvalue = null;
            
        if (atributos.getNamedItem("id") != null ) id = atributos.getNamedItem("id").getNodeValue();
        if (atributos.getNamedItem("largo") != null ) largo = atributos.getNamedItem("largo").getNodeValue();
        if (atributos.getNamedItem("inicio") != null ) inicio = atributos.getNamedItem("inicio").getNodeValue();
        if (atributos.getNamedItem("type") != null ) type = atributos.getNamedItem("type").getNodeValue();                       
        if (atributos.getNamedItem("tagName") != null ) tagName = atributos.getNamedItem("tagName").getNodeValue();            
        if (atributos.getNamedItem("formato") != null ) formato = atributos.getNamedItem("formato").getNodeValue();            
        if (atributos.getNamedItem("indexTab") != null ) index = atributos.getNamedItem("indexTab").getNodeValue();         
        if (atributos.getNamedItem("subarchivo") != null ) substructura = atributos.getNamedItem("subarchivo").getNodeValue();         
        if (atributos.getNamedItem("columna") != null ) columna = atributos.getNamedItem("columna").getNodeValue();         
        if (atributos.getNamedItem("simulate") != null ) {
            simulate = "true";
            valueVarSimulate = atributos.getNamedItem("simulate").getNodeValue();   
        }
        if (atributos.getNamedItem("hardvalue") != null ) hardvalue = atributos.getNamedItem("hardvalue").getNodeValue();
        
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
        field.setColumnaXLS(Integer.parseInt(columna));
        field.setSimulateValue(Boolean.parseBoolean(simulate));
        field.setValueSimulateVar(valueVarSimulate);
        field.setHardvalue(hardvalue);
        return field;
    }
    
    /**
     * Recupera los atributos de un nodo
     * @param nodo
     * @return
     */
    private static EntidadEstructuraVO getAtributesComercioNode(NamedNodeMap atributos) {
        //Setea Structura
        String databasecode = "";
        String archivo = "";
        String descripcion = "";
            
        if (atributos.getNamedItem("archivo") != null ) archivo = atributos.getNamedItem("archivo").getNodeValue();
        if (atributos.getNamedItem("databasecode") != null ) databasecode = atributos.getNamedItem("databasecode").getNodeValue();
        if (atributos.getNamedItem("descripcion") != null ) descripcion = atributos.getNamedItem("descripcion").getNodeValue();
        
        EntidadEstructuraVO field = new EntidadEstructuraVO();
        field.setArchivoId(archivo);
        field.setDatabasecode(databasecode);
        field.setDescripcion(descripcion);
        return field;
    }
    
    /**
     * Testde prueba
     * @param argv
     */
    public static void main(String argv[]) {
        ReaderConfigCore test = new ReaderConfigCore();
        test.loadConfig("D:\\Java\\wks\\jdeveloper\\botonlast\\src\\webpagobice\\public_html\\WEB-INF\\config\\cuadraturas.xml", true);
    }    
}

