package gosm.ui;

import java.io.InputStream;
import java.util.Locale;

import gosm.backend.GameManager;
import gosm.backend.StringLocalization;
import gosm.backend.TubeShiftException;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class UIConstants {
	public static final int STD_PADDING = 8;
	public static final int STD_SPACING = 8;
	public static final String STD_PANE_COLOR_AND_BORDER = "-fx-background-color: #ffffff;-fx-border-width: 1px;-fx-border-style: solid;-fx-border-color: #888888;";
	public static final String STD_PANE_COLOR = "-fx-background-color: #ffffff;";
	public static final String STD_TEST_COLOR = "-fx-background-color: #234567;";
	public static final String BTN_IDLE_STYLE = "-fx-background-color: white; -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius: 15; -fx-padding: 0px;";
	public static final String BTN_HOVER_STYLE = "-fx-background-color: white; -fx-border-color: #aaaaaa; -fx-border-width: 2px; -fx-border-radius: 15; -fx-padding: 0px;";
	public static final String BTN_PRESSED_STYLE = "-fx-background-color: white; -fx-border-color: #444444; -fx-border-width: 2px; -fx-border-radius: 20; -fx-padding: 0px;";
	private static Bitmapper bitmaps = null;
	private static GameManager gameManager = null;
	private static StringLocalization localize = null;
	
	
	private UIConstants() {
		// Do nothing here
	}
		
	public static Bitmapper getBitmaps() {
		return bitmaps;
	}

	public static GameManager getGameManager() {
		return gameManager;
	}



	public static void initBitmaps(Color back, Color tube, Color frame, Color frame2) {
		bitmaps = new Bitmapper(back, tube, frame, frame2);
	}
	
	
	
	public static Button createIconButton(String icon, String tooltip) {
		final Button b = new Button();
		if (tooltip != null) {
			b.setTooltip(new Tooltip(tooltip));
		}
		b.setStyle(BTN_IDLE_STYLE);
		ClassLoader classLoader = UIConstants.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("icons/" + icon + ".png");
		Image img = new Image(inputStream);
        ImageView imgView = new ImageView(img);
        b.setGraphic(imgView);
        b.setOnMouseEntered(e ->  b.setStyle(BTN_HOVER_STYLE));
        b.setOnMouseExited(e ->  b.setStyle(BTN_IDLE_STYLE));
        b.setOnMousePressed(e ->  b.setStyle(BTN_PRESSED_STYLE));
		return b;
	}

	public static void initGameManager(IDistribute distribute) {
		if (gameManager != null) {
			throw new TubeShiftException("GameManager is already initialized");
		}
		gameManager = new GameManager(distribute);
	}
	
	public static void initLocalization(InputStream strm, Locale...locales) {
		localize = new StringLocalization(strm, locales);
	}

	public static StringLocalization getLocalization() {
		return localize;
	}
}
