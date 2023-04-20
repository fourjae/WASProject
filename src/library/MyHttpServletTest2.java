package library;

import config.ServletConfig;
import exception.ServletException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class MyHttpServletTest2 implements Servlet {
    private ServletConfig servletConfig;
    public void init(ServletConfig servletConfig) throws ServletException {
        this.servletConfig = servletConfig;
    }

    public ServletConfig getServletConfig() {
        return this.servletConfig;
    }

    public void service(MyServletRequest request, MyServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        if("GET".equals(method)) {
            doGet(request, response);
        }
        else if("POST".equals(method)){
            doPost(request, response);
        }
        else {
            doNotMethod(request,response);
        }
    }

    public String getServletInfo() {
        return "MyHttpServlet";
    }

    public void destroy() {
        // clean up any resources here
    }
    protected void doGet(MyServletRequest request, MyServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        Map<String, String> paramMap = request.getParameters();
        response.setStatus(200);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("");
        out.println("<meta charset=\"utf-8\"><html><body><h1>Hello World!</h1>");
        out.println("");
        out.println("Get 전송 방식을 return 합니다. 이 곳은 /test2 경로입니다.");
        out.println("");
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
           String key = entry.getKey();
           String value = entry.getValue();
           out.println("<h3>" + key + " : " + value + "</h3>");
        }
        out.println("");
        out.println("</body></html>");
        out.flush();
        out.close();
    }

    protected void doPost(MyServletRequest request, MyServletResponse response) throws IOException{
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("");
        out.println("<meta charset=\"utf-8\"><html><body><h1>Hello World!</h1>");
        out.println("");
        out.println("Post 전송 방식을 return 합니다. 이 곳은 /test2 경로입니다.");
        out.println("");
        out.println("<h2>This is Request Body</h2>");
        out.println(request.getBody());
        out.println("");
        out.println("<h3>This is Response Header</h3>");
        out.println(request.getHeaders());
        out.println("</body></html>");
        out.flush();
        out.close();
    }

    protected void doNotMethod(MyServletRequest request, MyServletResponse response) throws IOException {
        response.setStatus(405);
        PrintWriter out = response.getWriter();
        out.println("HTTP/1.1 405 OK");
        out.println("Content-Type: text/html");
        out.println("");
        out.println("허용되지 않는 메소드 입니다.");
        out.flush();
        out.close();
    }


    @Deprecated
    public MyServletResponse processRequest(String request, Socket clientSocket) throws IOException {
        // 클라이언트와 연결된 소켓의 출력 스트림을 얻는다.
        OutputStream outputStream = clientSocket.getOutputStream();

        MyServletResponse response = new MyServletResponse(outputStream, "UTF-8");
        // 응답 본문 생성
        String responseBody = "Hello, World!";

        // 응답 헤더 설정
        response.setHeaders("Content-Type", "text/plain");
        response.setHeaders("Content-Length", String.valueOf(responseBody.length()));

        // 응답 본문 전송
        response.write(responseBody);

        // HTTP 응답 메시지 작성
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + responseBody.length() + "\r\n" +
                "\r\n" +
                responseBody;

        // HTTP 응답 메시지 전송
        outputStream.write(httpResponse.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
        // 응답 객체 반환
        return response;
    }
}
