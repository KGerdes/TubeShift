package gosm.ui;

import java.util.Date;
import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GameGrid extends Pane {

	private Canvas canvas;
	
	public GameGrid() {
		setStyle(UIConstants.STD_TEST_COLOR);
		/*
		Canvas canvas = new Canvas(1200, 500);
		GraphicsContext gcg = canvas.getGraphicsContext2D();
		gcg.fillRect(0, 0, 1200, 500);
		for (int i=0;i<16;i++) {
			gcg.drawImage(UIConstants.bitmaps.getImage(i), i * 74,100);
		}
		
		getChildren().add(canvas);
		
		setOnMouseClicked(e -> {
			GraphicsContext gc = canvas.getGraphicsContext2D();
			System.out.println("clicked");
			Random r = new Random(new Date().getTime());
			for (int i=0;i<16;i++) {
				gc.drawImage(UIConstants.bitmaps.getImage(r.nextInt(16)), i * 74,100);
			}
		});
		*/
	}
}
