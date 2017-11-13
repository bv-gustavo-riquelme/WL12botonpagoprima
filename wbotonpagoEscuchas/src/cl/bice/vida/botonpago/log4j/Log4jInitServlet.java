package cl.bice.vida.botonpago.log4j;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInitServlet extends HttpServlet {
    
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    /**
     * Inicializador de Log4j
     * @param config
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String baseDir = getServletContext().getRealPath("/");
        String confFile = config.getInitParameter("log4j-configuration");
        PropertyConfigurator.configureAndWatch( baseDir + confFile );            
        System.out.println("log4j log.Config " + baseDir + confFile);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType(CONTENT_TYPE);
    }
}
