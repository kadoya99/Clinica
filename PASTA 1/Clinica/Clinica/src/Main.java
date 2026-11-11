import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Clinica minhaClinica = new Clinica();

        Scanner teclado = new Scanner(System.in);

        List<Medicamento> estoque = GerenciadorArquivos.carregarMedicamentos("medicamentos.txt");

        int opcao = 0;

       
        while (opcao != 6) {
            System.out.println("\n--- BOAS VINDAS A CLÍNICA MÉDICA ---");
            System.out.println("1. Cadastrar Médico");
            System.out.println("2. Cadastrar Paciente");
            System.out.println("3. Agendar Consulta");
            System.out.println("4. Atender Consulta");
            System.out.println("5. Ver Histórico do Paciente");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = teclado.nextInt();
            teclado.nextLine(); 

            switch (opcao) {
               case 1:
    System.out.println("Nome do Médico: ");
    String nomeM = teclado.nextLine();
    System.out.println("Especialidade: ");
    String especialidadeM = teclado.nextLine();
    System.out.println("CRM: ");
    String crmM = teclado.nextLine();
    System.out.print("CPF: ");
    String cpfM = teclado.nextLine();
    System.out.print("Telefone: ");
    String telefoneM = teclado.nextLine();
    System.out.print("Data de Nascimento (dd/mm/aaaa): ");
    String dataNascM = teclado.nextLine();

    Medico novoMed = new Medico(nomeM, cpfM, telefoneM, dataNascM, especialidadeM, crmM);

 
    System.out.print("Deseja participar de um convênio? (s/n): ");
    if (teclado.nextLine().equalsIgnoreCase("s")) {
        System.out.print("Nome do convênio: ");
        String nomeConv = teclado.nextLine();

        Convenio novoConv = new Convenio(nomeConv, "C-" + nomeConv.length(), 0.2, 100.0);
        novoMed.addConvenio(novoConv);
    }

    System.out.print("Deseja cadastrar horários na agenda agora? (s/n): ");
    if (teclado.nextLine().equalsIgnoreCase("s")) {
        System.out.println("Digite os horários (ex: 10:00) ou 'fim' para encerrar:");
        while (true) {
            System.out.print("Horário: ");
            String hora = teclado.nextLine();
            if (hora.equalsIgnoreCase("fim")) break;
            novoMed.addHorario(hora);
        }
    }

    minhaClinica.regMedico(novoMed);
    System.out.println("Registro profissional do Dr.(a) " + nomeM + " concluído com sucesso. Sistema atualizado.");
    break;

                    case 2:
                    System.out.println("Nome do Paciente: ");
                    String nomeP = teclado.nextLine();
                    System.out.println("Data de Nascimento (dd/mm/aaaa): ");
                    String dataNascP = teclado.nextLine();
                    System.out.println("CPF: ");
                    String cpfP = teclado.nextLine();
                    System.out.println("Telefone: ");
                    String telefoneP = teclado.nextLine();

                    Paciente novoPac = new Paciente(nomeP, cpfP, telefoneP, dataNascP);

                    System.out.println("O Paciente possui alguma alergia ou condição médica pré-existente? (s/n): ");
                    if (teclado.nextLine().equalsIgnoreCase("s")) {
                        System.out.println("Descreva: ");
                        String historicoMedico = teclado.nextLine();
                        novoPac.addHistorico(historicoMedico);
                    minhaClinica.regPaciente(novoPac);
                    System.out.println("Cadastro do paciente " + nomeP + " finalizado. Prontuário liberado para o agendamento.");
                    }
                    minhaClinica.regPaciente(novoPac);
                    System.out.println("Cadastro do paciente " + nomeP + " finalizado. Prontuário liberado para o agendamento.");
                    break; 

                 case 3:
                    System.out.print("Digite o CRM do Médico: ");
                    String crmBusca = teclado.nextLine();
                    System.out.println("Digite o Nome do Paciente: ");
                    String nomeBusca = teclado.nextLine();
                 

                    Medico medFound = minhaClinica.buscarMedico(crmBusca);
                    Paciente pacFound = minhaClinica.buscarPaciente(nomeBusca);

                    if( medFound != null && pacFound != null) {

                    System.out.println("\n--- Agenda do Dr(a). " + medFound.getNome() + " ---");

                    boolean temHorario = false;
                    for(SlotTempo slot : medFound.getAgenda()){
                        if (!slot.isOcupado()) {
                            System.out.println("- " + slot.getDataHora());
                            temHorario = true;
                        }
                    }
                    if (!temHorario) {
                        System.out.println("Nenhum horário disponível no momento.");
                    } else {
                        System.out.print("Escolha um horário para agendar a consulta: ");
                        String horarioEscolhido = teclado.nextLine();

                        minhaClinica.agendarC(pacFound, medFound, horarioEscolhido);
                        System.out.println("Consulta agendada com sucesso para " + pacFound.getNome() + " com o Dr(a). " + medFound.getNome() + " no horário " + horarioEscolhido + ". Sistema atualizado.");
                    }
                    } else {
                        System.out.println("Médico ou Paciente não encontrado. Verifique os dados e tente novamente.");
                    }
                    break;
                    

             case 4:
                if(minhaClinica.getConsultas().isEmpty()){
                    System.out.println("Nenhuma consulta agendada para atendimento.");
                    break;
                }
                System.out.println("\n--- Consultas Aguardando Atendimento ---");
                for(int i = 0; i < minhaClinica.getConsultas().size(); i++){
                    Consulta c = minhaClinica.getConsultas().get(i);
                    System.out.println(i + ". Paciente: " + c.getPaciente().getNome() + " | Médico: " + c.getMedico().getNome() + " | Horário: " + c.getDataHora());

                }
                System.out.println("Escolha o número da consulta: ");
                int indiceConsulta = teclado.nextInt();
                teclado.nextLine();

                Consulta consultaAtual = minhaClinica.getConsultas().get(indiceConsulta);

                System.out.println("\n--- Registro de Sinais Vitais ---");
                System.out.print("Digite a Pressão Arterial (ex: 12,8): ");
                double pa = teclado.nextDouble();
                consultaAtual.addSinaisVitais("Pressão Arterial", pa, "mmHg");

                System.out.print("Digite a Temperatura (ex: 36,5): ");
                double temp = teclado.nextDouble();
                consultaAtual.addSinaisVitais("Temperatura", temp, "°C");
                
                System.out.print("Digite a Saturação de O2 (ex: 98): ");
                double sat = teclado.nextDouble();
                consultaAtual.addSinaisVitais("Saturação", sat, "%");
                teclado.nextLine();

                System.out.print("\nDigite o Diagnóstico (Doença): ");
                String nomeDoenca = teclado.nextLine();
                System.out.print("Descrição dos sintomas: ");
                String descSintomas = teclado.nextLine();

                Diagnostico diag = new Diagnostico(nomeDoenca, descSintomas);

                System.out.println("\n--- Estoque de Medicamentos ---");
                for (int i = 0; i < 10 && i < estoque.size(); i++) {
                System.out.println(i + " - " + estoque.get(i).getNome() + " (" + estoque.get(i).getIndicacao() + ")");
    }
                System.out.println("Escolha o número do medicamento para prescrever: ");
                int indiceMed = teclado.nextInt();
                teclado.nextLine();


                diag.addSugestao(estoque.get(indiceMed));

                consultaAtual.finalizarConsulta(diag);

                   System.out.println("\n========================================");
                System.out.println("       RESUMO DO ATENDIMENTO FINALIZADO ");
                System.out.println("========================================");
                 System.out.println("Paciente: " + consultaAtual.getPaciente().getNome());
                   System.out.println("Médico: " + consultaAtual.getMedico().getNome());
                System.out.println("Doença: " + diag.getNomeDoenca());
               System.out.println("\nSinais Vitais:");
                for (Object sv : consultaAtual.getSinaisVitais()) {
                  System.out.println("- " + sv.toString());
    }
    System.out.println("\nReceita Médica:");
   for (Object objM : consultaAtual.getMedicamentoPrescrito()) {
        Medicamento m = (Medicamento) objM;
        System.out.println("-> " + m.getNome());
         System.out.println("   INSTRUÇÃO: " + m.orientacaoUso()); 
    }
    System.out.println("========================================\n");
    break;

          case 5:
                System.out.println("Nome do Paciente: ");
                String nomeBuscaHist = teclado.nextLine();
                Paciente pacHist = minhaClinica.buscarPaciente(nomeBuscaHist);

                if (pacHist != null) {
                    System.out.println("\n--- Histórico do Paciente: " + pacHist.getNome() + " ---");

                    System.out.println("Alergias e Condições:");
                    for (String condicao : pacHist.getHistoricoMedico()) {
                        System.out.println("- " + condicao);
                    }

                    boolean aConsulta = false;

                    for (Consulta c : minhaClinica.getConsultas()) {
                        if (c.getPaciente().getNome().equals(pacHist.getNome()) && c.getDiagnostico() != null) {
                            aConsulta = true;
                            
                            System.out.println("\nData da Consulta: " + c.getDataHora());
                            System.out.println("Médico: " + c.getMedico().getNome());
                            System.out.println("Diagnóstico: " + c.getDiagnostico().getNomeDoenca());
                            System.out.println("Sintomas: " + c.getDiagnostico().getDescricao());
                            
                            System.out.println("Sinais Vitais:");
                            for (Object sv : c.getSinaisVitais()) {
                                System.out.println("- " + sv.toString());
                            }
                            
                            System.out.println("Medicamentos Receitados:");
                            for (Medicamento m : c.getDiagnostico().getMedicamentosPassados()) {
                                System.out.println("-> " + m.getNome() + " - " + m.orientacaoUso());
                            }
                            System.out.println("-----------------------------------");
                        }
                    }

                    if (aConsulta == false) {
                        System.out.println("Nenhuma consulta finalizada para esse paciente.");
                    }

                } else {
                    System.out.println("Paciente não encontrado.");
                }
                break;

                        
             case 6:

                 System.out.println("Sessão encerrada. Sistema desconetado com segurança.");   

                    break;



            }
        }
    }

}
