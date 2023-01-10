package Conexion;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import java.sql.DriverManager;

public class Connect {

    public Connection connect() {
        String url = "jdbc:sqlite:C:/DataBase_POO/Database.db";
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC"); // stackoverflow 
            conn = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Alert alertSQL = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alertSQL.setHeaderText(null);
            alertSQL.setTitle("ERROR");
            alertSQL.show();
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return conn;
    }
}
