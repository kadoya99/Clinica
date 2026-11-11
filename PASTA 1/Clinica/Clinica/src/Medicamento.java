public abstract class Medicamento {
private String nome;
private String composicao;
private String id;
private String indicacao;


public Medicamento(String nome, String composicao, String id, String indicacao) {
    this.nome = nome;
    this.composicao = composicao;
    this.id = id;
    this.indicacao = indicacao;

}

public abstract String orientacaoUso();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getComposicao() {
        return composicao;
    }

    public void setComposicao(String composicao) {
        this.composicao = composicao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndicacao() {
        return indicacao;
    }
    public void setIndicacao(String indicacao) {
        this.indicacao = indicacao;
    }
}

