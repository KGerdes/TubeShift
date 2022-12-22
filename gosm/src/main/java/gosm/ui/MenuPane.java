package gosm.ui;

import gosm.backend.Game;
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

	private IDistribute    distribute;
	private ComboBox<Game> gameSelect;
	private Button start;
	private Button stop;
	private Button newGame;
	private Button editGame;
	private GameEditor editor;
	
	public MenuPane(IDistribute distribute) {
		super();
		this.distribute = distribute;
		setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER);
		this.setSpacing(UIConstants.STD_SPACING);
		this.setPadding(new Insets(UIConstants.STD_PADDING));
		start = new Button("Starten");
		start.setOnMouseClicked(e -> {
			distribute.restartGame();
		});
		stop = new Button("Aufgeben");
		
		stop.setOnMouseClicked(e -> {
			distribute.stopGame();
		});
		Region r = new Region();
		newGame = new Button("Neues Spiel");
		newGame.setAlignment(Pos.CENTER_RIGHT);
		newGame.setOnMouseClicked(e -> {
			if (editor == null) {
				editor = new GameEditor(distribute);
			}
			editor.createNewGame();
		});
		editGame = new Button("Bearbeiten");
		editGame.setAlignment(Pos.CENTER_RIGHT);
		editGame.setOnMouseClicked(e -> {
			if (editor == null) {
				editor = new GameEditor(distribute);
			}
			editor.editGame(UIConstants.gameManager.getSelected());
		});
		gameSelect = new ComboBox<Game>();
		gameSelect.setMinWidth(160);
		gameSelect.setItems(getGameList());
		gameSelect.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object selectedGame) {
				if (selectedGame != null) {
					UIConstants.gameManager.setSelected((Game)selectedGame);
					distribute.startNewGame((Game) selectedGame);
				}
			}
		});
		HBox.setHgrow(r, Priority.ALWAYS);
		this.getChildren().addAll(gameSelect, start, stop, r, newGame, editGame);
		setRunningState(false);
	}

	private ObservableList<Game> getGameList() {
		ObservableList<Game> ol = FXCollections.observableArrayList();
		ol.addAll(UIConstants.gameManager.getGames());
		return ol;
	}

	public void setRunningState(boolean running) {
		gameSelect.disableProperty().set(running);
		start.disableProperty().set(running);
		newGame.disableProperty().set(running);
		stop.disableProperty().set(!running);
	}

	public void reloadGameList() {
		gameSelect.getItems().clear();
		gameSelect.getItems().addAll(UIConstants.gameManager.getGames());
		
	}
}
