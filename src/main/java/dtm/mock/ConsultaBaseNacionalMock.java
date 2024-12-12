package dtm.mock;

import dtm.mock.anotations.MockHttpRouteConfiguaration;
import dtm.mock.core.enums.HttpMethod;
import lombok.Data;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Data
@MockHttpRouteConfiguaration(route = "/GetSenatranPlus/{placa}/{apiKey}", httpMethod = HttpMethod.GET)
public class ConsultaBaseNacionalMock {
    private BaseNacional data = new BaseNacional();
    private int status = 1;
    private String message = "mensagem de teste";
    private Boolean isSucess = true;

    @Data
    @ToString
    public static class BaseNacional {
        private Proprietario proprietario = new Proprietario();
        private Senatran senatran = new Senatran();
        private List<Multa> multas = new ArrayList<>();
        private List<String> csv = new ArrayList<>();

        @Data
        @ToString
        public static class Proprietario {
            private String placa = "AAASSXX";
            private String marcaModelo = "2021";
            private String chassi = "232613";
            private String renavam = "23123";
            private String anoFabricacao = "2003";
            private String anoModelo = "2004";
            private String cor = "rosa bebe";
            private String cpfCnpjProprietario = "11122233345";
            private String nomeProprietario = "José Cleiton";
            private String municipio = "Ruy Barbosa";
            private String ufSigla = "BA";
        }

        @Data
        @ToString
        public static class Senatran {
            private String anoFabricacao = "2007";

            private String anoModelo = "2004";

            private String anoUltimoLicenciamento = "2003";

            private String capacidadePassageiro = "4";

            private String carroceria = "1";

            private String categoria = "ABC";

            private String chassi = "1as12d";

            private String cilindradas = "2";

            private String codigoTipoProprietario = "1";

            private String combustivel = "diesel";

            private String cor = "preta";

            private String cpfCnpjProprietario = "1234567891012131";

            private String cpfCnpjProprietarioAnterior = "11122233345";

            private String dataAquisicao = "2018";

            private String dataAtualizacao = "2019";

            private String dataAtualizacaoMre = "2019";

            private String dataCrlv = "2019";

            private String dataCrv = "2019";

            private String dataDistImportacao = "2019";

            private String dataTransferencia = "2019";

            private String deficienteFisico = "sim";

            private String descricaoRestricaoRfb = "descrição";

            private String descricaoTipoProprietario = "descrição";

            private String docAquisicaoTransferencia = "docAq";

            private String especie = "especie";

            private String marcaModelo = "marca 1";

            private String municipio = "Utinga";

            private String nomeProprietario = "Nara Gaspar";

            private String placa = "ABSCW";

            private String potencia = "12CV";

            private String procedencia = "duvidosa";

            private String renavam = "1121212";

            private String restricaoRenajude = "tem";

            private String restricaoRfb = "tem";

            private String restricao1 = "cabeca";

            private String restricao2 = "ombro";

            private String restricao3 = "joelho";

            private String restricao4 = "pe";

            private String rouboFurto = "foi roubado";

            private String situacao = "roubada";

            private String tipo = "daqueles ruim";

            private String ufSigla = "JAPAO";

            private String cmc = "cms";

            private String cmt = "smt";

            private String indicadorAlarme = "indica";

            private String pbt = "aaaa";

            private String qtdEixos = "780";

            private String indicadorComunicacaoVenda = "indica";

            private String indicadorLeilao = "indica";

            private String indicadorMultaRenainf = "indica";

            private String indicadorPendenciaEmissao = "indica";
            private String indicadorRecallMontadora = "indica";

            private String indicadorOstentaPIV = "indica";

            private String indicadorRestricaoPgfn = "indica";

            private String nomeArrendatario = "João Pessoa";

            private String numeroIdentificacaoArrendatario = "1";

            private String possuidorNome = "José ALdo";

            private String possuidorDocumento = "nao tem";

            private String possuidorOrigem = "possui";

            private String usuarioEhPossuidor = "eh nada";

            private String usuarioEhPrincipalCondutor = "eh as vezes";

            private String usuarioFoiPrincipalCondutor = "foi nada";

            private String permiteBaixarCrvlDigital = "permite";

            private List<String> listaCsv = List.of();
        }

        @Data
        @ToString
        public static class Multa {
            private String placa = "aaaaaa";
            private String renavam = "aaaaaa";
            private String numeroIdentificacaoProprietario = "aaaaaa";
            private String codigoOrgaoAutuador = "aaaaaa";
            private String descricaoOrgaoAutuador = "aaaaaa";
            private String numeroAutoInfracao = "aaaaaa";
            private String codigoInfracao = "aaaaaa";
            private String codigoDesdobramentoInfracao = "aaaaaa";
            private String descricaoInfracao = "aaaaaa";
            private String dataInfracao = "aaaaaa";
            private String horaInfracao = "aaaaaa";
            private String localOcorrenciaInfracao = "aaaaaa";
            private String codigoRenainf = "aaaaaa";
            private Double valorMulta = 11.22;
            private String dataVencimento = "aaaaaa";
        }
    }
}