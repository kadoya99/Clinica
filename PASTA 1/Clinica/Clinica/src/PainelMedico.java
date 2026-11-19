import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class PainelMedico extends JPanel implements Atualizavel {
    private TelaPrincipal tela;

    private JTextField txtNome = new JTextField();
    private JTextField txtEspecialidade = new JTextField();
    private JTextField txtCrm = new JTextField();
    private JTextField txtCpf = new JTextField();
    private JTextField txtTelefone = new JTextField();
    private JTextField txtNascimento = new JTextField();
    private JTextField txtValor = new JTextField();
    private JTextField txtConvenios = new JTextField();
    private JTextField txtHorarios = new JTextField();

    private JTextArea areaLista = new JTextArea();
    private JComboBox<Medico> cmbMedicos = new JComboBox<>();
    private JTextField txtNovoHorario = new JTextField(15);

    public PainelMedico(TelaPrincipal tela) {
        this.tela = tela;
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("Especialidade:"));
        form.add(txtEspecialidade);
        form.add(new JLabel("CRM:"));
        form.add(txtCrm);
        form.add(new JLabel("CPF:"));
        form.add(txtCpf);
        form.add(new JLabel("Telefone (com DDD):"));
        form.add(txtTelefone);
        form.add(new JLabel("Data de nascimento (dd/mm/aaaa):"));
        form.add(txtNascimento);
        form.add(new JLabel("Valor da consulta (R$):"));
        form.add(txtValor);
        form.add(new JLabel("Convênios aceitos (nome=% que o paciente paga, separados por vírgula):"));
        form.add(txtConvenios);
        form.add(new JLabel("Horários (dd/mm/aaaa hh:mm, separados por vírgula):"));
        form.add(txtHorarios);

        JButton btnCadastrar = new JButton("Cadastrar médico");
        btnCadastrar.addActionListener(e -> cadastrar());

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(btnCadastrar, BorderLayout.SOUTH);

        areaLista.setEditable(false);

        JButton btnHorario = new JButton("Adicionar horário");
        btnHorario.addActionListener(e -> adicionarHorario());

        JPanel rodape = new JPanel(new FlowLayout());
        rodape.add(new JLabel("Médico:"));
        rodape.add(cmbMedicos);
        rodape.add(new JLabel("Novo horário (dd/mm/aaaa hh:mm):"));
        rodape.add(txtNovoHorario);
        rodape.add(btnHorario);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(areaLista), BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }

    private void cadastrar() {
        try {
            double valor = Validador.numero("Valor da consulta", txtValor.getText(), 0.01, 100000);
            Medico m = new Medico(txtNome.getText(), txtCpf.getText(), txtTelefone.getText(),
                    txtNascimento.getText(), txtEspecialidade.getText(), txtCrm.getText(), valor);

            // o médico só é registrado no final, então se algo der erro nada fica pela metade
            if (!txtConvenios.getText().trim().isEmpty()) {
                for (String item : txtConvenios.getText().split(",")) {
                    String[] partes = item.split("=");
                    if (partes.length != 2) {
                        throw new DadoInvalidoException("Convênio deve estar no formato nome=percentual (ex: Unimed=20).");
                    }
                    double percentual = Validador.numero("Percentual do convênio", partes[1], 0, 100);
                    m.addConvenio(new Convenio(partes[0], percentual));
                }
            }
            if (!txtHorarios.getText().trim().isEmpty()) {
                for (String horario : txtHorarios.getText().split(",")) {
                    m.addHorario(horario);
                }
            }

            tela.getClinica().regMedico(m);
            tela.salvar();
            limparCampos();
            tela.aviso("Médico " + m.getNome() + " cadastrado com sucesso!");
        } catch (ClinicaException e) {
            tela.erro(e.getMessage());
        }
    }

    private void adicionarHorario() {
        Medico m = (Medico) cmbMedicos.getSelectedItem();
        if (m == null) {
            tela.erro("Cadastre um médico primeiro.");
            return;
        }
        try {
            m.addHorario(txtNovoHorario.getText());
            tela.salvar();
            txtNovoHorario.setText("");
        } catch (ClinicaException e) {
            tela.erro(e.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtEspecialidade.setText("");
        txtCrm.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtNascimento.setText("");
        txtValor.setText("");
        txtConvenios.setText("");
        txtHorarios.setText("");
    }

    @Override
    public void atualizar() {
        Medico selecionado = (Medico) cmbMedicos.getSelectedItem();
        cmbMedicos.removeAllItems();
        areaLista.setText("");
        for (Medico m : tela.getClinica().getMedicos()) {
            cmbMedicos.addItem(m);
            areaLista.append("Dr(a). " + m.getNome() + " | " + m.getEspecialidade() + " | CRM " + m.getCrm()
                    + " | Consulta: R$ " + String.format("%.2f", m.getValorConsulta()) + "\n");
            areaLista.append("   Convênios: " + m.getListaConvenio() + "\n");
            areaLista.append("   Agenda: " + m.getAgenda() + "\n\n");
        }
        if (selecionado != null) {
            cmbMedicos.setSelectedItem(selecionado);
        }
    }
}
