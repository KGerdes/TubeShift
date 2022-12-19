package gosm.ui.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import gosm.backend.Game;
import gosm.ui.Bitmapper;
import gosm.ui.GameImage;
import gosm.ui.UIConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameEditor extends BorderPane {
	
	private Stage stage;
	private Scene scene;
	private GameImage gameImage;
	private VBox controls;
	private TextField nameText;
	private ComboBox<Integer> widthText;
	private ComboBox<Integer> heightText;
	private GridPane grid;
	private List<ImageView> dragList = new ArrayList<>();
	
	protected GameEditor() {
		super();
		stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(this);
        stage.setTitle("Editor");
        stage.setScene(scene);
        stage.setResizable(false);
        this.setPadding(new Insets(20));
        createInterieur();
        stage.show();
	}
	
	private void createInterieur() {
		gameImage = new GameImage();
		gameImage.setGame(Game.createBlankGame(8,8));
		this.setCenter(gameImage);;
		controls = new VBox();
		controls.setPadding(new Insets(16));
		controls.setSpacing(12);
		AtomicReference<TextField> ar = new AtomicReference<>();
		VBox tmp = createText(ar, "Name", "");
		nameText = ar.get();
		controls.getChildren().add(tmp);
		ObservableList<Integer> values = FXCollections.observableArrayList();
		for (int i=Game.MIN_WIDTH;i<=Game.MAX_WIDTH;i++) {
			values.add(i);
		}
		AtomicReference<ComboBox<Integer>> arcb = new AtomicReference<>();
		tmp = createCombo(arcb, "Breite", values);
		widthText = arcb.get();
		controls.getChildren().add(tmp);
		tmp = createCombo(arcb, "Höhe", values);
		heightText = arcb.get();
		controls.getChildren().add(tmp);
		grid = new GridPane();
		grid.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
		Bitmapper bm = UIConstants.bitmaps;
		List<ImageView> images = new ArrayList<>();
		int row = 0;
		for (int i=0;i<Bitmapper.BMP_COUNT;i++) {
			images.add(new ImageView(bm.getImage(i)));
			if (images.size() == 4) {
				grid.addRow(row, images.get(0),images.get(1),images.get(2),images.get(3));
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
		        content.putString(String.valueOf("ImageIndex=" + String.valueOf(ai.get())));
		        db.setContent(content);
		        e.consume();
		        ai.incrementAndGet();
			});
		}
		
		gameImage.setOnDragOver(e -> {
			if (e.getGestureSource() instanceof ImageView) {
				e.acceptTransferModes(TransferMode.COPY);
			}
			e.consume();
		});
		gameImage.setOnDragDropped(e -> {
			int index = dragList.indexOf(e.getGestureSource());
			if (index >= 0) {
				handleDrop(e.getSceneX(), e.getSceneY(), index);
			}
			e.consume();
		});
		controls.getChildren().add(grid);
		this.setRight(controls);
	}
	
	private void handleDrop(double sceneX, double sceneY, int index) {
		int col = (int)(sceneX - gameImage.getLayoutX()) / Bitmapper.BMP_WIDTH;
		int row = (int)(sceneY - gameImage.getLayoutY()) / Bitmapper.BMP_WIDTH;
		gameImage.getGame().setData(col, row, index);
		gameImage.drawGame();
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
		combo.set(new ComboBox(values));
		combo.get().setMinWidth(80);
		res.getChildren().addAll(new Text(caption), combo.get());
		return res;
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
