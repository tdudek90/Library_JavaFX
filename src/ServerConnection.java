import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;


public class ServerConnection {


    private static ServerConnection ourInstance = new ServerConnection();

    public static ServerConnection getInstance() {
        return ourInstance;
    }

    private Connection connection;

    private ServerConnection() {
        try {
            File file = new File("C:\\Users\\Tomek\\Desktop\\JavaFX\\log.txt");
            Scanner scanner = new Scanner(file);

            final String DB = scanner.nextLine();
            final String USER = scanner.nextLine();
            final String USERPW = scanner.nextLine();
            final String DRIVER = scanner.nextLine();


            Class.forName(DRIVER).newInstance();
            connection = DriverManager.getConnection(DB, USER, USERPW);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connected");
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getNewStatement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement getNewPrepareStatement(String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
