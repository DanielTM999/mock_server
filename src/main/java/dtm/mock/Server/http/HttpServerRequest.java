package dtm.mock.Server.http;

import java.util.Map;

import dtm.mock.core.http.HttpRequest;
import dtm.mock.core.http.HttpSession;

public class HttpServerRequest implements HttpRequest{

    private Map<String, String> headers;
    private String body;
    private String method;
    private String route;
    private HttpSession session;

    public HttpServerRequest(Map<String, String> headers, String body, String method, String route, HttpSession session) {
        this.headers = headers;
        this.body = body;
        this.method = method;
        this.route = route;
        this.session = session;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getHeader(String key) {
        if(headers.containsKey(key)){
            return headers.get(key);
        }
        return null;
    }

    @Override
    public String getBody() {
       return body;
    }


    @Override
    public String getHtttpMethod() {
        return method;
    }


    @Override
    public String getRoute() {
        return route;
    }

    @Override
    public HttpSession getSession() {
        return session;
    }
    
}