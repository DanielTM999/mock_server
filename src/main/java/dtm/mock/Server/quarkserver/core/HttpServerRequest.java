package dtm.mock.server.quarkserver.core;

import java.net.InetSocketAddress;
import java.util.Map;

public interface HttpServerRequest {
    String getHttpMethod();
    String getRoute();
    String getProtocol();
    Map<String, String> getHeaders();
    String getHeader(String key);
    String getBody();
    HttpSession getSession();
    InetSocketAddress getInetSocketAddress();
}
