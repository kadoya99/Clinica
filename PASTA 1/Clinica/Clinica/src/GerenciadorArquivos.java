import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivos {

    public static List<Medicamento> carregarMedicamentos(String nomeArquivo) {
        List<Medicamento> lista = new ArrayList<>();

        
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                
                String[] partes = linha.split(";");
                
                if (partes.length < 5) continue; 

                String tipo = partes[0].toUpperCase().trim();
                String nome = partes[1];
                String composicao = partes[2];
                String id = partes[3];
                String indicacao = partes[4];

                
               switch (tipo) {
    case "ORAL":
        
        lista.add(new MedOral(nome, composicao, id, indicacao));
        break;
    case "INJETAVEL":
        
        lista.add(new MedInjetavel(nome, composicao, id, indicacao));
        break;
    case "TOPICO":
        
        lista.add(new MedTopico(nome, composicao, id, indicacao));
        break;
}
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de medicamentos: " + e.getMessage());
        }

        return lista;
    }
}
