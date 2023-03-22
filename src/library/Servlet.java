package library;

import config.ServletConfig;
import exception.ServletException;

import java.io.IOException;

public interface Servlet {

    public void init(ServletConfig servletConfig) throws ServletException;
    public ServletConfig getServletConfig();
    public void service(MyServletRequest request, MyServletResponse response) throws ServletException, IOException;
    public String getServletInfo();
    public void destroy();
}
