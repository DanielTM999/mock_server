package dtm.mock;

import dtm.mock.anotations.MockHttpRouteConfiguaration;
import dtm.mock.core.enums.HttpMethod;
import lombok.Data;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Data
@MockHttpRouteConfiguaration(route = "/sexo", httpMethod = HttpMethod.GET)
public class ConsultaFipeMock {
    private List<ConsultaFipe> data = new ArrayList<>();

    @Data
    @ToString
    public static class ConsultaFipe {
        private int valor = 2200;

        private String codigoFipe = "12323";
        private int anoModelo = 2023;
        private int anoFabricacao = 2022;

        private String mesReferencia = "fevereiro";

        private String marca = "ford";

        private String modelo = "SUV";
        private boolean principal = true;
    }
}