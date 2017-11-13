package cl.bice.vida.botonpago.api;

import cl.botonPago.api.Base64;
import cl.botonPago.api.BotonPagoException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

public class RespuestaBotonPago {
    public static final int APROBADO = 0;
    public static final int RECHAZADO = 1;
    public static final int PENDIENTE = 2;
    private DocumentBuilder builder;
    private Document document;
    private String idEmpresaCliente;
    private int estado;
    private long idTransaccionCliente;
    private long idTrxBice;
    private String xml;
    private String firma;
    
    private String xmlRespuesta;
    private String path;
    
    
    public RespuestaBotonPago(String xmlRespuesta, String firma) {
        this.xml = xmlRespuesta;
        this.firma = firma;

        init(this.xml);
    }
    
    public RespuestaBotonPago(String xmlRespuesta, String firma, String path) {
        this.xmlRespuesta = xmlRespuesta;
        this.firma = firma;
        this.path = path;
        
        crearRespuesta();
    }

    private Document createDocument(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            StringReader bld = new StringReader(xml);
            InputSource is = new InputSource(bld);
            return builder.parse(is);
        } catch (Exception e) {
            throw new BotonPagoException("Error al procesar xml de notificacion");
        }
    }

    private byte[] cargarLlavePublicaBice(String pathLlavePublicaBice) {
        try {
            InputStream fis2 = null;
            File privKeyFile = new File(pathLlavePublicaBice);
            fis2 = new FileInputStream(privKeyFile);
            int b = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((b = fis2.read()) != -1) {
                baos.write(b);
            }
            baos.flush();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new BotonPagoException(e);
        }
    }

    private void crearRespuesta() {
        try {
            byte[] llavePublica = cargarLlavePublicaBice(path);

            KeyFactory rSAKeyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = rSAKeyFactory.generatePublic(new X509EncodedKeySpec(llavePublica));

            Signature signObj = Signature.getInstance("SHA1withRSA");
            signObj.initVerify(pubKey);
            signObj.update(xmlRespuesta.getBytes());

            byte[] sign = Base64.decode(firma);
            if (!signObj.verify(sign)) {
                throw new BotonPagoException("La firma de la notificacion del banco BICE no es valido.");
            }
            
            init(xmlRespuesta);
        } catch (Exception e) {
            throw new BotonPagoException(e);
        }
    }

    private void init(String xml) {
        this.document = createDocument(xml);
        NodeList nodes_i = this.document.getDocumentElement().getChildNodes();

        Node nodeResp = nodes_i.item(0);
        if ((nodeResp == null) ||
            ((nodeResp.getNodeType() != 1) && (((Element) nodeResp).getTagName().equals("respuesta")))) {
            throw new BotonPagoException("Se esperaba el elemento <repuesta>");
        }
        nodes_i = nodeResp.getChildNodes();
        for (int i = 0; i < nodes_i.getLength(); i++) {
            Node node_i = nodes_i.item(i);
            if ((node_i != null) && (node_i.getFirstChild() != null)) {
                String valor = node_i.getFirstChild().getNodeValue();
                if (esElemento(node_i, "id-empresa-cliente")) {
                    this.idEmpresaCliente = valor;
                } else if (esElemento(node_i, "id-transaccion")) {
                    this.idTransaccionCliente = Long.parseLong(valor);
                } else if (esElemento(node_i, "id-bice")) {
                    this.idTrxBice = Long.parseLong(valor);
                } else if (esElemento(node_i, "estado")) {
                    this.estado = parseEstado(valor);
                } else {
                    throw new BotonPagoException("Elemento no esperado: " + node_i.getNodeName());
                }
            }
        }
    }

    private int parseEstado(String valor) {
        if ("APROBADO".equals(valor)) {
            return 0;
        }
        if ("RECHAZADO".equals(valor)) {
            return 1;
        }
        if ("PENDIENTE".equals(valor)) {
            return 2;
        }
        throw new BotonPagoException("Estado no esperado: " + valor);
    }

    private boolean esElemento(Node node_i, String nombreElemento) {
        return (node_i.getNodeType() == 1) && (((Element) node_i).getTagName().equals(nombreElemento));
    }

    public int getEstado() {
        return this.estado;
    }

    public String getIdEmpresaCliente() {
        return this.idEmpresaCliente;
    }

    public long getIdTrxCliente() {
        return this.idTransaccionCliente;
    }

    public long getIdTrxBice() {
        return this.idTrxBice;
    }
    
    
    public static void main(String[] args) {
        
        String xmlRespuesta = "<boton-pago-bice><respuesta><id-empresa-cliente>96777060</id-empresa-cliente><id-transaccion>1540272</id-transaccion><id-bice>121231</id-bice><estado>APROBADO</estado></respuesta></boton-pago-bice>";
        String firma = "";
        String path = "/Users/Haohmaru/.jdeveloper/system12.1.3.0.41.140521.1008/o.j2ee/drs/BotonPago/wpagobiceWebApp.war/WEB-INF/config/";
        
        CanastaBotonPago canasta = new CanastaBotonPago(path + "cliente-privado.der", xmlRespuesta);
        
        firma = canasta.getFirma2(xmlRespuesta);
        System.out.println(firma);
        ///Users/Haohmaru/.jdeveloper/system12.1.3.0.41.140521.1008/o.j2ee/drs/BotonPago/wpagobiceWebApp.war/xsl/resumenypagos/usuarioresumenypagos_HIPO.xslt
        RespuestaBotonPago respuesta = new RespuestaBotonPago(xmlRespuesta,firma ,  path + "bice-publico.der");
        
        
        
        System.out.println(respuesta.getIdTrxBice());
        
    }
}
