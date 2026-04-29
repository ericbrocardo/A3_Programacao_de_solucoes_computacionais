package modelo;

import java.time.LocalDate;

public class Movimentacao {
// (Opções Fixas)

    public enum Tipo {
        ENTRADA, SAIDA
    }
// ATRIBUTOS
    private int id;
    private Produto produto;
    private LocalDate data;
    private double quantidade;
    private Tipo tipo;
//CONSTRUTOR

    public Movimentacao() {
    }

    public Movimentacao(int id, Produto produto, LocalDate data,
            double quantidade, Tipo tipo) {
        this.id = id;
        this.produto = produto;
        this.data = data;
        this.quantidade = quantidade;
        this.tipo = tipo;
    }
//get e set

    public int getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public LocalDate getData() {
        return data;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProduto(Produto p) {
        this.produto = p;
    }

    public void setData(LocalDate d) {
        this.data = d;
    }

    public void setQuantidade(double q) {
        this.quantidade = q;
    }

    public void setTipo(Tipo t) {
        this.tipo = t;
    }
}
