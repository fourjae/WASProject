package library;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class MyServletResponse {
    // 응답 헤더
    private Map<String, String> headers = new HashMap<>();

    // 응답 상태 코드
    private int status;

    // 응답 본문
    private StringBuilder body = new StringBuilder();

    private Map<String, String> getHeaders(){ return headers; }
    private int getStatus(){
        return status;
    }
    private StringBuilder getBody(){
        return body;
    }

    /**
     * 응답 헤더를 설정한다.
     *
     * @param name  헤더 이름
     * @param value 헤더 값
     */
    public void setHeaders(String name, String value) {
        headers.put(name,value);
    }
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 응답 본문에 문자열을 추가한다.
     * @param content 추가할 문자열
     */
    public void write(String content){
        body.append(content);
    }

    private OutputStream outputStream;
    private String charset;

    public MyServletResponse(OutputStream outputStream, String charset){
        this.outputStream = outputStream;
        this.charset = charset;
    }

    public PrintWriter getWriter() throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, charset);
        return new PrintWriter(outputStreamWriter);
    }



}
