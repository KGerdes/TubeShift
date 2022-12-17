package gosm.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Bitmapper {
	
	public static final int BMP_COUNT = 16;
	
	public static final int BMP_WIDTH = 70;
	public static final int TUBE_RADIUS = 12;
	
	private Image[] image = new Image[16];
	
	public Bitmapper() {
		for (int i=0;i<image.length;i++) {
			image[i] = createImage(i);
		}
	}
	
	public Image getImage(int index) {
		return image[index];
	}

	private Image createImage(int i) {
		int halfmin = BMP_WIDTH / 2 - TUBE_RADIUS;
		int halfmax = BMP_WIDTH / 2 + TUBE_RADIUS;
		WritableImage wi = new WritableImage(BMP_WIDTH,BMP_WIDTH);
		
		Canvas canvas = new Canvas(BMP_WIDTH, BMP_WIDTH);
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.gray(0.8));
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		if ((i & 1) != 0) {
			gc.fillRect(halfmin, 0, TUBE_RADIUS * 2, BMP_WIDTH / 2);
		}
		if ((i & 2) != 0) {
			gc.fillRect(BMP_WIDTH / 2 - TUBE_RADIUS, halfmin, BMP_WIDTH, TUBE_RADIUS * 2);
		}
		if ((i & 4) != 0) {
			gc.fillRect(halfmin, BMP_WIDTH / 2 - TUBE_RADIUS, TUBE_RADIUS * 2, BMP_WIDTH);
		}
		if ((i & 8) != 0) {
			gc.fillRect(0, halfmin, BMP_WIDTH / 2 + TUBE_RADIUS, TUBE_RADIUS * 2);
		}
		canvas.snapshot(null, wi);
		return wi;
	}
}
