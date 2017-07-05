import javafx.scene.control.Alert;

/**
 * Created by Tomek on 2017-07-05.
 */
public class Utils {

    public static void openAlert(String title, String context){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        alert.showAndWait();
    }
}
