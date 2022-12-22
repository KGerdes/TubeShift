package gosm.ui;

import java.io.File;

import gosm.backend.Game;
import gosm.backend.GameManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GosN extends Application implements IDistribute {

	private static String[] systemArgs;
	private String workingDir;
	private Scene scene;
	private BorderPane mainPane;
	private GameGrid gameGrid;
	private MenuPane menu;
	private BottomControls controls;
	private boolean running;
	
	@Override
    public void start(Stage stage) {
		UIConstants.gameManager = new GameManager(this);
		initWorkingDir(systemArgs);
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
		UIConstants.gameManager.loadGames(workingDir);
	}
 
	@Override
	public void startNewGame(Game game) {
		controls.gameSet(game);
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
			controls.startGame(gameGrid.getSelectedGame());
		} else {
			controls.finishGame();
		}
	}

	@Override
	public boolean isRunning() {
		return controls.isRunning();
	}

	@Override
	public void stopGame() {
		setGameState(false);
	}
	
	@Override
	public void gameChanged(Game game) {
		menu.reloadGameList();
	}
	
	@Override
	public String getWorkingFile(String name) {
		if (name == null) {
			return workingDir;
		}
		return workingDir + File.separator + name;
	}
	
	@Override
	public void gridChanged() {
		controls.gridStep();
		
	}
	
	private void initWorkingDir(String[] args) {
		boolean found = false;
		String dir = System.getProperty("user.home") + File.separator + "tubeshift";
		for (String arg : args) {
			if (arg.equals("-dir")) {
				found = true;
			} else {
				if (found) {
					dir = arg;
					break;
				}
			}
		}
		int len = dir.length();
		File f = new File(dir);
		if (dir.charAt(len - 1) == '\\' || dir.charAt(len - 1) == '/') {
			dir = dir.substring(0,len-1);
		}
				
		if (!f.isDirectory()) {
			throw new IllegalArgumentException("Working directory not found, please create directory : " + dir);
		}
		
		workingDir = dir;
	}

	
    public static void main(String[] args) {
    	systemArgs = args;
        launch();
    }

}
