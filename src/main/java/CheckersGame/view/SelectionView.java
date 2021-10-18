package CheckersGame.view;

public interface SelectionView {

    default void set(int X, int Y) {
        remove();
        add(X, Y);
    }
    void add(int X, int Y);
    void remove();
}
