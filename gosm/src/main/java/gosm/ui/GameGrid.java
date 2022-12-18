package gosm.ui;

import java.util.Date;
import java.util.Random;

import gosm.backend.Game;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GameGrid extends Pane {

	private static final int IMAGE_OFFSET = 60;
	private Canvas canvas;
	private GameImage editable;
	private GameImage fixed;
	
	public GameGrid() {
		setStyle(UIConstants.STD_PANE_COLOR);
		editable = new GameImage();
		fixed = new GameImage();
		getChildren().add(editable);
		getChildren().add(fixed);
		activateGame(UIConstants.gameManager.getSelected());
	}

	private void activateGame(Game selected) {
		editable.setGame(selected.dup());
		fixed.setGame(selected);
		editable.setLayoutX(IMAGE_OFFSET);
		editable.setLayoutY(IMAGE_OFFSET);
		fixed.setLayoutX(IMAGE_OFFSET * 2 + editable.getWidth());
		fixed.setLayoutY(IMAGE_OFFSET);
		setWidth(fixed.getWidth() + fixed.getLayoutX() + IMAGE_OFFSET);
		setHeight(editable.getHeight() + IMAGE_OFFSET * 2);
	}
	
	public double getCalculatedHeight() {
		return editable.getLayoutY() * 2 + editable.getHeight();
	}
}
