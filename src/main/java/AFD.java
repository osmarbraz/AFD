
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

    private Map<String, List<JsonNode>> resultados = new HashMap<>();

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
        setTabelaTransicaoEstados();
    }

    /**
     * Recupera o próximo estado para uma entrada.
     *
     * @param proximoEntrada
     */
    private void getProximoEstado(String proximoEntrada) {
        //System.out.println("Recuperando próximo estado para -> " + proximoEntrada);
        //Avança somente para estados atuais diferentes de nulo
        if (this.estadoAtual != null) {
            this.estadoAtual = this.estadoAtual.getEstadoConectado().get(proximoEntrada);
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
     * Carrega os transiçõe de estado.
     */
    private void carregarEstados() {
        System.out.println("Carregando transições de estados");
        afdDados.get("estados").forEach(nome -> getEstados().put(nome.asText(),
                new Estado(nome.asText())
        )
        );
    }

    /**
     * Define o estado inicial.
     */
    private void setEstadoInicial() {
        System.out.println("Definindo estado inicial");
        afdDados.get("estadoInicial").forEach(estado -> {
            this.estadoInicial = matriz.get(estado.asText());
            this.estadoInicial.setInicial(true);
            this.estadoAtual = this.estadoInicial;
        });
    }

    /**
     * Define o estado final.
     */
    private void setEstadoFinal() {
        System.out.println("Definindo o estado final");
        afdDados.get("estadoFinal").forEach(estadoFinal -> {
            getEstados().get(estadoFinal.asText()).setAceito(true);
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
     * Recupera a tabela de transição.
     *
     * @param nome
     * @return
     */
    private JsonNode getTabelaTransicao(String nome) {
        System.out.println("Recuperando matriz transicao");
        return afdDados.get("matriz").get(nome);
    }

    /**
     * Recupera as transições de estado.
     *
     * @return
     */
    private Map<String, Estado> getEstados() {
        return matriz;
    }

    /**
     * Carrega a tabela de transição de estado.
     */
    private void setTabelaTransicaoEstados() {
        //Percorre os estados
        getEstados().values().forEach(estado -> {
            //Recupera a tabela para o estado
            JsonNode tabelaTransicao = getTabelaTransicao(estado.getNomeEstado());
            //Percorre os tokens do alfabeto
            getAlfabeto().forEach(alfabeto -> {
                //Especifica a transição(conexão) do estado
                estado.getEstadoConectado().put(
                        alfabeto.asText(),
                        getEstados().get(tabelaTransicao.get(alfabeto.asText()).asText())
                );
            });
        });
    }

    /**
     * Avalia as entradas.
     */
    public void avaliar() {
        System.out.println("Avaliando entradas");
        //Percorre as entradas do arquivo entradas.json
        for (JsonNode entrada : entradas.get("entradas")) {

            System.out.println("Entrada:" + entrada);
            entrada.forEach(transicao -> this.getProximoEstado(transicao.asText()));

            //Mostra se entrada foi aceita ou não
            if (this.estadoAtual != null && this.estadoAtual.eAceito()) {
                aprovado.add(entrada);
                System.out.println("Aprovada -> " + entrada);
            } else {
                rejeitado.add(entrada);
                System.out.println("Rejeitada -> " + entrada);
            }
            this.estadoAtual = this.estadoInicial;
            System.out.println("");
        }
        salvaResultados();
    }

    /**
     * Salva os resultados no arquivo 'resultados.json'.
     */
    private void salvaResultados() {
        resultados.put("Aprovado", aprovado);
        resultados.put("Rejeitado", rejeitado);
        System.out.println("Salvando o resultado em \"resultados_" + nome_afd + ".json\"");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("resultados_" + nome_afd + ".json"), this.resultados);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
