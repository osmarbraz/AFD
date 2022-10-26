import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author osmar
 */
public class Estado {

    private Map<String, Estado> estadoConectado = new HashMap<>();
    private String nomeEstado;
    private boolean inicial;
    private boolean aceito;

    public Estado(String nomeEstado) {
        this.nomeEstado = nomeEstado;
    }

    public Estado() {
    }

    public Map<String, Estado> getEstadoConectado() {
        return estadoConectado;
    }

    public void setEstadoConectado(Map<String, Estado> estadoConectado) {
        this.estadoConectado = estadoConectado;
    }

    public String getNomeEstado() {
        return nomeEstado;
    }

    public void setNomeEstado(String nomeEstado) {
        this.nomeEstado = nomeEstado;
    }

    public boolean eInicial() {
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