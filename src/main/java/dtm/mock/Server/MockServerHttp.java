package dtm.mock.Server;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;

import dtm.mock.core.MockServer;
import dtm.mock.core.enums.HttpMethod;

public class MockServerHttp implements MockServer{
    private HttpServer server;  
    private int port = 8081;
    private final Map<String, MockServerModel> mockEndpoints;

    public MockServerHttp(int port){
        this.port = port;
        mockEndpoints = new HashMap<>();
    }

    @Override
    public void registerEndpoint(String endpoint, HttpMethod httpMethods, Class<?> mockResponse) {
        try {
            Object responseInstance = mockResponse.getDeclaredConstructor().newInstance();
            MockServerModel model = new MockServerModel();
            model.setEndpoint(endpoint);
            model.setHttpMethod(httpMethods);
            model.setResponse(responseInstance);
            mockEndpoints.put(endpoint, model);
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
            mockEndpoints.put(endpoint, model);
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
        mockEndpoints.put(endpoint, model);
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void run() throws Exception{
        server = HttpServer.create(new InetSocketAddress(port), 0);
        mockEndpoints.forEach((k, v) -> {
            server.createContext(k, new MockServerhandler(v));
        });
        server.setExecutor(null);
        server.start();
        System.out.println(String.format("Servidor MOCK rodando na porta %d...", port));
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
