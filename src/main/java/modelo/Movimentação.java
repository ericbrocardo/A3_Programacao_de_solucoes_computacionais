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
        this.tipo = tipo
    
;
