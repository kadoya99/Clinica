public class Convenio {
    private String nome;
    private double percentualCoparticipacao; // de 0 a 100: quanto o paciente paga

    public Convenio(String nome, double percentualCoparticipacao) throws DadoInvalidoException {
        this.nome = Validador.texto("Nome do convênio", nome);
        if (!(percentualCoparticipacao >= 0 && percentualCoparticipacao <= 100)) {
            throw new DadoInvalidoException("O percentual do convênio deve estar entre 0 e 100.");
        }
        this.percentualCoparticipacao = percentualCoparticipacao;
    }

    public double calcularValorPaciente(double valorConsulta) {
        return valorConsulta * percentualCoparticipacao / 100;
    }

    public String getNome() {
        return nome;
    }

    public double getPercentualCoparticipacao() {
        return percentualCoparticipacao;
    }

    @Override
    public String toString() {
        return nome + " (paciente paga " + percentualCoparticipacao + "%)";
    }
}
