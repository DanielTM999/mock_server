package dtm.mock.Server;

import dtm.mock.core.enums.HttpMethod;
import lombok.Data;

@Data
public class MockServerModel {
    private String endpoint;
    private HttpMethod httpMethod;
    private Object response;
}
