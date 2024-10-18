# Mock Server HTTP

Este projeto é um servidor mock que simula respostas HTTP para testes e desenvolvimento. Ele permite que os desenvolvedores criem e registrem endpoints mockados, facilitando o desenvolvimento de aplicações sem depender de serviços externos.

## Funcionalidades

- **Registro de Endpoints**: Permite registrar endpoints com métodos HTTP (GET, POST, etc.) e fornecer respostas mockadas.
- **Respostas Mockadas**: As respostas podem ser objetos, strings ou lidas a partir de arquivos JSON.
- **Execução do Servidor**: O servidor pode ser iniciado em uma porta configurável, escutando as requisições nos endpoints registrados.

## Estrutura do Projeto

- `dtm.mock.Server`: Contém a implementação do servidor mock.
  - `MockServerHttp`: Classe principal que configura e executa o servidor.
  - `MockServerModel`: Modelo que representa um endpoint mockado.
  - `MockServerhandler`: Manipulador de requisições para endpoints.

## Como Usar

1. **Importe o Projeto**: Clone ou faça o download do projeto para o seu ambiente de desenvolvimento.
   
   ```bash
   git clone https://github.com/seuusuario/mock-server-http.git
    ```

## Dependências

Certifique-se de ter as dependências necessárias no seu projeto.

## Registro de Endpoints

Use o método `registerEndpoint` para adicionar os endpoints desejados. Você pode passar um objeto de resposta, uma string ou um caminho para um arquivo JSON.

```java
    MockServerHttp server = new MockServerHttp(8081);
    server.registerEndpoint("/api/test", HttpMethod.GET, "Resposta mockada", false);
    server.registerEndpoint("/api/test", HttpMethod.GET, "/json/jsonresponse.json", true);
```

## Inicie o Servidor

```java
    server.run();
```

## Teste os Endpoints

Acesse os endpoints registrados através de um cliente HTTP (como Postman ou curl) para verificar as respostas mockadas.

## Leitura de Arquivos JSON

Ao registrar um endpoint com uma resposta a partir de um arquivo JSON, o servidor irá validar se o arquivo existe e se é um arquivo válido. A leitura do conteúdo do arquivo é feita em UTF-8.

## Exemplo de Uso

Aqui está um exemplo de como configurar o servidor e registrar um endpoint:

```java
public class Main {
    public static void main(String[] args) throws Exception {
        MockServerHttp server = new MockServerHttp(8081);
        server.registerEndpoint("/api/test", HttpMethod.GET, "Resposta mockada", false);
        server.run();
    }
}
```
