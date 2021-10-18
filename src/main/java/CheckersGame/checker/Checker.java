package CheckersGame.checker;

public class Checker {
    public int x, y;
    public final CheckerColor color;
    private boolean isKing = false;

    private final int kingAchievementY;

    public Checker(CheckerColor color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
        kingAchievementY = color == CheckerColor.WHITE ? 0 : 7;
    }

    public boolean hasColor(CheckerColor color) {
        return this.color == color;
    }

    public boolean isKing() { return isKing; }

    public void becomeKing() {
        if (y == kingAchievementY) {
            isKing = true;
        }
    }

}
