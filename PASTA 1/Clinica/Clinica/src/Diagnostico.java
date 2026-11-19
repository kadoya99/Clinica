import java.util.ArrayList;
import java.util.List;

public class Diagnostico {

    private String nomeDoenca;
    private String descricao;
    private List<Medicamento> medicamentosPassados;

    public Diagnostico(String nomeDoenca, String descricao) throws DadoInvalidoException {
        this.nomeDoenca = Validador.texto("Doença", nomeDoenca);
        this.descricao = Validador.texto("Descrição dos sintomas", descricao);
        this.medicamentosPassados = new ArrayList<>();
    }

    public void addSugestao(Medicamento m) {
        this.medicamentosPassados.add(m);
    }

    public String getNomeDoenca() {
        return nomeDoenca;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<Medicamento> getMedicamentosPassados() {
        return medicamentosPassados;
    }
}
