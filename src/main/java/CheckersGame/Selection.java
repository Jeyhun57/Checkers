package CheckersGame;

import CheckersGame.checker.Checker;
import CheckersGame.view.SelectionView;

public class Selection {

    private Checker target = null;
    private final SelectionView view;

    public Selection(SelectionView view) {
        this.view = view;
    }

    public boolean isSet() {
        return target != null;
    }

    public void set() {
        view.set(target.x, target.y);
    }

    public void remove() {
        view.remove();
    }

    public void setTargetAndSelect(Checker target) {
        this.target = target;
        set();
    }

    public void setTarget(Checker target) {
        this.target = target;
    }

    public Checker getTarget() {
        return this.target;
    }
}
