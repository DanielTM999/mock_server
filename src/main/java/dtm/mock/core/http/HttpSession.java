package dtm.mock.core.http;

public interface HttpSession {
    void putSession(String key, Object value);
    Object getSession(String key);
}
