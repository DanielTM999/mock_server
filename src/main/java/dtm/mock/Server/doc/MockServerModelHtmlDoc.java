package dtm.mock.server.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dtm.mock.server.MockServerModel;

public final class MockServerModelHtmlDoc extends MockServerModel{
    
    private List<MockServerModel> mockServerModels;

    private String getHtmlString(){
        StringBuilder htmlBuilder = new StringBuilder()
            .append("<!DOCTYPE html>")
            .append("<html lang=\"en\">")

            .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<title>Mock API Documentation</title>")
                .append(getCss())
            .append("</head>")

            .append("<body>")
                .append("<div class='container'>")
                    .append("<h1>Mock API Documentation</h1>");

        this.mockServerModels = defineMockServerModels(getResponse());
        for (MockServerModel mockServerModel : this.mockServerModels) {
            htmlBuilder.append("<div class='endpoint'>")
                .append("<span class='method ").append(mockServerModel.getHttpMethod()).append("'>")
                .append(mockServerModel.getHttpMethod()).append("</span>")
                .append(" <strong>").append(mockServerModel.getEndpoint()).append("</strong>")
                .append("<div class='response-box'><strong>Response:</strong> ")
                .append("<pre>").append(mockServerModel.getResponse()).append("</pre>")
                .append("</div>")
                .append("</div>");
        }

        htmlBuilder.append("</div></body></html>");

        return htmlBuilder.toString();
    }

    private String getCss(){
        return """
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 20px;
                    background-color: #f8f9fa;
                }
                .container {
                    max-width: 800px;
                    margin: auto;
                    background: white;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                }
                h1 {
                    text-align: center;
                    color: #333;
                }
                .endpoint {
                    background-color: #f4f4f4;
                    padding: 10px;
                    border-radius: 5px;
                    margin: 10px 0;
                }
                .method {
                    font-weight: bold;
                    color: white;
                    padding: 5px 10px;
                    border-radius: 3px;
                }
                .GET { background-color: #4CAF50; }
                .POST { background-color: #2196F3; }
                .PUT { background-color: #FF9800; }
                .DELETE { background-color: #F44336; }
                .response-box {
                    background: #ddd;
                    padding: 10px;
                    margin-top: 5px;
                    border-radius: 5px;
                    white-space: pre-wrap;
                    font-family: monospace;
                }
            </style>
        """;
    }

    @SuppressWarnings("unchecked")
    private List<MockServerModel> defineMockServerModels(Object response){
        try {
            if (response instanceof Map<?, ?> map) {
                return (List<MockServerModel>)map.get("content");
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String toString(){
        return getHtmlString();
    }
}
