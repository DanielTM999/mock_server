package dtm.mock.server.quarkserver.security;


import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ServerConfiguration {
    private boolean sanitizeInput;
    private boolean cors;
    private long limitReq;
    private long maxPayloadSize;
    private Set<String> blockedIPs;

    public ServerConfiguration(){
        sanitizeInput = false;
        cors = false;
        limitReq = -1;
        maxPayloadSize = -1;
        blockedIPs = new HashSet<>();
    }
    
    public void blockedIPsList(Set<String> blockedIPs){
        this.blockedIPs = blockedIPs;
    }

    public void enableSanitizeInput(){
        sanitizeInput = true;
    }

    public long getMaxPayloadSize() {
        return maxPayloadSize;
    }

    public void enableCORS(){
        cors = true;
    }

    public void enablereqLimitRating(long limitReq){
        this.limitReq = limitReq;
    }

}
