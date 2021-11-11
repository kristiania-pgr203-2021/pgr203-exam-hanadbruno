package no.kristiania;

import org.logevents.observers.AbstractHttpPostJsonLogEventObserver;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpMessage {
    public String lineStart;
    public final Map<String, String> headerFields = new HashMap<>();

    public static void main(String[] args) {

    }

    public static Map<String, String> parseQueryParameters(String query) {
        Map<String, String> queryMap = new HashMap<>();
        for (String queryParameter : query.split("&")) {
            int equalsPos = queryParameter.indexOf('=');
            String parameterName = queryParameter.substring(0, equalsPos);
            String parameterValue = queryParameter.substring(equalsPos+1);
            queryMap.put(parameterName, parameterValue);
        }
        return queryMap;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String messageBody;

    public HttpMessage(Socket socket) throws IOException {
        lineStart = HttpMessage.readLine(socket);
        readHeaderFields(socket);
        if (headerFields.containsKey("Content-Length")) {
            messageBody = HttpMessage.readBytes(socket, getContentLength());
        }
    }
    public HttpMessage(String lineStart, String messageBody){
     this.lineStart = lineStart;
     this.messageBody = messageBody;
}
    public int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }

    public String getHeader(String headerName) {
        return headerFields.get(headerName);
    }

    static String readBytes(Socket socket, int contentLength) throws IOException {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            buffer.append((char)socket.getInputStream().read());
        }
        return buffer.toString();
    }

    private void readHeaderFields(Socket socket) throws IOException {
        String headerLine;
        while (!(headerLine = HttpMessage.readLine(socket)).isBlank()) {
            int colonPos = headerLine.indexOf(':');
            String headerField = headerLine.substring(0, colonPos);
            String headerValue = headerLine.substring(colonPos+1).trim();
            headerFields.put(headerField, headerValue);
        }
    }

    static String readLine(Socket socket) throws IOException {
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = socket.getInputStream().read()) != '\r') {
            buffer.append((char)c);
        }
        int expectedNewline = socket.getInputStream().read();
        assert expectedNewline == '\n';
        return buffer.toString();
    }
    public void write(Socket socket) throws IOException {
        String response = lineStart + "\r\n"+
                "Content-Length: " + messageBody.length() + "\r\n" +
                "Content-Type: text/html;charset=utf-8; " +  "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                messageBody;
        socket.getOutputStream().write(response.getBytes());
    }
}
