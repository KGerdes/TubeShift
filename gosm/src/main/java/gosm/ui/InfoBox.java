package gosm.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class InfoBox<C extends Control> extends VBox {
	
	private Label header;
	private C bottom;
	
	public InfoBox(String info, C bottom, int width) {
		super();
		this.setSpacing(2);
		this.setPadding(new Insets(2));
		this.setStyle("-fx-border-width: 1px;-fx-border-style: solid;-fx-border-color: #888888;");
		this.header = new Label();
		this.header.setStyle("-fx-background-color: #cccccc;");
		this.header.setAlignment(Pos.BASELINE_CENTER);
		this.header.setMinWidth(width);
		this.header.setMaxWidth(width);
		this.header.setText(info);
		this.bottom = bottom;
		this.bottom.setMinWidth(width);
		this.bottom.setMaxWidth(width);
		this.getChildren().addAll(header, bottom);
	}
}
