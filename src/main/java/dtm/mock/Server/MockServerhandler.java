package dtm.mock.Server;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dtm.mock.anotations.MockHttpStatusCode;

public class MockServerhandler implements HttpHandler{
    private MockServerModel serverModel;

    public MockServerhandler(MockServerModel serverModel){
        this.serverModel = serverModel;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(validEndPoint(exchange)){
            String response = "{}";
            int statusCode = 200;
            if(serverModel.getResponse() != null){
                if(serverModel.getResponse() instanceof String){
                    response = serverModel.getResponse().toString();
                    exchange.sendResponseHeaders(statusCode, response.length());
                }else{
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        response = mapper.writeValueAsString(serverModel.getResponse());
                        statusCode = getCode(serverModel.getResponse());
                    } catch (Exception e) {
                        String error = "{\"serverError\": \"%s\"}";
                        response = String.format(error, (e.getMessage() == null) ? "" : e.getMessage());
                        statusCode = 500;
                    }
                    exchange.sendResponseHeaders(statusCode, response.length());
                }
            }
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private int getCode(Object reponse){
        if(reponse.getClass().isAnnotationPresent(MockHttpStatusCode.class)){
            MockHttpStatusCode httpStatusCode = reponse.getClass().getAnnotation(MockHttpStatusCode.class);
            return httpStatusCode.statusCode();
        }
        return 200;
    }
    
    private boolean validEndPoint(HttpExchange exchange) throws IOException{
        String requestedEndpoint = exchange.getRequestURI().getPath();
        String httpMethod = exchange.getRequestMethod();
        
        if (!httpMethod.equalsIgnoreCase(serverModel.getHttpMethod().toString())) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return false;
        }

        String endpointPattern = serverModel.getEndpoint().replaceAll("\\{[^/]+\\}", "[^/]+");

        if (!requestedEndpoint.matches(endpointPattern)) {
            sendResponse(exchange, 404, "Not Found");
            return false;
        }

        return true;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        exchange.sendResponseHeaders(statusCode, responseBody.length());
        OutputStream os = exchange.getResponseBody();
        os.write(responseBody.getBytes());
        os.close();
    }

}
