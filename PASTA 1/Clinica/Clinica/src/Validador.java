import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Validador {

    // STRICT faz o 31/02 ser recusado em vez de ajustado
    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    // esses caracteres são separadores no arquivo, então não podem ser digitados
    private static final String PROIBIDOS = ";#@";

    public static String texto(String campo, String valor) throws DadoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DadoInvalidoException("O campo " + campo + " é obrigatório.");
        }
        valor = valor.trim();
        for (int i = 0; i < PROIBIDOS.length(); i++) {
            if (valor.indexOf(PROIBIDOS.charAt(i)) >= 0) {
                throw new DadoInvalidoException("O campo " + campo + " não pode ter os caracteres ; # @");
            }
        }
        return valor;
    }

    public static String cpf(String valor) throws DadoInvalidoException {
        String numeros = soNumeros("CPF", valor);
        if (numeros.length() != 11) {
            throw new DadoInvalidoException("O CPF deve ter 11 números.");
        }
        return numeros;
    }

    public static String telefone(String valor) throws DadoInvalidoException {
        String numeros = soNumeros("Telefone", valor);
        if (numeros.length() != 10 && numeros.length() != 11) {
            throw new DadoInvalidoException("O telefone deve ter 10 ou 11 números (com DDD).");
        }
        return numeros;
    }

    // tira pontos, traços, parênteses e espaços e confere se sobrou só número
    private static String soNumeros(String campo, String valor) throws DadoInvalidoException {
        texto(campo, valor);
        String limpo = valor.replace(".", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
        for (int i = 0; i < limpo.length(); i++) {
            if (!Character.isDigit(limpo.charAt(i))) {
                throw new DadoInvalidoException("O campo " + campo + " deve conter apenas números.");
            }
        }
        return limpo;
    }

    public static String dataNascimento(String valor) throws DadoInvalidoException {
        texto("Data de nascimento", valor);
        try {
            LocalDate data = LocalDate.parse(valor.trim(), FORMATO_DATA);
            if (data.isAfter(LocalDate.now())) {
                throw new DadoInvalidoException("A data de nascimento não pode ser no futuro.");
            }
            return valor.trim();
        } catch (DateTimeParseException e) {
            throw new DadoInvalidoException("Data de nascimento inválida. Use dd/mm/aaaa.");
        }
    }

    public static String dataHora(String valor) throws DadoInvalidoException {
        texto("Horário", valor);
        try {
            LocalDateTime.parse(valor.trim(), FORMATO_DATA_HORA);
            return valor.trim();
        } catch (DateTimeParseException e) {
            throw new DadoInvalidoException("Horário inválido. Use dd/mm/aaaa hh:mm (ex: 10/12/2026 14:30).");
        }
    }

    // aceita vírgula ou ponto como separador decimal
    public static double numero(String campo, String valor, double minimo, double maximo) throws DadoInvalidoException {
        texto(campo, valor);
        double numero;
        try {
            numero = Double.parseDouble(valor.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new DadoInvalidoException("O campo " + campo + " deve ser um número.");
        }
        // escrito assim para também recusar NaN
        if (!(numero >= minimo && numero <= maximo)) {
            throw new DadoInvalidoException("O campo " + campo + " deve estar entre " + minimo + " e " + maximo + ".");
        }
        return numero;
    }
}
