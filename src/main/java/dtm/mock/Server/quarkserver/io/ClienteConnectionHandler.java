package dtm.mock.server.quarkserver.io;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import dtm.mock.server.quarkserver.core.HttpServerRequest;
import dtm.mock.server.quarkserver.core.HttpSession;
import dtm.mock.server.quarkserver.core.RouteExecutor;
import dtm.mock.server.quarkserver.exceptions.ToManyRequestException;
import dtm.mock.server.quarkserver.io.writer.RequestWriter;
import dtm.mock.server.quarkserver.security.IpRateLimiter;
import dtm.mock.server.quarkserver.security.ServerConfiguration;


public class ClienteConnectionHandler<T> implements CompletionHandler<AsynchronousSocketChannel, T>{
    private T context;
    private final RouteExecutor routeExecutor;
    private final AsynchronousServerSocketChannel asynchronousServerSocketChannel;
    private final ServerConfiguration configuration;
    private static Map<String, IpRateLimiter> ipRateLimiter;

    public ClienteConnectionHandler(T context, RouteExecutor routeExecutor, ServerConfiguration configuration, AsynchronousServerSocketChannel asynchronousServerSocketChannel){
        this.context = context;
        this.routeExecutor = routeExecutor;
        this.asynchronousServerSocketChannel = asynchronousServerSocketChannel;
        this.configuration = (configuration != null) ? configuration : new ServerConfiguration(false, false, -1, -1, new HashSet<>());
    }

    @Override
    public void completed(AsynchronousSocketChannel clientChannel, T arg1) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        clientChannel.read(buffer, null, new CompletionHandler<Integer, T>() {
            @Override
            public void completed(Integer result, T attachment){
                if(result > 0){
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    dispachRequest(new String(bytes), clientChannel);
                    
                }
                acceptConnection();
            }

            @Override
            public void failed(Throwable arg0, T arg1) {
                RequestWriter.write(clientChannel, arg0.getMessage(), null, 500);
                acceptConnection();
            }
        });
    }

    @Override
    public void failed(Throwable arg0, T arg1) {
        acceptConnection();
    }

    private void acceptConnection() {
        asynchronousServerSocketChannel.accept(null, new ClienteConnectionHandler<>(context, routeExecutor, configuration, asynchronousServerSocketChannel));
    }

    private void dispachRequest(String request, AsynchronousSocketChannel clientChannel){
        try {
            isPayloadSizeExceeded(request.getBytes());
            SocketAddress remoteAddress = clientChannel.getRemoteAddress();
            String ip = "unknow";
            InetSocketAddress inetSocketAddress = null;
            if (remoteAddress instanceof InetSocketAddress inetSocketAddressCasting){
                inetSocketAddress = inetSocketAddressCasting;
                ip = inetSocketAddress.getAddress().getHostAddress();
            }
            allowedIp(ip);
            validLimitRating(clientChannel, ip);
            HttpServerResponseImple response = new HttpServerResponseImple();
            if(configuration.isCors()){
                response.addHeader("Access-Control-Allow-Origin", "null");
                response.addHeader("Access-Control-Allow-Methods", "");
                response.addHeader("Access-Control-Allow-Headers", "");
            }

            this.routeExecutor.execute(HttpConnectionImple.builder()
                .request(buildRequest(request, ip, inetSocketAddress))
                .response(response)
                .build()
            );
            response.send(clientChannel);
        } catch (ToManyRequestException e) {
            RequestWriter.write(clientChannel, e.getMessage(), new HashMap<>(){{
                put("tryIn", "60");
            }}, 429);
        }catch (Exception e) {
            RequestWriter.write(clientChannel, e.getMessage(), null, 500);
        }
    }

    private HttpServerRequest buildRequest(String request, String ip, InetSocketAddress inetSocketAddress) throws Exception{
        try {
            HttpRequestImple httpRequest = new HttpRequestImple();

            String[] lines = request.split("\\r?\\n");
    
            if (lines.length > 0) {
                String[] firstLineParts = lines[0].split(" ");
                if (firstLineParts.length == 3){
                    httpRequest.setHttpMethod(sanitizeInput(firstLineParts[0]));
                    httpRequest.setRoute(sanitizeInput(firstLineParts[1])); 
                    httpRequest.setProtocol(sanitizeInput(firstLineParts[2]));
                }
            }
            Map<String, String> headers = new HashMap<>();
            int i = 1;
            for (; i < lines.length; i++){
                String line = lines[i];
                if (line.isEmpty()){
                    break;
                }
                String[] headerParts = line.split(":", 2);
                if (headerParts.length == 2) {
                    headers.put(sanitizeInput(headerParts[0].trim()), sanitizeInput(headerParts[1].trim())); 
                }
            }
            httpRequest.setHeaders(headers);
    
            StringBuilder body = new StringBuilder();
            for (i = i + 1; i < lines.length; i++) {
                body.append(sanitizeInput(lines[i])).append("\n");
            }
            httpRequest.setBody(body.toString().trim());
    
            httpRequest.setInetSocketAddress(inetSocketAddress);
            
            SessionStorageServer storageServer = SessionStorageServer.getInstance();
            HttpSession session;
            if(storageServer.containsSession(ip)){
                session = storageServer.getSession(ip);
                if(session == null){
                    session = new HttpSessionImple();
                }
            }else{
                session = new HttpSessionImple();
            }
    
            storageServer.add(ip, session);
            httpRequest.setSession(session);
    
            return httpRequest;
        } catch (Exception e) {
            throw new Exception("not HTTP request valid");
        }
    }
    
    public String sanitizeInput(String input) {
        if(configuration.isSanitizeInput()){
            String sanitized = input.replaceAll("&", "&amp;")
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("\"", "&quot;")
                    .replaceAll("'", "&#39;");
                sanitized = sanitized.replaceAll("[;`&|\\$\\(\\)\\{\\}]", "");
        
            return sanitized;
        }
        return input;
    }

    private void startMap(){
        if(ipRateLimiter == null){
            ipRateLimiter = new ConcurrentHashMap<>();
        }
    }

    private void validLimitRating(AsynchronousSocketChannel clientChannel, String ip) throws ToManyRequestException{
        if(configuration.getLimitReq() > 1){
            startMap();
            AtomicReference<IpRateLimiter> limiterRef = new AtomicReference<>(ipRateLimiter.getOrDefault(ip, new IpRateLimiter()));
            IpRateLimiter limiter = limiterRef.get();

            if (limiter.getResetTime() == null || limiter.getResetTime().isBefore(Instant.now())) {
                limiter.setResetTime(Instant.now().plusSeconds(60));
                limiter.getRequestCount().set(0);
            }
            
            ipRateLimiter.put(ip, limiter);
            int count = limiter.getRequestCount().incrementAndGet();

            if(count >= configuration.getLimitReq()){
                limiter.getBlock().set(true);
                throw new ToManyRequestException("Too Many Requests");
            }
            

        }
    } 

    private void isPayloadSizeExceeded(byte[] requestBody) throws Exception{
        if (configuration.getMaxPayloadSize() != -1 && requestBody.length > configuration.getMaxPayloadSize()) {
            throw new Exception("Payload size exceeds the maximum allowed limit.");
        }

    }

    private void allowedIp(String ip) throws Exception{
        Set<String> blockedIPs = configuration.getBlockedIPs();
        if(configuration.getBlockedIPs() != null){
            if(blockedIPs.contains(ip)){
                throw new Exception(String.format("The IP address '%s' is blocked and cannot access.", ip));
            }
        }
    }

}
