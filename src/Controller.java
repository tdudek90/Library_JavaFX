import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    TextField loginText;
    @FXML
    PasswordField passwordText;
    @FXML
    TextField name;
    @FXML
    TextField lastName;
    @FXML
    PasswordField passwordRegText;
    @FXML
    PasswordField rePasswordText;
    @FXML
    TextField number;


    public void signIn() {
        if (isFormLoginValid()) {
            return;
        }

        System.out.println(loginText.getText() + " " + passwordText.getText());
        Statement statement = ServerConnection.getInstance().getNewStatement();
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user WHERE name ='" + loginText.getText() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            int counter = 0;
            while (resultSet.next()) {
                String passwordFromDataBase = resultSet.getString("password");
                if (passwordFromDataBase.equals(passwordText.getText())) {
                    Utils.openAlert("Sign in", "Login successful");
                } else {
                    Utils.openAlert("Error", "Wrong password");
                }
                counter++;
            }
            if (counter == 0) {
                Utils.openAlert("Error", "User don't exist");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    private boolean isFormLoginValid() {
        if (loginText.getText().trim().length() > 4 || passwordText.getText().trim().length() > 4) {
            return true;
        }
        Utils.openAlert("Sign in", "Your login and password must be at least 4 characters long");
        return false;
    }

    public void register()  {
        try {
            if (isLoginExist()){
                Utils.openAlert("Error", "Login already exist");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ServerConnection serverConnection = ServerConnection.getInstance();
        String sql = "INSERT INTO user (name, lastName, number, password) VALUES (?,?,?,?)";
        PreparedStatement statement = serverConnection.getNewPrepareStatement(sql);
        try {
            statement.setString(1, name.getText());
            statement.setString(2, lastName.getText());
            statement.setString(3, passwordRegText.getText());
            statement.setString(4, rePasswordText.getText());
            statement.setString(4, number.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User has been registered!");
    }

    private boolean isLoginExist() throws SQLException {
        ServerConnection serverConnection = ServerConnection.getInstance();
        String imie = name.getText();
        String sql = "SELECT * FROM user WHERE name = ?";
        PreparedStatement statement = serverConnection.getNewPrepareStatement(sql);
        statement.setString(1, imie);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            System.out.println("User exist");
            return true;
        } else {
            System.out.println("User does not exist");
            return false;
        }
    }

}
