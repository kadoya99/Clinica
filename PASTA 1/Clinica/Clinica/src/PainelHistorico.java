import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class PainelHistorico extends JPanel implements Atualizavel {
    private TelaPrincipal tela;

    private JComboBox<Paciente> cmbPacientes = new JComboBox<>();
    private JTextArea areaHistorico = new JTextArea();

    public PainelHistorico(TelaPrincipal tela) {
        this.tela = tela;
        setLayout(new BorderLayout());

        JButton btnMostrar = new JButton("Ver histórico");
        btnMostrar.addActionListener(e -> mostrar());

        JPanel topo = new JPanel(new FlowLayout());
        topo.add(new JLabel("Paciente:"));
        topo.add(cmbPacientes);
        topo.add(btnMostrar);

        areaHistorico.setEditable(false);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(areaHistorico), BorderLayout.CENTER);
    }

    private void mostrar() {
        Paciente p = (Paciente) cmbPacientes.getSelectedItem();
        if (p == null) {
            tela.erro("Não há paciente selecionado.");
            return;
        }

        areaHistorico.setText("");
        areaHistorico.append("--- HISTÓRICO DO PACIENTE: " + p.getNome() + " ---\n");
        areaHistorico.append("Alergias e condições:\n");
        if (p.getHistoricoMedico().isEmpty()) {
            areaHistorico.append("- Nenhuma\n");
        }
        for (String condicao : p.getHistoricoMedico()) {
            areaHistorico.append("- " + condicao + "\n");
        }

        boolean achou = false;
        for (Consulta c : tela.getClinica().getConsultas()) {
            if (!c.getPaciente().getCpf().equals(p.getCpf())) {
                continue;
            }
            achou = true;
            areaHistorico.append("\nData da consulta: " + c.getDataHora() + "\n");
            areaHistorico.append("Médico: " + c.getMedico().getNome() + "\n");
            areaHistorico.append("Valor: R$ " + String.format("%.2f", c.getValorCobrado()) + "\n");

            if (!c.isAtendida()) {
                areaHistorico.append("Situação: aguardando atendimento\n");
                continue;
            }
            areaHistorico.append("Diagnóstico: " + c.getDiagnostico().getNomeDoenca() + "\n");
            areaHistorico.append("Sintomas: " + c.getDiagnostico().getDescricao() + "\n");
            areaHistorico.append("Sinais vitais:\n");
            for (SinaisVitais sv : c.getSinaisVitais()) {
                areaHistorico.append("- " + sv + "\n");
            }
            areaHistorico.append("Medicamentos receitados:\n");
            for (Medicamento m : c.getDiagnostico().getMedicamentosPassados()) {
                areaHistorico.append("-> " + m.getNome() + " - " + m.orientacaoUso() + "\n");
            }
        }
        if (!achou) {
            areaHistorico.append("\nNenhuma consulta para esse paciente.\n");
        }
    }

    @Override
    public void atualizar() {
        Paciente antes = (Paciente) cmbPacientes.getSelectedItem();
        cmbPacientes.removeAllItems();
        for (Paciente p : tela.getClinica().getPacientes()) {
            cmbPacientes.addItem(p);
        }
        if (antes != null) {
            cmbPacientes.setSelectedItem(antes);
        }
    }
}
