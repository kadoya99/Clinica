public class MedTopico extends Medicamento {
    
    public MedTopico(String nome, String composicao, String id, String indicacao) {
        super(nome, composicao, id, indicacao);
    }
    @Override
    public String orientacaoUso() {
        return "Uso externo. Aplicar sobre a pele e massagear.";
    }

}