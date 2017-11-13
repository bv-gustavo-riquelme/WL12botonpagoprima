package cl.bicevida.botonpago.servlet;

import cl.bice.vida.botonpago.common.util.URLUtil;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class CambioContextoBChile extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String urlCGI = request.getParameter("URLCGI");
        response.setContentType(CONTENT_TYPE);
        
        PrintWriter out = response.getWriter();
        String xml = request.getParameter("data");
        out.println(URLUtil.callUrlPost(urlCGI, xml, "POST"));
        out.flush();
        out.close();
    }
}
