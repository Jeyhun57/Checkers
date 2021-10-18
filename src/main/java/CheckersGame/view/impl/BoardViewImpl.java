package CheckersGame.view.impl;

import CheckersGame.OnTileSelectedListener;
import CheckersGame.checker.Checker;
import CheckersGame.view.BoardView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.HashMap;
import java.util.Map;

import static CheckersGame.CheckersApp.TILES;


public class BoardViewImpl extends GridPane implements BoardView {
    private final double fieldSize;

    private final Map<Checker, CheckerViewImpl> checkersMap = new HashMap<>();

    public BoardViewImpl(double fieldSize) {
        this.fieldSize = fieldSize;
        this.setPrefSize(fieldSize, fieldSize);

        for (int i = 0; i < TILES; i++) {
            this.getColumnConstraints().add(new ColumnConstraints(fieldSize / (double) TILES));
            this.getRowConstraints().add(new RowConstraints(fieldSize / (double) TILES));
        }
    }

    @Override
    public void showChecker(Checker checker) {
        this.add(getOrAddCheckerView(checker), checker.x, checker.y);
    }

    @Override
    public void showAsKing(Checker checker) {
        getOrAddCheckerView(checker).showAsKing();
    }

    @Override
    public void moveChecker(Checker checker) {
        this.getChildren().remove(getOrAddCheckerView(checker));
        this.add(getOrAddCheckerView(checker), checker.x, checker.y);
    }

    @Override
    public void removeChecker(Checker checker) {
        this.getChildren().remove(getOrAddCheckerView(checker));
    }

    @Override
    public void setOnTileSelectedListener(OnTileSelectedListener listener) {
        setOnMouseClicked((final MouseEvent click) -> {
            int row = (int) (click.getY() / (fieldSize / TILES));
            int col = (int) (click.getX() / (fieldSize / TILES));

            if (row >= 0 && row <= TILES - 1 && col >= 0 && col <= TILES - 1) {
                listener.onTileSelected(col, row);
            }
        });
    }

    private CheckerViewImpl getOrAddCheckerView(Checker checker) {
        if (checkersMap.containsKey(checker)) return checkersMap.get(checker);

        CheckerViewImpl checkerView = new CheckerViewImpl(checker.color, fieldSize / TILES);
        checkersMap.put(checker, checkerView);
        return checkerView;
    }
}
