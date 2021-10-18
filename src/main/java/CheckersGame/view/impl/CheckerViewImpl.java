package CheckersGame.view.impl;

import CheckersGame.checker.CheckerColor;
import CheckersGame.view.CheckerView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CheckerViewImpl extends ImageView implements CheckerView {

    private final CheckerColor checkerColor;

    public CheckerViewImpl(CheckerColor checkerColor, double size) {
        super(new Image(checkerColor == CheckerColor.WHITE ? "white_checker.png" : "black_checker.png"));
        this.checkerColor = checkerColor;
        this.setFitHeight(size);
        this.setFitWidth(size);

    }

    @Override
    public void showAsKing() {
        this.setImage(new Image(checkerColor == CheckerColor.WHITE ? "white_king.png" : "black_king.png"));
    }
}
