package cl.bice.vida.botonpago.core2.escritor.config;

import cl.bice.vida.botonpago.core2.vo.FieldStructureVo;
import cl.bice.vida.botonpago.core2.vo.FileStructureVo;

import java.io.File;

import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class WriterConfigCore {

    public static Hashtable config;


    /**
     * Lee Configuracion
     * de procesamiento de archivos
     */
    public static void loadConfig(String fileXml, boolean isfile) {
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
                        campo.setIndexadd(x);
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
        String nombre      = "";
        String tipo        = "";
        String descripcion = "";
        String delimitador = null;
        String tagrow      = null;
        String download    = "false";
        String nombrecamp  = "true";
        String filaini     = "0";

        if (atributos.getNamedItem("download") != null ) download = atributos.getNamedItem("download").getNodeValue();
        if (atributos.getNamedItem("nombre") != null ) nombre = atributos.getNamedItem("nombre").getNodeValue();
        if (atributos.getNamedItem("tipo") != null ) tipo = atributos.getNamedItem("tipo").getNodeValue();
        if (atributos.getNamedItem("delimitador") != null ) delimitador = atributos.getNamedItem("delimitador").getNodeValue();
        if (atributos.getNamedItem("tagRow") != null ) tagrow = atributos.getNamedItem("tagRow").getNodeValue();
        if (atributos.getNamedItem("descripcion") != null ) descripcion = atributos.getNamedItem("descripcion").getNodeValue();
        if (atributos.getNamedItem("filainicio") != null ) filaini = atributos.getNamedItem("filainicio").getNodeValue();
        if (atributos.getNamedItem("nombrecampos") != null ) nombrecamp = atributos.getNamedItem("nombrecampos").getNodeValue();

        //Setea Archivo
        FileStructureVo fileStruct = new FileStructureVo();
        fileStruct.setDownload(Boolean.parseBoolean(download));
        fileStruct.setNombre(nombre);
        fileStruct.setTipoArchivo(tipo);
        fileStruct.setDelimitador(delimitador);
        fileStruct.setTagRow(tagrow);
        fileStruct.setDescripcion(descripcion);
        fileStruct.setFilaInicio(new Integer(filaini));
        fileStruct.setUsoNombresCampos(new Boolean(nombrecamp));
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
        String decimales = "0";
        String sepdecimales = null;
        String rellenarDerecha = null;
        String rellenarIzquierda = null;

        if (atributos.getNamedItem("id") != null ) id = atributos.getNamedItem("id").getNodeValue();
        if (atributos.getNamedItem("largo") != null ) largo = atributos.getNamedItem("largo").getNodeValue();
        if (atributos.getNamedItem("inicio") != null ) inicio = atributos.getNamedItem("inicio").getNodeValue();
        if (atributos.getNamedItem("type") != null ) type = atributos.getNamedItem("type").getNodeValue();
        if (atributos.getNamedItem("tagName") != null ) tagName = atributos.getNamedItem("tagName").getNodeValue();
        if (atributos.getNamedItem("formato") != null ) formato = atributos.getNamedItem("formato").getNodeValue();
        if (atributos.getNamedItem("indexTab") != null ) index = atributos.getNamedItem("indexTab").getNodeValue();
        if (atributos.getNamedItem("subarchivo") != null ) substructura = atributos.getNamedItem("subarchivo").getNodeValue();
        if (atributos.getNamedItem("columna") != null ) columna = atributos.getNamedItem("columna").getNodeValue();
        if (atributos.getNamedItem("decimales") != null ) decimales = atributos.getNamedItem("decimales").getNodeValue();
        if (atributos.getNamedItem("sepdecimales") != null ) sepdecimales = atributos.getNamedItem("sepdecimales").getNodeValue();
        if (atributos.getNamedItem("rellenarDerecha") != null ) rellenarDerecha = atributos.getNamedItem("rellenarDerecha").getNodeValue();
        if (atributos.getNamedItem("rellenarIzquierda") != null ) rellenarIzquierda = atributos.getNamedItem("rellenarIzquierda").getNodeValue();
        if (atributos.getNamedItem("simulate") != null ) {
            simulate = "true";
            valueVarSimulate = atributos.getNamedItem("simulate").getNodeValue();
        }
        if (atributos.getNamedItem("hardvalue") != null ) hardvalue = atributos.getNamedItem("hardvalue").getNodeValue();

        FieldStructureVo field = new FieldStructureVo();
        //LECTOR - ESCRITOR
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

        //ESCRITOR
        field.setNumdecimales(Integer.parseInt(decimales));
        field.setSeparadorDecimal(sepdecimales);
        field.setRellenarDerecha(rellenarDerecha);
        field.setRellenarIzquierda(rellenarIzquierda);

        return field;
    }

    /**
     * Testde prueba
     * @param argv
     */
    public static void main(String argv[]) {
        WriterConfigCore test = new WriterConfigCore();
        test.loadConfig("D:\\Java\\wks\\jdeveloper\\botonlast\\src\\webpagobice\\public_html\\WEB-INF\\config\\cuadraturas.xml", true);
    }
}


