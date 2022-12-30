package gosm.ui;

import gosm.backend.Game;
import gosm.backend.GameState;
import gosm.ui.editor.GameEditor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class MenuPane extends HBox {

	private static final String GAME_UUID = "game-uuid";
	private IDistribute    distribute;
	private ComboBox<Game> gameSelect;
	private Button start;
	private Button stop;
	private Button pause;
	private Button newGame;
	private Button editGame;
	private GameEditor editor;
	
	public MenuPane(IDistribute distribute) {
		super();
		this.distribute = distribute;
		setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER);
		this.setSpacing(UIConstants.STD_SPACING);
		this.setPadding(new Insets(UIConstants.STD_PADDING));
		start = UIConstants.createIconButton("play_circle", "Beginnen");
		start.setOnMouseClicked(e -> {
			distribute.restartGame();
		});
		pause = UIConstants.createIconButton("pause_circle", "Spiel pausieren");
		pause.setOnMouseClicked(e -> {
			distribute.pauseGame();
		});
		stop = UIConstants.createIconButton("stop_circle", "Aufgeben");
		stop.setOnMouseClicked(e -> {
			distribute.stopGame();
		});
		Region r = new Region();
		newGame = UIConstants.createIconButton("add_circle", "Neues Spielbrett anlegen");
		newGame.setAlignment(Pos.CENTER_RIGHT);
		newGame.setOnMouseClicked(e -> {
			if (editor == null) {
				editor = new GameEditor(distribute);
			}
			editor.createNewGame();
		});
		editGame = UIConstants.createIconButton("draw", "Spielbrett bearbeiten");
		editGame.setAlignment(Pos.CENTER_RIGHT);
		editGame.setOnMouseClicked(e -> {
			if (editor == null) {
				editor = new GameEditor(distribute);
			}
			editor.editGame(UIConstants.gameManager.getSelected());
		});
		Region r2 = new Region();
		r2.setMinWidth(30);
		gameSelect = new ComboBox<Game>();
		gameSelect.setPadding(new Insets(4,0,4,0));
		gameSelect.setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER + "-fx-font-size: 20px;-fx-font-weight: bold;");
		gameSelect.setMinWidth(160);
		gameSelect.setItems(getGameList());
		gameSelect.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object selectedGame) {
				if (selectedGame != null) {
					Game selgame = (Game)selectedGame;
					UIConstants.gameManager.setSelected(selgame);
					distribute.startNewGame(selgame);
					distribute.setProp(GAME_UUID, selgame.getKey());
				}
			}
		});
		HBox.setHgrow(r, Priority.ALWAYS);
		this.getChildren().addAll(gameSelect, r2, start, pause, stop, r, newGame, editGame);
		setRunningState(GameState.OFFLINE);
	}

	private ObservableList<Game> getGameList() {
		ObservableList<Game> ol = FXCollections.observableArrayList();
		ol.addAll(UIConstants.gameManager.getGames());
		return ol;
	}

	public void setRunningState(GameState running) {
		gameSelect.disableProperty().set(!running.isOffline());
		start.disableProperty().set(running.isRunning());
		pause.disableProperty().set(!running.isRunning());
		stop.disableProperty().set(!running.isRunning());
		newGame.disableProperty().set(!running.isOffline());
		editGame.disableProperty().set(!running.isOffline());
	}

	public void reloadGameList() {
		gameSelect.getItems().clear();
		gameSelect.getItems().addAll(UIConstants.gameManager.getGames());
		
	}

	public void showPropGame() {
		String guuid = distribute.getProp(GAME_UUID);
		if (guuid != null) {
			Game g = UIConstants.gameManager.getGameByUUID(guuid);
			if (g != null) {
				gameSelect.setValue(g);
			}
		}
	}
	
}
