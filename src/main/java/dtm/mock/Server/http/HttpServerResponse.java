package dtm.mock.Server.http;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import dtm.mock.core.http.HttpResponse;

public class HttpServerResponse implements HttpResponse{

    private StringBuilder response;
    private SocketChannel cliente;
    private int statusCode;
    private Map<String, String> headers;

    public HttpServerResponse(SocketChannel cliente, String host){
        response = new StringBuilder();
        this.headers = new HashMap<>();
        this.headers.put("Date", ZonedDateTime.now(java.time.ZoneOffset.UTC).toString());
        this.headers.put("Server", "ServerHTTP/1.0");
        this.headers.put("Host", host);
        this.cliente = cliente;
        statusCode = 200;
    }

    @Override
    public HttpResponse addHeader(String s, String value) {
        this.headers.put(s, value);
        return this;
    }

    @Override
    public HttpResponse append(String s) {
        response.append(s);
        return this;
    }

    @Override
    public HttpResponse append(int s) {
        response.append(s);
        return this;
    }

    @Override
    public HttpResponse append(double s) {
        response.append(s);
        return this;
    }

    @Override
    public HttpResponse append(boolean s) {
        response.append(s);
        return this;
    }

    @Override
    public HttpResponse append(BigDecimal s) {
        response.append(s);
        return this;
    }

    @Override
    public HttpResponse append(Object s) {
        response.append(s);
        return this;
    }

    @Override
    public void writer() {
        try {
            byte[] bs = builderResponse().getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.wrap(bs);
            cliente.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void statusCode(int code) {
        this.statusCode = code;
    }

    private String builderResponse(){
        StringBuilder finalResponse = new StringBuilder();
        finalResponse.append(getCode()).append("\r\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            finalResponse.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        finalResponse.append("Content-Length: ").append(response.toString().getBytes(StandardCharsets.UTF_8).length).append("\r\n");
        finalResponse.append("\r\n");
        finalResponse.append(response.toString());
        return finalResponse.toString();
    }

    private String getCode() {
        if (statusCode >= 200 && statusCode < 300) {
            return "HTTP/1.1 200 OK";
        } else if (statusCode >= 300 && statusCode < 400) {
            return "HTTP/1.1 3xx Redirection";
        } else if (statusCode >= 400 && statusCode < 500) {
            return "HTTP/1.1 4xx Client Error";
        } else if (statusCode >= 500 && statusCode < 600) {
            return "HTTP/1.1 500 Internal Server Error";
        } else {
            return "HTTP/1.1 200 OK";
        }
    }

    
}