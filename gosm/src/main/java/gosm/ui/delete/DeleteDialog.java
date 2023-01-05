package gosm.ui.delete;

import java.util.logging.Level;
import java.util.logging.Logger;

import gosm.backend.Game;
import gosm.backend.StringLocalization;
import gosm.ui.IDistribute;
import gosm.ui.UIConstants;
import gosm.ui.info.InfoDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DeleteDialog {

	private static Logger log = Logger.getLogger(DeleteDialog.class.getName());
	private IDistribute distribute;
	private VBox  pane;
	private Stage stage;
	private Scene scene;
	private CheckBox delGame;
	private CheckBox delRanking;
	private Button delBtn;
	
	
	public DeleteDialog(IDistribute distribute) {
		stage = new Stage();
		stage.getIcons().add(distribute.getApplicationIcon());
        stage.initModality(Modality.APPLICATION_MODAL);
        this.distribute = distribute;
	}
	
	private DeleteDialog createInterieur(Game game) {
		StringLocalization sl = UIConstants.getLocalization();
		pane = new VBox();
		scene = new Scene(pane);
        stage.setTitle(sl.getByObject(this, "Title"));
        stage.setScene(scene);
        stage.setResizable(false);
        pane.setPadding(new Insets(16,40,16,16));
        pane.setSpacing(40);
        delGame = new CheckBox(sl.getByObject(this, "ChkGame", game.getName()));
        delGame.selectedProperty().addListener(e -> { deletableState(true); });
        pane.getChildren().add(delGame);
        if (game.hasHighscore()) {
        	delRanking = new CheckBox(sl.getByObject(this, "ChkRanking", game.getName()));
        	pane.getChildren().add(delRanking);
        	delRanking.selectedProperty().addListener(e -> { deletableState(true); });
        }
        HBox tmp = new HBox();
        Button closeBtn = new Button(sl.getByObject(this, "CloseBtn"));
        delBtn = new Button(sl.getByObject(this, "DeleteBtn"));
        closeBtn.setDefaultButton(true);
        closeBtn.setOnMouseClicked(e -> stage.close());
        
        delBtn.setDisable(true);
        delBtn.setOnMouseClicked(e -> { 
        	if (deletableState(false)) {
        		if (UIConstants.getGameManager().deleteGame(game)) {
        			distribute.gameDeleted();
        		}
        	}
        	stage.close();
        });
        	
        Region r = new Region();
        tmp.getChildren().addAll(closeBtn, r, delBtn);
        HBox.setHgrow(r, Priority.ALWAYS);
        pane.getChildren().add(tmp);
        return this;
	}
	
	private boolean deletableState(boolean setState) {
		boolean res = delGame.isSelected() && (delRanking == null || delRanking.isSelected());
		if (setState) {
			delBtn.setDisable(!res);
		}
		log.log(Level.INFO, "deletableState({0})", res);
		return res;
	}

	public DeleteDialog show() {
		stage.show();
		return this;
	}
	
	public static DeleteDialog showInfoDialog(IDistribute distribute, Game game) {
		return new DeleteDialog(distribute).createInterieur(game).show();
	}
}
