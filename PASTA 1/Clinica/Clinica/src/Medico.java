import java.util.ArrayList;
import java.util.List;

public class Medico extends Pessoa { 
    private String especialidade;
    private String crm;
    private List<Convenio> listaConvenio;
    private List<SlotTempo> agenda;

    public Medico(String nome, String cpf, String telefone, String dataNasc, String especialidade, String crm) {
        super(nome, cpf, telefone, dataNasc);
        this.crm = crm;
        this.especialidade = especialidade;
        this.listaConvenio = new ArrayList<>();
        this.agenda = new ArrayList<>();
    }

    public void addConvenio(Convenio convenio) {
        this.listaConvenio.add(convenio);
    }

    public void addHorario(String horario) {
        this.agenda.add(new SlotTempo(horario));
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public List<Convenio> getListaConvenio() {
        return listaConvenio;
    }

    public List<SlotTempo> getAgenda() {
        return agenda;
    }

    public void exibirHorarioLivre() {
        System.out.println("Horários livres do Dr(a). " + this.getNome() + ":");
        boolean temHorario = false;
        for(SlotTempo slot : agenda){
            if (!slot.isOcupado()) {
                System.out.println("- " + slot.getDataHora());
                temHorario = true;
            }
        }
        if (!temHorario) {
            System.out.println("Nenhum horário disponível no momento.");
        }   

    }
}