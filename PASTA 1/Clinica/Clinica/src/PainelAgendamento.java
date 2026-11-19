import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class PainelAgendamento extends JPanel implements Atualizavel {
    private TelaPrincipal tela;

    private JComboBox<Medico> cmbMedicos = new JComboBox<>();
    private JComboBox<Paciente> cmbPacientes = new JComboBox<>();
    private JComboBox<String> cmbHorarios = new JComboBox<>();

    public PainelAgendamento(TelaPrincipal tela) {
        this.tela = tela;
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Médico:"));
        form.add(cmbMedicos);
        form.add(new JLabel("Paciente:"));
        form.add(cmbPacientes);
        form.add(new JLabel("Horário livre:"));
        form.add(cmbHorarios);

        JButton btnAgendar = new JButton("Agendar");
        btnAgendar.addActionListener(e -> agendar());

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(form, BorderLayout.NORTH);
        centro.add(btnAgendar, BorderLayout.CENTER);
        add(centro, BorderLayout.NORTH);

        // quando troca o médico, os horários livres mudam
        cmbMedicos.addActionListener(e -> carregarHorarios());
    }

    private void agendar() {
        Medico medico = (Medico) cmbMedicos.getSelectedItem();
        Paciente paciente = (Paciente) cmbPacientes.getSelectedItem();
        String horario = (String) cmbHorarios.getSelectedItem();
        if (medico == null || paciente == null || horario == null) {
            tela.erro("Escolha um médico, um paciente e um horário livre.");
            return;
        }
        try {
            Consulta c = tela.getClinica().agendarC(paciente, medico, horario);
            tela.salvar();
            tela.aviso("Consulta agendada para " + c.getDataHora() + ".\nValor a pagar: R$ "
                    + String.format("%.2f", c.getValorCobrado()));
        } catch (ClinicaException e) {
            tela.erro(e.getMessage());
        }
    }

    private void carregarHorarios() {
        cmbHorarios.removeAllItems();
        Medico medico = (Medico) cmbMedicos.getSelectedItem();
        if (medico == null) {
            return;
        }
        for (SlotTempo slot : medico.getAgenda()) {
            if (!slot.isOcupado()) {
                cmbHorarios.addItem(slot.getDataHora());
            }
        }
    }

    @Override
    public void atualizar() {
        Medico medicoAntes = (Medico) cmbMedicos.getSelectedItem();
        Paciente pacienteAntes = (Paciente) cmbPacientes.getSelectedItem();

        cmbMedicos.removeAllItems();
        for (Medico m : tela.getClinica().getMedicos()) {
            cmbMedicos.addItem(m);
        }
        cmbPacientes.removeAllItems();
        for (Paciente p : tela.getClinica().getPacientes()) {
            cmbPacientes.addItem(p);
        }

        if (medicoAntes != null) {
            cmbMedicos.setSelectedItem(medicoAntes);
        }
        if (pacienteAntes != null) {
            cmbPacientes.setSelectedItem(pacienteAntes);
        }
        carregarHorarios();
    }
}
