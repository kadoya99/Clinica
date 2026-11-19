public class SinaisVitais {
    private String nome;
    private double valor;
    private String unidade;

    public SinaisVitais(String nome, double valor, String unidade) {
        this.nome = nome;
        this.valor = valor;
        this.unidade = unidade;
    }

    public String paraTexto() {
        return nome + "@" + valor + "@" + unidade;
    }

    @Override
    public String toString() {
        return nome + ": " + valor + " " + unidade;
    }
}
