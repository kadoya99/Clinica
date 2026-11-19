import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal extends JFrame {
    private Clinica clinica = new Clinica();
    private List<Medicamento> estoque = new ArrayList<>();
    private List<Atualizavel> paineis = new ArrayList<>();

    public TelaPrincipal() {
        super("Clínica Médica");

        try {
            estoque = GerenciadorArquivos.carregarMedicamentos("medicamentos.txt");
            GerenciadorArquivos.carregarTudo(clinica, estoque);
        } catch (ArquivoException e) {
            // fecha sem salvar nada, para não apagar o que já existe nos arquivos
            JOptionPane.showMessageDialog(null, e.getMessage() + "\nO programa será fechado.",
                    "Erro ao carregar dados", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        PainelMedico painelMedico = new PainelMedico(this);
        PainelPaciente painelPaciente = new PainelPaciente(this);
        PainelAgendamento painelAgendamento = new PainelAgendamento(this);
        PainelAtendimento painelAtendimento = new PainelAtendimento(this);
        PainelHistorico painelHistorico = new PainelHistorico(this);

        paineis.add(painelMedico);
        paineis.add(painelPaciente);
        paineis.add(painelAgendamento);
        paineis.add(painelAtendimento);
        paineis.add(painelHistorico);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Médicos", painelMedico);
        abas.addTab("Pacientes", painelPaciente);
        abas.addTab("Agendar consulta", painelAgendamento);
        abas.addTab("Atender consulta", painelAtendimento);
        abas.addTab("Histórico", painelHistorico);
        add(abas);

        atualizarTudo();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Clinica getClinica() {
        return clinica;
    }

    public List<Medicamento> getEstoque() {
        return estoque;
    }

    // chamado pelos painéis depois de qualquer alteração
    public void salvar() {
        try {
            GerenciadorArquivos.salvarTudo(clinica);
        } catch (ArquivoException e) {
            erro(e.getMessage());
        }
        atualizarTudo();
    }

    public void atualizarTudo() {
        for (Atualizavel painel : paineis) {
            painel.atualizar();
        }
    }

    public void erro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
}
