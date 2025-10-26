package com.game.config;
import java.sql.Connection;
import java.sql.DriverManager;

public class TestMySQLConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/rpg",
                    "root",
                    "261508arnLAR!"
            );
            System.out.println("Подключение успешно!");
            conn.close();
        } catch (Exception e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
        }
    }
}