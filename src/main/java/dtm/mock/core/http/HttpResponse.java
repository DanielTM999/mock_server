package dtm.mock.core.http;

import java.math.BigDecimal;

public interface HttpResponse {
    HttpResponse append(String s);
    HttpResponse append(int s);
    HttpResponse append(double s);
    HttpResponse append(boolean s);
    HttpResponse append(BigDecimal s);
    HttpResponse append(Object s);
    HttpResponse addHeader(String s, String value);
    void statusCode(int code);
    void writer();
}
