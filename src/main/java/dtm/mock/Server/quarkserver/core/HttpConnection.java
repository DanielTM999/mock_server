package dtm.mock.server.quarkserver.core;

public interface HttpConnection {
    HttpServerRequest getRequest();
    HttpServerResponse getResponse();
}
