package dtm.mock.server.quarkserver.core;

@FunctionalInterface
public interface RouteExecutor {
    void execute(HttpConnection connection);
}
