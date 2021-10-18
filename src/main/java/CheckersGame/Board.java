package CheckersGame;

import CheckersGame.checker.Checker;
import CheckersGame.checker.CheckerColor;
import CheckersGame.view.BoardView;
import CheckersGame.view.SelectionView;

import java.util.ArrayList;
import java.util.List;

import static CheckersGame.CheckersApp.TILES;

public class Board {
    protected GameCompletedListener gameCompletedListener;

    protected final Selection selection;
    protected final Checker[][] checkers = new Checker[TILES][TILES];
    protected CheckerColor currentPlayerSide = CheckerColor.WHITE;

    protected boolean mustKill = false;
    protected boolean isMultipleKill = false;

    protected final BoardView boardView;

    public Board(BoardView boardView, SelectionView selectionView) {
        this.boardView = boardView;
        selection = new Selection(selectionView);

        for (int i = 0; i < checkers.length; i++) {
            int j = (i % 2 == 0) ? 1 : 0;
            while (j < checkers.length) {
                if (i < 3) {
                    checkers[i][j] = new Checker(CheckerColor.BLACK, j, i);
                    boardView.showChecker(checkers[i][j]);
                } else if (i > 4) {
                    checkers[i][j] = new Checker(CheckerColor.WHITE, j, i);
                    boardView.showChecker(checkers[i][j]);
                }
                j += 2;
            }
            boardView.setOnTileSelectedListener((X, Y) -> {
                if (tileContainsChecker(X, Y) && !isMultipleKill) {
                    if (checkers[Y][X].hasColor(currentPlayerSide))
                        selection.setTargetAndSelect(checkers[Y][X]);
                } else moveType(X, Y);
            });
        }
    }


    protected boolean tileExists(int X, int Y) {
        return X >= 0 && X < TILES && Y >= 0 && Y < TILES;
    }

    protected boolean tileContainsChecker(int X, int Y) {
        return checkers[Y][X] != null;
    }

    protected void moveType(int X, int Y) {
        if ((X + Y) % 2 == 1 && selection.isSet()) {
            int diffX = Math.abs(X - selection.getTarget().x);
            int diffY = Math.abs(Y - selection.getTarget().y);

            int signedDiffY = Y - selection.getTarget().y;
            boolean moveBack = currentPlayerSide == CheckerColor.WHITE ? signedDiffY > 0 : signedDiffY < 0;

            if (diffX != diffY) return;
            int moveDist = diffX;

            if (checkersBetween(selection.getTarget().x, selection.getTarget().y, X, Y) == 0) {
                // just move
                if (!selection.getTarget().isKing() && (moveBack || moveDist != 1)) return;
                if (!mustKill) {
                    simpleMove(X, Y);
                }
            } else if (checkersBetween(selection.getTarget().x, selection.getTarget().y, X, Y) == 1 &&
                    checkerBetween(selection.getTarget().x, selection.getTarget().y, X, Y).color != selection.getTarget().color) {
                // kill
                killWithCheck(X, Y);
            }
        }
    }

    protected Checker checkerBetween(int startX, int startY, int endX, int endY) {
        int sideX = endX - startX > 0 ? 1 : -1;
        int sideY = endY - startY > 0 ? 1 : -1;

        for (int x = startX + sideX, y = startY + sideY; x != endX; x += sideX, y += sideY) {
            if (checkers[y][x] != null) return checkers[y][x];
        }

        return null;
    }

    protected int checkersBetween(int startX, int startY, int endX, int endY) {
        int sideX = endX - startX > 0 ? 1 : -1;
        int sideY = endY - startY > 0 ? 1 : -1;

        int checkersCount = 0;

        for (int x = startX, y = startY; x != endX; x += sideX, y += sideY) {
            if (checkers[y][x] != null) checkersCount++;
        }
        return checkersCount - 1;
    }


    protected void simpleMove(int X, int Y) {
        moveChecker(selection.getTarget(), X, Y);
        becomeKingWithCheck(selection.getTarget());
        changePlayer();
    }

    protected void moveChecker(Checker checker, int X, int Y) {
        checkers[checker.y][checker.x] = null;
        checkers[Y][X] = checker;
        checker.x = X;
        checker.y = Y;

        boardView.moveChecker(checker);

    }

    protected void removeCapturedChecker(Checker captured) {
        checkers[captured.y][captured.x] = null;
        boardView.removeChecker(captured);
    }

    protected void killWithCheck(int X, int Y /* куда ходит */) {
        Checker checker = selection.getTarget(); /* кто бьет */
        Checker capturedChecker = checkerBetween(selection.getTarget().x, selection.getTarget().y, X, Y); // кого бьет

        if (capturedChecker == null) return;
        if (!checker.isKing() && Math.abs(X - checker.x) != 2) return;

        if (!capturedChecker.hasColor(currentPlayerSide) && !tileContainsChecker(X, Y)) {
            moveChecker(checker, X, Y);
            becomeKingWithCheck(checker);
            removeCapturedChecker(capturedChecker);
            if (canKill(checker)) {
                selection.setTargetAndSelect(checker);
                isMultipleKill = true;
            } else {
                isMultipleKill = false;
                if (hasGameFinishedAlready()) {
                    if (gameCompletedListener != null)
                        gameCompletedListener.onGameFinished(getWinner());
                } else
                    changePlayer();
            }
        }
    }

    protected boolean canKill(Checker checker /* кто бьет */) {
        int rowShift = 1, colShift = 1;
        for (int i = 0, c = 1; i < 4; i++, c *= (-1)) {
            rowShift *= c;
            colShift *= -c;

            int X = checker.x, Y = checker.y;

            do {
                X += colShift; // кого бьем
                Y += rowShift; // кого бьем
            } while (tileExists(X, Y) && !tileContainsChecker(X, Y) && checker.isKing());


            if (tileExists(X, Y) && tileContainsChecker(X, Y)
                    && !checkers[Y][X].hasColor(currentPlayerSide)
                    && tileExists(X + colShift, Y + rowShift) && !tileContainsChecker(X + colShift, Y + rowShift)
            ) {
                return true;
            }
        }
        return false;
    }

    protected void changePlayer() {
        currentPlayerSide = currentPlayerSide == CheckerColor.WHITE ? CheckerColor.BLACK : CheckerColor.WHITE;
        selection.remove();
        selection.setTarget(null);
        mustKill = false;
        for (Checker checker : getAllCheckers(currentPlayerSide)) {
            if (canKill(checker)) {
                mustKill = true;
                break;
            }
        }
    }

    protected List<Checker> getAllCheckers(CheckerColor color) {
        List<Checker> checkersOfThatColor = new ArrayList<>();

        for (Checker[] checkersRow : checkers) {
            for (Checker checker : checkersRow) {
                if (checker != null && checker.color == color)
                    checkersOfThatColor.add(checker);
            }
        }
        return checkersOfThatColor;
    }

    protected boolean hasGameFinishedAlready() {
        return getAllCheckers(CheckerColor.WHITE).isEmpty() || getAllCheckers(CheckerColor.BLACK).isEmpty();
    }

    public void setOnGameFinishedListener(GameCompletedListener gameCompletedListener) {
        this.gameCompletedListener = gameCompletedListener;
    }

    protected void becomeKingWithCheck(Checker checker) {
        if (checker.y == (checker.color == CheckerColor.WHITE ? 0 : TILES - 1)) {
            checker.becomeKing();
            boardView.showAsKing(checker);
        }
    }

    protected CheckerColor getWinner() {
        for (Checker[] checkersRow : checkers) {
            for (Checker c : checkersRow) {
                if (c == null) continue;
                return c.color;
            }
        }

        return null;
    }
}
