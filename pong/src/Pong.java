import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Pong extends Application {

    private final int SCREEN_WIDTH = 1000;
    private final int SCREEN_HEIGHT = 750;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT, Color.BLACK);
        final Canvas canvas = new Canvas(SCREEN_WIDTH,SCREEN_HEIGHT);

        root.getChildren().add(canvas);

        stage.setTitle("Pong");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Game pong = new Game(stage);
    }
}