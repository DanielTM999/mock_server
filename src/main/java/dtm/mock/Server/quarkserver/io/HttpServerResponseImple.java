package dtm.mock.server.quarkserver.io;

import java.math.BigDecimal;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Map;
import dtm.mock.server.quarkserver.core.HttpServerResponse;
import dtm.mock.server.quarkserver.io.writer.RequestWriter;


class HttpServerResponseImple implements HttpServerResponse{

    private final StringBuilder stringBuilder;
    private final Map<String, String> headers;
    private int statusCode;

    public HttpServerResponseImple(){
        stringBuilder = new StringBuilder();
        headers = new HashMap<>();
        statusCode = 200;
    }

    @Override
    public HttpServerResponse append(String s) {
        stringBuilder.append(s);
        return this;
    }

    @Override
    public HttpServerResponse append(int s) {
        stringBuilder.append(s);
        return this;
    }

    @Override
    public HttpServerResponse append(double s) {
        stringBuilder.append(s);
        return this;
    }

    @Override
    public HttpServerResponse append(boolean s) {
        stringBuilder.append(s);
        return this;
    }

    @Override
    public HttpServerResponse append(BigDecimal s) {
        stringBuilder.append(s);
        return this;
    }

    @Override
    public HttpServerResponse append(Object s) {
        stringBuilder.append(s);
        return this;
    }

    @Override
    public HttpServerResponse contentType(String mimeType) {
        headers.put("Content-Type", mimeType);
        return this;
    }

    @Override
    public HttpServerResponse addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    @Override
    public HttpServerResponse addCookie(String name, String value) {
        headers.put("Set-Cookie", name + "=" + value);
        return this;
    }

    @Override
    public HttpServerResponse addCookie(String name, String value, int maxAge, String path, boolean secure,boolean httpOnly) {
        StringBuilder cookie = new StringBuilder();
        cookie.append(name).append("=").append(value);

        if (maxAge > 0) {
            cookie.append("; Max-Age=").append(maxAge);
        }
        
        if (path != null && !path.isEmpty()) {
            cookie.append("; Path=").append(path);
        }
        
        if (secure) {
            cookie.append("; Secure");
        }
        
        if (httpOnly) {
            cookie.append("; HttpOnly");
        }

        headers.put("Set-Cookie", cookie.toString());
        return this;
    }

    @Override
    public HttpServerResponse redirect(String location) {
        headers.put("Location", location);
        statusCode = 302;
        return this;
    }

    @Override
    public void statusCode(int code) {
        this.statusCode = code;
    }
    
    public void send(AsynchronousSocketChannel clientChannel){
        RequestWriter.write(clientChannel, stringBuilder.toString(), headers, statusCode);
    }

}
