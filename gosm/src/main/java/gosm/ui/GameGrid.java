package gosm.ui;

import java.util.ArrayList;
import java.util.List;


import gosm.backend.Game;
import gosm.backend.GameState;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameGrid extends Pane {

	private static final int RECT_WIDTH_OFF = 10;

	private static final int RECT_WIDTH = 24;

	private static final int IMAGE_OFFSET = 60;
	
	private IDistribute distribute;
	private Canvas canvas;
	private GameImage editable;
	private GameImage fixed;
	private Game pausedGame;
	private Label paused;
	
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
		paused = new Label("Pausiert");
		paused.setStyle("-fx-font-size: 48px;-fx-font-weight: bold;");
		paused.setAlignment(Pos.BASELINE_CENTER);
		paused.setVisible(false);
		paused.setTextFill(UIConstants.getBitmaps().getTubes());
		editable.getChildren().add(paused);
		activateGame(UIConstants.getGameManager().getSelected(), false);
		localize();
	}
	
	public void localize() {
		paused.setText(UIConstants.getLocalization().getByObject(this, "Paused"));	
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
		paused.setMinWidth(editable.getWidth());
		paused.setMinHeight(editable.getHeight());
		paused.setMaxWidth(editable.getWidth());
		paused.setMaxHeight(editable.getHeight());
		double width = Math.max(fixed.getWidth() + fixed.getLayoutX() + IMAGE_OFFSET, 1000.0);
		setWidth(width);
		setHeight(editable.getHeight() + IMAGE_OFFSET * 2 + 40);
		paintButtons();
		distribute.setGameState(mixit ? GameState.RUNNING : GameState.OFFLINE);
		if (fixed.getGame().isFinished(editable.getGame())) {
			distribute.setGameState(GameState.OFFLINE);
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
			Rectangle r = new Rectangle(editable.getLayoutX() - (RECT_WIDTH + RECT_WIDTH_OFF), 
							editable.getLayoutY() + row * Bitmapper.BMP_WIDTH + 4, 
							RECT_WIDTH, Bitmapper.BMP_WIDTH - 8.0);
			initRectangle(r, row, null, false);
			r = new Rectangle(editable.getLayoutX() + editable.getWidth() + RECT_WIDTH_OFF, 
					editable.getLayoutY() + row * Bitmapper.BMP_WIDTH + 4, 
					RECT_WIDTH, Bitmapper.BMP_WIDTH - 8.0);
			initRectangle(r, row, null, true);
		}
		for (int col=0;col < editable.getGame().getWidth();col++) {
			Rectangle r = new Rectangle(editable.getLayoutX() + col * Bitmapper.BMP_WIDTH + 4, 
							editable.getLayoutY() - (RECT_WIDTH + RECT_WIDTH_OFF), 
							Bitmapper.BMP_WIDTH - 8.0, RECT_WIDTH);
			initRectangle(r, null, col, false);
			r = new Rectangle(editable.getLayoutX() + col * Bitmapper.BMP_WIDTH + 4, 
					editable.getLayoutY() + editable.getHeight() + RECT_WIDTH_OFF, 
					Bitmapper.BMP_WIDTH - 8.0, RECT_WIDTH);
			initRectangle(r, null, col, true);
		}
	}
	
	private void initRectangle(final Rectangle r, Integer row, Integer col, boolean up) {
		r.setFill(UIConstants.getBitmaps().getBackground());
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
				if (isGameFinished()) {
					distribute.setGameState(GameState.OFFLINE);
				}
			}
		});
		rectangles.add(r);
		r.setOnMouseEntered(e -> {
			if (distribute.isRunning()) {
				r.setFill(UIConstants.getBitmaps().getTubes());
				r.setStroke(UIConstants.getBitmaps().getFrame());
				r.setScaleX(1.1);
				r.setScaleY(1.1);
			}
		});
		r.setOnMouseExited(e -> {
			r.setFill(UIConstants.getBitmaps().getBackground());
			r.setStroke(Color.gray(0.5));
			r.setScaleX(1.0);
			r.setScaleY(1.0);
		});
	}
	
	public Game getSelectedGame() {
		return fixed.getGame();
	}
	
	public double getCalculatedHeight() {
		return editable.getLayoutY() * 2 + editable.getHeight() + RECT_WIDTH_OFF;
	}

	public boolean isGameFinished() {
		return fixed.getGame().isFinished(editable.getGame());
	}

	public void pauseGame(boolean toPause) {
		paused.setVisible(toPause);
		if (toPause) {
			pausedGame = editable.getGame();
			Game tmp = Game.createBlankGame(pausedGame.getWidth(), pausedGame.getHeight());
			editable.setGame(tmp);
		} else {
			editable.setGame(pausedGame);
		}
	}

	public void redrawBitmaps(Bitmapper bitmaps) {
		editable.setBitmapper(bitmaps);
		fixed.setBitmapper(bitmaps);
		editable.redraw();
		fixed.redraw();
		for (Rectangle r : rectangles) {
			r.setStroke(UIConstants.getBitmaps().getFrame());
			r.setFill(UIConstants.getBitmaps().getBackground());
		}
		paused.setTextFill(UIConstants.getBitmaps().getTubes());
	}
}
