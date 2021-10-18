package CheckersGame.view;

import CheckersGame.OnTileSelectedListener;
import CheckersGame.checker.Checker;

public interface BoardView {

    void showChecker(Checker checker);

    void showAsKing(Checker checker);

    void moveChecker(Checker checker);

    void removeChecker(Checker checker);

    void setOnTileSelectedListener(OnTileSelectedListener listener);
}
