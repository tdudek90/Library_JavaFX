import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    TextField loginText;

    @FXML
    PasswordField passwordText;


    public void signIn() {

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
}
