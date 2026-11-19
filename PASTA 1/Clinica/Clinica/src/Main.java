import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // telas Swing devem ser criadas nessa thread própria do Swing
        SwingUtilities.invokeLater(() -> new TelaPrincipal());
    }
}
