package weather;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.TimeUnit;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("weatherForm.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        Controller controller = loader.<Controller>getController();
        primaryStage.getScene();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        /**
         * Second thread to actualise time, background and other values.
         */
        Thread thread = new Thread() {
            @Override
            public void run() {
                int counter = 0;
                while (controller.isClose() == false) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        counter = counter + 1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //lambda
                    Platform.runLater(() -> controller.actualizeTime());
                    //method reference
                    Platform.runLater(controller::setBackgroundImage);

                    if (counter == 3600) {
                        controller.actualize();
                        counter = 0;
                    }
                }
            }
        };
        thread.start();
    }
}