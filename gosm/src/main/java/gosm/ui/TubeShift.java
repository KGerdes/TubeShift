package gosm.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import gosm.backend.Game;
import gosm.backend.GameManager;
import gosm.backend.GameState;
import gosm.backend.TubeShiftException;
import gosm.ui.settings.SettingsDialog;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TubeShift extends Application implements IDistribute {

	private static String[] systemArgs;
	private String workingDir;
	private Scene scene;
	private BorderPane mainPane;
	private GameGrid gameGrid;
	private MenuPane menu;
	private BottomControls controls;
	private GameState gameState = GameState.OFFLINE;
	private Properties sysprops;
	private File propFile;
	private boolean propsChanged = false;
	
	@Override
    public void start(Stage stage) {
		UIConstants.gameManager = new GameManager(this);
		initWorkingDir(systemArgs);
		initBitmaps();
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
        menu.showPropGame();
    }
	
	@Override
	public void stop() {
		if (propsChanged) {
			storeProps();
		}
	}
	
	private void storeProps() {
		try {
			sysprops.store(new FileOutputStream(propFile), null);
			propsChanged = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void resize() {
		scene.getWindow().setWidth(scene.getX() + gameGrid.getWidth() + 18);
		scene.getWindow().setHeight(scene.getY() + gameGrid.getCalculatedHeight() + 140);
	}
	
	private void initBitmaps() {
		UIConstants.initBitmaps(readColor(IDistribute.RGB_BACKGROUND_PROP,SettingsDialog.DEFAULT_BACKGROUND_COLOR), 
								readColor(IDistribute.RGB_TUBES_PROP, SettingsDialog.DEFAULT_TUBES_COLOR), 
								readColor(IDistribute.RGB_FRAME_PROP, SettingsDialog.DEFAULT_FRAME_COLOR));
	}
	
	private Color readColor(String probname, Color def) {
		try {
			String[] rgb = getProp(probname).split(",");
			return Color.rgb(Integer.parseInt(rgb[0]), 
					Integer.parseInt(rgb[1]), 
					Integer.parseInt(rgb[2]));
		} catch (Exception e) {
			return def;
		}
	}
	
	private void writeColor(String propname, Color rgb) {
		StringBuilder sb = new StringBuilder();
		sb.append((int)(255 * rgb.getRed())).append(",")
			.append((int)(255 * rgb.getGreen())).append(",")
			.append((int)(255 * rgb.getBlue()));
		setProp(propname, sb.toString());
	}
	
	@Override
	public void changeImageColors(Color background, Color tubes, Color frame) {
		writeColor(IDistribute.RGB_BACKGROUND_PROP,background); 
		writeColor(IDistribute.RGB_TUBES_PROP, tubes); 
		writeColor(IDistribute.RGB_FRAME_PROP, frame);
		storeProps();
		initBitmaps();
		
		gameGrid.redrawBitmaps(UIConstants.bitmaps);
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
		if (gameState == GameState.OFFLINE) {
			gameGrid.activateGame(gameGrid.getSelectedGame(), true);
		} else {
			if (gameState == GameState.PAUSED) {
				setGameState(GameState.RUNNING);
			}
		}
	}

	@Override
	public void setGameState(GameState stateToSet) {
		if (gameState == stateToSet) {
			return;
		}
		System.out.println("GameState " + gameState + " -> " + stateToSet);
		GameState old = this.gameState;
		this.gameState = stateToSet;
		menu.setRunningState(stateToSet);
		if (stateToSet.isRunning()) {
			if (old == GameState.PAUSED) {
				controls.pauseGame(false);
				gameGrid.pauseGame(false);
			} else {
				controls.startGame(gameGrid.getSelectedGame());
			}
		} else {
			if (stateToSet == GameState.OFFLINE) {
				if (gameGrid != null) {
					if (gameGrid.isGameFinished()) { 
						controls.finishGame();
					} else {
						controls.stopGame();
					}
				}
			} else {
				controls.pauseGame(true);
				gameGrid.pauseGame(true);
			}
		}
	}

	@Override
	public boolean isRunning() {
		return controls.isRunning();
	}

	@Override
	public void stopGame() {
		setGameState(GameState.OFFLINE);
	}
	
	@Override
	public void pauseGame() {
		setGameState(GameState.PAUSED);
	}
	
	@Override
	public void gameChanged(Game game) {
		menu.reloadGameList();
		menu.selectGame(game);
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
		
		propFile = new File(getWorkingFile("tubeshift.properties"));
		sysprops = new Properties();
		if (propFile.exists()) {
			try {
				sysprops.load(new FileInputStream(propFile));
			} catch (Exception e) {
				throw new TubeShiftException(e.getMessage(), e);
			}
		}
	}

	
    public static void main(String[] args) {
    	systemArgs = args;
        launch();
    }

	@Override
	public String getProp(String propname) {
		return sysprops.getProperty(propname);
	}

	@Override
	public void setProp(String propname, String newValue) {
		propsChanged = true;
		sysprops.setProperty(propname, newValue);
	}

	@Override
	public Game getGame() {
		return gameGrid.getSelectedGame();
	}


}
