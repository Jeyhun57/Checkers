package CheckersGame;

import CheckersGame.checker.CheckerColor;

public interface GameCompletedListener {
    void onGameFinished(CheckerColor winner);
}
