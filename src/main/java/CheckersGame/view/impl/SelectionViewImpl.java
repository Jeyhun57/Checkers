package CheckersGame.view.impl;

import CheckersGame.view.SelectionView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


public class SelectionViewImpl extends ImageView implements SelectionView {

    private final GridPane field;

    public SelectionViewImpl(GridPane field, double width, double height) {
        super(new Image("selection.png"));
        setFitHeight(height);
        setFitWidth(width);
        this.field = field;
    }

    @Override
    public void add(int X, int Y) {
        field.add(this, X, Y);
    }

    @Override
    public void remove() {
        field.getChildren().remove(this);
    }
}
