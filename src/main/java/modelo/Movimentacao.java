package modelo;

import java.time.LocalDate;

/**
 * Representa uma movimentação de estoque no sistema de controle de estoque.
 * Uma movimentação pode ser uma entrada ou saída de produto.
 */
public class Movimentacao {

    /**
     * Opções de tipo disponíveis para uma movimentação.
     */
    public enum Tipo {
        ENTRADA, SAIDA
    }

    private int id;
    private Produto produto;
    private LocalDate data;
    private double quantidade;
    private Tipo tipo;

    /**
     * Construtor padrão sem argumentos.
     */
    public Movimentacao() {
    }

    /**
     * Construtor com todos os atributos da movimentação.
     *
     * @param id         Identificador da movimentação
     * @param produto    Produto relacionado à movimentação
     * @param data       Data em que a movimentação ocorreu
     * @param quantidade Quantidade movimentada
     * @param tipo       Tipo da movimentação (ENTRADA ou SAIDA)
     */
    public Movimentacao(int id, Produto produto, LocalDate data,
            double quantidade, Tipo tipo) {
        this.id = id;
        this.produto = produto;
        this.data = data;
        this.quantidade = quantidade;
        this.tipo = tipo;
    }

    /**
     * Retorna o identificador da movimentação.
     *
     * @return id da movimentação
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o produto relacionado à movimentação.
     *
     * @return produto da movimentação
     */
    public Produto getProduto() {
        return produto;
    }

    /**
     * Retorna a data em que a movimentação ocorreu.
     *
     * @return data da movimentação
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Retorna a quantidade movimentada.
     *
     * @return quantidade da movimentação
     */
    public double getQuantidade() {
        return quantidade;
    }

    /**
     * Retorna o tipo da movimentação.
     *
     * @return tipo da movimentação (ENTRADA ou SAIDA)
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * Define o identificador da movimentação.
     *
     * @param id Identificador a ser definido
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Define o produto relacionado à movimentação.
     *
     * @param p Produto a ser definido
     */
    public void setProduto(Produto p) {
        this.produto = p;
    }

    /**
     * Define a data em que a movimentação ocorreu.
     *
     * @param d Data a ser definida
     */
    public void setData(LocalDate d) {
        this.data = d;
    }

    /**
     * Define a quantidade movimentada.
     *
     * @param q Quantidade a ser definida
     */
    public void setQuantidade(double q) {
        this.quantidade = q;
    }

    /**
     * Define o tipo da movimentação.
     *
     * @param t Tipo a ser definido (ENTRADA ou SAIDA)
     */
    public void setTipo(Tipo t) {
        this.tipo = t;
    }
}