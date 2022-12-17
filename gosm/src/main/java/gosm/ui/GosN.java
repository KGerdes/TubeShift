package gosm.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GosN extends Application {

	private Pane gameBox;
	private GameGrid gameGrid;
	
	@Override
    public void start(Stage stage) {
		gameBox = new Pane();
		
		gameBox.setStyle("-fx-background-color: #ffffff;");
		gameGrid = new GameGrid();
		gameBox.getChildren().add(gameGrid);
		gameGrid.setVisible(true);
        Scene scene = new Scene(gameBox, 800, 600);
        stage.setTitle("Game of simple Numbers");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
