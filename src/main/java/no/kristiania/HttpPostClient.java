package no.kristiania;

import java.io.IOException;
import java.net.Socket;

public class HttpPostClient {
    private final HttpMessage httpMessage;
    private final int statusCode;

    public HttpPostClient(String host, int port, String requestTarget, String contentBody) throws IOException {
        Socket socket = new Socket(host, port);

        String request = "POST " + requestTarget + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Connection: close\r\n" +
                "Content-Length: " + contentBody.length() + "\r\n" +
                "\r\n" +
                contentBody;
        socket.getOutputStream().write(request.getBytes());

        httpMessage = new HttpMessage(socket);
        String[] statusLine = httpMessage.lineStart.split(" ");
        this.statusCode = Integer.parseInt(statusLine[1]);
    }

    public int getStatusCode() {
        return statusCode;
    }
}
