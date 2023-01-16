package gosm.ui.settings;


import gosm.backend.Game;
import gosm.backend.StringLocalization;
import gosm.ui.Bitmapper;
import gosm.ui.GameImage;
import gosm.ui.IDistribute;
import gosm.ui.UIConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsDialog extends VBox {

	private static final int COL_BTN_WIDTH = 120;
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	public static final Color DEFAULT_TUBES_COLOR = Color.gray(0.5);
	public static final Color DEFAULT_FRAME_COLOR = Color.gray(0.9);
	public static final Color DEFAULT_FRAME2_COLOR = Color.WHITE;
	
	
	private IDistribute distribute;
	private StringLocalization sl;
	private Stage stage;
	private Scene scene;
	private RadioButton german;
	private RadioButton english;
	private Button defBtn;
	private Button undoBtn;
	private Button close;
	private Button saveAndClose;
	
	private Label lblColBackground;
	private ColorPicker colBackground;
	
	private Label lblColTubes;
	private ColorPicker colTubes;
	
	private Label lblColBorder1;
	private ColorPicker colBorder1;
	
	private Label lblColBorder2;
	private ColorPicker colBorder2;
	
	private GameImage viewBmp;
	private Bitmapper localBmp;
	private Game localGame = null;
	private boolean initialized = false;
	
	public SettingsDialog(IDistribute distribute) {
		super();
		localGame = createLocalGame();
		this.distribute = distribute;
		sl = UIConstants.getLocalization();
		
		stage = new Stage();
		stage.getIcons().add(distribute.getApplicationIcon());
        stage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(this);
        stage.setTitle(sl.getByObject(this, "Options"));
        stage.setScene(scene);
        stage.setResizable(false);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(8,16,16,16));
        this.setSpacing(12);
        createInterieur();
        initialized = true;
	}
	
	private Game createLocalGame() {
		int[][] data = new int[4][4];
		Game g = new Game("",4,4, data);
		int[] v = {6,5,5,3, 10,19,17,10, 10,18,16,10, 12,5,5,9 };
		int index = 0;
		for (int x=0;x<4;x++) {
			for (int y=0;y<4;y++) {
				g.setData(x, y, v[index++]);
			}
		}
		return g;
	}
	
	private void createInterieur() {
		localBmp = new Bitmapper(UIConstants.getBitmaps().getBackground(),
				UIConstants.getBitmaps().getTubes(),
				UIConstants.getBitmaps().getFrame(),
				UIConstants.getBitmaps().getFrame2());
		
		viewBmp = new GameImage(localBmp);
		viewBmp.setGame(localGame);
		this.getChildren().add(viewBmp);
		
		HBox tmp = new HBox();
		tmp.setPadding(new Insets(12,0,0,0));
		tmp.setSpacing(40);
		tmp.setAlignment(Pos.BASELINE_CENTER);
		defBtn = new Button("Systemfarben");
		defBtn.setOnMouseClicked(e -> 
			recycle(DEFAULT_BACKGROUND_COLOR, DEFAULT_TUBES_COLOR, DEFAULT_FRAME_COLOR, DEFAULT_FRAME2_COLOR)
		);
		defBtn.setMinWidth(COL_BTN_WIDTH);
		undoBtn = new Button("Eingangsfarben");
		undoBtn.setOnMouseClicked(e -> 
			recycle(UIConstants.getBitmaps().getBackground(),
					UIConstants.getBitmaps().getTubes(),
					UIConstants.getBitmaps().getFrame(),
					UIConstants.getBitmaps().getFrame2())
		);
		undoBtn.setMinWidth(COL_BTN_WIDTH);
		Region r = new Region();
		tmp.getChildren().addAll(defBtn, r, undoBtn);
		HBox.setHgrow(r, Priority.ALWAYS);
		this.getChildren().add(tmp);
		HBox store = new HBox();
		store.setSpacing(12);		
		lblColBackground = new Label();
		colBackground = createCP(store, lblColBackground, "Hintergrundfarbe");
		r = new Region();
		store.getChildren().add(r);
		HBox.setHgrow(r, Priority.ALWAYS);
		lblColTubes = new Label();
		colTubes = createCP(store, lblColTubes, "Rohrfarbe");
		this.getChildren().add(store);
		store = new HBox();
		store.setSpacing(12);
		lblColBorder1 = new Label();
		colBorder1 = createCP(store, lblColBorder1, "Rahmenfarbe I");
		r = new Region();
		store.getChildren().add(r);
		HBox.setHgrow(r, Priority.ALWAYS);
		lblColBorder2 = new Label();
		colBorder2 = createCP(store, lblColBorder2, "Rahmenfarbe II");
		
		this.getChildren().add(store);
		colBackground.setValue(localBmp.getBackground());
		colTubes.setValue(localBmp.getTubes());
		colBorder1.setValue(localBmp.getFrame());
		colBorder2.setValue(localBmp.getFrame2());
		
		HBox radios = new HBox();
		radios.setPadding(new Insets(20,0,20,0));
		ToggleGroup tg =  new ToggleGroup();
		german = new RadioButton("Deutsch");
		german.setToggleGroup(tg);
		english = new RadioButton("English");
		english.setToggleGroup(tg);
		r = new Region();
		HBox.setHgrow(r, Priority.ALWAYS);
		radios.getChildren().addAll(german, r, english);
		this.getChildren().add(radios);
		
		if (sl.getSelectedName().equals("en_GB")) {
			english.setSelected(true);
		} else {
			german.setSelected(true);
		}
		HBox hbox = new HBox();
		close = new Button("Schließen");
		close.setMinWidth(COL_BTN_WIDTH);
		close.setOnMouseClicked(e -> 
			stage.close()
		);
		saveAndClose = new Button("Speichern");
		saveAndClose.setDefaultButton(true);
		saveAndClose.setMinWidth(COL_BTN_WIDTH);
		saveAndClose.setOnMouseClicked(e -> {
			if (english.selectedProperty().get()) {
				distribute.setSettingsLocale(sl.getLocaleByName("en_GB"));
			} else {
				distribute.setSettingsLocale(sl.getLocaleByName("de_DE"));
			}
			distribute.changeImageColors(localBmp.getBackground(), 
										localBmp.getTubes(), 
										localBmp.getFrame(), 
										localBmp.getFrame2());
			stage.close();
		});
		hbox.getChildren().addAll(saveAndClose, close);
		hbox.setPadding(new Insets(12,0,0,0));
		hbox.setSpacing(40);
		hbox.setAlignment(Pos.BASELINE_CENTER);
		this.getChildren().add(hbox);
		localize();
	}
	
	public void localize() {
		stage.setTitle(sl.getByObject(this, "Options"));
		defBtn.setText(sl.getByObject(this, "SysColors"));
		undoBtn.setText(sl.getByObject(this, "EntryColors"));
		lblColBackground.setText(sl.getByObject(this, "BackgroundColor"));
		lblColTubes.setText(sl.getByObject(this, "TubeColor"));
		lblColBorder1.setText(sl.getByObject(this, "BorderColor1"));
		lblColBorder2.setText(sl.getByObject(this, "BorderColor2"));
		saveAndClose.setText(sl.getByObject(this, "SaveBtn"));
		close.setText(sl.getByObject(this, "CloseBtn"));
	}
	
	private void recycle(Color defaultBackgroundColor, Color defaultTubesColor, Color defaultFrameColor, Color color) {
		initialized = false;
		
		colBackground.setValue(defaultBackgroundColor);
		colTubes.setValue(defaultTubesColor);
		colBorder1.setValue(defaultFrameColor);
		colBorder2.setValue(color);
		initialized = true;
		redrawColorChanges();
	}

	private ColorPicker createCP(HBox hbox, Label lbl, String title) {
		VBox vb = new VBox();
		vb.setSpacing(4);
		final ColorPicker cp = new ColorPicker();
		cp.valueProperty().addListener(e -> 
			redrawColorChanges()
		);
		lbl.setText(title);
		vb.getChildren().addAll(lbl, cp);
		hbox.getChildren().add(vb);
		return cp;
	}
	
	private void redrawColorChanges() {
		if (!initialized) {
			return;
		}
		localBmp = new Bitmapper(colBackground.getValue(),
				colTubes.getValue(),
				colBorder1.getValue(),
				colBorder2.getValue());
		viewBmp.setBitmapper(localBmp);
		viewBmp.setGame(localGame);
	}
	
	public static void showSettings(IDistribute distribute) {
		SettingsDialog settingsDlg = new SettingsDialog(distribute);
		settingsDlg.scene.getWindow().sizeToScene();
		settingsDlg.stage.show();
	}
}
