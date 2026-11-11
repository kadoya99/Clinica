import java.util.ArrayList;
import java.util.List;

public class Diagnostico {

    private String nomeDoenca;
    private String descricao;
    private List<Medicamento> medicamentosPassados;
    
    public Diagnostico(String nomeDoenca, String descricao) {
        this.nomeDoenca = nomeDoenca;
        this.descricao = descricao;
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