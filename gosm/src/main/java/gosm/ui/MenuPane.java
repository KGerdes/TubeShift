package gosm.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class MenuPane extends HBox {

	public MenuPane() {
		super();
		setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER);
		this.setSpacing(UIConstants.STD_SPACING);
		this.setPadding(new Insets(UIConstants.STD_PADDING));
		Button b = new Button("Neu");
		this.getChildren().add(b);
	}
}
