
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author osmar
 */
public class AFD {

    private JsonNode afdDados;

    private Map<String, Estado> matriz = new HashMap<>();

    private Estado estadoInicial;
    private Estado estadoAtual;
    private JsonNode entradas;

    private List<JsonNode> aprovado = new ArrayList<>();

    private List<JsonNode> rejeitado = new ArrayList<>();
    
    private String nome_afd;

    /**
     * Construtor sem parâmetro.
     *
     * @param nome_afd
     */
    public AFD(String nome_afd) {
        System.out.println("Inicializando AFD");
        this.nome_afd = nome_afd;
        getDadosJSON();
        carregarEstados();
        setEstadoInicial();
        setEstadoFinal();
        carregaMatriz();
    }

    /**
     * Recupera o próximo estado para um token de entrada.
     *
     * @param proximoToken
     */
    private void getProximoEstado(String proximoToken) {
        //System.out.println("Recuperando próximo estado para -> " + proximoToken);
        //Avança somente para estados atuais diferentes de nulo
        if (this.estadoAtual != null) {
            //Atualiza o estado atual com o próximo estado conectado do token
            this.estadoAtual = this.estadoAtual.getEstadoConectado().get(proximoToken);
        }
    }

    /**
     * Recupera os dados dos arquivos json.
     */
    private void getDadosJSON() {
        System.out.println("Recuperando informacoes do arquivo \"afd_" + nome_afd + ".json\"");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            afdDados = objectMapper.readTree(new File("afd_" + nome_afd + ".json"));
            entradas = objectMapper.readTree(new File("entradas_" + nome_afd + ".json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Carrega os estado dos dados para a matriz.
     */
    private void carregarEstados() {
        System.out.println("Carregando transições de estados");
        afdDados.get("estados").forEach(nome -> getMatriz().put(nome.asText(),
                new Estado(nome.asText())
        )
        );
    }

    /**
     * Define o estado inicial.
     */
    private void setEstadoInicial() {
        System.out.println("Definindo estado inicial");
        //Carrega o estado inicial dos dados
        afdDados.get("estadoInicial").forEach(estado -> {
            //Carrega da matriz o estado inicial
            this.estadoInicial = getMatriz().get(estado.asText());
            //Marca o estado como inicial
            this.estadoInicial.setEstadoInicial(true);
            //Define o estado atual com o inicial
            this.estadoAtual = this.estadoInicial;
        });
    }

    /**
     * Define o estado final.
     */
    private void setEstadoFinal() {
        System.out.println("Definindo o estado final");
        //Carrega o estado finaldos dados
        afdDados.get("estadoFinal").forEach(estado -> {
            getMatriz().get(estado.asText()).setEstadoFinal(true);
        });
    }

    /**
     * Recupera o alfabeto.
     *
     * @return
     */
    private JsonNode getAlfabeto() {
        System.out.println("Recuperando o alfabeto");
        return afdDados.get("alfabeto");
    }

    /**
     * Recupera as transições para um estado.
     *
     * @param nome
     * @return
     */
    private JsonNode getTransicoesEstado(String nome) {
        System.out.println("Recuperando as transições para o estado:" + nome);
        return afdDados.get("matriz").get(nome);
    }

    /**
     * Recupera a matriz de transições de estado.
     *
     * @return
     */
    private Map<String, Estado> getMatriz() {
        return matriz;
    }

    /**
     * Carrega a matriz de transições de estado.
     */
    private void carregaMatriz() {
        //Percorre os estados do automâto
        getMatriz().values().forEach(estado -> {
            //Recupera a tabela de transição para o estado do arquivo JSON
            JsonNode tabelaTransicao = getTransicoesEstado(estado.getNomeEstado());
            //Percorre os tokens do alfabeto
            getAlfabeto().forEach(token -> {
                //Especifica a transição(conexão) do estado para o token do alfabeto
                estado.getEstadoConectado().put(
                        token.asText(),
                        getMatriz().get(tabelaTransicao.get(token.asText()).asText())
                );
            });
        });
    }

    /**
     * Avalia as entradas.
     */
    public void avaliar() {
        System.out.println("\nAvaliando entradas");
        //Percorre as entradas do arquivo entradas_X.json
        for (JsonNode entrada : entradas.get("entradas")) {

            System.out.println("Entrada:" + entrada);
            //Recupera o próximo estado para cada token da entrada
            entrada.forEach(token -> {
                this.getProximoEstado(token.asText());
            }
            );

            //Mostra se entrada foi aceita ou não, se o estado é diferente de nulo e o estado atual é estado final.
            if (this.estadoAtual != null && this.estadoAtual.eEstadoFinal()) {
                aprovado.add(entrada);
                System.out.println("Aprovada -> " + entrada);
            } else {
                rejeitado.add(entrada);
                System.out.println("Rejeitada -> " + entrada);
            }
            //Atualiza o estado atual
            this.estadoAtual = this.estadoInicial;
            System.out.println("");
        }
        //Salva os resultados
        salvaResultados();
    }

    /**
     * Salva os resultados no arquivo 'resultados.json'.
     */
    private void salvaResultados() {
        Map<String, List<JsonNode>> resultados = new HashMap<>();
        
        resultados.put("Aprovado", aprovado);
        resultados.put("Rejeitado", rejeitado);
        System.out.println("Salvando o resultado em \"resultados_" + nome_afd + ".json\"");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("resultados_" + nome_afd + ".json"), resultados);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
