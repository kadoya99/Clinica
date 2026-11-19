// Quem implementa sabe se transformar em uma linha de texto para ser salvo em arquivo.
// Campos são separados por ";", itens de lista por "#" e partes de um item por "@".
public interface Persistivel {
    String paraLinha();
}
