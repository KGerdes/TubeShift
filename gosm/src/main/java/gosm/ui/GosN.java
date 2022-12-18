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

	private Scene scene;
	private BorderPane mainPane;
	private GameGrid gameGrid;
	private MenuPane menu;
	private BottomControls controls;
	
	@Override
    public void start(Stage stage) {
		addGames();
		menu = new MenuPane();
		controls = new BottomControls();
		mainPane = new BorderPane();
		mainPane.setStyle("-fx-background-color: #ffffff;");
		gameGrid = new GameGrid();
		mainPane.setCenter(gameGrid);
		mainPane.setTop(menu);
		mainPane.setBottom(controls);
		System.out.println("menu   : " + menu.getHeight());
		System.out.println("game   : " + gameGrid.getCalculatedHeight());
		System.out.println("bottom : " + controls.getHeight());
        scene = new Scene(mainPane, 600, 400);
        stage.setTitle("TubeShift");
        stage.setScene(scene);
        stage.setResizable(false);
        resize();
        
        stage.show();
    }
	
	private void resize() {
		scene.getWindow().setWidth(scene.getX() + gameGrid.getWidth() + 18);
		scene.getWindow().setHeight(scene.getY() + gameGrid.getCalculatedHeight() + 140);
	}
	
	private void addGames() {
		UIConstants.gameManager.addGame("Rechteck", 4, 4, "br,lr,lr,bl, tb,-,-,tb, tb,-,-,tb, tr,lr,lr,lt");
		UIConstants.gameManager.addGame("Rechteck2", 4, 4, "br,lr,lr,bl, tb,-,-,tb, tb,-,-,tb, tr,lr,lr,lt");
	}
 
    public static void main(String[] args) {
        launch();
    }

}
