package gosm.ui;

import gosm.backend.Game;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GameImage extends Pane {
	
	private Canvas canvas = null;
	private Game game;
	
	public GameImage() {
		super();
		canvas = new Canvas(1,1);
		getChildren().add(canvas);
	}

	public void setGame(Game selected) {
		this.game = selected;
		this.setWidth(getWidthInPx());
		this.setHeight(getHeightInPx());
		canvas.setWidth(getWidth());
		canvas.setHeight(getHeight());
		drawGame();
	}
	
	public Game getGame() {
		return game;
	}
	
	public int getWidthInPx() {
		return game.getWidth() * Bitmapper.BMP_WIDTH + 2;
	}
	
	public int getHeightInPx() {
		return game.getHeight() * Bitmapper.BMP_WIDTH + 2;
	}
	
	public void drawGame() {
		GraphicsContext gcg = canvas.getGraphicsContext2D();
		gcg.fillRect(0, 0, getWidth(), getHeight());
		for (int col=0;col < game.getWidth();col++) {
			for (int row=0;row < game.getHeight();row++) {
				gcg.drawImage(UIConstants.bitmaps.getImage(game.getItem(col, row)), 1 + col * Bitmapper.BMP_WIDTH, 1 + row * Bitmapper.BMP_WIDTH);
			}
		}
	}
	
	public void redraw() {
		drawGame();
	}
}
