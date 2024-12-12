package dtm.mock;

import dtm.mock.Server.MockServerHttp;
import dtm.mock.core.MockServer;

public class Main {

    public static void main(String[] args) throws Exception{
    
        MockServer mockServer = new MockServerHttp(80);
        mockServer.registerEndpoint(ConsultaBaseNacionalMock.class);
        mockServer.registerEndpoint(ConsultaFipeMock.class);

        mockServer.run();
    }

}
