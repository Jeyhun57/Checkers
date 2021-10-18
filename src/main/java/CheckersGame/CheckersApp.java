package CheckersGame;

import CheckersGame.checker.CheckerColor;
import CheckersGame.view.impl.BoardViewImpl;
import CheckersGame.view.impl.SelectionViewImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

public class CheckersApp extends Application implements GameCompletedListener {

    public static int TILES = 8;
    private Pane rootPane;
    static final double boardSize = 800;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        rootPane = new Pane();
        rootPane.setBackground(new Background(new BackgroundImage(
                new Image("chessboard.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, true, true)
        )));

        BoardViewImpl fieldView = new BoardViewImpl(boardSize);
        Board board = new Board(fieldView, new SelectionViewImpl(fieldView, boardSize / TILES, boardSize / TILES));
        board.setOnGameFinishedListener(this);
        rootPane.getChildren().add(fieldView);

        Scene scene = new Scene(rootPane, boardSize, boardSize);
        scene.setOnKeyPressed((KeyEvent key) -> {
            if (key.getCode() == KeyCode.ENTER) {
                restart();
            }
        });
        primaryStage.setTitle("Russian checkers");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @Override
    public void onGameFinished(CheckerColor winner) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Игра окончена!");
        alert.setContentText((winner == CheckerColor.BLACK ? "Черные" : "Белые") + " победили! Сыграть еще раз?");

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            restart();
        } else {
            Platform.exit();
        }
    }

    private void restart() {
        rootPane.getChildren().clear();

        BoardViewImpl fieldView = new BoardViewImpl(boardSize);
        Board board = new Board(fieldView, new SelectionViewImpl(fieldView, boardSize / TILES, boardSize / TILES));
        board.setOnGameFinishedListener(this);
        rootPane.getChildren().add(fieldView);
    }
}
