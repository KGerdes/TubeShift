package gosm.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Bitmapper {
	
	public static final int BMP_COUNT = 24;
	
	public static final int BMP_WIDTH = 70;
	public static final int TUBE_RADIUS = 12;
	
	
	private Color background;
	private Color tubes;
	private Color frame;
	private Color inverseFrame;
	private Image[] image = new Image[BMP_COUNT];
	
	public Bitmapper(Color background, Color tubes, Color frame) {
		this.background = reorg(background, false);
		this.tubes = reorg(tubes, false);
		this.frame = reorg(frame, false);
		this.inverseFrame = Color.WHITE;
		for (int i=0;i<image.length;i++) {
			image[i] = createImage(i);
		}
	}
	
	private Color reorg(Color old, boolean inverse) {
		if (!inverse) {
		return Color.rgb((int)(old.getRed() * 255.0),
						(int)(old.getGreen() * 255.0),
						(int)(old.getBlue() * 255.0));
		}
		return Color.rgb((int)(255.0 - old.getRed() * 255.0),
				(int)(255.0 - old.getGreen() * 255.0),
				(int)(255.0 - old.getBlue() * 255.0));
	}

	public Color getBackground() {
		return background;
	}

	public Color getTubes() {
		return tubes;
	}



	public Color getFrame() {
		return frame;
	}



	public Image getImage(int index) {
		return image[index];
	}
	

	private Image createImage(int index) {
		int indizes2[] = { 1,3,0,2};
		int halfmin = BMP_WIDTH / 2 - TUBE_RADIUS;
		WritableImage wi = new WritableImage(BMP_WIDTH,BMP_WIDTH);
		
		Canvas canvas = new Canvas(BMP_WIDTH, BMP_WIDTH);
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(background);
		gc.setStroke(frame);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setFill(tubes);
		gc.strokeRect(0, 0, BMP_WIDTH, BMP_WIDTH);
		if (index < 16) {
			if ((index & 1) != 0) {
				gc.fillRect(halfmin, 0, TUBE_RADIUS * 2, BMP_WIDTH / 2);
			}
			if ((index & 2) != 0) {
				gc.fillRect(BMP_WIDTH / 2, halfmin, BMP_WIDTH, TUBE_RADIUS * 2);
			}
			if ((index & 4) != 0) {
				gc.fillRect(halfmin, BMP_WIDTH / 2, TUBE_RADIUS * 2, BMP_WIDTH);
			}
			if ((index & 8) != 0) {
				gc.fillRect(0, halfmin, BMP_WIDTH / 2, TUBE_RADIUS * 2);
			}
			if (index > 0) {
				gc.fillOval(BMP_WIDTH / 2 - TUBE_RADIUS, BMP_WIDTH / 2 - TUBE_RADIUS, TUBE_RADIUS * 2, TUBE_RADIUS * 2);
			}
		} else {
			
			double rad = BMP_WIDTH / 2 + TUBE_RADIUS;
			double x = 0;
			double y = 0;
			double x2 = -1;
			double y2 = -1;
			
			index = index - 16;
			if ((index & 1) != 0) {
				x = BMP_WIDTH;
			}
			if ((index & 2) != 0) {
				y = BMP_WIDTH;
			}
			if (index >= 4) {
				int index2 = indizes2[index-4];
				x2 = 0;
				y2 = 0;
				if ((index2 & 1) != 0) {
					x2 = BMP_WIDTH;
				}
				if ((index2 & 2) != 0) {
					y2 = BMP_WIDTH;
				}
			}
			gc.fillOval(x - rad, y - rad, rad * 2, rad * 2);
			gc.setFill(background);
			rad = BMP_WIDTH / 2 - TUBE_RADIUS;
			gc.fillOval(x - rad, y - rad, rad * 2, rad * 2);
			if (x2 >= 0) {
				gc.setFill(tubes);
				rad = BMP_WIDTH / 2 + TUBE_RADIUS;
				gc.fillOval(x2 - rad, y2 - rad, rad * 2, rad * 2);
				gc.setFill(background);
				rad = BMP_WIDTH / 2 - TUBE_RADIUS;
				gc.fillOval(x2 - rad, y2 - rad, rad * 2, rad * 2);
			}
		}
		gc.strokeRect(0, 0, BMP_WIDTH, BMP_WIDTH);
		gc.setStroke(inverseFrame);
		gc.strokeRect(2, 2, BMP_WIDTH - 6, BMP_WIDTH - 6);
		canvas.snapshot(null, wi);
		return wi;
	}
}
