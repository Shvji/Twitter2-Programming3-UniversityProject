package Client.JavaFXGUI.Controllers;

import Client.JavaFXGUI.Classes.StageFacade;
import Shared.ErrorHandling.Exceptions.FollowException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * A general controller for GUI elements that have a username as input.
 */
public class TargetUserOperationController extends ConnectedUIController {
    @FXML
    protected Button cancelBtn;

    @FXML
    protected Button submitBtn;

    @FXML
    protected Label title;

    @FXML
    protected TextField usernameField;

    /**
     * Executes the event related to cancelBtn. Closes the GUI.
     * @param event the event arisen by cancelBtn.
     */
    @FXML
    protected void cancelBtnClick(ActionEvent event) {
        System.out.println("Cancel Button Clicked.");
        StageFacade.closeStageFromBtn(cancelBtn);
    }

    /**
     * Executes the event related to submitBtn.
     * @param event the event arisen by submitBtn.
     * @throws IOException
     * @throws FollowException
     */
    @FXML
    protected void submitBtnClick(ActionEvent event) throws IOException, FollowException {
        System.out.println("Submit Clicked.");
    }

    /**
     * Initializes controller's fields.
     */
    public void init() {
        System.out.println("Init launched.");
    }

}
