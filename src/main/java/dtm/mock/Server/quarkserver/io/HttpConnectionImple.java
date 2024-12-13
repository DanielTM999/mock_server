package dtm.mock.server.quarkserver.io;


import dtm.mock.server.quarkserver.core.HttpConnection;
import dtm.mock.server.quarkserver.core.HttpServerRequest;
import dtm.mock.server.quarkserver.core.HttpServerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class HttpConnectionImple implements HttpConnection{
    private HttpServerRequest request;
    private HttpServerResponse response;
}
