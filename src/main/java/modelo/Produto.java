package modelo;

/**
 * Representa um produto no sistema de controle de estoque.
 * Cada produto possui informações de preço, unidade, quantidades
 * de estoque e está associado a uma categoria.
 */
public class Produto {

    private int id;
    private String nome;
    private double preco;
    private String unidade;
    private double qtdEstoque;
    private double qtdMinima;
    private double qtdMaxima;
    private Categoria categoria;

    /**
     * Construtor padrão sem argumentos.
     */
    public Produto() {
    }

    /**
     * Construtor com todos os atributos do produto.
     *
     * @param id         Identificador do produto
     * @param nome       Nome do produto
     * @param preco      Preço do produto
     * @param unidade    Unidade de medida do produto
     * @param qtdEstoque Quantidade atual em estoque
     * @param qtdMinima  Quantidade mínima permitida em estoque
     * @param qtdMaxima  Quantidade máxima permitida em estoque
     * @param categoria  Categoria à qual o produto pertence
     */
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

    /**
     * Retorna o identificador do produto.
     *
     * @return id do produto
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o nome do produto.
     *
     * @return nome do produto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o preço do produto.
     *
     * @return preço do produto
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Retorna a unidade de medida do produto.
     *
     * @return unidade do produto
     */
    public String getUnidade() {
        return unidade;
    }

    /**
     * Retorna a quantidade atual em estoque do produto.
     *
     * @return quantidade em estoque
     */
    public double getQtdEstoque() {
        return qtdEstoque;
    }

    /**
     * Retorna a quantidade mínima permitida em estoque do produto.
     *
     * @return quantidade mínima
     */
    public double getQtdMinima() {
        return qtdMinima;
    }

    /**
     * Retorna a quantidade máxima permitida em estoque do produto.
     *
     * @return quantidade máxima
     */
    public double getQtdMaxima() {
        return qtdMaxima;
    }

    /**
     * Retorna a categoria à qual o produto pertence.
     *
     * @return categoria do produto
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Define o identificador do produto.
     *
     * @param id Identificador a ser definido
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Define o nome do produto.
     *
     * @param nome Nome a ser definido
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Define o preço do produto.
     *
     * @param preco Preço a ser definido
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }

    /**
     * Define a unidade de medida do produto.
     *
     * @param unidade Unidade a ser definida
     */
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    /**
     * Define a quantidade atual em estoque do produto.
     *
     * @param v Quantidade em estoque a ser definida
     */
    public void setQtdEstoque(double v) {
        this.qtdEstoque = v;
    }

    /**
     * Define a quantidade mínima permitida em estoque do produto.
     *
     * @param v Quantidade mínima a ser definida
     */
    public void setQtdMinima(double v) {
        this.qtdMinima = v;
    }

    /**
     * Define a quantidade máxima permitida em estoque do produto.
     *
     * @param v Quantidade máxima a ser definida
     */
    public void setQtdMaxima(double v) {
        this.qtdMaxima = v;
    }

    /**
     * Define a categoria à qual o produto pertence.
     *
     * @param c Categoria a ser definida
     */
    public void setCategoria(Categoria c) {
        this.categoria = c;
    }

    /**
     * Retorna o nome do produto como representação textual do objeto.
     *
     * @return nome do produto
     */
    @Override
    public String toString() {
        return nome;
    }
}