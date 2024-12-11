package dtm.mock.core;

import dtm.mock.core.enums.HttpMethod;

public interface MockServer {
    void registerEndpoint(String endpoint, HttpMethod httpMethods, Class<?> mockResponse);
    void registerEndpoint(String endpoint, HttpMethod httpMethods, Object mockResponse);
    void registerEndpoint(String endpoint, HttpMethod httpMethods, String mockResponse, boolean jsonFile);
    void registerEndpoint(Object mockResponse);
    void registerEndpoint(Class<?> mockResponse);
    void setPort(int port);
    void run() throws Exception;
}
