package modelo;

/**
 * Representa uma categoria de produto no sistema de controle de estoque.
 * Cada categoria possui um tamanho e um tipo de embalagem fixos.
 */
public class Categoria {

    /**
     * Opções de tamanho disponíveis para uma categoria.
     */
    public enum Tamanho {
        PEQUENO, MEDIO, GRANDE
    }

    /**
     * Opções de embalagem disponíveis para uma categoria.
     */
    public enum Embalagem {
        LATA, VIDRO, PLASTICO
    }

    private int id;
    private String nome;
    private Tamanho tamanho;
    private Embalagem embalagem;

    /**
     * Construtor padrão sem argumentos.
     */
    public Categoria() {
    }

    /**
     * Construtor com todos os atributos da categoria.
     *
     * @param id       Identificador da categoria
     * @param nome     Nome da categoria
     * @param tamanho  Tamanho da categoria
     * @param embalagem Tipo de embalagem da categoria
     */
    public Categoria(int id, String nome, Tamanho tamanho, Embalagem embalagem) {
        this.id = id;
        this.nome = nome;
        this.tamanho = tamanho;
        this.embalagem = embalagem;
    }

    /**
     * Retorna o identificador da categoria.
     *
     * @return id da categoria
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o nome da categoria.
     *
     * @return nome da categoria
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o tamanho da categoria.
     *
     * @return tamanho da categoria
     */
    public Tamanho getTamanho() {
        return tamanho;
    }

    /**
     * Retorna o tipo de embalagem da categoria.
     *
     * @return embalagem da categoria
     */
    public Embalagem getEmbalagem() {
        return embalagem;
    }

    /**
     * Define o identificador da categoria.
     *
     * @param id Identificador a ser definido
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Define o nome da categoria.
     *
     * @param nome Nome a ser definido
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Define o tamanho da categoria.
     *
     * @param t Tamanho a ser definido
     */
    public void setTamanho(Tamanho t) {
        this.tamanho = t;
    }

    /**
     * Define o tipo de embalagem da categoria.
     *
     * @param e Embalagem a ser definida
     */
    public void setEmbalagem(Embalagem e) {
        this.embalagem = e;
    }

    /**
     * Retorna o nome da categoria como representação textual do objeto.
     *
     * @return nome da categoria
     */
    @Override
    public String toString() {
        return nome;
    }
}