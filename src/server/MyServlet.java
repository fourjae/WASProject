package server;
import common.CommonService;
import library.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MyServlet {
    private static final int portNumber = 80;
    private static final String webRoot = "/Users/hwang-yeongha/Desktop/WASProject/WASProject/src";
    private static final Properties servletMappings = new Properties();
    private static final FileInputStream fis;

    static {
        try {
            fis = new FileInputStream(webRoot + "/server.properties");
            servletMappings.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CommonService commonService = new CommonService();

    public static void ServletStart() throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Run Server : " + portNumber);

            while (true) {
                //Thread Pool
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> processRequest(clientSocket)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processRequest(Socket clientSocket) {
        try {
            GetInfo getInfo = new GetInfo();
            if (!commonService.isNullValueCheck(clientSocket)) {
                // 소켓으로부터 InputStream을 생성
                // 클라이언트와 연결된 소켓의 입력 스트림을 얻는다.
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();

                //Reader를 통해 문자열로 처리한다. 윈도우 기본 읽기 MS949 외부에서는 EUC-KR을 사용해서 둘이 호환되어서 읽혀진 것
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                String requestLine = in.readLine();
                BufferedReader requestData = in;

                String servletName = getServletName(requestLine);
                if (!"/favicon.ico".equals(servletName)) {
                    //만약 매핑되는 경로가 없어 NullPointerException이 발생할 경우 예외 처리 필요
                    Class<?> servletClass = Class.forName(servletMappings.getProperty(servletName));
                    Servlet servlet = (Servlet) servletClass.newInstance();

                    Map<String, String> getHeadersInfo = getInfo.GetHeaderInfo(requestData);

                    Map<String, String> getParameterInfo = getInfo.GetParameterInfo(requestLine);

                    String method = getInfo.GetMethodInfo(requestLine);

                    String path = getInfo.GetPathInfo(requestLine);

                    Map<String, String> getBodyInfo = null;

                    if (!"GET".equals(method)){
                        getBodyInfo = getInfo.GetBodyInfo(requestData, getHeadersInfo.get("Content-Length"));
                    }

                    //응답 및 객체 생성
                    MyServletRequest myServletRequest  = getInfo.GetMyHttpServletInfo(getHeadersInfo, getParameterInfo, method, path, getBodyInfo);
                    MyServletResponse myServletResponse = new MyServletResponse(outputStream, "UTF-8");

                    //여기부터 시작 myServletRequest
                    servlet.service(myServletRequest, myServletResponse);


//                MyServletResponse response = ((MyHttpServlet) servlet).service(request, clientSocket);
            }
                clientSocket.close();

        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 요청을 읽어오는 코드 구현
    private static void readRequest(Socket clientSocket) throws IOException {

    }

    // 요청 URL에서 서블릿 이름을 추출하는 코드 구현
    private static String getServletName(String request) {

        // 요청 URL 추출
        String requestUrl = getRequestUrl(request);
        if (requestUrl.indexOf("?") != -1) {
            int index = requestUrl.indexOf("?"); // "?" 문자열의 인덱스를 찾습니다.
            requestUrl = requestUrl.substring(0, index); // 문자열의 0부터 ? 이전까지의 부분 문자열을 가져옵니다.
        }
        // 요청 URL에서 확장자 제거
        String servletName = requestUrl.replace(".do", "");

        return servletName;
    }

    // 응답을 전송하는 코드 구현
    private static void writeResponse(Socket clientSocket, String response) throws IOException {


    }


    private static String getRequestUrl(String request) {
        // HTTP 요청 메시지에서 요청 URL 추출
        String[] requestLines = request.split("\r\n");
        String requestLine = requestLines[0];
        String[] requestTokens = requestLine.split("\\s+");
        String requestUrl = requestTokens[1];

        return requestUrl;
    }

}
