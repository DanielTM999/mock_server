package dtm.mock.server.quarkserver.core;

import java.math.BigDecimal;

public interface HttpServerResponse {
    HttpServerResponse append(String s);
    HttpServerResponse append(int s);
    HttpServerResponse append(double s);
    HttpServerResponse append(boolean s);
    HttpServerResponse append(BigDecimal s);
    HttpServerResponse append(Object s);
    HttpServerResponse contentType(String mimeType);

    HttpServerResponse addHeader(String key, String value);

    HttpServerResponse addCookie(String name, String value);
    HttpServerResponse addCookie(String name, String value, int maxAge, String path, boolean secure, boolean httpOnly);

    HttpServerResponse redirect(String location);

    void statusCode(int code);
}
