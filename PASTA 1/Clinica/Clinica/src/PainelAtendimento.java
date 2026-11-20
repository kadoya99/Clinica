import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class PainelAtendimento extends JPanel implements Atualizavel {
    private TelaPrincipal tela;

    private JComboBox<Consulta> cmbConsultas = new JComboBox<>();
    private JTextField txtPressao = new JTextField();
    private JTextField txtTemperatura = new JTextField();
    private JTextField txtSaturacao = new JTextField();
    private JTextField txtDoenca = new JTextField();
    private JTextField txtSintomas = new JTextField();
    private JList<Medicamento> listaMedicamentos;
    private JTextArea areaResumo = new JTextArea();

    public PainelAtendimento(TelaPrincipal tela) {
        this.tela = tela;
        setLayout(new BorderLayout());

        // o estoque não muda durante o programa, então a lista é montada uma vez só
        listaMedicamentos = new JList<>(tela.getEstoque().toArray(new Medicamento[0]));
        listaMedicamentos.setVisibleRowCount(5);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Consulta aguardando atendimento:"));
        form.add(cmbConsultas);
        form.add(new JLabel("Pressão arterial em cmHg (ex: 12,8):"));
        form.add(txtPressao);
        form.add(new JLabel("Temperatura em °C (ex: 36,5):"));
        form.add(txtTemperatura);
        form.add(new JLabel("Saturação de O2 em % (ex: 98):"));
        form.add(txtSaturacao);
        form.add(new JLabel("Diagnóstico (doença):"));
        form.add(txtDoenca);
        form.add(new JLabel("Descrição dos sintomas:"));
        form.add(txtSintomas);

        // a lista fica fora do GridLayout, senão todas as linhas ficam com a altura dela
        JPanel painelRemedios = new JPanel(new BorderLayout());
        painelRemedios.add(new JLabel("Medicamentos (Ctrl para escolher vários, ou nenhum):"), BorderLayout.NORTH);
        painelRemedios.add(new JScrollPane(listaMedicamentos), BorderLayout.CENTER);

        JButton btnFinalizar = new JButton("Finalizar atendimento");
        btnFinalizar.addActionListener(e -> finalizar());

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.NORTH);
        topo.add(painelRemedios, BorderLayout.CENTER);
        topo.add(btnFinalizar, BorderLayout.SOUTH);

        areaResumo.setEditable(false);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(areaResumo), BorderLayout.CENTER);
    }

    private void finalizar() {
        Consulta consulta = (Consulta) cmbConsultas.getSelectedItem();
        if (consulta == null) {
            tela.erro("Não há consulta selecionada.");
            return;
        }
        try {
            double pressao = Validador.numero("Pressão arterial", txtPressao.getText(), 1, 30);
            double temperatura = Validador.numero("Temperatura", txtTemperatura.getText(), 30, 45);
            double saturacao = Validador.numero("Saturação", txtSaturacao.getText(), 0, 100);
            Diagnostico diag = new Diagnostico(txtDoenca.getText(), txtSintomas.getText());
            for (Medicamento m : listaMedicamentos.getSelectedValuesList()) {
                diag.addSugestao(m);
            }

            List<SinaisVitais> sinais = new ArrayList<>();
            sinais.add(new SinaisVitais("Pressão Arterial", pressao, "cmHg"));
            sinais.add(new SinaisVitais("Temperatura", temperatura, "°C"));
            sinais.add(new SinaisVitais("Saturação", saturacao, "%"));

            consulta.finalizarConsulta(sinais, diag);
            tela.salvar();
            mostrarResumo(consulta);
            limparCampos();
        } catch (ClinicaException e) {
            tela.erro(e.getMessage());
        }
    }

    private void mostrarResumo(Consulta c) {
        Diagnostico diag = c.getDiagnostico();
        areaResumo.setText("");
        areaResumo.append("RESUMO DO ATENDIMENTO FINALIZADO\n");
        areaResumo.append("Paciente: " + c.getPaciente().getNome() + "\n");
        areaResumo.append("Médico: " + c.getMedico().getNome() + "\n");
        areaResumo.append("Valor cobrado: R$ " + String.format("%.2f", c.getValorCobrado()) + "\n");
        areaResumo.append("Doença: " + diag.getNomeDoenca() + "\n\n");
        areaResumo.append("Sinais vitais:\n");
        for (SinaisVitais sv : c.getSinaisVitais()) {
            areaResumo.append("- " + sv + "\n");
        }
        areaResumo.append("\nReceita médica:\n");
        if (diag.getMedicamentosPassados().isEmpty()) {
            areaResumo.append("- Nenhum medicamento prescrito\n");
        }
        for (Medicamento m : diag.getMedicamentosPassados()) {
            areaResumo.append("-> " + m.getNome() + "\n");
            areaResumo.append("   INSTRUÇÃO: " + m.orientacaoUso() + "\n");
        }
    }

    private void limparCampos() {
        txtPressao.setText("");
        txtTemperatura.setText("");
        txtSaturacao.setText("");
        txtDoenca.setText("");
        txtSintomas.setText("");
        listaMedicamentos.clearSelection();
    }

    @Override
    public void atualizar() {
        cmbConsultas.removeAllItems();
        // só aparecem as consultas que ainda não foram atendidas
        for (Consulta c : tela.getClinica().getConsultas()) {
            if (!c.isAtendida()) {
                cmbConsultas.addItem(c);
            }
        }
    }
}
