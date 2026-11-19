import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class PainelPaciente extends JPanel implements Atualizavel {
    private TelaPrincipal tela;

    private JTextField txtNome = new JTextField();
    private JTextField txtCpf = new JTextField();
    private JTextField txtTelefone = new JTextField();
    private JTextField txtNascimento = new JTextField();
    private JTextField txtConvenio = new JTextField();
    private JTextField txtHistorico = new JTextField();
    private JTextArea areaLista = new JTextArea();

    public PainelPaciente(TelaPrincipal tela) {
        this.tela = tela;
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("CPF:"));
        form.add(txtCpf);
        form.add(new JLabel("Telefone (com DDD):"));
        form.add(txtTelefone);
        form.add(new JLabel("Data de nascimento (dd/mm/aaaa):"));
        form.add(txtNascimento);
        form.add(new JLabel("Convênio (deixe vazio se não tiver):"));
        form.add(txtConvenio);
        form.add(new JLabel("Alergia ou condição pré-existente (opcional):"));
        form.add(txtHistorico);

        JButton btnCadastrar = new JButton("Cadastrar paciente");
        btnCadastrar.addActionListener(e -> cadastrar());

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(btnCadastrar, BorderLayout.SOUTH);

        areaLista.setEditable(false);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(areaLista), BorderLayout.CENTER);
    }

    private void cadastrar() {
        try {
            Paciente p = new Paciente(txtNome.getText(), txtCpf.getText(), txtTelefone.getText(),
                    txtNascimento.getText(), txtConvenio.getText());
            if (!txtHistorico.getText().trim().isEmpty()) {
                p.addHistorico(txtHistorico.getText());
            }

            tela.getClinica().regPaciente(p);
            tela.salvar();
            limparCampos();
            tela.aviso("Paciente " + p.getNome() + " cadastrado com sucesso!");
        } catch (ClinicaException e) {
            tela.erro(e.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtNascimento.setText("");
        txtConvenio.setText("");
        txtHistorico.setText("");
    }

    @Override
    public void atualizar() {
        areaLista.setText("");
        for (Paciente p : tela.getClinica().getPacientes()) {
            String convenio = p.getConvenio().isEmpty() ? "nenhum" : p.getConvenio();
            areaLista.append(p.getNome() + " | CPF " + p.getCpf() + " | Tel. " + p.getTelefone()
                    + " | Nasc. " + p.getDtNasc() + " | Convênio: " + convenio + "\n");
        }
    }
}
