package dtm.mock.server;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import dtm.mock.anotations.MockHttpRouteConfiguaration;
import dtm.mock.core.MockServer;
import dtm.mock.core.enums.HttpMethod;
import dtm.mock.server.quarkserver.core.HttpServer;
import dtm.mock.server.quarkserver.io.HttpServerIO;


public class MockServerHttp implements MockServer{
    private HttpServer server;  
    private int port = 8081;
    private final List<MockServerModel> mockEndpoints;

    public MockServerHttp(int port){
        this.port = port;
        mockEndpoints = new ArrayList<>();
    }

    @Override
    public void registerEndpoint(Object mockResponse){
        Class<?> mockResponseClass = mockResponse.getClass();

        String className = mockResponseClass.getName();
        String endpoint = className.replace('.', '/');
        HttpMethod httpMethod = HttpMethod.GET;

        if(mockResponseClass.isAnnotationPresent(MockHttpRouteConfiguaration.class)){
            MockHttpRouteConfiguaration configuaration = mockResponseClass.getAnnotation(MockHttpRouteConfiguaration.class);
            endpoint = configuaration.route();
            httpMethod = configuaration.httpMethod();
        }

        MockServerModel model = new MockServerModel();
        model.setEndpoint(endpoint);
        model.setHttpMethod(httpMethod);
        model.setResponse(mockResponse);
        mockEndpoints.add(model);

    }

    @Override
    public void registerEndpoint(Class<?> mockResponse){
        try {
            Object responseInstance = mockResponse.getDeclaredConstructor().newInstance();
            registerEndpoint(responseInstance);
        }catch (Exception e) {
            throw new RuntimeException("Failed to create mock response instance", e);
        }
    }

    @Override
    public void registerEndpoint(String endpoint, HttpMethod httpMethods, Class<?> mockResponse) {
        try {
            Object responseInstance = mockResponse.getDeclaredConstructor().newInstance();
            MockServerModel model = new MockServerModel();
            model.setEndpoint(endpoint);
            model.setHttpMethod(httpMethods);
            model.setResponse(responseInstance);
            mockEndpoints.add(model);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create mock response instance", e);
        }
    }

    @Override
    public void registerEndpoint(String endpoint, HttpMethod httpMethods, String mockResponse, boolean jsonFile) {
        try {
            String response = mockResponse;
            if(jsonFile){
                response = readJson(mockResponse);
            }
            MockServerModel model = new MockServerModel();
            model.setEndpoint(endpoint);
            model.setHttpMethod(httpMethods);
            model.setResponse(response);
            mockEndpoints.add(model);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create mock response instance", e);
        }
    }

    @Override
    public void registerEndpoint(String endpoint, HttpMethod httpMethods, Object mockResponse) {
        MockServerModel model = new MockServerModel();
        model.setEndpoint(endpoint);
        model.setHttpMethod(httpMethods);
        model.setResponse(mockResponse);
        mockEndpoints.add(model);
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void run() throws Exception{
        server = new HttpServerIO(port, "localhost");
        server.setRouteExecutor(new MockServerhandler(mockEndpoints));
        server.start();
    }

    private String readJson(String path) throws Exception{
        File json = new File(path);

        if (!json.exists() || !json.isFile() || !path.endsWith(".json")) {
            throw new IllegalArgumentException("O arquivo deve ser um JSON válido.");
        }

        byte[] contentBytes = Files.readAllBytes(Paths.get(json.getAbsolutePath()));
        return new String(contentBytes, "UTF-8");
    }
}
