import java.util.ArrayList;
import java.util.List;

public class Medico extends Pessoa implements Persistivel {
    private String especialidade;
    private String crm;
    private double valorConsulta;
    private List<Convenio> listaConvenio;
    private List<SlotTempo> agenda;

    public Medico(String nome, String cpf, String telefone, String dataNasc, String especialidade, String crm,
                  double valorConsulta) throws DadoInvalidoException {
        super(nome, cpf, telefone, dataNasc);
        this.especialidade = Validador.texto("Especialidade", especialidade);
        this.crm = Validador.texto("CRM", crm);
        if (!(valorConsulta > 0)) {
            throw new DadoInvalidoException("O valor da consulta deve ser maior que zero.");
        }
        this.valorConsulta = valorConsulta;
        this.listaConvenio = new ArrayList<>();
        this.agenda = new ArrayList<>();
    }

    public void addConvenio(Convenio convenio) throws DadoInvalidoException {
        if (buscarConvenio(convenio.getNome()) != null) {
            throw new DadoInvalidoException("O convênio " + convenio.getNome() + " já foi adicionado.");
        }
        this.listaConvenio.add(convenio);
    }

    public void addHorario(String horario) throws DadoInvalidoException {
        horario = Validador.dataHora(horario);
        if (buscarSlot(horario) != null) {
            throw new DadoInvalidoException("O horário " + horario + " já existe na agenda.");
        }
        this.agenda.add(new SlotTempo(horario));
    }

    public SlotTempo buscarSlot(String dataHora) {
        for (SlotTempo slot : agenda) {
            if (slot.getDataHora().equals(dataHora)) {
                return slot;
            }
        }
        return null;
    }

    // devolve null se o médico não aceita esse convênio
    public Convenio buscarConvenio(String nome) {
        for (Convenio c : listaConvenio) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                return c;
            }
        }
        return null;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public double getValorConsulta() {
        return valorConsulta;
    }

    public List<Convenio> getListaConvenio() {
        return listaConvenio;
    }

    public List<SlotTempo> getAgenda() {
        return agenda;
    }

    @Override
    public String paraLinha() {
        List<String> convenios = new ArrayList<>();
        for (Convenio c : listaConvenio) {
            convenios.add(c.getNome() + "@" + c.getPercentualCoparticipacao());
        }
        List<String> horarios = new ArrayList<>();
        for (SlotTempo slot : agenda) {
            horarios.add(slot.getDataHora());
        }
        return getNome() + ";" + getCpf() + ";" + getTelefone() + ";" + getDtNasc() + ";" + especialidade + ";"
                + crm + ";" + valorConsulta + ";" + String.join("#", convenios) + ";" + String.join("#", horarios);
    }

    @Override
    public String toString() {
        return getNome() + " (CRM " + crm + ")";
    }
}
