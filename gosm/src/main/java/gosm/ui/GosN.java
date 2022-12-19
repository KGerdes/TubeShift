package gosm.ui;

import gosm.backend.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GosN extends Application implements IDistribute {

	private Scene scene;
	private BorderPane mainPane;
	private GameGrid gameGrid;
	private MenuPane menu;
	private BottomControls controls;
	private boolean running;
	
	@Override
    public void start(Stage stage) {
		addGames();
		menu = new MenuPane(this);
		controls = new BottomControls(this);
		mainPane = new BorderPane();
		mainPane.setStyle("-fx-background-color: #ffffff;");
		gameGrid = new GameGrid(this);
		mainPane.setCenter(gameGrid);
		mainPane.setTop(menu);
		mainPane.setBottom(controls);
		scene = new Scene(mainPane, 600, 400);
        stage.setTitle("TubeShift");
        stage.setScene(scene);
        stage.setResizable(false);
        resize();
        stage.setOnCloseRequest(event -> {
            controls.closeApplication();
        });
        stage.show();
    }
	
	private void resize() {
		scene.getWindow().setWidth(scene.getX() + gameGrid.getWidth() + 18);
		scene.getWindow().setHeight(scene.getY() + gameGrid.getCalculatedHeight() + 140);
	}
	
	private void addGames() {
		UIConstants.gameManager.addGame("Rechteck", 4, 4, "br,lr,lr,bl, tb,-,-,tb, tb,-,-,tb, tr,lr,lr,lt");
		UIConstants.gameManager.addGame("Rechteck2", 5, 4, "br,lr,lr,lr,bl, tb,-,-,-,tb, tb,-,-,-,tb, tr,lr,lr,lr,lt");
	}
 
    public static void main(String[] args) {
        launch();
    }

	@Override
	public void startNewGame(Game game) {
		gameGrid.activateGame(game, false);
		resize();
	}

	@Override
	public void restartGame() {
		gameGrid.activateGame(gameGrid.getSelectedGame(), true);
		
	}

	@Override
	public void setGameState(boolean running) {
		this.running = running;
		menu.setRunningState(running);
		if (running) {
			controls.startGame();
		} else {
			controls.finishGame();
		}
		
	}

	@Override
	public boolean isRunning() {
		return controls.isRunning();
	}

}
