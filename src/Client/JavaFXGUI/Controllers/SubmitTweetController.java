package Client.JavaFXGUI.Controllers;

import Client.JavaFXGUI.Classes.PopUpWrapper;
import Client.JavaFXGUI.Classes.StageFacade;
import Shared.ErrorHandling.Exceptions.SubmitTweetException;
import Shared.Packet.Packet;
import Shared.Packet.RequestCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for SubmitTweet.fxml
 */
public class SubmitTweetController extends ConnectedUIController {

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField hashtagField;

    @FXML
    private TextArea messageField;

    @FXML
    private Button submitTweetBtn;

    /**
     * Handles the behavior of cancelBtn. It closes the stage.
     * @param event
     */
    @FXML
    void cancelBtnClick(ActionEvent event) {
        StageFacade.closeStageFromBtn(cancelBtn);
    }

    /**
     * Handles the behavior or submitTweetBtn. Retrieves the data from hashtagField and messageField and sends the tweet to the server.
     * @param event
     * @throws SubmitTweetException if the tweet data is somewhat invalid.
     * @throws IOException if the packet can't be sent.
     */
    @FXML
    void submitTweetBtnClick(ActionEvent event) throws SubmitTweetException, IOException {
        String hashtag = hashtagField.getText();
        String message = messageField.getText();

        if (message.length() > 140) throw new SubmitTweetException("Your tweet is too long! Max 140 chars!");

        List<String> packetData = new ArrayList<>();
        packetData.add(hashtag);
        packetData.add(message);

        sendSessionedPacket(RequestCode.SUBMIT_TWEET, packetData);
        Packet tweetResult = getAndDisconnect();

        if (tweetResult.isSuccessful) {
            System.out.println("Tweet submit success!");

            String successMessage = "Tweet submitted!";

            PopUpWrapper.showDialog2("Success", successMessage);

            StageFacade.closeStageFromBtn(submitTweetBtn);
        }
        else {
            throw new SubmitTweetException("An error occurred while submitting your tweet. Retry later.");
        }
    }
}
