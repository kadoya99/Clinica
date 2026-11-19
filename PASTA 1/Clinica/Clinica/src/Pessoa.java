public abstract class Pessoa {
    private String nome;
    private String telefone;
    private String cpf;
    private String dataNasc;

    public Pessoa(String nome, String cpf, String telefone, String dataNasc) throws DadoInvalidoException {
        this.nome = Validador.texto("Nome", nome);
        this.cpf = Validador.cpf(cpf);
        this.telefone = Validador.telefone(telefone);
        this.dataNasc = Validador.dataNascimento(dataNasc);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws DadoInvalidoException {
        this.nome = Validador.texto("Nome", nome);
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) throws DadoInvalidoException {
        this.telefone = Validador.telefone(telefone);
    }

    public String getCpf() {
        return cpf;
    }

    public String getDtNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) throws DadoInvalidoException {
        this.dataNasc = Validador.dataNascimento(dataNasc);
    }
}
