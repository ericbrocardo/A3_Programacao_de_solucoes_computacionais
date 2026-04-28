package model;

public class Produto {
    private String nome;
    private double precoUnitario;
    private String unidade;
    private int quantidadeEstoque;
    private int quantidadeMinima;
    private int quantidadeMaxima;
    private Categoria categoria;


    public Produto(String nome, double preco, String un, int quantidadeMin, int quantidadeMax, Categoria categoriaSelecionada) {
        this.nome = nome;
        this.precoUnitario = preco;
        this.unidade = un;
        this.quantidadeMinima = quantidadeMin;
        this.quantidadeMaxima = quantidadeMax;
        this.categoria = categoriaSelecionada;
        this.quantidadeEstoque = 0;
    }

    public String getNome() {
        return nome;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public String getUnidade() {
        return unidade;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public int getQuantidadeMaxima() {
        return quantidadeMaxima;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public void setQuantidadeMaxima(int quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

}
