import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    @FXML
    TableView<Book> book;
    @FXML
    TableColumn<Book, Integer> id;
    @FXML
    TableColumn<Book, String> title;
    @FXML
    TableColumn<Book, String> author;
    @FXML
    TableColumn<Book, Integer> pages;
    @FXML
    Button signInButton;


    public void signIn() {
        if (!isFormLoginValid()) {
            return;
        } else {
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
                        switchToUserMenu();
                    } else {
                        Utils.openAlert("Error", "Wrong password");
                    }
                    counter++;
                }
                if (counter == 0) {
                    Utils.openAlert("Error", "User does not exist");
                }
                loginText.clear();
                passwordText.clear();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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


    public void register() {
        try {
            if (isLoginExist()) {
                Utils.openAlert("Error", "Login already exist");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ServerConnection serverConnection = ServerConnection.getInstance();
        String sql = "INSERT INTO user (name, password, lastName, number) VALUES (?,?,?,?)";
        PreparedStatement statement = serverConnection.getNewPrepareStatement(sql);
        try {
            statement.setString(1, name.getText());
            statement.setString(3, lastName.getText());
            statement.setString(2, passwordRegText.getText());
            statement.setString(2, rePasswordText.getText());
            statement.setString(4, number.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.execute();
            name.clear();
            lastName.clear();
            passwordRegText.clear();
            rePasswordText.clear();
            number.clear();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User has been registered!");
    }

    private boolean isLoginExist() throws SQLException {
        ServerConnection serverConnection = ServerConnection.getInstance();
        String name = this.name.getText();
        String sql = "SELECT * FROM user WHERE name = ?";
        PreparedStatement statement = serverConnection.getNewPrepareStatement(sql);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            System.out.println("User exist");
            return true;
        } else {
            System.out.println("User does not exist");
            return false;
        }
    }

    public void showBooks() {
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        pages.setCellValueFactory(new PropertyValueFactory<>("pages"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        book.getItems().setAll(bookList());
    }

    private List<Book> bookList() {
        Statement statement = ServerConnection.getInstance().getNewStatement();
        List<Book> list = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int pages = resultSet.getInt("pages");
                int id = resultSet.getInt("id");

                list.add(new Book(title, author, pages, id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void switchToUserMenu() {
        Stage stage = (Stage) signInButton.getScene().getWindow();
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("userMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene userScene = new Scene(parent);
        stage.setTitle("User Menu");
        stage.setResizable(false);
        stage.setScene(userScene);
        stage.show();
    }


}
