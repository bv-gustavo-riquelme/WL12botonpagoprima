package cl.bice.vida.botonpago.core2.vo;
import cl.bice.vida.botonpago.core2.lector.config.ReaderConfigCore;

import cl.bice.vida.botonpago.utils.FechaUtil;
import cl.bice.vida.botonpago.utils.StringUtil;

import java.io.Serializable;
import java.io.StringReader;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;


public class FileStructureVo implements Serializable {
    //Varibles
    private String nombre;
    private String tipoArchivo;
    private String delimitador;
    private String tagRow;
    private String descripcion;
    private Hashtable campos;
    private String linea;
    private int filaInicio;
    private boolean upload;
    private boolean download;
    private String databasecode;
    private boolean usoUltimoRegistro;
    private boolean usoNombresCampos;
    
    public FileStructureVo() {
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }
    
    public void addField(String pk, FieldStructureVo field) {
        if (getCampos()==null) campos = new Hashtable();
        campos.put(pk, field);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setTagRow(String tagRow) {
        this.tagRow = tagRow;
    }

    public String getTagRow() {
        return tagRow;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    
    /**
     * Setea data de registro para que esta clase la interpretse
     * y sea capas de lozalizar los valores segun se lo pida directamente
     * el interpresador.
     * 
     * @param dataRow
     */
    public void setDataRow(String dataRow) {
        linea = dataRow;
    }
   
    /**
     * recupera el valor de un campo segun la data especificada
     * para la lectura, en caso de ser encontrador el campo
     * este responde un Object con el tipo de valor solicitado
     * <p>
     *   <code>
     *     FileStructureVo filestr = config.get(0);
     *     while (String linea = archivo.getNextLine()) {
     *        filestr.setDataRow(linea);
     *        String vlId = (java.lang.String) filestr.getValueRow("id");
     *        Date vlFecha = (java.util.Date) filestr.getValueRow("fecha");
     *        Long vlMonto = (java.lang.Long) filestr.getValueRow("monto");       
     *     }
     *   </code>
     * </p>
     * @param idField
     * @return
     */
    public synchronized Object getValueRow(String idField) {
        return localizeValueFromId(idField);
    }    
    
    /**
     * Recuper ael valor de un campo
     * @param idFiled
     * @return
     */
    private synchronized Object localizeValueFromId(String idField) {
        Object resp = null;
        if (linea != null) {
            FieldStructureVo campo = (FieldStructureVo) campos.get(idField);
            if (campo!=null) {
                if ( tipoArchivo.equalsIgnoreCase("LargoFijo") ) resp = getLargoFijoValue(campo);
                if ( tipoArchivo.equalsIgnoreCase("Delimitado") ) resp = getDelimitadoValue(campo);
                if ( tipoArchivo.equalsIgnoreCase("XML") ) resp = getXMLValue(campo);
                if ( tipoArchivo.equalsIgnoreCase("XLS") ) resp = getXLSValue(campo);
            } else {
                 String lineaoriginal = this.linea;
                 //Se revisa si existe alguna substructura que pueda contener el subvalor solicitado
                 Iterator campos = this.campos.values().iterator();
                 while (campos.hasNext()) {
                     FieldStructureVo fld = (FieldStructureVo) campos.next();
                     if (fld.getSubstructura()!= null) {
                         FileStructureVo filePrc = (FileStructureVo)ReaderConfigCore.config.get(fld.getSubstructura());            
                         FieldStructureVo camposub = (FieldStructureVo) filePrc.campos.get(idField);
                         if (camposub!=null) {
                             Object parent = null;
                             //recupero la informacion de la linea segun el campo que tenia el valor
                              if ( tipoArchivo.equalsIgnoreCase("LargoFijo") ) parent = getLargoFijoValue(fld);
                              if ( tipoArchivo.equalsIgnoreCase("Delimitado") ) parent = getDelimitadoValue(fld);
                              if ( tipoArchivo.equalsIgnoreCase("XML") ) parent = getXMLValue(fld);
                              if (parent != null) {
                                 linea = getObjectToString(parent);
                                 //localizo el valor en la substructura
                                 if ( filePrc.getTipoArchivo().equalsIgnoreCase("LargoFijo") ) resp = getLargoFijoValue(camposub);
                                 if ( filePrc.getTipoArchivo().equalsIgnoreCase("Delimitado") ) resp = getDelimitadoValue(camposub);
                                 if ( filePrc.getTipoArchivo().equalsIgnoreCase("XML") ) resp = getXMLValue(camposub);
                              }
                         }                         
                   }
                   this.linea = lineaoriginal;
                   if (resp !=null) break;
                 }
            }
        }
        return resp;
    }
    
    /**
     * recupera el valor en base a largo fijo
     * @param campo
     * @return
     */
    private Object getLargoFijoValue(FieldStructureVo campo) {
        Object data = null;
        //Campos Duros
        if (campo != null && campo.getHardvalue() != null && campo.getHardvalue().length() > 0) {
            data = convertirValorToType(campo.getHardvalue(), campo);
            return data;
        }
        
        int posini = campo.getPosicionInicial()-1;
        int largov = posini + campo.getLargo();
        if (linea != null && largov <= linea.length() ) {
            String valor = linea.substring(posini,largov);        
            data = convertirValorToType(valor, campo);
        }
        return data;
    }
    
    /**
     * Recupera el valor en base a un campo delimitado
     * @param campo
     * @return
     */
    private Object getDelimitadoValue(FieldStructureVo campo) {
        Object data = null;
        //Campos Duros
        if (campo != null && campo.getHardvalue() != null && campo.getHardvalue().length() > 0) {
            data = convertirValorToType(campo.getHardvalue(), campo);
            return data;
        }
        
        String[] tok = linea.split(delimitador);
        String valor = null;
        boolean found = false;
        for (int index=0; index < tok.length; index++) {
            String valortmp = tok[index];
            if ((index+1) == campo.getIndexTab()) {
                found = true;
                valor = valortmp;
                break;   
            }
        }
        if (found == false && campo.getTipo().equalsIgnoreCase("java.lang.String")) valor = "";
        if (valor != null) {
            data = convertirValorToType(valor, campo);
        }
        return data;
    }
    
    /**
     * Recupera el valor en base a una structura de registro
     * de tipo XML
     * @param campo
     * @return
     */
    private Object getXMLValue(FieldStructureVo campo) {
        Object data = null;
        //Campos Duros
        if (campo != null && campo.getHardvalue() != null && campo.getHardvalue().length() > 0) {
            data = convertirValorToType(campo.getHardvalue(), campo);
            return data;
        }
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(this.linea)));   
            NodeList nodoes = document.getElementsByTagName(campo.getTagNombre());
            if (nodoes != null) {
                Element element1 = (Element) nodoes.item(0);
                NodeList fstNm = element1.getChildNodes();
                String valor = fstNm.item(0).getNodeValue();
                data = convertirValorToType(valor, campo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }    

    /**
     * Recupera el valor en base a una structura de registro
     * de tipo XLS
     * @param campo
     * @return
     */
    private Object getXLSValue(FieldStructureVo campo) {
        Object data = null;
        //Campos Duros
        if (campo != null && campo.getHardvalue() != null && campo.getHardvalue().length() > 0) {
            data = convertirValorToType(campo.getHardvalue(), campo);
            return data;
        }
        
        StringTokenizer tokens = new StringTokenizer(linea, "|");
        String valor = null;
        int index = 1;
        while (tokens.hasMoreTokens()) {
            String valortmp = tokens.nextToken();
            if (index == campo.getIndexTab()) {
                valor = valortmp;
                break;   
            }
            index++;
        }
        if (valor != null) {
            data = convertirValorToType(valor, campo);
        }
        return data;
    }    

    
    /**
     * Convierte un dato a un valor especifico
     * segun el tipo de dato solicitado
     * @param valor
     * @param tipo
     * @return
     */
    private Object convertirValorToType(String valor, FieldStructureVo campo) {
        Object rep = null;
        try {
            if (campo.getTipo().equalsIgnoreCase("java.lang.String")) rep = new String(valor);
            if (campo.getTipo().equalsIgnoreCase("java.lang.Long")) {
                Double valorad = new Double(StringUtil.replaceString(valor.trim(),",","."));
                rep = new Long(valorad.longValue());
            }
            if (campo.getTipo().equalsIgnoreCase("java.lang.Double")) {
                rep = new Double(StringUtil.replaceString(valor.trim(),",","."));
            }
            if (campo.getTipo().equalsIgnoreCase("java.lang.Boolean")) rep = new Boolean(valor.trim());
            if (campo.getTipo().equalsIgnoreCase("java.lang.Float")) {
                Double valorad = new Double(StringUtil.replaceString(valor.trim(),",","."));
                rep = new Float(valorad.doubleValue());
            }
            if (campo.getTipo().equalsIgnoreCase("java.lang.Integer")) {
                Double valorad = new Double(StringUtil.replaceString(valor.trim(),",","."));
                rep = new Integer(valorad.intValue());
            }
            if (campo.getTipo().equalsIgnoreCase("java.util.Date")) {
                rep = FechaUtil.getFechaByError(campo.getFormato(), valor);            
            }
        } catch (Exception e) {
            rep = new Exception("campo : '" +campo.getDescripcionCampo() + "' con problema : " + e.getMessage()+"; ");
        }
        return rep;
    }
    
    /**
     * Convierte un dato a un valor especifico
     * segun el tipo de dato solicitado
     * @param valor
     * @param tipo
     * @return
     */
    private String getObjectToString(Object objeto) {
        String rep = null;
        try {
            if (objeto instanceof java.lang.String) rep = (String) objeto;
            if (objeto instanceof java.lang.Long) rep = Long.toString(((Long) objeto));
            if (objeto instanceof java.lang.Double) rep = Double.toString(((Double)objeto));
            if (objeto instanceof java.lang.Boolean) rep = Boolean.toString(((Boolean)objeto));
            if (objeto instanceof java.lang.Float) rep = Float.toString(((Float)objeto));
            if (objeto instanceof java.lang.Integer) rep = Integer.toString(((Integer)objeto));
            if (objeto instanceof java.util.Date) rep = FechaUtil.getFechaFormateoStandar(((java.util.Date)objeto));            
        } catch (Exception e) {
            e.printStackTrace();    
        }
        return rep;
    }    

    public void setFilaInicio(int filaInicio) {
        this.filaInicio = filaInicio;
    }

    public int getFilaInicio() {
        return filaInicio;
    }

    public void setUsoUltimoRegistro(boolean usoUltimoRegistro) {
        this.usoUltimoRegistro = usoUltimoRegistro;
    }

    public boolean isUsoUltimoRegistro() {
        return usoUltimoRegistro;
    }

    public void setCampos(Hashtable campos) {
        this.campos = campos;
    }

    public Hashtable getCampos() {
        return campos;
    }

    public void setDelimitador(String delimitador) {
        this.delimitador = delimitador;
    }

    public String getDelimitador() {
        return delimitador;
    }

    
    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setDatabasecode(String databasecode) {
        this.databasecode = databasecode;
    }

    public String getDatabasecode() {
        return databasecode;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public boolean isDownload() {
        return download;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getLinea() {
        return linea;
    }

    public void setUsoNombresCampos(boolean usoNombresCampos) {
        this.usoNombresCampos = usoNombresCampos;
    }

    public boolean isUsoNombresCampos() {
        return usoNombresCampos;
    }
}
