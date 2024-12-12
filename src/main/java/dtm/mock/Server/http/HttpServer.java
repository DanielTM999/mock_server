package dtm.mock.Server.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import dtm.mock.core.http.HttpAction;
import dtm.mock.core.http.HttpResponse;
import dtm.mock.core.http.HttpSession;

public class HttpServer {
    private ServerSocketChannel listener;
    private InetSocketAddress address;
    private String host;
    private int port = 8081;
    private Map<String, HttpSession> cashServer;
    private final ExecutorService executors;

    public HttpServer(int port){
        executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        cashServer = new HashMap<>();
        this.port = port;
        this.host = "127.0.0.1";
    }

    public void start(HttpAction action){
        start(action, null);
    }

    public void start(HttpAction action, String startMessage){
        address = new InetSocketAddress(this.host, this.port);
        try {

            listener = ServerSocketChannel.open();
            listener.bind(address);
            String hostServer = listener.getLocalAddress().toString();
            if(startMessage != null){
                System.out.println(startMessage);
            }
            while (true) {
                executors.execute(() -> {
                    try {
                        SocketChannel cliente = listener.accept();
                        InetSocketAddress remoteAddress = (InetSocketAddress) cliente.getRemoteAddress();
                        String ip = remoteAddress.getHostName();
                        HttpSession session = putCashIfNotExists(ip);
                        String reqBase = readRequestBase(cliente);
                        Map<String, String> headers = formatHeaders(reqBase);
                        String body = reqBase.substring(reqBase.indexOf("\r\n\r\n"), reqBase.length());
                        String htttpMethod = getMethod(reqBase);
                        String route = getRoute(reqBase);
                        HttpResponse response = new HttpServerResponse(cliente, hostServer);
                        action.execute(new HttpServerRequest(headers, body, htttpMethod, route, session), response);
                        try {
                            response.writer();
                        } catch (Exception e) {
                        }
                        cliente.close();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }finally{
            executors.shutdown();
            try {
                if (!executors.awaitTermination(60, TimeUnit.SECONDS)) { 
                    executors.shutdownNow(); 
                } 
                if (listener != null) {
                    listener.close(); 
                }
            } catch (InterruptedException | IOException ex) {
                executors.shutdownNow();
                Thread.currentThread().interrupt(); 
            }
        }
        
    }

    private String readRequestBase(SocketChannel cliente) throws Exception{
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = cliente.read(buffer);
        if (bytesRead == -1) {
            cliente.close();
            return null;
        }
        buffer.flip();
        String requestData = StandardCharsets.UTF_8.decode(buffer).toString();
        buffer.clear();
        return requestData;
    }

    private Map<String, String> formatHeaders(String requestData){
        requestData.substring(0, requestData.indexOf("\r\n\r\n"));
        Map<String, String> headers = new HashMap<>(); 
        int tail = 0;
        int current = tail + 1;
        String[] headersSplip = requestData.split(":");
        
        if(headersSplip.length % 2 == 0){
            for (int i = 0; i < (headersSplip.length / 2); i++) {
                headers.put(headersSplip[tail], headersSplip[current]);
                tail++;
                current++;
            }
        }


        return headers;
    }

    private String getMethod(String reqBase){
        String httpMethodName = reqBase.substring(0, reqBase.indexOf(" "));
        return httpMethodName;
    }

    private String getRoute(String reqBase){
        String route = reqBase.substring(reqBase.indexOf("/"), reqBase.length());
        route = route.substring(0, route.indexOf(" "));
        return route;
    }

    private HttpSession putCashIfNotExists(String ip){
        HttpSession session = new HttpServerSession();
        synchronized(cashServer){
            if(!cashServer.containsKey(ip)){
                cashServer.put(ip, session);
            }else{
                session = cashServer.get(ip);
            }
        }

        return session;
    }

}
