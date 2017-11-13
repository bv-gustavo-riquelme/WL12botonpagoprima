package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.util.RequestUtils;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet(name = "ReceptorIntermedioPagoSantander", urlPatterns = {"/faces/ReceptorPago/pagoBSantander/notificacion" })
public class ReceptorIntermedioPagoSantander extends HttpServlet {
    @SuppressWarnings("compatibility:-6223470775590564952")
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(ReceptorIntermedioPagoSantander.class);
    

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        logger.info("ReceptorIntermedioPagoSantander fue llamado");
        RequestUtils.printRequest(request, "ReceptorIntermedioPagoSantander");
        request.getSession().removeAttribute("timetolivepagoweb");
        response.setContentType("text/html");
        
        response.getOutputStream().print("<NOTIFICA>OK</NOTIFICA>");
        
        logger.info("ReceptorIntermedioPagoSantander se responde <NOTIFICA>OK</NOTIFICA>");
    }
}
