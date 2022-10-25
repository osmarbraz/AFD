/**
 *
 * @author osmar
 */
public class Principal {

    public static void main(String[] args) {
        System.out.println("Automato Finito Determinístico(AFD)");
        
        AFD afd = new AFD("0");
        afd.avaliar();
    }
}
