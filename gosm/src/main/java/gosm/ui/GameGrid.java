package gosm.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import gosm.backend.Game;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameGrid extends Pane {

	private static final int IMAGE_OFFSET = 60;
	
	private IDistribute distribute;
	private Canvas canvas;
	private GameImage editable;
	private GameImage fixed;
	
	List<Rectangle> rectangles = new ArrayList<>();
	
	public GameGrid(IDistribute distribute) {
		this.distribute = distribute;
		setStyle(UIConstants.STD_PANE_COLOR);
		editable = new GameImage();
		fixed = new GameImage();
		canvas = new Canvas(1000,1000);
		getChildren().add(canvas);
		getChildren().add(editable);
		getChildren().add(fixed);
		activateGame(UIConstants.gameManager.getSelected(), false);
	}

	public void activateGame(Game selected, boolean mixit) {
		if (mixit) {
			editable.setGame(selected.dup().mixit());
		} else {
			editable.setGame(selected.dup());
		}
		fixed.setGame(selected);
		editable.setLayoutX(IMAGE_OFFSET);
		editable.setLayoutY(IMAGE_OFFSET);
		fixed.setLayoutX(IMAGE_OFFSET * 2 + editable.getWidth());
		fixed.setLayoutY(IMAGE_OFFSET);
		setWidth(fixed.getWidth() + fixed.getLayoutX() + IMAGE_OFFSET);
		setHeight(editable.getHeight() + IMAGE_OFFSET * 2);
		paintButtons();
		distribute.setGameState(mixit);
		if (fixed.getGame().isFinished(editable.getGame())) {
			distribute.setGameState(false);
		}
	}
	
	private void paintButtons() {
		for (Rectangle r : rectangles) {
			getChildren().remove(r);
		}
		rectangles.clear();
		GraphicsContext gcg = canvas.getGraphicsContext2D();
		gcg.setFill(Color.WHITE);
		gcg.setStroke(Color.gray(0.7));
		gcg.fillRect(0, 0, getWidth(), getHeight());
		for (int row=0;row < editable.getGame().getHeight();row++) {
			Rectangle r = new Rectangle(editable.getLayoutX() - 30, 
							editable.getLayoutY() + row * Bitmapper.BMP_WIDTH + 4, 
							20, Bitmapper.BMP_WIDTH - 8);
			initRectangle(r, row, null, false);
			r = new Rectangle(editable.getLayoutX() + editable.getWidth() + 10, 
					editable.getLayoutY() + row * Bitmapper.BMP_WIDTH + 4, 
					20, Bitmapper.BMP_WIDTH - 8);
			initRectangle(r, row, null, true);
		}
		for (int col=0;col < editable.getGame().getWidth();col++) {
			Rectangle r = new Rectangle(editable.getLayoutX() + col * Bitmapper.BMP_WIDTH + 4, 
							editable.getLayoutY() - 30, 
							Bitmapper.BMP_WIDTH - 8, 20);
			initRectangle(r, null, col, false);
			r = new Rectangle(editable.getLayoutX() + col * Bitmapper.BMP_WIDTH + 4, 
					editable.getLayoutY() + editable.getHeight() + 10, 
					Bitmapper.BMP_WIDTH - 8, 20);
			initRectangle(r, null, col, true);
		}
	}
	
	private void initRectangle(Rectangle r, Integer row, Integer col, boolean up) {
		r.setFill(Color.WHITE);
		r.setStroke(Color.gray(0.5));
		getChildren().add(r);
		r.setOnMouseClicked(e -> {
			if (distribute.isRunning()) {
				if (row != null) {
					editable.getGame().shiftRow(row, up);
					distribute.gridChanged();
				} else {
					editable.getGame().shiftColumn(col, up);
					distribute.gridChanged();
				}
				editable.redraw();
				if (fixed.getGame().isFinished(editable.getGame())) {
					distribute.setGameState(false);
				}
			}
		});
		rectangles.add(r);
	}
	
	public Game getSelectedGame() {
		return fixed.getGame();
	}
	
	public double getCalculatedHeight() {
		return editable.getLayoutY() * 2 + editable.getHeight();
	}
}
