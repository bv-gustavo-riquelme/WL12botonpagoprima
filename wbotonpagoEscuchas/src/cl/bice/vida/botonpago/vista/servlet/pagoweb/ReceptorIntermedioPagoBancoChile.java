package cl.bice.vida.botonpago.vista.servlet.pagoweb;

import cl.bice.vida.botonpago.util.RequestUtils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet(name = "ReceptorIntermedioPagoBancoChile", urlPatterns = { "/faces/ReceptorPago/pagoBChile/notificacion" })
public class ReceptorIntermedioPagoBancoChile extends HttpServlet {
    @SuppressWarnings("compatibility:7599664731294045169")
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(ReceptorIntermedioPagoBancoChile.class);
    
    
    private static final String CONTENT_TYPE = "text/html; charset=ISO-8859-1";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("ReceptorIntermedioPagoBancoChile fue llamado");
        request.getSession().removeAttribute("timetolivepagoweb");
        logger.info("ReceptorIntermedioPagoBancoChile -> " + request.getParameterMap());
        
        RequestUtils.printRequest(request, "ReceptorIntermedioPagoBancoChile");         
       
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print("<NOTIFICA>OK</NOTIFICA>");
        logger.info("Se responde <NOTIFICA>OK</NOTIFICA>");
    }
}
