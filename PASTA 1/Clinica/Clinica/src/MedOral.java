public class MedOral extends Medicamento {
    
    public MedOral(String nome, String composicao, String id, String indicacao) {
        super(nome, composicao, id, indicacao);
    }
    @Override
    public String orientacaoUso() {
        return "Ingerir com água. Não mastigar";
    }

}
