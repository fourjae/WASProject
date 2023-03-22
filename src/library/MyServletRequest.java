package library;

import java.util.Map;

public class MyServletRequest {
    private Map<String, String> headers;
    private Map<String, String> parameters;
    private String method;
    private String path;
    private Map<String, String> body;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }
}
