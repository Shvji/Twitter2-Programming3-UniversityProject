package Client.JavaFXGUI.Controllers;

import Client.ClientClass.TwitterClientWrapper;
import Client.JavaFXGUI.Classes.PopUpWrapper;
import Client.JavaFXGUI.Classes.StageFacade;
import Shared.ErrorHandling.Exceptions.FetchException;
import Shared.Packet.Packet;
import Shared.Packet.RequestCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class UserFeedController extends FeedController {
    public UserFeedController() {
        System.out.println("User Feed Controller Initialized");
    }

    @Override
    @FXML
    // Follow Someone
    protected void btn1Click(ActionEvent event) {
        System.out.println("Follow Someone Button Clicked");
        PopUpWrapper.showStage("Follow", "Follow");
    }

    @FXML
    @Override
    // Submit Tweet
    protected void btn2Click(ActionEvent event) {
        System.out.println("Submit Tweet Button Clicked");
        PopUpWrapper.showStage("SubmitTweet", "Submit Tweet");
    }

    @FXML
    @Override
    // Log Out
    protected void btn3Click(ActionEvent event) throws IOException {
        System.out.println("Logout Button Clicked");

        sendSessionedPacket(RequestCode.USER_LOGOUT, null);
        String message;

        if (getAndDisconnect().isSuccessful) message = "Logout success!";
        else message = "Session is either invalid or expired, or another error happened on logout. You may close the application.";

        PopUpWrapper.showStage("Home", "Home");
        PopUpWrapper.showDialog("Dialog", "Log Out", message);

        StageFacade.closeStageFromBtn(this.btn3);
    }

    @Override
    @FXML
    protected void updateTweetsBtnClick(ActionEvent event) throws IOException, FetchException {
        System.out.println("Update Tweets Button Clicked.");

        sendSessionedPacket(RequestCode.FETCH_TWEETS, null);
        Packet fetchTweetsResult = getAndDisconnect();

        if (fetchTweetsResult.isSuccessful) {
            printTweets(fetchTweetsResult);
        }
        else {
            switch (fetchTweetsResult.errorCode) {
                case FETCH_TWEETS_ERROR -> throw new FetchException("An error occurred while fetching latest tweets!");
                default -> throw new FetchException("An unknown error occurred while fetching latest tweets.");
            }
        }
    }

    @Override
    public void init() {
        this.hashtagField.setVisible(false);
        this.btn1.setText("Follow Someone");
        this.btn2.setText("Submit Tweet");
        this.btn3.setText("Log Out");
        this.loggedAsLabel.setText(TwitterClientWrapper.getSession().username);
    }
}
