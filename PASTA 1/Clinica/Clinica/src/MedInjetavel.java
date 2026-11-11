public class MedInjetavel extends Medicamento {
    
    public MedInjetavel(String nome, String composicao, String id, String indicacao) {
        super(nome, composicao, id, indicacao);
    }
    @Override
    public String orientacaoUso() {
        return "Aplicação via injeção. Deve ser administrado por profissional qualificado.";
    }

}
