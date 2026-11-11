public abstract class Pessoa {
    private String nome;
    private String telefone;
    private String cpf;
    private String dataNasc;


    public Pessoa(String nome, String cpf, String telefone, String dataNasc) {
       this.nome = nome;
       this.cpf = cpf;
       this.telefone = telefone;
       this.dataNasc = dataNasc;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public String getDtNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

}
