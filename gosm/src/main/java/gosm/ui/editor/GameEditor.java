package gosm.ui.editor;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameEditor extends BorderPane {
	
	private Stage stage;
	private Scene scene;
	
	
	protected GameEditor() {
		super();
		stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(this, 600, 400);
        stage.setTitle("Editor");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
	}
	
	public GameEditor createNewGame() {
		stage.show();
		return this;
	}
	
	
	public static GameEditor newGame() {
		GameEditor edit = new GameEditor();
		return edit.createNewGame();
	}

}
