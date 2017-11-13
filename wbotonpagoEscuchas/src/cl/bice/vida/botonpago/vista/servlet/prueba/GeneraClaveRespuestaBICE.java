package cl.bice.vida.botonpago.vista.servlet.prueba;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GeneraClaveRespuestaBICE", urlPatterns = { "/faces/generaclaverespuestabice" })
public class GeneraClaveRespuestaBICE extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=ISO-8859-1";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        String xml = request.getParameter("XML");
        String privateKeyPath = getServletContext().getRealPath("/") + File.separator;
            
        //privateKeyPath = privateKeyPath + File.separator + "wbotonpagoEscuchasWebApp.war" + File.separator + "WEB-INF"+ File.separator; 
        privateKeyPath = privateKeyPath.replace("classes", "");
        privateKeyPath += "WEB-INF/config/cliente-privado.der";
        
        System.out.println("privateKeyPath -> " + privateKeyPath);
        
        cl.bice.vida.botonpago.api.CanastaBotonPago canasta = new cl.bice.vida.botonpago.api.CanastaBotonPago(privateKeyPath);
          
          
        String firma = canasta.getFirma2(xml);
        
        
        out.println("<html>");
        out.println("<head><title>GeneraClaveRespuestaBICE</title></head>");
        out.println("<body>");
        System.out.println("-------> " + firma);
        out.println("<h1>Clave encriptada</h1>");
        out.println("<p>" + firma + "</p>");
        out.println("</body></html>");
        out.close();
    }
}
