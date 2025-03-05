package dtm.mock.server.quarkserver.io;


import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import dtm.mock.server.quarkserver.core.HttpServer;
import dtm.mock.server.quarkserver.core.RouteExecutor;
import dtm.mock.server.quarkserver.security.ServerConfiguration;

public final class HttpServerIO implements HttpServer{
    private boolean printTrace;
    private int port;
    private String host;
    private RouteExecutor routeExecutor;
    private InetSocketAddress inetSocketAddress;
    private ServerConfiguration configuration;
    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public HttpServerIO(){
        configureServerProps(0, null);
    }

    public HttpServerIO(int port){
        configureServerProps(port, null);
    }

    public HttpServerIO(String host){
        configureServerProps(0, host);
    }

    public HttpServerIO(int port, String host){
        configureServerProps(port, host);
    }
    
    @Override
    public void start() {
        start(null);
    }

    @Override
    public void start(Runnable onStart) {
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(this.inetSocketAddress);
            if(routeExecutor == null){
                print("routeExecutor is not Defined", "ERROR", true);
                print("Exit", "ERROR", true);
                if(!printTrace){
                    throw new RuntimeException("routeExecutor is not Defined");
                }
                System.exit(1);
            }

            asynchronousServerSocketChannel.accept(null, new ClienteConnectionHandler<Void>(null, routeExecutor, configuration, asynchronousServerSocketChannel));
            print("server running on: http://"+host+":"+port, "INFO", printTrace);
            if(onStart != null){
                onStart.run();
            }
            while (true) {
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
           print(e.getMessage(), "ERROR", true);
        }
    }

    @Override
    public void setRouteExecutor(RouteExecutor routeExecutor) {
        this.routeExecutor = routeExecutor;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }
    
    @Override
    public void enablePrintTrace() {
        this.printTrace = true;
    }

    @Override
    public void setConfiguration(ServerConfiguration configuration) {
       this.configuration = configuration;
    }

    private void configureServerProps(int port, String host){
        if(host != null && host.equalsIgnoreCase("localhost")){
            this.host = "127.0.0.1";
        }

        if(host == null){
            this.host = "127.0.0.1"; 
        }

        if(port == 0){
            this.port = 80;
        }else{
            this.port = port;
        }

        this.inetSocketAddress = new InetSocketAddress(this.host, this.port);
    }

    private void print(String msg, String type, boolean show){
        if (show) {
            String baseMessage = "[%s]: %s";
            if(type == null){
                type = "INFO";
            }
            if(msg == null){
                msg = "";
            }
            System.out.println(String.format(baseMessage, type, msg));
        }
    }

}
