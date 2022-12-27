package gosm.ui;

import java.io.InputStream;

import gosm.backend.GameManager;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UIConstants {
	public static int STD_PADDING = 8;
	public static int STD_SPACING = 8;
	public static final String STD_PANE_COLOR_AND_BORDER = "-fx-background-color: #ffffff;-fx-border-width: 1px;-fx-border-style: solid;-fx-border-color: #888888;";
	public static final String STD_PANE_COLOR = "-fx-background-color: #ffffff;";
	public static final String STD_TEST_COLOR = "-fx-background-color: #234567;";
	public static final String BTN_IDLE_STYLE = "-fx-background-color: white; -fx-border-color: white; -fx-border-width: 2px; -fx-border-radius: 15; -fx-padding: 0px;";
	public static final String BTN_HOVER_STYLE = "-fx-background-color: white; -fx-border-color: #aaaaaa; -fx-border-width: 2px; -fx-border-radius: 15; -fx-padding: 0px;";
	public static final String BTN_PRESSED_STYLE = "-fx-background-color: white; -fx-border-color: #444444; -fx-border-width: 2px; -fx-border-radius: 20; -fx-padding: 0px;";
	public static Bitmapper bitmaps = new Bitmapper();
	public static GameManager gameManager = null;
	
	
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
        b.setOnMouseEntered(e -> { b.setStyle(BTN_HOVER_STYLE); });
        b.setOnMouseExited(e -> { b.setStyle(BTN_IDLE_STYLE); });
        b.setOnMousePressed(e -> { b.setStyle(BTN_PRESSED_STYLE); });
        // b.setOnMouseReleased(e -> { b.setStyle(BTN_HOVER_STYLE); });
		return b;
	}
}
