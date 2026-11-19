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

    public void regMedico(Medico m) throws CadastroDuplicadoException {
        for (Medico existente : medicos) {
            if (existente.getCpf().equals(m.getCpf())) {
                throw new CadastroDuplicadoException("Já existe um médico com o CPF " + m.getCpf() + ".");
            }
            if (existente.getCrm().equalsIgnoreCase(m.getCrm())) {
                throw new CadastroDuplicadoException("Já existe um médico com o CRM " + m.getCrm() + ".");
            }
        }
        this.medicos.add(m);
    }

    public void regPaciente(Paciente p) throws CadastroDuplicadoException {
        for (Paciente existente : pacientes) {
            if (existente.getCpf().equals(p.getCpf())) {
                throw new CadastroDuplicadoException("Já existe um paciente com o CPF " + p.getCpf() + ".");
            }
        }
        this.pacientes.add(p);
    }

    public Consulta agendarC(Paciente p, Medico m, String dataHora) throws AgendamentoException {
        SlotTempo slot = m.buscarSlot(dataHora);
        if (slot == null) {
            throw new AgendamentoException("O horário " + dataHora + " não existe na agenda do médico " + m.getNome() + ".");
        }
        if (slot.isOcupado()) {
            throw new AgendamentoException("O horário " + dataHora + " já está ocupado.");
        }
        // o mesmo paciente não pode estar em duas consultas ao mesmo tempo
        for (Consulta c : consultas) {
            if (c.getPaciente().getCpf().equals(p.getCpf()) && c.getDataHora().equals(dataHora)) {
                throw new AgendamentoException("O paciente " + p.getNome() + " já tem uma consulta em " + dataHora + ".");
            }
        }

        // se o médico aceita o convênio do paciente, o paciente paga só a coparticipação
        double valor = m.getValorConsulta();
        Convenio convenio = m.buscarConvenio(p.getConvenio());
        if (convenio != null) {
            valor = convenio.calcularValorPaciente(valor);
        }

        Consulta nova = new Consulta(m, p, dataHora, valor);
        this.consultas.add(nova);
        slot.setOcupado(true);
        return nova;
    }

    public Medico buscarMedico(String crm) throws NaoEncontradoException {
        for (Medico m : this.medicos) {
            if (m.getCrm().equalsIgnoreCase(crm)) {
                return m;
            }
        }
        throw new NaoEncontradoException("Médico com CRM " + crm + " não encontrado.");
    }

    public Paciente buscarPaciente(String cpf) throws NaoEncontradoException {
        for (Paciente p : this.pacientes) {
            if (p.getCpf().equals(cpf)) {
                return p;
            }
        }
        throw new NaoEncontradoException("Paciente com CPF " + cpf + " não encontrado.");
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
