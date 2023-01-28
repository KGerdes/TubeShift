package gosm.ui;

import gosm.backend.Game;
import gosm.backend.GameState;
import gosm.backend.StringLocalization;
import gosm.ui.delete.DeleteDialog;
import gosm.ui.editor.GameEditor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
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
	private Button copyGame;
	private Button editGame;
	private Button deleteGame;
	private GameEditor editor;
	
	public MenuPane(IDistribute distribute) {
		super();
		StringLocalization sl = UIConstants.getLocalization();
		this.distribute = distribute;
		setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER);
		this.setSpacing(UIConstants.STD_SPACING);
		this.setPadding(new Insets(UIConstants.STD_PADDING));
		start = UIConstants.createIconButton("play_circle", sl.getByObject(this, "Start"));
		start.setOnMouseClicked(e -> 
			distribute.restartGame()
		);
		pause = UIConstants.createIconButton("pause_circle", sl.getByObject(this, "Pause"));
		pause.setOnMouseClicked(e -> 
			distribute.pauseGame()
		);
		stop = UIConstants.createIconButton("stop_circle", sl.getByObject(this, "Stop"));
		stop.setOnMouseClicked(e -> 
			distribute.stopGame()
		);
		Region r = new Region();
		newGame = UIConstants.createIconButton("add_circle", sl.getByObject(this, "NewBoard"));
		newGame.setAlignment(Pos.CENTER_RIGHT);
		newGame.setOnMouseClicked(e -> {
			if (editor == null) {
				editor = new GameEditor(distribute);
			}
			editor.createNewGame();
		});
		copyGame = UIConstants.createIconButton("copy", sl.getByObject(this, "CopyBoard"));
		copyGame.setAlignment(Pos.CENTER_RIGHT);
		copyGame.setOnMouseClicked(e -> {
			if (editor == null) {
				editor = new GameEditor(distribute);
			}
			Game g = UIConstants.getGameManager().getSelected().duplicateWithNewKey();
			g.setName(g.getName() + " " + sl.getByObject(this, "CopyNameExt"));
			editor.editGame(g);
		});
		editGame = UIConstants.createIconButton("draw", sl.getByObject(this, "EditBoard"));
		editGame.setAlignment(Pos.CENTER_RIGHT);
		editGame.setOnMouseClicked(e -> {
			if (editor == null) {
				editor = new GameEditor(distribute);
			}
			editor.editGame(UIConstants.getGameManager().getSelected());
		});
		deleteGame = UIConstants.createIconButton("delete", sl.getByObject(this, "DeleteBoard"));
		deleteGame.setAlignment(Pos.CENTER_RIGHT);
		deleteGame.setOnMouseClicked(e -> {
			DeleteDialog.showInfoDialog(distribute, UIConstants.getGameManager().getSelected());
		});
		Region r2 = new Region();
		r2.setMinWidth(30);
		gameSelect = new ComboBox<>();
		gameSelect.setPadding(new Insets(4,0,4,0));
		gameSelect.setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER + "-fx-font-size: 20px;-fx-font-weight: bold;");
		gameSelect.setMinWidth(160);
		gameSelect.setItems(getGameList());
		gameSelect.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object selectedGame) {
				if (selectedGame != null) {
					Game selgame = (Game)selectedGame;
					UIConstants.getGameManager().setSelected(selgame);
					distribute.startNewGame(selgame);
					distribute.setProp(GAME_UUID, selgame.getKey());
				}
			}
		});
		HBox.setHgrow(r, Priority.ALWAYS);
		this.getChildren().addAll(gameSelect, r2, start, pause, stop, r, newGame, copyGame, deleteGame, editGame);
		setRunningState(GameState.OFFLINE);
		localize();
	}
	
	public void localize() {
		StringLocalization sl = UIConstants.getLocalization();
		start.setTooltip(new Tooltip(sl.getByObject(this, "Start")));
		pause.setTooltip(new Tooltip(sl.getByObject(this, "Pause")));
		stop.setTooltip(new Tooltip(sl.getByObject(this, "Stop")));
		newGame.setTooltip(new Tooltip(sl.getByObject(this, "NewBoard")));
		copyGame.setTooltip(new Tooltip(sl.getByObject(this, "CopyBoard")));
		editGame.setTooltip(new Tooltip(sl.getByObject(this, "EditBoard")));
		deleteGame.setTooltip(new Tooltip(sl.getByObject(this, "DeleteBoard")));
	}
	

	private ObservableList<Game> getGameList() {
		ObservableList<Game> ol = FXCollections.observableArrayList();
		ol.addAll(UIConstants.getGameManager().getGames());
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
		gameSelect.getItems().addAll(UIConstants.getGameManager().getGames());
	}
	
	public void selectGame(Game game) {
		gameSelect.setValue(game);
	}

	public void showPropGame() {
		String guuid = distribute.getProp(GAME_UUID);
		if (guuid != null) {
			Game g = UIConstants.getGameManager().getGameByUUID(guuid);
			if (g != null) {
				gameSelect.setValue(g);
			}
		}
	}

	
	
}
