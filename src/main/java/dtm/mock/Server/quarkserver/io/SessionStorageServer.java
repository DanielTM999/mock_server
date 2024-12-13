package dtm.mock.server.quarkserver.io;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dtm.mock.server.quarkserver.core.HttpSession;


class SessionStorageServer {
    private static SessionStorageServer sessionStorageServer;
    
    public static SessionStorageServer getInstance(){
        if(SessionStorageServer.sessionStorageServer == null){
            SessionStorageServer.sessionStorageServer = new SessionStorageServer();
        }

        return SessionStorageServer.sessionStorageServer;
    }

    private final Map<String, HttpSession> sessionsLocations;

    private SessionStorageServer(){
        sessionsLocations = new ConcurrentHashMap<>();
    }

    public void add(String ip, HttpSession httpSession){
        sessionsLocations.put(ip, httpSession);
    }

    public boolean containsSession(String ip){
        return sessionsLocations.containsKey(ip);
    }

    public HttpSession getSession(String ip){
        return sessionsLocations.getOrDefault(ip, null);
    }
    
}
