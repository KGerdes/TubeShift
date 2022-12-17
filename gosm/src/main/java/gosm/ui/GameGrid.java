package gosm.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class GameGrid extends GridPane {

	
	public GameGrid() {
		setGridLinesVisible(true);
		Label l = new Label("Test");
		getChildren().add(l);
		setStyle("-fx-background-color: #cccccc;");
		ColumnConstraints cc = new ColumnConstraints();
		
	}
}
