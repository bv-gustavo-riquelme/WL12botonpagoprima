package cl.bice.vida.botonpago.modelo.rest;

import cl.bice.vida.botonpago.common.util.URLUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class RESTfulCallUtil {
    
    private static final Logger LOGGER = Logger.getLogger(RESTfulCallUtil.class);

    /**
     * Test para llamar a un servicio REST
     * @param uri
     * @param method
     * @return
     */
    public static String call(String uri, String method, String username, String password,
                              List<RESTfulParametroDto> parametros) {
        String retorno = null;
        URL url;
        HttpURLConnection connection;
        try {

            String urigo = uri;
            if (method != null && method.equalsIgnoreCase("GET") && parametros.size() > 0) {
                urigo = urigo + "?";
                if (parametros != null && parametros.size() > 0) {
                    for (int iu = 0; iu < parametros.size(); iu++) {
                        urigo = urigo + parametros.get(iu).getNombre() + "=" + parametros.get(iu).getValor() + "&";
                    }
                }
                if (urigo.endsWith("&")) {
                    urigo = urigo.substring(0, urigo.length() - 1);
                }
            }
            url = new URL(urigo);

            //SETEA USAURIO Y CLAVE
            String login = username + ":" + password;
            String encoding = login != null ? new sun.misc.BASE64Encoder().encode(login.getBytes()) : null;

            //CONEXION HTTP
            /*
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept", "application/json");
            
            if (username != null && password != null)
                connection.setRequestProperty("Authorization", "Basic " + encoding);
            */
            StringBuilder postData = new StringBuilder();
            StringBuffer buffer = new StringBuffer();
            buffer.append("{");
            if (method != null && method.equalsIgnoreCase("POST")) {
                if (parametros != null && parametros.size() > 0) {
                    for (int iu = 0; iu < parametros.size(); iu++) {
                        if (postData.length() != 0) {
                            postData.append('&');
                            buffer.append(",");
                        }
                        
                        buffer.append("\"" + parametros.get(iu).getNombre() + "\": \"" + parametros.get(iu).getValor() + "\"" );
                        postData.append(URLEncoder.encode(parametros.get(iu).getNombre(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(parametros.get(iu).getValor()), "UTF-8"));

                    }
                    LOGGER.info("=============================================================================");
                    LOGGER.info("Servicio Rest ["+uri+"] PostData [" + buffer.toString() + "]");
                    LOGGER.info("=============================================================================");
                    //byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                    buffer.append("}");
                    retorno = URLUtil.callUrlPost(uri, buffer.toString(), "POST");
                    //connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    //connection.getOutputStream().write(postDataBytes);
                }
            }

            //InputStream json = connection.getInputStream();
            //retorno = getStringFromInputStream(json);
            System.out.println(retorno);
            //connection.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return retorno;
    }

    public static void main(String[] args) {
        List<RESTfulParametroDto> parametros = new ArrayList<RESTfulParametroDto>();
        parametros.add(new RESTfulParametroDto("idOrdenCanal", "LWM6MXD7"));
        parametros.add(new RESTfulParametroDto("idTransaccion","1680227"));
        parametros.add(new RESTfulParametroDto("tipoPago", "ONLINE")); //ONLINE RENDICION
        parametros.add(new RESTfulParametroDto("estadoPago", "PAGADO")); //PAGADO RECHAZADO
        parametros.add(new RESTfulParametroDto("token", "PRUEBA")); //PAGADO RECHAZADO
        //parametros.add(new RESTfulParametroDto("claveFuenteOrigen","notengo"));
        parametros.add(new RESTfulParametroDto("checksum",
                                               "_APP_NAME_.MTY4MDIyNztPTkxJTkU7UEFHQURPO2JpY2V2aWRh.MjAxNi0xMS0xMSAxMDo1NjoyOS4xNzM=.d9e2a25b69554de7122bfa8685be4d531472c66db81eeb11830cf65c69a25703"));
        //String json = RESTfulCallUtil.call("http://bicevida-ventaonline-dev.elasticbeanstalk.com/payments/notification.xml", "POST",null,null, parametros);
        String json =
            RESTfulCallUtil.call("http://10.140.212.131:3000/payments/notification.xml", "POST", null, null, parametros);
        System.out.println(json);
        /*
                idOrdenCanal :LWM6MXD7
                idTransaccion :1680227
                tipoPago : ONLINE
                idOrdenCanal :PAGADO
                checksum : _APP_NAME_.MTY4MDIyNztPTkxJTkU7UEFHQURPO2JpY2V2aWRh
                .MjAxNi0xMS0xMSAxMDo1NjoyOS4xNzM=
                .d9e2a25b69554de7122bfa8685be4d531472c66db81eeb11830cf65c69a25703


                 */
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
