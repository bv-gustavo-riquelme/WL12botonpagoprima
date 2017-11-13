package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.util.RequestUtils;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet(name = "ReceptorTerminoPagoBancoBICE", urlPatterns = { "/faces/ReceptorPago/ReceptorTerminoPagoBancoBICE" })
public class ReceptorTerminoPagoBancoBICE extends HttpServlet {
    @SuppressWarnings("compatibility:-940064514418507229")
    private static final long serialVersionUID = 1L;

    private static final String CONTENT_TYPE = "text/html; charset=ISO-8859-1";
    
    private static final Logger logger = Logger.getLogger(ReceptorComprobanteBancoBICE.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Metodo para procesar el termino de pago
     * Corresponde a XML con data de termino
     * 
     * <boton-pago-bice>
     *  <respuesta>
     *   <id-empresa-cliente></id-empresa-cliente>
     *   <id-transaccion>234234</id-transaccion>
     *   <id-bice>12123</id-bice>
     *   <estado>APROBADO</estado>
     *  </respuesta>
     * </boton-pago-bice>
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        logger.info("doPost() - iniciando receptor de termino final y verificacion de termino real de pago");
        RequestUtils.printRequest(request, "ReceptorTerminoPagoBancoBICE");
        ReceptorComprobanteBancoBICE serv = new ReceptorComprobanteBancoBICE();
        
        String publicKeyPath = getServletContext().getRealPath("/") + File.separator;
        publicKeyPath = publicKeyPath.replace("classes/", "");
        publicKeyPath += "WEB-INF/config/bice-publico.der";
        serv.setPath(publicKeyPath);
        
        serv.processRequest(request, response, false);
        logger.info("doPost() - terminando y escribiendo close");
    }
}
