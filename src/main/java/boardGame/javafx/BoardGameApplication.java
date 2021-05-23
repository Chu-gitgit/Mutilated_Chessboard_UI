package boardGame.javafx;


import boardGame.results.GameResultDao;
import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.tinylog.Logger;
import util.guice.PersistenceModule;

import javax.inject.Inject;
import java.util.List;


/**
 *  connecting with jpa unit
 */
@Slf4j
public class BoardGameApplication extends Application {

    private final GuiceContext context;

    {
        context = new GuiceContext(this, () -> List.of(
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        install(new PersistenceModule("boardGame"));
                        bind(GameResultDao.class);
                    }
                }
        ));
    }

    @Inject
    private FXMLLoader fxmlLoader;


    /**
     * the scene generated
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Logger.info("Starting application");
        context.init();
        fxmlLoader.setLocation(getClass().getResource("/fxml/launch.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Board Game");
        stage.show();
    }
}
