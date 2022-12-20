package gosm.ui;

import gosm.backend.Game;
import gosm.ui.editor.GameEditor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class MenuPane extends HBox {

	private IDistribute    distribute;
	private ComboBox<Game> gameSelect;
	private Button start;
	private Button stop;
	private Button newGame;
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
		newGame = new Button("Neues Spiel");
		newGame.setOnMouseClicked(e -> {
			if (editor != null) {
				editor.createNewGame();
			} else {
				editor = GameEditor.newGame();
			}
		});
		gameSelect = new ComboBox<Game>();
		gameSelect.setMinWidth(160);
		gameSelect.setItems(getGameList());
		gameSelect.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object selectedGame) {
				distribute.startNewGame((Game) selectedGame);
			}
		});
		this.getChildren().addAll(gameSelect, start, stop, newGame);
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
}
