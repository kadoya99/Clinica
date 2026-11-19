import java.util.ArrayList;
import java.util.List;

public class Paciente extends Pessoa implements Persistivel {
    private String convenio; // nome do convênio, vazio se não tiver
    private List<String> historicoMedico;

    public Paciente(String nome, String cpf, String telefone, String dataNasc, String convenio)
            throws DadoInvalidoException {
        super(nome, cpf, telefone, dataNasc);
        if (convenio == null || convenio.trim().isEmpty()) {
            this.convenio = "";
        } else {
            this.convenio = Validador.texto("Convênio", convenio);
        }
        this.historicoMedico = new ArrayList<>();
    }

    public void addHistorico(String observacao) throws DadoInvalidoException {
        this.historicoMedico.add(Validador.texto("Alergia/condição", observacao));
    }

    public String getConvenio() {
        return convenio;
    }

    public List<String> getHistoricoMedico() {
        return historicoMedico;
    }

    @Override
    public String paraLinha() {
        return getNome() + ";" + getCpf() + ";" + getTelefone() + ";" + getDtNasc() + ";"
                + convenio + ";" + String.join("#", historicoMedico);
    }

    @Override
    public String toString() {
        return getNome() + " (CPF " + getCpf() + ")";
    }
}
