package dtm.mock.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtm.mock.anotations.MockHttpStatusCode;
import dtm.mock.core.http.HttpAction;
import dtm.mock.core.http.HttpRequest;
import dtm.mock.core.http.HttpResponse;



public class MockServerhandler implements HttpAction{
    private List<MockServerModel> serverModelList;
    private MockServerModel serverModel;

    public MockServerhandler(List<MockServerModel> serverModels){
        this.serverModelList = serverModels;
    }

    @Override
    public void execute(HttpRequest request, HttpResponse response){
        try {
            if(validEndPoint(response, request)){
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
    
                    sendResponse(response, statusCode, responseValue, new HashMap<>(){{
                        put("Content-Type", "application/json");
                    }});
                }
            }
        } catch (Exception e) {
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
    
    private boolean validEndPoint(HttpResponse exchange, HttpRequest req) throws IOException{
        String requestedEndpoint = req.getRoute();
        String httpMethod = req.getHtttpMethod();

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

    private void sendResponse(HttpResponse resp, int statusCode, String responseBody) throws IOException {
        resp.statusCode(statusCode);
        resp.append(responseBody);
    }

    private void sendResponse(HttpResponse resp, int statusCode, String responseBody, Map<String, String> headers) throws IOException {
        resp.statusCode(statusCode);
        for (Map.Entry<String, String> entry: headers.entrySet()) {
            resp.addHeader(entry.getKey(), entry.getValue());
        }
        resp.append(responseBody);
    }

}
