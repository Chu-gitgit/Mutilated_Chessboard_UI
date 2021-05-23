package boardGame.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.tinylog.Logger;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class LaunchController {

    public Button startButton;
    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private TextField playerA;

    @FXML
    private TextField playerB;

    @FXML
    private Label errorLabel;


    /**
     * if one of player names is empty, showing warning message
     * else print the name on the console.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void startAction(ActionEvent actionEvent) throws IOException {
        if (playerA.getText().isEmpty() || playerB.getText().isEmpty()) {
            errorLabel.setText("* Username(s) is empty!");
        } else {
            fxmlLoader.setLocation(getClass().getResource("/fxml/ui.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<BoardGameController>getController().setPlayerA(playerA.getText());
            fxmlLoader.<BoardGameController>getController().setPlayerB(playerB.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            Logger.info("Player A is set to {}, loading game scene.", playerA.getText());
            Logger.info("Player B is set to {}, loading game scene.", playerB.getText());
        }
    }
}
