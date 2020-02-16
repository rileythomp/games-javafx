import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game {
    private Pane gameRoot;
    private Scene gameScene;

    private final int SCREEN_WIDTH = 1000;
    private final int SCREEN_HEIGHT = 750;

    private final double BALL_RADIUS = 10;
    private final Paint BALL_COLOR = Color.WHITESMOKE;
    private final double PLAYER_SPEED = 10;

    private AnimationTimer gameTimer;
    private boolean gameStarted;
    private int score;

    private Text scoreText;

    private Circle ball;
    private double balldx;
    private double balldy;

    private Rectangle playerPaddle;

    private Rectangle opponentPaddle;
    private double opponentdy;

    Stage gameStage;

    // inits
    public Game(Stage stage) {
        gameStage = stage;
        gameStarted = false;
        balldx = -1;
        balldy = 1;
        opponentdy = 0; // 1
        score = 0;

        gameRoot = new Pane();

        scoreText = new Text();
        scoreText.setText(""+score);
        scoreText.setX(SCREEN_WIDTH/2);
        scoreText.setY(SCREEN_HEIGHT/2);
        scoreText.setFill(Color.BLACK);
        scoreText.setFont(Font.font("Verdana", FontWeight.BOLD, 25));

        // x,y is center
        ball = new Circle(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, BALL_RADIUS, BALL_COLOR);

        // x,y is top left
        playerPaddle = new Rectangle(50, SCREEN_HEIGHT/2 - 50, 10, 100);
        playerPaddle.setFill(Color.WHITESMOKE);
        opponentPaddle = new Rectangle(SCREEN_WIDTH - 50, SCREEN_HEIGHT/2 - 50, 10, 100);
        opponentPaddle.setFill(Color.WHITESMOKE);

        gameRoot.getChildren().add(scoreText);
        gameRoot.getChildren().add(ball);
        gameRoot.getChildren().add(playerPaddle);
        gameRoot.getChildren().add(opponentPaddle);

        gameScene = new Scene(gameRoot, SCREEN_WIDTH, SCREEN_HEIGHT, Color.BLACK);

        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode pressed = event.getCode();
                if (!gameStarted) {
                    StartGame();
                }

                if (pressed == KeyCode.UP) {
                    UpdatePlayerPaddle(-1 *PLAYER_SPEED);
                }
                else if (pressed == KeyCode.DOWN) {
                    UpdatePlayerPaddle(PLAYER_SPEED);
                }
                else if (pressed == KeyCode.Q) {
                    System.exit(0);
                }
            }
        });

        gameStage.setScene(gameScene);
    }

    private void StartGame() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                UpdateBallPos();
                UpdateOpponentPaddle();
            }
        };
        gameTimer.start();
        gameStarted = true;
        scoreText.setFill(Color.WHITESMOKE);
    }

    private void UpdateBallPos() {
        if (ball.getCenterX() - ball.getRadius() + balldx <= 0 || ball.getCenterX() + ball.getRadius() + balldy >= SCREEN_WIDTH) {
            gameTimer.stop();
            PromptNewGame();
        }

        if (balldx < 0 && ball.getCenterX() + balldx - ball.getRadius() <= playerPaddle.getX() + playerPaddle.getWidth()) {
            if (ball.getCenterY() >= playerPaddle.getY() && ball.getCenterY() <= playerPaddle.getY() + playerPaddle.getHeight()) {
                balldx = -1*balldx;
                if (balldx == 1) {
                    balldx = -1.5;
                }
                score += 1;
                scoreText.setText(""+score);
            }
        }
        else if (balldx > 0 && ball.getCenterX() + balldx + ball.getRadius() >= opponentPaddle.getX()) {
            if (ball.getCenterY() >= opponentPaddle.getY() && ball.getCenterY() <= opponentPaddle.getY() + opponentPaddle.getHeight()) {
                balldx = -1*balldx;
            }
        }

        if (balldy > 0 && balldy + ball.getCenterY() + ball.getRadius() >= SCREEN_HEIGHT) {
            balldy = -1*balldy;
        }
        else if (balldy < 0 && ball.getCenterY() - ball.getRadius() + balldy <= 0) {
            balldy = -1*balldy;
        }

        ball.setCenterX(ball.getCenterX() + balldx);
        ball.setCenterY(ball.getCenterY() + balldy);
    }

    private void UpdatePlayerPaddle(double dy) {
        if (playerPaddle.getY() + playerPaddle.getHeight() + dy >= SCREEN_HEIGHT || playerPaddle.getY() + dy <= 0) {
            return;
        }


        playerPaddle.setY(playerPaddle.getY() + dy);
    }

    private void UpdateOpponentPaddle() {
        if (opponentPaddle.getY() + opponentPaddle.getHeight() >= SCREEN_HEIGHT || opponentPaddle.getY() <= 0) {
            opponentdy = -1*opponentdy;
        }
        else if (opponentPaddle.getY() + opponentPaddle.getHeight()/2 >= ball.getCenterY()) {
            opponentdy = -1;
        }
        else if (opponentPaddle.getY() + opponentPaddle.getHeight()/2 <= ball.getCenterY()) {
            opponentdy = 1;
        }
        opponentPaddle.setY(opponentPaddle.getY() + opponentdy);
    }

    private void PromptNewGame() {
        Rectangle box = new Rectangle();
        box.setHeight(SCREEN_HEIGHT/2);
        box.setWidth(SCREEN_WIDTH/2);
        box.setX(SCREEN_WIDTH/2 - box.getWidth()/2);
        box.setY(SCREEN_HEIGHT/2 - box.getHeight()/2);
        box.setFill(Color.WHITESMOKE);

        Text msg = new Text();
        msg.setText(
            "Uh oh! You lost.\n\n" +
            "Final Score: " + score + "\n\n" +
            "Press p to play again or q to quit"
        );
        msg.setX(box.getX() + 10);
        msg.setY(box.getY() + 20);

        gameRoot.getChildren().add(box);
        gameRoot.getChildren().add(msg);

        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode pressed = event.getCode();
                if (pressed == KeyCode.P) {
                    Game pong = new Game(gameStage);
                }
                else if (pressed == KeyCode.Q) {
                    System.exit(0);
                }
            }
        });
    }
}
