package dtm.mock.server.quarkserver.core;

import java.time.Instant;
import java.util.Optional;

public interface HttpSession {
    void setExpirationTime(long seconds);
    void deleteElement(String key);
    Optional<Instant> getExpirationTime();
    void insert(String key, Object value);
    Object getSession(String key);
    <T> T getSession(String key, Class<T> type);
}
