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
    //CONSTRUTOR

    public Categoria() {
    }

    public Categoria(int id, String nome, Tamanho tamanho, Embalagem embalagem) {
        this.id = id;
        this.nome = nome;
        this.tamanho = tamanho;
        this.embalagem = embalagem;
    }
//GET
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Tamanho getTamanho() {
        return tamanho;
    }

    public Embalagem getEmbalagem() {
        return embalagem;
    }
//SET
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTamanho(Tamanho t) {
        this.tamanho = t;
    }

    public void setEmbalagem(Embalagem e) {
        this.embalagem = e;
    }
//METODO DE SOBRESCRITA
    @Override
    public String toString() {
        return nome;
    }
}