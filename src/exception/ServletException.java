package exception;

public class ServletException extends Exception {
    public ServletException(){
        super();
    }
    public ServletException(String message){
        super(message);
    }
    public ServletException(Throwable rootCause) {
        super(rootCause);
    }

    public ServletException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}
