package cl.bice.vida.botonpago.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

public class URLUtil {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(URLUtil.class);


    /**
     * Verifica si existe URL
     * @param URLName
     * @return
     */
    public static boolean exists(String URLName) {
        boolean resultado = false;
        logger.info("===> Verificando Existencia URL:" + URLName);
        try {

            URLName = StringUtil.replaceString(URLName, "https:", "http:");
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setConnectTimeout(1500); //1 segundo para verificar
            con.setRequestMethod("HEAD");
            resultado = (con.getResponseCode() == HttpURLConnection.HTTP_OK) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            resultado = false;
        }
        logger.info("===> Resultado URL:" + resultado);
        return resultado;
    }

    public static void main(String[] args) {
        try {
            //String mensaje = "<MensajeE><MPINIComer><MPINI><IDCOM>9677706001</IDCOM><IDTRX>9677706001539772</IDTRX><TOTAL>1047</TOTAL><NROPAGOS>1</NROPAGOS><DETALLE><SRVREC>003028</SRVREC><MONTO>1047</MONTO><GLOSA>Credito Hipotecario Libre Ele</GLOSA><CANTIDAD>0001</CANTIDAD><PRECIO>1047</PRECIO><DATOADIC>9677706001539772086066839677706001</DATOADIC></DETALLE></MPINI></MPINIComer><DirLlavePriv>../Llaves/9665641001_20160831.pri</DirLlavePriv></MensajeE>";
            //String mensaje = "<MPINI><IDCOM>9665641001</IDCOM><IDTRX>9665641001540491</IDTRX><TOTAL>1000</TOTAL><NROPAGOS>1</NROPAGOS><DETALLE><SRVREC>005218</SRVREC><MONTO>1000</MONTO><GLOSA>APV Inversion</GLOSA><CANTIDAD>0001</CANTIDAD><PRECIO>1000</PRECIO><DATOADIC>9665641001540491103591829665641001</DATOADIC></DETALLE></MPINI>";
            //String mensaje = "<MPCON><IDCOM>9665641001</IDCOM><IDTRX>9665641001540531</IDTRX><TOTAL>43548</TOTAL><IDREG>41732</IDREG></MPCON>";
            String mensaje = "<INICIO><ENCABEZADO> <ID_SESSION>1679757</ID_SESSION> <RUT_DV_CON>967770604</RUT_DV_CON> <CONV_CON>14104441</CONV_CON> <SERVICIO>BICE HIPOTECARIA</SERVICIO> <RUT_DV_CLIENTE>86066831</RUT_DV_CLIENTE> <PAG_RET>http://190.196.3.35:80/webpagobice/jsp/closewindow.jsp</PAG_RET> <TIPO_CONF>nomq</TIPO_CONF> <METODO_REND>POST</METODO_REND> <PAG_REND>http://190.196.3.35:80/webpagobice/ReceptorComprobanteBancoEstado</PAG_REND> <BANCO>0012</BANCO> <CANT_MPAGO>1</CANT_MPAGO> <TOTAL>1</TOTAL></ENCABEZADO><MULTIPAGO>  <GLOSA_MPAGO>BICE HIPOTECARIA</GLOSA_MPAGO>  <ID_MPAGO>1679757</ID_MPAGO>  <PAGO>    <RUT_DV_EMP>967770604</RUT_DV_EMP>    <NUM_CONV>14104441</NUM_CONV>    <FEC_TRX>20161108</FEC_TRX>    <HOR_TRX>134632</HOR_TRX>    <FEC_VENC>20161108</FEC_VENC>    <GLOSA>BICE HIPOTECARIA</GLOSA>    <COD_PAGO/>    <MONTO>1</MONTO>  </PAGO></MULTIPAGO></INICIO>";
            
            String url ="https://test.bancoestado.cl/transa/mod_pago/entrada.asp";// "https://aplicaciones-desa.bicevida.cl/bchBicevida/cgi-bin/cgicomer";
            //String response = callUrlPost(url, "ACTION=VP&"+mensaje, "POST");
            String response = callUrlPost(url, "ent="+mensaje, "GET");
            System.out.println(response);
            /*
            HttpClient client = new HttpClient();
            
            PostMethod method = new PostMethod(url);
            method.addParameter("ACTION", "VP");
            method.addParameter("&", mensaje);
            int statusCode = client.executeMethod(method);
            String xmlResp = method.getResponseBodyAsString();

            System.out.println(xmlResp);
            */
            /*
            HttpPost httpPost = new HttpPost(url);


            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("ACTION", "VP"));
            postParameters.add(new BasicNameValuePair("tx",mensaje));
    
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpPost);
            System.out.println(urlResponseInputStreamToString(response.getEntity().getContent()) );
            */
            
            /*Document document = DocumentHelper.parseText(response);
            
            Node node = document.selectSingleNode("//MensajeS/MPINIFirma");   
            System.out.println(node.getStringValue());
            
            CloseableHttpClient httpclient = HttpClients.createDefault();
                    try {
                        HttpHost target = new HttpHost(url, 443, "https");
                        HttpHost proxy = new HttpHost(url, 8080, "http");

                        RequestConfig config = RequestConfig.custom()
                                .setProxy(proxy)
                                .build();
                        HttpGet request = new HttpGet("/");
                        request.setConfig(config);

                        System.out.println("Executing request " + request.getRequestLine() + " to " + target + " via " + proxy);

                        CloseableHttpResponse response2 = httpclient.execute(target, request);
                        try {
                            System.out.println("----------------------------------------");
                            System.out.println(response2.getStatusLine());
                            System.out.println(EntityUtils.toString(response2.getEntity()));
                        } finally {
                            response2.close();
                        }
                    } finally {
                        httpclient.close();
                    }*/
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo encargado de llamar sin importar si es 
     * con certificado de seguridad o sin el
     * @param uri -> url a la cual se llamara
     * @param mensaje -> parametros que se enviaran despues de la url
     * @return -> se retorna string obtenido desde url
     */
    public static String callUrlPost(String uri, String mensaje, String method) throws IOException {
        logger.info("LLamando a la url [" + uri + "]method[" + method + "]mensaje[" + mensaje + "]");
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        String retorno = "";
        try {
            boolean isHTTPS = false;
            if(uri.toLowerCase().contains("https")) {
                logger.info("URL es Https ->" + uri);
                isHTTPS = true;
            } 
            
            client = clientLoadCertificated(isHTTPS);
            if(client != null) {
                if(method.equalsIgnoreCase("GET")) {
                    URL url = new URL(uri + "?" + mensaje);
                    String nullFragment = null;
                    URI urii = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), nullFragment);
                    HttpGet request = new HttpGet(urii.toString() );
                    response = client.execute(request);
                    retorno = urlResponseInputStreamToString(response.getEntity().getContent());
                
                } else {
                    //Para utilizar POST se debe implementar el metodo de llamada post    
                    logger.info("LLamando URL["+uri+"]mensaje["+mensaje+"]");
                    HttpPost post = new HttpPost(uri);
                    /*
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    
                    String[] arrayParametros = mensaje.split("&");
                    for(String s: arrayParametros) {
                        String[] params = s.split("=");
                        postParameters.add(new BasicNameValuePair(params[0], params[1]));    
                    }
                    post.setEntity(new UrlEncodedFormEntity(postParameters));
                    
                    HttpEntity entity = new ByteArrayEntity(mensaje.getBytes("UTF-8"));
                    */
                    HttpEntity postEntity = new ByteArrayEntity(mensaje.getBytes("UTF-8"));
                    //StringEntity postEntity = new StringEntity(mensaje);
                    post.setEntity(postEntity);
                    
                    post.setHeader("Content-Type", "application/json; charset=UTF-8");
                    response = client.execute(post);
                    retorno = urlResponseInputStreamToString(response.getEntity().getContent());
                }
            } else {
                logger.error("Error al obtener cliente: No se pudo instanciar el cliente");    
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        } catch(URISyntaxException e ) {
            e.printStackTrace();
            throw new IOException(e);
        } finally {
            closeURlConnections(client, response);    
        }
        logger.info("Respuesta LLamada [" + retorno + "]");
        return retorno;
    }
    
    private static String urlResponseInputStreamToString(InputStream in) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        
        return result.toString();
    }
    
    private static CloseableHttpClient clientLoadCertificated(boolean isHTTPS) {
        CloseableHttpClient client = null;
        try {
            if(isHTTPS) {
                SSLContext ctx = SSLContext.getInstance("TLS"); 
                X509TrustManager tm = new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
    
                            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                            }
    
                            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                            }
                        };
                ctx.init(null, new TrustManager[] { tm }, null);
                SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("https", ssf).build();
                
                HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
                client = HttpClients.custom().setConnectionManager(cm).build();
            } else {
                client = HttpClients.createDefault();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }    
        return client;
    }
    
    private static void closeURlConnections(CloseableHttpClient client, CloseableHttpResponse response ) {
        try {
            if(client != null) {
                client.close();
            }
            if( response != null) {
                response.close();
            }
        } catch (IOException e) {
            logger.error("Ocurrio un error la cerrar UrlConnections", e);
        }
    }
    
    /**
     * Recupera el contenido del request en un String
     * @param request
     * @return String con el contenido de la pagina
     * @throws IOException
     */
    public static String getStringRequestHttpEncapsulated(HttpServletRequest request, String varname) throws IOException {
        String inputLine;
        inputLine = request.getParameter(varname);
        
        return inputLine; 
    }
}
