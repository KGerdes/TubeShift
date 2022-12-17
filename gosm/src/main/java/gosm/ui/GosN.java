package gosm.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GosN extends Application {

	private BorderPane mainPane;
	private GameGrid gameGrid;
	private MenuPane menu;
	private BottomControls controls;
	
	@Override
    public void start(Stage stage) {
		menu = new MenuPane();
		controls = new BottomControls();
		mainPane = new BorderPane();
		mainPane.setStyle("-fx-background-color: #ffffff;");
		gameGrid = new GameGrid();
		mainPane.setCenter(gameGrid);
		mainPane.setTop(menu);
		mainPane.setBottom(controls);
        Scene scene = new Scene(mainPane, 800, 600);
        stage.setTitle("TubeShift");
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(); //
    }

}
