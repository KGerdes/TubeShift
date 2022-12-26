package gosm.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Bitmapper {
	
	public static final int BMP_COUNT = 20;
	
	public static final int BMP_WIDTH = 70;
	public static final int TUBE_RADIUS = 12;
	
	private Image[] image = new Image[BMP_COUNT];
	
	public Bitmapper() {
		for (int i=0;i<image.length;i++) {
			image[i] = createImage(i);
		}
	}
	
	public Image getImage(int index) {
		return image[index];
	}
	

	private Image createImage(int index) {
		int halfmin = BMP_WIDTH / 2 - TUBE_RADIUS;
		WritableImage wi = new WritableImage(BMP_WIDTH,BMP_WIDTH);
		
		Canvas canvas = new Canvas(BMP_WIDTH, BMP_WIDTH);
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.gray(0.6));
		gc.setStroke(Color.gray(0.90));
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.strokeRect(0, 0, BMP_WIDTH, BMP_WIDTH);
		if (index < 16) {
			if ((index & 1) != 0) {
				gc.fillRect(halfmin, 0, TUBE_RADIUS * 2, BMP_WIDTH / 2);
			}
			if ((index & 2) != 0) {
				gc.fillRect(BMP_WIDTH / 2 - TUBE_RADIUS, halfmin, BMP_WIDTH, TUBE_RADIUS * 2);
			}
			if ((index & 4) != 0) {
				gc.fillRect(halfmin, BMP_WIDTH / 2 - TUBE_RADIUS, TUBE_RADIUS * 2, BMP_WIDTH);
			}
			if ((index & 8) != 0) {
				gc.fillRect(0, halfmin, BMP_WIDTH / 2 + TUBE_RADIUS, TUBE_RADIUS * 2);
			}
		} else {
			double rad = BMP_WIDTH / 2 + TUBE_RADIUS;
			double x = 0;
			double y = 0;
			
			index = index - 16;
			if ((index & 1) != 0) {
				x = BMP_WIDTH;
			}
			if ((index & 2) != 0) {
				y = BMP_WIDTH;
			}
			gc.fillOval(x - rad, y - rad, rad * 2, rad * 2);
			gc.setFill(Color.WHITE);
			rad = BMP_WIDTH / 2 - TUBE_RADIUS;
			gc.fillOval(x - rad, y - rad, rad * 2, rad * 2);
		}
		gc.strokeRect(0, 0, BMP_WIDTH, BMP_WIDTH);
		canvas.snapshot(null, wi);
		return wi;
	}
}
