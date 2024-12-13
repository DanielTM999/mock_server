package dtm.mock.server.quarkserver.io;

import java.net.InetSocketAddress;
import java.util.Map;
import dtm.mock.server.quarkserver.core.HttpServerRequest;
import dtm.mock.server.quarkserver.core.HttpSession;
import lombok.Data;

@Data
class HttpRequestImple implements HttpServerRequest{

    private String httpMethod;
    private String route;
    private String body;
    private String protocol;
    private Map<String, String> headers;
    private HttpSession session;
    private InetSocketAddress inetSocketAddress;

    @Override
    public String getHeader(String key) {
        return headers.getOrDefault(key, null);
    }
}
