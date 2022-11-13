package Client.JavaFXGUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TweetController implements Controller {

    @FXML
    private Label dateField;

    @FXML
    private Label hashtagField;

    @FXML
    private Label messageField;

    @FXML
    private Label usernameField;

    public TweetController() {
        System.out.println("TweetController instantiated.");
    }

    public void setFields(String username, String hashtag, String message, String date) {
        usernameField.setText(username);
        hashtagField.setText(hashtag);
        messageField.setText(message);
        dateField.setText(date);
    }
}
