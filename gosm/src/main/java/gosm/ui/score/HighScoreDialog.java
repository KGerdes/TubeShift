package gosm.ui.score;

import java.util.List;
import java.util.Optional;
import gosm.backend.Game;
import gosm.backend.HighScoreEntry;
import gosm.backend.StringLocalization;
import gosm.ui.IDistribute;
import gosm.ui.UIConstants;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HighScoreDialog extends VBox {

	private StringLocalization sl;
	private static final String FX_ALIGNMENT_CENTER_RIGHT = "-fx-alignment: CENTER-RIGHT;";
	private Stage stage;
	private Scene scene;
	private Label positionText;
	private TextField name;
	private TableView<HighScoreEntry> tv;
	private Optional<HighScoreEntry> optionalScore;
	private Game game;
	private boolean closed = false;
	
	public HighScoreDialog(IDistribute distribute, Game game, HighScoreEntry hse) {
		super();
		sl = UIConstants.getLocalization();
		this.game = game;
		optionalScore = Optional.ofNullable(hse);
		stage = new Stage();
		stage.getIcons().add(distribute.getApplicationIcon());
        stage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(this);
		
        stage.setTitle(sl.getByObject(this, "Title", game.getName()));
        stage.setScene(scene);
        stage.setResizable(false);
        this.setPadding(new Insets(20));
        createInterieur();
	}
	
	private void createInterieur() {
		VBox vtmp = null;
		setSpacing(12);
		setPadding(new Insets(12));
		if (optionalScore.isPresent()) {
			if (game.ranked(optionalScore.get()) > 0) {
				String s = String.format(sl.getByObject(this, "YouGotIt", optionalScore.get().getPoints(), game.ranked(optionalScore.get())));
				positionText = new Label(s);
				positionText.setStyle("-fx-background-color: #ddffdd;");
				
				Label namelbl = new Label("Name");
				name = new TextField("");
				vtmp = new VBox();
				vtmp.getChildren().addAll(namelbl, name);
			} else {
				String s = String.format(sl.getByObject(this, "YouGotItNot", optionalScore.get().getPoints()));
				positionText = new Label(s);
				positionText.setStyle("-fx-background-color: #ffdddd;");
				optionalScore = Optional.ofNullable(null);
			}
			positionText.setStyle(positionText.getStyle() + "-fx-border-width: 1px;-fx-border-color: #000000;");
			positionText.setPadding(new Insets(12));
			positionText.setAlignment(Pos.BASELINE_CENTER);
		}
		tv = new TableView<>();
		tv.setMinWidth(450);
		tv.setMinHeight(500);
		tv.setPrefWidth(450);
		tv.setSelectionModel(new NoSelectionModel<>(tv));
		addColumns();
		double[] parts = { 14,30,15,15,20 };
		int index = 0;
		for (TableColumn<HighScoreEntry, ?> tc : tv.getColumns()) {
			tc.prefWidthProperty().bind(tv.widthProperty().multiply(parts[index++] / 100.0));
			tc.setResizable(false);
		}
		List<HighScoreEntry> tmplist = game.getHighScore();
		if (optionalScore.isPresent()) {
			Game.addHighScore(tmplist, optionalScore.get());
		}
		tv.getItems().addAll(tmplist);
		
		
		if (positionText != null) {
			getChildren().addAll(positionText);
		}
		if (optionalScore.isPresent()) {
			tv.scrollTo(optionalScore.get());
			getChildren().addAll(vtmp);
			name.setText(optionalScore.get().getName());
		}
		Button close = new Button(sl.getByObject(this, "Close"));
		close.setOnMouseClicked(e -> {
			closeWindow();
		});
		getChildren().addAll(tv, close);
		this.setAlignment(Pos.TOP_CENTER);
		stage.setOnCloseRequest(e -> {
			closeWindow();
		});
		
	}
	
	private void closeWindow() {
		if (optionalScore.isPresent() && !closed) {
			optionalScore.get().setName(name.getText());
			game.addHighScore(optionalScore.get());
			UIConstants.getGameManager().saveGame(game);
		}
		closed = true;
		stage.close();
	}
	
	private void addColumns() {
		TableColumn<HighScoreEntry,Long> ti = new TableColumn<>();
		ti.setStyle(FX_ALIGNMENT_CENTER_RIGHT);
		ti.setText(sl.getByObject(this, "Ranking"));
		ti.setCellValueFactory(this::getCellInt);
		ti.setCellFactory(column -> getCell(Pos.CENTER_RIGHT));
		tv.getColumns().add(ti);
		TableColumn<HighScoreEntry,String> tc = new TableColumn<>();
		tc.setText(sl.getByObject(this, "Name"));
		tc.setStyle(FX_ALIGNMENT_CENTER_RIGHT);
		tc.setCellValueFactory(new PropertyValueFactory<>("name"));
		tc.setCellFactory(column -> getCell(Pos.CENTER_RIGHT));
		tv.getColumns().add(tc);
		ti = new TableColumn<>();
		ti.setStyle(FX_ALIGNMENT_CENTER_RIGHT);
		ti.setText(sl.getByObject(this, "Points"));
		ti.setCellFactory(column -> getCell(Pos.BASELINE_CENTER));
		ti.setCellValueFactory(new PropertyValueFactory<>("points"));
		tv.getColumns().add(ti);
		ti = new TableColumn<>();
		ti.setText(sl.getByObject(this, "Steps"));
		ti.setStyle("-fx-alignment: BASELINE_CENTER");
		ti.setCellFactory(column -> getCell(Pos.BASELINE_CENTER));
		ti.setCellValueFactory(new PropertyValueFactory<>("steps"));
		tv.getColumns().add(ti);
		tc = new TableColumn<>();
		tc.setText(sl.getByObject(this, "Time"));
		tc.setCellFactory(column -> getCell(Pos.BASELINE_CENTER));
		tc.setCellValueFactory(new PropertyValueFactory<>("time"));
		tc.setStyle("-fx-alignment: BASELINE_CENTER;");
		tv.getColumns().add(tc);
		
	}
	
	private <T> TableCell<HighScoreEntry, T> getCell(Pos p) {
		return new TableCell<HighScoreEntry, T>() {
			@Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : getItem().toString());
                setGraphic(null);
                int index = getIndex();
                if (index >= 0 && index < getTableView().getItems().size()) {
                	StringBuilder sb = new StringBuilder("-fx-alignment: ").append(p.toString()).append(";");
	                HighScoreEntry hse = getTableView().getItems().get(getIndex());
	                if (hse == optionalScore.orElse(null)) {
	                	sb.append("-fx-background-color: #aaffaa");
	                } else {
	                	sb.append(index % 2 == 0 ? "-fx-background-color: #ffffff" : "-fx-background-color: #f8f8f8");
	                }
	                setStyle(sb.toString());
                }
            }
		};
	}
	
	private ObservableValue<Long> getCellInt(TableColumn.CellDataFeatures<HighScoreEntry,Long> cdf) {
		SimpleObjectProperty<Long> prop = new SimpleObjectProperty<>();
		prop.set(cdf.getValue().getRang());
		return prop;
	}
	
	public static void showHighScore(IDistribute distribute, Game game, HighScoreEntry hse) {
		HighScoreDialog hsd = new HighScoreDialog(distribute, game, hse);
		hsd.scene.getWindow().sizeToScene();
		hsd.stage.show();
	}
}
