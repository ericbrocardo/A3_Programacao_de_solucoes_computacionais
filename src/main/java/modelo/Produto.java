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
    //GET
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public String getUnidade() {
        return unidade;
    }

    public double getQtdEstoque() {
        return qtdEstoque;
    }

    public double getQtdMinima() {
        return qtdMinima;
    }

    public double getQtdMaxima() {
        return qtdMaxima;
    }

    public Categoria getCategoria() {
        return categoria;
    }
//SET
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public void setQtdEstoque(double v) {
        this.qtdEstoque = v;
    }

    public void setQtdMinima(double v) {
        this.qtdMinima = v;
    }

    public void setQtdMaxima(double v) {
        this.qtdMaxima = v;
    }

    public void setCategoria(Categoria c) {
        this.categoria = c;
    }
    //METODO DE SOBRESCRITA
    @Override
    public String toString() {
        return nome;
    }
}