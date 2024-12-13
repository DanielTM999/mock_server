package dtm.mock.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtm.mock.anotations.MockHttpStatusCode;
import dtm.mock.server.quarkserver.core.HttpConnection;
import dtm.mock.server.quarkserver.core.HttpServerRequest;
import dtm.mock.server.quarkserver.core.HttpServerResponse;
import dtm.mock.server.quarkserver.core.RouteExecutor;



public class MockServerhandler implements RouteExecutor{
    private List<MockServerModel> serverModelList;
    private MockServerModel serverModel;

    public MockServerhandler(List<MockServerModel> serverModels){
        this.serverModelList = serverModels;
    }

    @Override
    public void execute(HttpConnection arg0) {
        try {
            HttpServerResponse responseServer = arg0.getResponse();
            HttpServerRequest requestServer = arg0.getRequest();
            if(validEndPoint(responseServer, requestServer)){
                String responseValue = "{}";
                int statusCode = 200;
                
                if(serverModel.getResponse() != null){
                    if(serverModel.getResponse() instanceof String){
                        responseValue = serverModel.getResponse().toString();
                    }else{
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            responseValue = mapper.writeValueAsString(serverModel.getResponse());
                            statusCode = getCode(serverModel.getResponse());
                        } catch (Exception e) {
                            String error = "{\"serverError\": \"%s\"}";
                            responseValue = String.format(error, (e.getMessage() == null) ? "" : e.getMessage());
                            statusCode = 500;
                        }
                    }
    
                    sendResponse(responseServer, statusCode, responseValue, new HashMap<>(){{
                        put("Content-Type", "application/json");
                    }});
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCode(Object reponse){
        if(reponse.getClass().isAnnotationPresent(MockHttpStatusCode.class)){
            MockHttpStatusCode httpStatusCode = reponse.getClass().getAnnotation(MockHttpStatusCode.class);
            return httpStatusCode.statusCode();
        }
        return 200;
    }
    
    private boolean validEndPoint(HttpServerResponse exchange, HttpServerRequest req) throws IOException{
        String requestedEndpoint = req.getRoute();
        String httpMethod = req.getHttpMethod();

        for (MockServerModel mockServerModel : serverModelList) {
            String endpointPattern = mockServerModel.getEndpoint().replaceAll("\\{[^/]+\\}", "[^/]+");

            if(requestedEndpoint.matches(endpointPattern)){
                this.serverModel = mockServerModel;
                break;
            }

        }

        if (serverModel == null || !httpMethod.equalsIgnoreCase(serverModel.getHttpMethod().toString())) {
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

    private void sendResponse(HttpServerResponse resp, int statusCode, String responseBody) throws IOException {
        resp.statusCode(statusCode);
        resp.append(responseBody);
    }

    private void sendResponse(HttpServerResponse resp, int statusCode, String responseBody, Map<String, String> headers) throws IOException {
        resp.statusCode(statusCode);
        for (Map.Entry<String, String> entry: headers.entrySet()) {
            resp.addHeader(entry.getKey(), entry.getValue());
        }
        resp.append(responseBody);
    }


}
