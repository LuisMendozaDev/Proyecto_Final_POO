import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

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

public class ControladorInicioSesion {
    PreparedStatement ps;
    ResultSet rs;

    private int numeroIntentos = 1;

    @FXML
    private Button botonRegistar;

    @FXML
    private Button botonIngresar;

    @FXML
    private Button botonLimpiar;

    @FXML
    private PasswordField cajaContrasena;

    @FXML
    private TextField cajaUsuario;

    @FXML
    void limpiar(ActionEvent event) {
        cajaContrasena.setText("");
        cajaUsuario.setText("");
    }

    private boolean inicioSesion() {
        Connection conexion = null;
        Connect onConnect = new Connect();
        boolean encontrado = false;
        try {
            conexion = onConnect.connect();
            ps = conexion.prepareStatement("SELECT * FROM usuarios WHERE usuario=? AND contrasena = ?");
            ps.setString(1, cajaUsuario.getText().trim());
            ps.setString(2, cajaContrasena.getText());
            rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Bienvenid@ ");
                encontrado = true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Usuario o contraseña incorrecto\nLe queda " + (3 - numeroIntentos)
                                + (((3 - numeroIntentos) > 1) ? " intentos" : " intento")
                                + " para ingresarlos correctamente");
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.showAndWait();
            }
            conexion.close();
        } catch (Exception ex) {
            System.err.println("Error: " + ex);
        }
        this.numeroIntentos++;
        return encontrado;
    }

    @FXML
    void ingresar(ActionEvent event) throws IOException {
        boolean inicioValido = false;
        if (cajaUsuario.getText().isEmpty() || cajaContrasena.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "No deje campos vacios");
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.showAndWait();
        } else {
            inicioValido = true;
        }
        if (inicioValido == true) {
            boolean iniciarSesion = inicioSesion();
            if (iniciarSesion == false && numeroIntentos > 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "El usuario que ha estado ingresando no existe \nSe redigirá a la ventana de registro");
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.showAndWait();
                // Para crear una ventana necesitas un nuevo Stage (Escenario)
                Stage stage = new Stage();
                // Cargas el FXML que queres que abra en un Parent
                Parent root = FXMLLoader.load(getClass().getResource("/Vista/registro.fxml"));
                // Se declara una Scene y se le asigna el FXML (Una Scene es la ventana)
                Scene scene = new Scene(root);
                // Establecemos la scena en el Stage
                stage.setScene(scene);
                // titulo para la ventana
                stage.setTitle("Registro");
                // El formulario se maximiza y se muestra
                stage.show();
                // Cerramos la ventana anterior de Login. La obtenemos a partir de un control
                // (Button)
                Stage old = (Stage) botonIngresar.getScene().getWindow();
                old.close();
            }
            if (iniciarSesion == true) {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/Vista/registroEstudiantes.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Registro Estudiantes");
                stage.show();
                Stage old = (Stage) botonIngresar.getScene().getWindow();
                old.close();
            }
        }

    }

    @FXML
    void registrar(ActionEvent e) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/Vista/registro.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Registro");
        stage.show();
        Stage old = (Stage) botonIngresar.getScene().getWindow();
        old.close();
    }
}
