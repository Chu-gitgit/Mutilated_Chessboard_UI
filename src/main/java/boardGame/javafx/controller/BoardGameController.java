package boardGame.javafx.controller;

import boardGame.results.GameResult;
import boardGame.results.GameResultDao;
import boardGame.state.Game;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.tinylog.Logger;
import util.javafx.ControllerHelper;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Slf4j
public class BoardGameController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gameResultDao;

    @FXML
    public Label stepsLabel;

    @FXML
    private GridPane board;

    @FXML
    public Button resetButton;

    @FXML
    public Button giveupFinishButton;

    private Instant startTime;
    private String playerA;
    private String playerB;

    public static IntegerProperty step = new SimpleIntegerProperty();

    public void setPlayerA(String playerA) {
        this.playerA = playerA;
    }

    public void setPlayerB(String playerB) {
        this.playerB = playerB;
    }


    /**
     * initialize the board,create every square in the each cell
     * initial step as 0, and get the current time
     */
    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                board.add(square, j, i);
            }
        }
        Game.steps = 0;
        step.set(Game.steps);
        stepsLabel.textProperty().bind(step.asString());
        startTime = Instant.now();
        Game.resetStep();
    }


    /**
     *  in each square,the color of coin set Transparent on default,
     *  when click the mouse, the square created.
     * @return
     */
    private StackPane createSquare() {
        var square = new StackPane();
        var coin = new Circle(20);
        coin.setFill(Color.TRANSPARENT);
        square.getChildren().add(coin);
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);  //handle mouse click
        return square;
    }


    /**
     *  even step , set coin of color on BlUE(first player), add one coin next to it(right)
     *  odd step, set coin of color on RED (second player), add one coin next to it(down)
     * @param col
     * @param row
     * @param step
     */
    private void nextSquare(int col, int row, int step) {
        var square = new StackPane();
        var coin = new Circle(20);
        if (step % 2 == 0) {
            coin.setFill(Color.RED);
        } else {
            coin.setFill(Color.BLUE);
        }
        square.getChildren().add(coin);
        if (step % 2 == 0) {
            board.add(square, col, row + 1);
        } else {
            board.add(square, col + 1, row);
        }

    }


    /**
     * when click the mouse, the console showing the position of each square.
     * odd number of step, the coin set color with Blue, else set color in Red
     * when there is space for next player, showing continue.
     * else
     *       showing the current player is win!
     *
     *
     * @param event
     * @throws RuntimeException
     */
    @FXML
    private void handleMouseClick(MouseEvent event) throws RuntimeException {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.info("Mouse click on square ({},{})", row, col);
        Circle coin = (Circle) square.getChildren().get(0);
        coin.getFill();
        if (Game.clickable(col, row, Game.steps % 2 == 0 ? 1 : 2)) {
            Game.addStep(col, row, Game.steps % 2 == 0 ? 1 : 2);
            Logger.info("Steps: {}", Game.steps);
            if (Game.steps % 2 == 0) {
                coin.setFill(Color.RED);
            } else {
                coin.setFill(Color.BLUE);
            }
            nextSquare(col, row, Game.steps);
            if (Game.isWin(Game.steps)) {
                Logger.info("Continue.");
            } else {
                Logger.info("Player {} is win!", Game.steps % 2 == 1 ? "A" : "B");
            }
            step.set(Game.steps);
            try {
                stepsLabel.textProperty().set(step.toString());
            } catch (RuntimeException ignored) {
            }
        }
    }


    /**
     * when click on reset button, the information showing on console
     * the new scene created, the table will be empty
     * @param actionEvent
     * @throws IOException
     */
    public void handleResetButton(ActionEvent actionEvent) throws IOException {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Logger.info("Resetting game");
        step.set(0);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ui.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    /**
     * click on the finishButton, the records  saved
     * @param actionEvent
     * @throws IOException
     */
    public void handleGiveUpFinishButton(ActionEvent actionEvent) throws IOException {
        var buttonText = ((Button) actionEvent.getSource()).getText();
        Logger.debug("{} is pressed", buttonText);
        Logger.debug("Saving result");
        gameResultDao.persist(createGameResult());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        ControllerHelper.loadAndShowFXML(fxmlLoader, "/fxml/highscores.fxml", stage);
    }



    private GameResult createGameResult() {
        return GameResult.builder()
                .player(Game.steps % 2 == 1 ? playerA : playerB)
                .solved(!Game.isWin(Game.steps))
                .duration(Duration.between(startTime, Instant.now()))
                .steps(Game.steps)
                .build();
    }

}
