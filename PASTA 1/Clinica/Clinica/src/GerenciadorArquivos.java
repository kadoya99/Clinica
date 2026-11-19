import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivos {

    public static final String ARQ_MEDICOS = "medicos.txt";
    public static final String ARQ_PACIENTES = "pacientes.txt";
    public static final String ARQ_CONSULTAS = "consultas.txt";

    public static List<Medicamento> carregarMedicamentos(String nomeArquivo) throws ArquivoException {
        List<Medicamento> lista = new ArrayList<>();

        for (String linha : lerLinhas(nomeArquivo, false)) {
            String[] partes = linha.split(";");

            if (partes.length < 5) continue; // linha incompleta é ignorada

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
        return lista;
    }

    // Aqui entra a interface: qualquer lista de Persistivel pode ser salva do mesmo jeito
    public static void salvar(String nomeArquivo, List<? extends Persistivel> lista) throws ArquivoException {
        List<String> linhas = new ArrayList<>();
        for (Persistivel p : lista) {
            linhas.add(p.paraLinha());
        }
        try {
            Files.write(Paths.get(nomeArquivo), linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ArquivoException("Erro ao salvar o arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }

    public static void salvarTudo(Clinica clinica) throws ArquivoException {
        salvar(ARQ_MEDICOS, clinica.getMedicos());
        salvar(ARQ_PACIENTES, clinica.getPacientes());
        salvar(ARQ_CONSULTAS, clinica.getConsultas());
    }

    // A ordem importa: as consultas dependem de médicos e pacientes já carregados
    public static void carregarTudo(Clinica clinica, List<Medicamento> estoque) throws ArquivoException {
        carregarMedicos(clinica);
        carregarPacientes(clinica);
        carregarConsultas(clinica, estoque);
    }

    // Se o arquivo não existir ainda (primeira vez), devolve lista vazia
    private static List<String> lerLinhas(String nomeArquivo, boolean podeNaoExistir) throws ArquivoException {
        if (podeNaoExistir && !new File(nomeArquivo).exists()) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(Paths.get(nomeArquivo), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ArquivoException("Erro ao ler o arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }

    // Lista vazia no arquivo vira texto vazio; split("#") em texto vazio daria 1 item vazio
    private static String[] itens(String campo) {
        if (campo.isEmpty()) {
            return new String[0];
        }
        return campo.split("#");
    }

    private static void carregarMedicos(Clinica clinica) throws ArquivoException {
        int numeroLinha = 0;
        for (String linha : lerLinhas(ARQ_MEDICOS, true)) {
            numeroLinha++;
            if (linha.trim().isEmpty()) continue;
            try {
                String[] c = linha.split(";", -1); // -1 mantém campos vazios no final
                if (c.length != 9) {
                    throw new DadoInvalidoException("quantidade de campos incorreta");
                }
                Medico m = new Medico(c[0], c[1], c[2], c[3], c[4], c[5], Double.parseDouble(c[6]));
                for (String item : itens(c[7])) {
                    String[] partes = item.split("@");
                    m.addConvenio(new Convenio(partes[0], Double.parseDouble(partes[1])));
                }
                for (String horario : itens(c[8])) {
                    m.addHorario(horario);
                }
                clinica.regMedico(m);
            } catch (ClinicaException | RuntimeException e) {
                throw new ArquivoException("Linha " + numeroLinha + " de " + ARQ_MEDICOS + " inválida: " + e.getMessage());
            }
        }
    }

    private static void carregarPacientes(Clinica clinica) throws ArquivoException {
        int numeroLinha = 0;
        for (String linha : lerLinhas(ARQ_PACIENTES, true)) {
            numeroLinha++;
            if (linha.trim().isEmpty()) continue;
            try {
                String[] c = linha.split(";", -1);
                if (c.length != 6) {
                    throw new DadoInvalidoException("quantidade de campos incorreta");
                }
                Paciente p = new Paciente(c[0], c[1], c[2], c[3], c[4]);
                for (String condicao : itens(c[5])) {
                    p.addHistorico(condicao);
                }
                clinica.regPaciente(p);
            } catch (ClinicaException | RuntimeException e) {
                throw new ArquivoException("Linha " + numeroLinha + " de " + ARQ_PACIENTES + " inválida: " + e.getMessage());
            }
        }
    }

    private static void carregarConsultas(Clinica clinica, List<Medicamento> estoque) throws ArquivoException {
        int numeroLinha = 0;
        for (String linha : lerLinhas(ARQ_CONSULTAS, true)) {
            numeroLinha++;
            if (linha.trim().isEmpty()) continue;
            try {
                String[] c = linha.split(";", -1);
                if (c.length != 8) {
                    throw new DadoInvalidoException("quantidade de campos incorreta");
                }
                Medico medico = clinica.buscarMedico(c[0]);
                Paciente paciente = clinica.buscarPaciente(c[1]);
                SlotTempo slot = medico.buscarSlot(c[2]);
                if (slot == null || slot.isOcupado()) {
                    throw new DadoInvalidoException("horário " + c[2] + " inexistente ou repetido");
                }

                Consulta consulta = new Consulta(medico, paciente, c[2], Double.parseDouble(c[3]));
                slot.setOcupado(true);

                // doença preenchida = consulta já atendida
                if (!c[4].isEmpty()) {
                    Diagnostico diag = new Diagnostico(c[4], c[5]);
                    for (String id : itens(c[7])) {
                        diag.addSugestao(buscarMedicamento(estoque, id));
                    }
                    List<SinaisVitais> sinais = new ArrayList<>();
                    for (String item : itens(c[6])) {
                        String[] partes = item.split("@");
                        sinais.add(new SinaisVitais(partes[0], Double.parseDouble(partes[1]), partes[2]));
                    }
                    consulta.finalizarConsulta(sinais, diag);
                }
                clinica.getConsultas().add(consulta);
            } catch (ClinicaException | RuntimeException e) {
                throw new ArquivoException("Linha " + numeroLinha + " de " + ARQ_CONSULTAS + " inválida: " + e.getMessage());
            }
        }
    }

    private static Medicamento buscarMedicamento(List<Medicamento> estoque, String id) throws NaoEncontradoException {
        for (Medicamento m : estoque) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        throw new NaoEncontradoException("Medicamento " + id + " não existe em medicamentos.txt.");
    }
}
