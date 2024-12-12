package dtm.mock.core.http;



@FunctionalInterface
public interface HttpAction {
    void execute(HttpRequest request, HttpResponse response);
}
