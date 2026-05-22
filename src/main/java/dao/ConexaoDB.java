package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados MySQL.
 * Fornece métodos estáticos para obter e encerrar conexões.
 */
public class ConexaoDB {

    private static final String URL = "jdbc:mysql://localhost:3306/estoque"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=America/Sao_Paulo";
    private static final String USER = "root";
    private static final String PASSWORD = "Dw6o981MxY9h";

    /**
     * Retorna uma conexão ativa com o banco de dados.
     *
     * @return Objeto Connection com a conexão estabelecida
     * @throws RuntimeException se não for possível conectar ao banco
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar: " + e.getMessage(), e);
        }
    }

    /**
     * Encerra a conexão com o banco de dados caso ela esteja aberta.
     *
     * @param conn Conexão a ser encerrada
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}