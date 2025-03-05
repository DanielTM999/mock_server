package dtm.mock.core;

import dtm.mock.core.enums.HttpMethod;

public interface MockServer {
    void registerEndpoint(String endpoint, HttpMethod httpMethods, Class<?> mockResponse);
    void registerEndpoint(String endpoint, HttpMethod httpMethods, Object mockResponse);
    void registerEndpoint(String endpoint, HttpMethod httpMethods, String mockResponse, boolean jsonFile);
    void registerEndpoint(Object mockResponse);
    void registerEndpoint(Class<?> mockResponse);
    void setPort(int port);
    int getPort();
    void run() throws Exception;
    void run(Runnable onStart) throws Exception;
    void enableJsonDoc(String endpoint);
    void enableHtmlDoc(String endpoint);
    void enableServerPrintTrace();
}
