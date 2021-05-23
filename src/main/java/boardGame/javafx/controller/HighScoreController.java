package boardGame.javafx.controller;

import boardGame.results.GameResult;
import boardGame.results.GameResultDao;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.tinylog.Logger;
import util.guice.PersistenceModule;
import util.javafx.ControllerHelper;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Slf4j
public class HighScoreController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gameResultDao;

    @FXML
    private TableView<GameResult> highScoreTable;

    @FXML
    private TableColumn<GameResult, String> player;

    @FXML
    private TableColumn<GameResult, Integer> steps;

    @FXML
    private TableColumn<GameResult, ZonedDateTime> created;


    /**
     *  the scene of high score recording
     *  get top 5 result
     *
     */
    @FXML
    private void initialize() {
        Logger.debug("Loading high scores...");
        List<GameResult> highScoreList = gameResultDao.findBest(5);

        player.setCellValueFactory(new PropertyValueFactory<>("player"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
        created.setCellValueFactory(new PropertyValueFactory<>("created"));
        Injector injector = Guice.createInjector(new PersistenceModule("boardGame"));
        gameResultDao = injector.getInstance(GameResultDao.class);

        created.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter;

            {
                formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
            }

            @Override
            protected void updateItem(ZonedDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        ObservableList<GameResult> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);

        highScoreTable.setItems(observableResult);
    }


    /**
     * if click on Start game button
     * the new scene of launch page created
     *
     * @param actionEvent
     * @throws IOException
     */
    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        ControllerHelper.loadAndShowFXML(fxmlLoader, "/fxml/launch.fxml", stage);
    }

}
