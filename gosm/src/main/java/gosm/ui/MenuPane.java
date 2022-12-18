package gosm.ui;

import gosm.backend.Game;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class MenuPane extends HBox {

	private ComboBox<Game> gameSelect;
	
	public MenuPane() {
		super();
		setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER);
		this.setSpacing(UIConstants.STD_SPACING);
		this.setPadding(new Insets(UIConstants.STD_PADDING));
		Button b = new Button("Neu");
		gameSelect = new ComboBox<Game>();
		gameSelect.setItems(getGameList());
		gameSelect.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object selectedGame) {
				
			}
		});
		this.getChildren().addAll(b, gameSelect);
	}

	private ObservableList<Game> getGameList() {
		ObservableList<Game> ol = FXCollections.observableArrayList();
		ol.addAll(UIConstants.gameManager.getGames());
		return ol;
	}
}
