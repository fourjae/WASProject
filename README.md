# WASProject
순수 JAVA 코드를 이용하여 나만의 WAS 만들기 (동적인 Servlet을 구현하기)

최대한 간단하게 구현해 보았으며, 자세한 코드들은 소스에 존재합니다.

MyServlet.java
```java
while (true) {
    //Thread Pool
    Socket clientSocket = serverSocket.accept();
    new Thread(() -> processRequest(clientSocket)).start();
    }
```
서버가 시작되면 Socket객체의 accept(); 메소드를 사용하여 요청을 기다립니다. 이때 연결이 들어오기 전까지는 블록상태가 됩니다.

이후 클라이언트로부터 서버에 요청이 들어오면 processRequest 함수에 clientSocket, 즉 들어온 클라이언트의 값을 Socket 객체로 넘겨주며

새로운 Thread를 생성하여 호출합니다.

---

```java
  // 소켓으로부터 InputStream을 생성
  // 클라이언트와 연결된 소켓의 입력 스트림을 얻는다.
  InputStream inputStream = clientSocket.getInputStream();
  OutputStream outputStream = clientSocket.getOutputStream();

  //Reader를 통해 문자열로 처리한다. 윈도우 기본 읽기 MS949 외부에서는 EUC-KR을 사용해서 둘이 호환되어서 읽혀진 것
  BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
```
먼저 getInputStream() 메소드를 통해 소켓에 들어온 데이터를 InputStream으로 반환하여 읽기 위한 준비를 합니다.
outputStream()을 통해 클라이언트에게 전송해줄 OutputStream 객체를 생성합니다.

---

```java
//만약 매핑되는 경로가 없어 NullPointerException이 발생할 경우 예외 처리 필요
Class<?> servletClass = Class.forName(servletMappings.getProperty(servletName));
Servlet servlet = (Servlet) servletClass.newInstance();
```
요청한 url에서 파싱한 서블릿 이름에 대해 Class(MyHttpServletTest, MyHttpServletTest2 어떤 클래스를 사용할지)를 객체로 만들고
인스턴스를 생성한 뒤 내가 만든 interface Servlet 객체로 만듭니다.
---

```java
GetInfo getInfo = new GetInfo();
//생략
Map<String, String> getHeadersInfo = getInfo.GetHeaderInfo(requestData);
Map<String, String> getParameterInfo = getInfo.GetParameterInfo(requestLine);
String method = getInfo.GetMethodInfo(requestLine);
String path = getInfo.GetPathInfo(requestLine);

```
header와 parameter method path를 기본적으로 직접 파싱하여 가져옵니다.

---

```java
Map<String, String> getBodyInfo = null;
if (!"GET".equals(method)){
  getBodyInfo = getInfo.GetBodyInfo(requestData, getHeadersInfo.get("Content-Length"));
}
```
GET방식이 아닐경우 본문에 담아 내용이 들어오기 때문에 Body 부분을 파싱하는 코드를 가져와야 합니다.

![image](https://user-images.githubusercontent.com/47708717/233420278-333db2a5-b9eb-49ae-85b7-064d065af5d9.png)
위 이미지에서 request 구조를 보면 request line , request headers, request body 로 나뉘어 있는 부분을 알 수 있고
request line과 request headers는 가져왔으니 GET 방식에는 없는 request body를 가져옵니다. (그림과 달리 GET 요청시에는 Content-Length가 없습니다. 요청하는 크기를 알아내기 위해 사용합니다.)

---

실제로 다음 구현을 보면
```java
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
```
getHeadersInfo.get("Content-Length") 에서 가져온 Content-Length의 길이를 파라미터로 넘겨준뒤 int Length로 변수를 만들고
그 길이만큼 buffer를 읽는 것을 알 수 있습니다.(header를 모두 읽게 됩니다.)
이후 GetBodyParameterInfo 함수를 통해 key value 형식으로 body내에 있는 모든 요청 변수와 값을 읽습니다.

---

```java
//응답 및 객체 생성
MyServletRequest myServletRequest  = getInfo.GetMyHttpServletInfo(getHeadersInfo, getParameterInfo, method, path, getBodyInfo);
MyServletResponse myServletResponse = new MyServletResponse(outputStream, "UTF-8");

//여기부터 시작 myServletRequest
servlet.service(myServletRequest, myServletResponse);
```
요청할 서블릿 객체와 응답할 서블릿 객체를 만듭니다.
인스턴스를 생성한 servlet의 service 메소드를 시작합니다.

---

```java
public void service(MyServletRequest request, MyServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        if("GET".equals(method)) {
            doGet(request, response);
        }
        else if("POST".equals(method)){
            doPost(request, response);
        }
        else {
            response.setStatus(405);
            PrintWriter out = response.getWriter();
            out.println("허용되지 않는 메소드 입니다.");
            out.flush();
            out.close();
        }
    }
```
method가 GET 방식일 경우 doGet 함수를 실행하여 요청과 응답을 처리합니다.
method가 POST 방식일 경우 doPost 함수를 실행하여 요청과 응답을 처리합니다.
그 외의 방식일 경우 허용되지 않는 메소드를 출력하고 처리합니다.
