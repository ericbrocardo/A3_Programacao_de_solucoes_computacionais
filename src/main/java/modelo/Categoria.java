package modelo;

public class Categoria {
// (Opções Fixas)
    public enum Tamanho {
        PEQUENO, MEDIO, GRANDE
    }

    public enum Embalagem {
        LATA, VIDRO, PLASTICO
    }
    // ATRIBUTOS
    private int id;
    private String nome;
    private Tamanho tamanho;
    private Embalagem embalagem;
