package gosm.ui.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import gosm.backend.Game;
import gosm.backend.StringLocalization;
import gosm.ui.Bitmapper;
import gosm.ui.GameImage;
import gosm.ui.IDistribute;
import gosm.ui.UIConstants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameEditor extends BorderPane {
	
	private IDistribute distribute;
	private StringLocalization sl;
	private Stage stage;
	private Scene scene;
	private GameImage gameImage;
	private VBox controls;
	private TextField nameText;
	private ComboBox<Integer> widthText;
	private ComboBox<Integer> heightText;
	private GridPane grid;
	private Button close;
	private Button save;
	private List<ImageView> dragList = new ArrayList<>();
	private boolean relevantDataChanged;
	
	public GameEditor(IDistribute distribute) {
		super();
		this.distribute = distribute;
		sl = UIConstants.getLocalization();
		stage = new Stage();
		stage.getIcons().add(distribute.getApplicationIcon());
        stage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(this);
        stage.setTitle(sl.getByObject(this, "Title"));
        stage.setScene(scene);
        stage.setResizable(false);
        this.setPadding(new Insets(20));
        createInterieur();
        stage.show();
	}
	
	private void createInterieur() {
		gameImage = new GameImage();
		gameImage.setGame(Game.createBlankGame(8,8));
		gameImage.setOnMouseClicked(me -> {
			int row = (int)(me.getY() - gameImage.getLayoutY()) / Bitmapper.BMP_WIDTH;
			int col = (int)(me.getX() - gameImage.getLayoutX()) / Bitmapper.BMP_WIDTH;
			if (me.getButton().equals(MouseButton.PRIMARY)) {
				scrollField(row, col, +1);
			} else {
				if (me.getButton().equals(MouseButton.SECONDARY)) {
					scrollField(row, col, -1);
				}
			}
		});
		this.setLeft(gameImage);
		controls = new VBox();
		controls.setPadding(new Insets(0,0,0,16));
		controls.setSpacing(12);
		AtomicReference<TextField> ar = new AtomicReference<>();
		VBox tmp = createText(ar, sl.getByObject(this, "Name"), "");
		nameText = ar.get();
		nameText.textProperty().addListener(e -> {
			save.setDisable(false);
			gameImage.getGame().setName(nameText.getText());
		});
		controls.getChildren().add(tmp);
		HBox htmp = new HBox();
		ObservableList<Integer> values = FXCollections.observableArrayList();
		for (int i=Game.MIN_WIDTH;i<=Game.MAX_WIDTH;i++) {
			values.add(i);
		}
		AtomicReference<ComboBox<Integer>> arcb = new AtomicReference<>();
		tmp = createCombo(arcb, sl.getByObject(this, "Width"), values);
		widthText = arcb.get();
		widthText.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object selectedInt) {
				resizeEditGame((Integer)selectedInt, gameImage.getGame().getHeight());
				dataWasChanged(true);
			}
		});
		htmp.getChildren().add(tmp);
		tmp = createCombo(arcb, sl.getByObject(this, "Height"), values);
		heightText = arcb.get();
		heightText.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object selectedInt) {
				resizeEditGame(gameImage.getGame().getWidth(), (Integer)selectedInt);
				dataWasChanged(true);
			}
		});
		Region rt = new Region();
		htmp.getChildren().addAll(rt,tmp);
		HBox.setHgrow(rt, Priority.ALWAYS);
		controls.getChildren().add(htmp);
		grid = new GridPane();
		grid.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
		Bitmapper bm = UIConstants.getBitmaps();
		List<ImageView> images = new ArrayList<>();
		int row = 0;
		for (int i=0;i<Bitmapper.BMP_COUNT;i++) {
			images.add(new ImageView(bm.getImage(i)));
			if (images.size() == 5) {
				grid.addRow(row, images.get(0),images.get(1),images.get(2),images.get(3), images.get(4));
				row++;
				dragList.addAll(images);
				images.clear();
			}
		}
		AtomicInteger ai = new AtomicInteger(0);
		for (final ImageView iv : dragList) {
			iv.setOnDragDetected(e -> {
				Dragboard db = iv.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
		        content.putString(String.valueOf("ImageIndex=") + String.valueOf(ai.get()));
		        db.setContent(content);
		        e.consume();
		        ai.incrementAndGet();
			});
		}
		
		gameImage.setOnDragOver(e -> {
			if (e.getGestureSource() instanceof ImageView) {
				e.acceptTransferModes(TransferMode.COPY);
			} else {
				if (e.getGestureSource() instanceof GameImage) {
					e.acceptTransferModes(TransferMode.COPY);
				}
			}
			e.consume();
		});
		gameImage.setOnDragDropped(e -> {
			int index = dragList.indexOf(e.getGestureSource());
			if (index >= 0) {
				handleDrop(e.getSceneX(), e.getSceneY(), index);
			} else {
				String indexStr = e.getDragboard().getString();
				if (indexStr != null) {
					String[] v = indexStr.split("=");
					if (v.length == 2 && v[0].equals("ImageIndex")) {
						int val = Integer.valueOf(v[1]);
						if (val >= 0 && val < Bitmapper.BMP_COUNT) {
							handleDrop(e.getSceneX(), e.getSceneY(), val);
						}
					}
				}
			}
			e.consume();
		});
		gameImage.setOnDragDetected(e -> {
			Dragboard db = gameImage.startDragAndDrop(TransferMode.ANY);
			ClipboardContent content = new ClipboardContent();
	        content.putString(String.valueOf("ImageIndex=" + getBitmapOfGame(e.getSceneX(), e.getSceneY()) ));
	        db.setContent(content);
			e.consume();
		});
		controls.getChildren().add(grid);
		Region r = new Region();
		HBox btns = new HBox();
		btns.setSpacing(12);
		close = new Button(sl.getByObject(this,"Close"));
		close.setOnMouseClicked(e -> { 
			closeEditor(); 
		});
		save  = new Button(sl.getByObject(this, "Save"));
		save.setOnMouseClicked(e -> {
			Game g = UIConstants.getGameManager().getGameByUUID(gameImage.getGame().getKey());
			if (g != null && relevantDataChanged && g.hasHighscore()) {
				Alert a = new Alert(Alert.AlertType.CONFIRMATION, sl.getByObject(this, "MsgHasRanking"), ButtonType.OK, ButtonType.CANCEL);
				a.setHeaderText(null);
				Optional<ButtonType> obt = a.showAndWait();
				if (obt.isPresent() && obt.get() == ButtonType.OK) {
					storeGame();
				}
			} else {
				storeGame();
			}
		});
		save.setMinWidth(100);
		close.setMinWidth(100);
		btns.getChildren().addAll(close, save);
		controls.getChildren().add(r);
		controls.getChildren().add(btns);
		btns.setAlignment(Pos.BOTTOM_RIGHT);
		this.setRight(controls);
		VBox.setVgrow(r, Priority.ALWAYS);
	}
	
	private void storeGame() {
		if (relevantDataChanged) {
			gameImage.getGame().setName(nameText.getText());
			UIConstants.getGameManager().saveGame(gameImage.getGame());
			distribute.gameChanged(gameImage.getGame());
		} else {
			UIConstants.getGameManager().changeGameName(gameImage.getGame().getKey(), gameImage.getGame().getName());
		}
		closeEditor();
	}
	
	private void scrollField(int row, int col, int toAdd) {
		int index = gameImage.getGame().getData(col, row);
		index = (index + Bitmapper.BMP_COUNT + toAdd) % Bitmapper.BMP_COUNT;
		gameImage.getGame().setData(col, row, index);
		gameImage.drawGame();
	}
	
	private void closeEditor() {
		stage.close();
	}
	
	protected void resizeEditGame(int width, int height) {
		Game g = gameImage.getGame().resize(width, height);
		gameImage.setGame(g);
		scene.getWindow().sizeToScene();
	}

	private void handleDrop(double sceneX, double sceneY, int index) {
		dataWasChanged(true);
		int col = (int)(sceneX - gameImage.getLayoutX()) / Bitmapper.BMP_WIDTH;
		int row = (int)(sceneY - gameImage.getLayoutY()) / Bitmapper.BMP_WIDTH;
		gameImage.getGame().setData(col, row, index);
		gameImage.drawGame();
	}
	
	private void dataWasChanged(boolean activate) {
		relevantDataChanged = activate;
		save.setDisable(!activate);
	}

	private int getBitmapOfGame(double sceneX, double sceneY) {
		int col = (int)(sceneX - gameImage.getLayoutX()) / Bitmapper.BMP_WIDTH;
		int row = (int)(sceneY - gameImage.getLayoutY()) / Bitmapper.BMP_WIDTH;
		return gameImage.getGame().getData(col, row);
	}

	private VBox createText(AtomicReference<TextField> textField, String caption, String value) {
		VBox res = new VBox();
		res.setSpacing(4);
		textField.set(new TextField(value));
		res.getChildren().addAll(new Text(caption), textField.get());
		return res;
	}
	
	private VBox createCombo(AtomicReference<ComboBox<Integer>> combo, String caption, ObservableList<Integer> values) {
		VBox res = new VBox();
		res.setSpacing(4);
		combo.set(new ComboBox<>(values));
		combo.get().setMinWidth(80);
		res.getChildren().addAll(new Text(caption), combo.get());
		return res;
	}
	
	private void setAttribs() {
		relevantDataChanged = false;
		nameText.setText(gameImage.getGame().getName());
		widthText.setValue(gameImage.getGame().getWidth());
		heightText.setValue(gameImage.getGame().getHeight());
		dataWasChanged(false);
	}

	public GameEditor createNewGame() {
		gameImage.setGame(Game.createBlankGame(6,6));
		setAttribs();
		stage.show();
		return this;
	}
	
	public void editGame(Game selected) {
		gameImage.setGame(selected.duplicate());
		resizeEditGame(selected.getWidth(), selected.getHeight());
		setAttribs();
		stage.show();
	}

}
