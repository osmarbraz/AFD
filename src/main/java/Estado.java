import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author osmar
 */
public class Estado {

    private Map<String, Estado> situacaoConectado = new HashMap<>();
    private String nomeEstado;
    private boolean inicial;
    private boolean aceito;

    public Estado(String nomeEstado) {
        this.nomeEstado = nomeEstado;
    }

    public Estado() {
    }

    public Map<String, Estado> getStatusConnected() {
        return situacaoConectado;
    }

    public void setStatusConnected(Map<String, Estado> statusConnected) {
        this.situacaoConectado = statusConnected;
    }

    public String getNomeEstado() {
        return nomeEstado;
    }

    public void setNomeEstado(String nomeEstado) {
        this.nomeEstado = nomeEstado;
    }

    public boolean isInicial() {
        return inicial;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }

    public boolean eAceito() {
        return aceito;
    }

    public void setAceito(boolean aceito) {
        this.aceito = aceito;
    }    
}