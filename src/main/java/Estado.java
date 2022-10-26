import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author osmar
 */
public class Estado {

    //Mapa com as conexões do estado
    private Map<String, Estado> estadoConectado = new HashMap<>();
    private String nomeEstado;
    //Indica se o estado é o inicial
    private boolean estadoInicial;
    //Indica se o estado é o final
    private boolean estadoFinal;

    public Estado(String nomeEstado) {
        this.nomeEstado = nomeEstado;
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

    public boolean eEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(boolean estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public boolean eEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(boolean estadoFinal) {
        this.estadoFinal = estadoFinal;
    }    
}