import server.MyServlet;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        MyServlet myServlet = new MyServlet();
        myServlet.ServletStart();
    }

}