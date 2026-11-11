public class Convenio {
    private String nome;
    private String codigo;
    private double percentualCoparticipacao;
    private double valorMensalidade;

        public Convenio(String nome, String codigo, double percentualCoparticipacao, double valorMensalidade) {
            this.nome = nome;
            this.codigo = codigo;
            this.percentualCoparticipacao = percentualCoparticipacao;
            this.valorMensalidade = valorMensalidade;
        }

     public double calcularValorPaciente(double valorConsulta) {
        return valorConsulta * percentualCoparticipacao;

     }

      public String getNome() {
        return nome;
      }

      public String getCodigo(){
        return codigo;
      }

      public double getPercentualCoparticipacao() {
        return percentualCoparticipacao;
      }

      public double getValorMensalidade() {
        return valorMensalidade;
      }

    }
