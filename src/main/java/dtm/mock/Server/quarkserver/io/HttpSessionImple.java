package dtm.mock.server.quarkserver.io;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import dtm.mock.server.quarkserver.core.HttpSession;


class HttpSessionImple implements HttpSession{

    private Instant instant;
    private long expirationTime;
    private final Map<String, Object> sessionValues;

    public HttpSessionImple(){
        sessionValues = new ConcurrentHashMap<>();
        expirationTime = -1;
    }

    @Override
    public void insert(String key, Object value) {
        sessionValues.put(key, value);
    }

    @Override
    public Object getSession(String key) {
        return sessionValues.getOrDefault(key, null);
    }

    @Override
    public <T> T getSession(String key, Class<T> type) {
        try {
            Object value = sessionValues.getOrDefault(key, null);
            return type.cast(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Optional<Instant> getExpirationTime() {
        if(expirationTime < 0){
            return Optional.empty();
        }
        return Optional.ofNullable(instant);
    }

    @Override
    public void setExpirationTime(long seconds){
        instant = Instant.now().plusSeconds(seconds);
    }

    @Override
    public void deleteElement(String key) {
        sessionValues.remove(key);
    }


}
