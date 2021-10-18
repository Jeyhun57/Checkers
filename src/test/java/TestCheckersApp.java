import CheckersGame.Board;
import CheckersGame.OnTileSelectedListener;
import CheckersGame.checker.Checker;
import CheckersGame.checker.CheckerColor;
import CheckersGame.view.BoardView;
import CheckersGame.view.SelectionView;
import org.junit.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import static CheckersGame.CheckersApp.TILES;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.*;


public class TestCheckersApp extends Board {
    private static OnTileSelectedListener onTileSelectedListener;

    private void click(int X, int Y) {
        onTileSelectedListener.onTileSelected(X, Y);
    }

    public TestCheckersApp() {
        super(new BoardView() {
                  @Override
                  public void showChecker(Checker checker) {

                  }

                  @Override
                  public void showAsKing(Checker checker) {

                  }

                  @Override
                  public void moveChecker(Checker checker) {

                  }

                  @Override
                  public void removeChecker(Checker checker) {

                  }

                  @Override
                  public void setOnTileSelectedListener(OnTileSelectedListener listener) {
                      onTileSelectedListener = listener;
                  }
              },
                new SelectionView() {
                    @Override
                    public void add(int X, int Y) {

                    }

                    @Override
                    public void remove() {

                    }
                });
    }

    private int checkersCount() {
        int count = 0;
        for (Checker[] checkersRow : checkers)
            for (Checker checker : checkersRow) {
                if (checker != null)
                    count++;
            }
        return count;
    }

    @Test
    public void testInitialState() {
        assertEquals(currentPlayerSide, CheckerColor.WHITE);
        assertEquals(checkersCount(), getAllCheckers(CheckerColor.WHITE).size() + getAllCheckers(CheckerColor.BLACK).size());

        for (Checker whiteChecker : getAllCheckers(CheckerColor.WHITE)) {
            assertTrue(whiteChecker.y > 4);
            assertNotEquals(whiteChecker.x + whiteChecker.y, 0);
        }

        for (Checker blackChecker : getAllCheckers(CheckerColor.BLACK)) {
            assertTrue(blackChecker.y < 3);
            assertNotEquals(blackChecker.x + blackChecker.y, 0);
        }
    }

    @Test
    public void noKings() {
        for (Checker checker : getAllCheckers(CheckerColor.WHITE)) {
            assertFalse(checker.isKing());
        }

        for (Checker checker : getAllCheckers(CheckerColor.BLACK)) {
            assertFalse(checker.isKing());
        }
    }

    @Test
    @RepeatedTest(100)
    public void testSelection() {
        int clickedX = new Random().nextInt(TILES), clickedY = new Random().nextInt(TILES);
        assumeTrue(tileContainsChecker(clickedX, clickedY));
        assumeTrue(checkers[clickedY][clickedX].color == currentPlayerSide);

        click(clickedX, clickedY);
        assertEquals(selection.getTarget(), checkers[clickedY][clickedX]);
    }

    @Test
    @RepeatedTest(100)
    public void testMovement() {
        int clickedX = new Random().nextInt(TILES), clickedY = new Random().nextInt(TILES);
        assumeTrue(tileContainsChecker(clickedX, clickedY));
        assumeTrue(checkers[clickedY][clickedX].color == currentPlayerSide);

        Checker checker = checkers[clickedY][clickedX];
        click(clickedX, clickedY);

        assumeFalse(mustKill);

        int diffY = currentPlayerSide == CheckerColor.WHITE ? -1 : 1;
        int whereMoveX = clickedX - 1, whereMoveY = clickedY + diffY;
        assumeTrue(tileExists(whereMoveX, whereMoveY));
        assumeFalse(tileContainsChecker(whereMoveX, whereMoveY));


        click(whereMoveX, whereMoveY);

        assertNull(checkers[clickedY][clickedX]);
        assertNotNull(checkers[whereMoveY][whereMoveX]);
        assertEquals(checker, checkers[whereMoveY][whereMoveX]);

        assertEquals(whereMoveY, checker.y);
        assertEquals(whereMoveX, checker.x);

    }

    @Test
    public void testKill() {
        assertEquals(currentPlayerSide, CheckerColor.WHITE);

        click(6, 5);
        click(5, 4);

        assertEquals(currentPlayerSide, CheckerColor.BLACK);

        click(3, 2);
        click(4, 3);

        assertEquals(currentPlayerSide, CheckerColor.WHITE);

        click(5, 4);
        click(3, 2);

        assertNull(checkers[3][4]);
        assertNotEquals(getAllCheckers(CheckerColor.WHITE).size(), getAllCheckers(CheckerColor.BLACK).size());
    }

    @Test
    public void becomeKing() {
        int whites = getAllCheckers(CheckerColor.WHITE).size(), blacks = getAllCheckers(CheckerColor.BLACK).size();
        click(6, 5);
        click(5, 4); // 1w

        click(3, 2); // 2b
        click(4, 3);

        click(5, 4); // 3w
        click(3, 2);
        blacks--;

        assertNull(checkers[3][4]);
        assertEquals(blacks, getAllCheckers(CheckerColor.BLACK).size());

        click(2, 1); // 4b
        click(4, 3);
        whites--;

        assertNull(checkers[3][2]);
        assertEquals(whites, getAllCheckers(CheckerColor.WHITE).size());

        click(7, 6); // 5w
        click(6, 5);

        click(4, 3); // 6b
        click(5, 4);

        click(6, 5); // 7w
        click(4, 3);
        blacks--;

        assertNull(checkers[4][5]);
        assertEquals(blacks, getAllCheckers(CheckerColor.BLACK).size());

        click(5, 2); // 8b
        click(3, 4);
        whites--;

        assertNull(checkers[3][4]);
        assertEquals(whites, getAllCheckers(CheckerColor.WHITE).size());

        click(2, 5); // 9w
        click(4, 3);
        blacks--;

        assertNull(checkers[4][3]);
        assertEquals(blacks, getAllCheckers(CheckerColor.BLACK).size());

        click(1, 2); // 10b
        click(2, 3);

        click(4, 3); // 11w
        click(3, 2);

        click(1, 0); // 12b
        click(2, 1);

        click(3, 2); // 13w
        click(1, 0);
        blacks--;

        assertNull(checkers[1][2]);
        assertEquals(blacks, getAllCheckers(CheckerColor.BLACK).size());

        assertTrue(checkers[0][1].isKing());

    }
}
