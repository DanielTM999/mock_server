package dtm.mock.server.quarkserver.io.writer;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestWriter {
    
    public static void write(AsynchronousSocketChannel clientChannel, String response, Map<String, String> headers, int statusCode){
        if(headers == null){
            headers = new HashMap<>();
        }
        StringBuilder finalResponse = new StringBuilder();

        finalResponse.append("HTTP/1.1 ").append(statusCode).append(" ").append(getStatusMessage(statusCode)).append("\r\n");

        int contentLength = response == null ? 0 : response.getBytes(StandardCharsets.UTF_8).length;
        headers.put("Content-Length", String.valueOf(contentLength));

        for (Map.Entry<String, String> header : headers.entrySet()) {
            finalResponse.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        finalResponse.append("\r\n");

        if (response != null && !response.isEmpty()) {
            finalResponse.append(response);
        }

        byte[] responseBytes = finalResponse.toString().getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.wrap(responseBytes);

        clientChannel.write(buffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer result, Void attachment) {
                try {
                    clientChannel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
                try {
                    clientChannel.close();  
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void write(AsynchronousSocketChannel clientChannel, String response, Map<String, String> headers, int statusCode, Map<String, String> cookies) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (cookies != null) {
            for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                headers.put("Set-Cookie", cookie.getKey() + "=" + cookie.getValue() + "; Path=/; HttpOnly");
            }
        }
        write(clientChannel, response, headers, statusCode);
    }

    public static void writeRedirect(AsynchronousSocketChannel clientChannel, String location, int statusCode) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", location);
        write(clientChannel, null, headers, statusCode);
    }

    private static String getStatusMessage(int statusCode) {
        switch (statusCode) {
            case 200:
                return "OK";
            case 301:
                return "Moved Permanently";
            case 302:
                return "Found";
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 500:
                return "Internal Server Error";
            default:
                return "";
        }
    }

}
