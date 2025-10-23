package com.codeup.minitiendariwi.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane; 


public class DatabaseConnection {

    
    private static final String URL = "jdbc:mysql://localhost:3306/mini_tienda_riwi";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 
    
    // El driver se carga automáticamente en versiones modernas, pero se puede incluir
    // static {
    //     try {
    //         Class.forName("com.mysql.cj.jdbc.Driver");
    //     } catch (ClassNotFoundException e) {
    //         e.printStackTrace();
    //         JOptionPane.showMessageDialog(null, "Error al cargar el driver de MySQL.", "Error de BD", JOptionPane.ERROR_MESSAGE);
    //     }
    // }


    public static Connection getConnection() {
        try {
            // El método getConnection es el que establece la conexión
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al conectar con la BD: " + e.getMessage() + "\n" +
                "Asegúrate de que MySQL esté corriendo y los datos de conexión sean correctos.", 
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

  
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, 
                    "Error al cerrar la conexión: " + e.getMessage(), 
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}