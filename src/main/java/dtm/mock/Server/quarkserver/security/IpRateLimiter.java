package dtm.mock.server.quarkserver.security;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
public class IpRateLimiter {
    @Setter
    private Instant resetTime;
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final AtomicBoolean block = new AtomicBoolean(false);
}
