import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import org.sqlite.SQLiteException;

import Conexion.Connect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorRegistro {
    PreparedStatement ps;
    ResultSet rs;
    @FXML
    private Button botonRegistrar;

    @FXML
    private Button botonLimpiar;

    @FXML
    private PasswordField cajaContrasena;

    @FXML
    private TextField cajaUsuario;

    @FXML
    private PasswordField cajaConfirmarContrasena;

    @FXML
    void limpiar(ActionEvent event) {
        cajaContrasena.setText("");
        cajaConfirmarContrasena.setText("");
        cajaUsuario.setText("");
    }

    private void registro() {
        Connection conexion = null;
        Connect onConnect = new Connect();
        try {
            conexion = onConnect.connect(); // establecemos la conexion
            ps = conexion.prepareStatement(
                    "INSERT INTO usuarios (usuario,contrasena) VALUES (?,?)");
            if (cajaUsuario.getLength() > 20) {
                Alert alertEmpty = new Alert(Alert.AlertType.ERROR, "El usuario debe tener menos de 20 caracteres");
                alertEmpty.setHeaderText(null);
                alertEmpty.setTitle("Error");
                alertEmpty.showAndWait();
            } else {
                ps.setString(1, cajaUsuario.getText().trim());
            }
            ps.setString(2, cajaContrasena.getText());

            int resultado = ps.executeUpdate(); // ejecutamos la insercion de la base de datos
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Registro insertado correctamente");
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/Vista/registroEstudiantes.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Registro Estudiantes");
                stage.show();
                Stage old = (Stage) botonRegistrar.getScene().getWindow();
                old.close();
            } else {
                JOptionPane.showMessageDialog(null, "Error al insertar el Registro");
            }
            conexion.close();
        } catch (Exception ex) {
            System.err.println("Error: " + ex);
            if (ex instanceof SQLiteException && cajaUsuario.getLength() <= 20) {
                Alert alertEmpty = new Alert(Alert.AlertType.ERROR, "Este usuario ya existe");
                alertEmpty.setHeaderText(null);
                alertEmpty.setTitle("Error");
                alertEmpty.showAndWait();
            }
        }
    }

    @FXML
    void registrar(ActionEvent event) throws IOException {
        boolean registroValido = false;
        if (cajaContrasena.getText().isEmpty() | cajaContrasena.getText().isEmpty() | cajaUsuario.getText().isEmpty()) {
            Alert alertEmpty = new Alert(Alert.AlertType.ERROR, "No deje campos vacios");
            alertEmpty.setHeaderText(null);
            alertEmpty.setTitle("Error");
            alertEmpty.showAndWait();
        } else {
            if (cajaContrasena.getText().equals(cajaConfirmarContrasena.getText())) {
                registroValido = true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Las contraseñas no coinciden");
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.showAndWait();
            }
        }
        if (registroValido == true) {
            registro();
        }

    }
}
