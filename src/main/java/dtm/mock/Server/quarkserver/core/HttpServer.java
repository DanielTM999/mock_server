package dtm.mock.server.quarkserver.core;

import dtm.mock.server.quarkserver.security.ServerConfiguration;

public interface HttpServer {
    void setPort(int port);
    void setHost(String host);
    void enablePrintTrace();
    void setConfiguration(ServerConfiguration configuration);
    void setRouteExecutor(RouteExecutor routeExecutor);
    void start(Runnable onStart);
    void start();
}
