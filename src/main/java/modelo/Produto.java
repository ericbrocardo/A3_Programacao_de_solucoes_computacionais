package modelo;

public class Produto {
// ATRIBUTOS
    private int id;
    private String nome;
    private double preco;
    private String unidade;
    private double qtdEstoque;
    private double qtdMinima;
    private double qtdMaxima;
    private Categoria categoria;
    
//CONSTRUTOR
    public Produto() {
    }

    public Produto(int id, String nome, double preco, String unidade,
            double qtdEstoque, double qtdMinima, double qtdMaxima,
            Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.unidade = unidade;
        this.qtdEstoque = qtdEstoque;
        this.qtdMinima = qtdMinima;
        this.qtdMaxima = qtdMaxima;
        this.categoria = categoria;
    }