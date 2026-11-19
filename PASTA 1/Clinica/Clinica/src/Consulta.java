import java.util.ArrayList;
import java.util.List;

public class Consulta implements Persistivel {
    private Medico medico;
    private Paciente paciente;
    private String dataHora;
    private double valorCobrado; // já com o desconto do convênio, se houver
    private Diagnostico diagnostico; // null enquanto a consulta não foi atendida
    private List<SinaisVitais> sinaisVitais;

    public Consulta(Medico medico, Paciente paciente, String dataHora, double valorCobrado) {
        this.medico = medico;
        this.paciente = paciente;
        this.dataHora = dataHora;
        this.valorCobrado = valorCobrado;
        this.sinaisVitais = new ArrayList<>();
    }

    public boolean isAtendida() {
        return diagnostico != null;
    }

    public void finalizarConsulta(List<SinaisVitais> sinais, Diagnostico d) throws ConsultaJaAtendidaException {
        if (isAtendida()) {
            throw new ConsultaJaAtendidaException("Esta consulta já foi atendida.");
        }
        this.sinaisVitais.addAll(sinais);
        this.diagnostico = d;
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

    public double getValorCobrado() {
        return valorCobrado;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    @Override
    public String paraLinha() {
        String doenca = "";
        String descricao = "";
        String sinais = "";
        String medicamentos = "";
        if (diagnostico != null) {
            doenca = diagnostico.getNomeDoenca();
            descricao = diagnostico.getDescricao();
            List<String> listaSinais = new ArrayList<>();
            for (SinaisVitais sv : sinaisVitais) {
                listaSinais.add(sv.paraTexto());
            }
            sinais = String.join("#", listaSinais);
            List<String> ids = new ArrayList<>();
            for (Medicamento m : diagnostico.getMedicamentosPassados()) {
                ids.add(m.getId());
            }
            medicamentos = String.join("#", ids);
        }
        return medico.getCrm() + ";" + paciente.getCpf() + ";" + dataHora + ";" + valorCobrado + ";"
                + doenca + ";" + descricao + ";" + sinais + ";" + medicamentos;
    }

    @Override
    public String toString() {
        return dataHora + " - " + paciente.getNome() + " com " + medico.getNome();
    }
}
