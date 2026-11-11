import java.util.ArrayList;
import java.util.List;

public class Paciente extends Pessoa {
    private List<String> historicoMedico;
    private List<String> listaConsulta;

    public Paciente(String nome, String cpf, String telefone, String dataNasc) {
        super(nome, cpf, telefone, dataNasc);
        this.historicoMedico = new ArrayList<>();
        this.listaConsulta = new ArrayList<>();
    }
    public void addHistorico(String observacao){
        this.historicoMedico.add(observacao);
    }

    public void addConsulta(String dataHora){
        this.listaConsulta.add(dataHora);
    }
    public List<String> getHistoricoMedico() {
        return historicoMedico;
    }

    public List<String> getListaConsulta() {
        return listaConsulta;
    }
}