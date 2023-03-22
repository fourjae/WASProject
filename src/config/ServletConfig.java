package config;

import java.util.Iterator;
import java.util.Map;

public class ServletConfig implements ServletConfiguration {
    private String servletName;

    public ServletConfig(String servletName) {
        this.servletName = servletName;
    }

    @Override
    public String getServletName() {
        return servletName;
    }

}
