package org.example.projet4dx.util.DAO;

import org.example.projet4dx.util.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
    public static Connection connect() {
        String url = Config.get("db.url");
        String driver = Config.get("db.driver");

        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);
            System.out.println("Connexion établie avec succès !");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
