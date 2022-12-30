package gosm.ui.settings;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import gosm.backend.Game;
import gosm.backend.HighScoreEntry;
import gosm.ui.Bitmapper;
import gosm.ui.GameImage;
import gosm.ui.IDistribute;
import gosm.ui.UIConstants;
import gosm.ui.score.HighScoreDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsDialog extends VBox {

	public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	public static final Color DEFAULT_TUBES_COLOR = Color.gray(0.5);
	public static final Color DEFAULT_FRAME_COLOR = Color.gray(0.9);
	
	
	private IDistribute distribute;
	private Stage stage;
	private Scene scene;
	private ColorPicker colBackground;
	private ColorPicker colTubes;
	private ColorPicker colBorder;
	private GameImage viewBmp;
	private Bitmapper localBmp;
	private Button close;
	private Button saveAndClose;
	private Game localGame = null;
	private boolean initialized = false;
	
	public SettingsDialog(IDistribute distribute) {
		super();
		int[][] data = new int[4][6];
		localGame = new Game("",6,4, data);
		int index = 0;
		for (int x=0;x<6;x++) {
			for (int y=0;y<4;y++) {
				localGame.setData(x, y, index++);
			}
		}
		this.distribute = distribute;
		stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(this);
        stage.setTitle("Einstellungen");
        stage.setScene(scene);
        stage.setResizable(false);
        this.setPadding(new Insets(8,16,16,16));
        this.setSpacing(12);
        createInterieur();
        initialized = true;
	}
	
	private void createInterieur() {
		localBmp = new Bitmapper(UIConstants.bitmaps.getBackground(),
				UIConstants.bitmaps.getTubes(),
				UIConstants.bitmaps.getFrame());
		
		viewBmp = new GameImage(localBmp);
		viewBmp.setGame(localGame);
		this.getChildren().add(viewBmp);
		
		HBox tmp = new HBox();
		tmp.setPadding(new Insets(12,0,0,0));
		tmp.setSpacing(40);
		tmp.setAlignment(Pos.BASELINE_CENTER);
		Button defBtn = new Button("Systemfarben");
		defBtn.setOnMouseClicked(e -> {
			recycle(DEFAULT_BACKGROUND_COLOR, DEFAULT_TUBES_COLOR, DEFAULT_FRAME_COLOR);
		});
		Button undoBtn = new Button("Eingangsfarben");
		undoBtn.setOnMouseClicked(e -> {
			recycle(UIConstants.bitmaps.getBackground(),
					UIConstants.bitmaps.getTubes(),
					UIConstants.bitmaps.getFrame());
		});
		tmp.getChildren().addAll(defBtn, undoBtn);
		this.getChildren().add(tmp);
		colBackground = createCP("Hintergrundfarbe");
		colTubes = createCP("Rohrfarbe");
		colBorder = createCP("Rahmenfarbe");
		
		colBackground.setValue(localBmp.getBackground());
		colTubes.setValue(localBmp.getTubes());
		colBorder.setValue(localBmp.getFrame());
		
		HBox hbox = new HBox();
		close = new Button("Schließen");
		close.setOnMouseClicked(e -> {
			stage.close();
		});
		saveAndClose = new Button("Speichern");
		saveAndClose.setOnMouseClicked(e -> {
			distribute.changeImageColors(localBmp.getBackground(), localBmp.getTubes(), localBmp.getFrame());
			stage.close();
		});
		hbox.getChildren().addAll(close, saveAndClose);
		hbox.setPadding(new Insets(12,0,0,0));
		hbox.setSpacing(40);
		hbox.setAlignment(Pos.BASELINE_CENTER);
		this.getChildren().add(hbox);
		
	}
	
	private void recycle(Color defaultBackgroundColor, Color defaultTubesColor, Color defaultFrameColor) {
		initialized = false;
		
		colBackground.setValue(defaultBackgroundColor);
		colTubes.setValue(defaultTubesColor);
		colBorder.setValue(defaultFrameColor);
		initialized = true;
		redrawColorChanges();
	}

	private ColorPicker createCP(String title) {
		VBox vb = new VBox();
		vb.setSpacing(4);
		final ColorPicker cp = new ColorPicker();
		cp.valueProperty().addListener(e -> {
			redrawColorChanges();
		});
		Label lbl = new Label(title);
		vb.getChildren().addAll(lbl, cp);
		this.getChildren().add(vb);
		return cp;
	}
	
	private void redrawColorChanges() {
		if (!initialized) {
			return;
		}
		localBmp = new Bitmapper(colBackground.getValue(),
				colTubes.getValue(),
				colBorder.getValue());
		viewBmp.setBitmapper(localBmp);
		viewBmp.setGame(localGame);
	}
	
	public static void showSettings(IDistribute distribute) {
		SettingsDialog settingsDlg = new SettingsDialog(distribute);
		settingsDlg.scene.getWindow().sizeToScene();
		settingsDlg.stage.show();
	}
}
