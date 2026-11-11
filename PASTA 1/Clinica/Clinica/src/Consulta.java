import java.util.ArrayList;
import java.util.List;

public class Consulta {
    private Medico medico;
    private Paciente paciente;
    private String dataHora;
    private Diagnostico diagnostico;

    private List<SinaisVitais> sinaisVitais;
    private List<Medicamento> medicamentoPrescrito;

    public Consulta ( Medico medico, Paciente paciente, String dataHora){

        this.medico = medico;
        this.paciente = paciente;
        this.dataHora = dataHora;
        this.medicamentoPrescrito = new ArrayList<>();
        this.sinaisVitais = new ArrayList<SinaisVitais>();
    }

    public void finalizarConsulta(Diagnostico d){
        this.diagnostico = d;

        for (Medicamento m : d.getMedicamentosPassados()) {
            this.indicaMedicamento(m);
        }
    }

    public void indicaMedicamento (Medicamento medicamento){
        this.medicamentoPrescrito.add(medicamento);
    }

    public void addSinaisVitais(String nome, double valor, String unidade) {
        this.sinaisVitais.add(new SinaisVitais(nome, valor, unidade));
    }

    public List<SinaisVitais> getSinaisVitais() {
        return sinaisVitais;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

     public Diagnostico getDiagnostico() {
        return this.diagnostico;
    }
    public List<Medicamento> getMedicamentoPrescrito() {
        return medicamentoPrescrito;
    }
}
