package library;

import common.CommonService;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public class GetInfo {
    private static CommonService commonService = new CommonService();
    public static Map GetHeaderInfo(BufferedReader in) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            // ":" 를 기준으로 key와 value를 분리하여 Map에 저장
            String[] parts = line.split(": ");
            headers.put(parts[0], parts[1]);
        }
        return headers;
    }
    
    public static Map GetBodyInfo(BufferedReader in, String contentLength) throws IOException {
        if (commonService.isNullValueCheck(in) || commonService.isNullValueCheck(contentLength)) { return null; }
        StringBuilder requestBody = new StringBuilder();
        int Length = Integer.parseInt(contentLength);
        char[] buffer = new char[Length];
        in.read(buffer, 0, Length);
        requestBody.append(buffer);
        Map<String, String> bodyMap = GetBodyParameterInfo(requestBody.toString());
        return bodyMap;
    }

    public static Map GetParameterInfo(String readLine) throws IOException {
        if (commonService.isNullValueCheck(readLine)) { return null; }
        // requestLine에서 URL 추출
        String[] requestLineParts = readLine.split(" ");
        String url = requestLineParts[1];
        Map<String, String> parameterMap = new HashMap<>();
        // URL에서 파라미터 추출
        int queryStringIndex = url.indexOf('?');
        if (queryStringIndex != -1) {
            String queryString = url.substring(queryStringIndex + 1);
            String[] queryParts = queryString.split("&");
            for (String queryPart : queryParts) {
                String[] parameterParts = queryPart.split("=");
                String parameterName = URLDecoder.decode(parameterParts[0], "UTF-8");
                String parameterValue = URLDecoder.decode(parameterParts[1], "UTF-8");
                // parameterName과 parameterValue를 사용하여 처리한다.
                parameterMap.put(parameterName,parameterValue);
            }
        }
        return parameterMap;
    }

    public static Map GetBodyParameterInfo(String body){
        if (commonService.isNullValueCheck(body)) { return null; }

        String[] bodyParams = body.split("&");
        Map<String, String> parameterMap = new HashMap<>();
        for (String param : bodyParams) {
            String[] keyValue = param.split("=");
            parameterMap.put(keyValue[0], keyValue[1]);
        }
        return parameterMap;

    }


    public static String GetMethodInfo(String readLine) throws IOException{
        if (commonService.isNullValueCheck(readLine)) { return null; }
        String[] requestLineParts = readLine.split(" ");
        String method = requestLineParts[0];
        return method;
    }
    public static String GetPathInfo(String readLine) throws IOException{
        if (commonService.isNullValueCheck(readLine)) { return null; }
        String[] requestLineParts = readLine.split(" ");
        String url = requestLineParts[1];
        String path = "/";
        int queryStringIndex = url.indexOf('?');
        if (queryStringIndex != -1) {
            path = url.substring(0,queryStringIndex);
        }
        return path;
    }

    public static MyServletRequest GetMyHttpServletInfo(Map<String, String> headers, Map<String, String> parameters, String method, String path, Map<String, String> body) {
        MyServletRequest myServletRequest = new MyServletRequest();
        myServletRequest.setHeaders(headers);
        myServletRequest.setParameters(parameters);
        myServletRequest.setMethod(method);
        myServletRequest.setPath(path);
        myServletRequest.setBody(body);
        return myServletRequest;
    }
}
