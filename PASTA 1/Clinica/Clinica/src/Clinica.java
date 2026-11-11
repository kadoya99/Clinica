import java.util.ArrayList;
import java.util.List;

public class Clinica {
    private List<Medico> medicos;
    private List<Paciente> pacientes;
    private List<Consulta> consultas;

    public Clinica() {

        this.medicos = new ArrayList<>();
        this.consultas = new ArrayList<>();
        this.pacientes = new ArrayList<>();

    }

    public void regMedico(Medico m) {
        this.medicos.add(m);
        System.out.println("Médico " + m.getNome() + " cadastrado com sucesso!");
    }

    public void listAgendaM(String crmP) {
        for (Medico m : this.medicos) {
            if (m.getCrm().equals(crmP)) {
                System.out.println("Agenda do Dr(a). " + m.getNome() + ".");
                System.out.println(m.getAgenda());
                return;
            }
        }
        System.out.println("Médico com CRM " + crmP + " não encontrado.");

    }

    public void regPaciente(Paciente p) {
        this.pacientes.add(p);
        System.out.println("Paciente " + p.getNome() + " cadastrado com sucesso!");
    }

    public void agendarC(Paciente p, Medico m, String dataHora) {

        SlotTempo slotEncontrado = null;
        for (SlotTempo slot : m.getAgenda()) {
            if (slot.getDataHora().equals(dataHora)) {
                slotEncontrado = slot;
                break;
            }
        }

        if (slotEncontrado == null) {
            System.out.println("Erro: Horário não disponível para o médico " + m.getNome());
        return;

        }

        if (slotEncontrado.isOcupado()) {
            System.out.println("Erro: Horário já ocupado para o médico " + m.getNome());
            return;
        }

        Consulta nova = new Consulta(m, p, dataHora);
        this.consultas.add(nova);
        slotEncontrado.setOcupado(true);

        p.addConsulta(dataHora);

        System.out.println("Consulta agendada com sucesso: " + dataHora);


    }

    public void historicoPaciente(String buscaCPF){

        for(Paciente p : this.pacientes){
            if (p.getCpf().equals(buscaCPF)){
                System.out.println("--- HISTÓRICO DO PACIENTE: " + p.getNome() + " ---");
                System.out.println("Consultas: " + p.getListaConsulta());
                System.out.println("Histórico Médico: " + p.getHistoricoMedico());
                return;
            }
        }
        System.out.println("Paciente com CPF " + buscaCPF + " não encontrado.");
    }

    public Medico buscarMedico(String crmBusca) {
        for (Medico m : this.medicos) {
            if (m.getCrm().equals(crmBusca)) {
                return m;
            }
        }
        return null;
    }

    public Paciente buscarPaciente(String nomeBusca) {
        for (Paciente p : this.pacientes) {
            if (p.getNome().equals(nomeBusca)) {
                return p;
            }
        }
        return null;
    }

public List<Medico> getMedicos() {
        return this.medicos;
    }
    
  public List<Consulta> getConsultas() {
        return this.consultas;
    }

 public List<Paciente> getPacientes() {
        return this.pacientes;
    }

}