package gosm.ui.score;

import java.util.List;
import java.util.Optional;

import gosm.backend.Game;
import gosm.backend.HighScoreEntry;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HighScoreDialog extends VBox {

	private IDistribute distribute;
	private Stage stage;
	private Scene scene;
	private Label namelbl;
	private Label positionText;
	private TextField name;
	private TableView<HighScoreEntry> tv;
	private Optional<HighScoreEntry> optionalScore;
	private Game game;
	
	public HighScoreDialog(IDistribute distribute, Game game, HighScoreEntry hse) {
		super();
		this.game = game;
		optionalScore = Optional.ofNullable(hse);
		this.distribute = distribute;
		stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
		scene = new Scene(this);
        stage.setTitle("Highscore von Spiel '" + game.getName() + "'");
        stage.setScene(scene);
        stage.setResizable(false);
        this.setPadding(new Insets(20));
        createInterieur();
	}
	
	private void createInterieur() {
		setSpacing(12);
		setPadding(new Insets(12));
		if (optionalScore.isPresent()) {
			if (game.ranked(optionalScore.get()) > 0) {
				String s = String.format("Herzlichen Glückwunsch! \nSie haben mit %d Punkten den %d. Platz erreicht.", optionalScore.get().getPoints(), game.ranked(optionalScore.get()));
				positionText = new Label(s);
				namelbl = new Label("Name");
				name = new TextField("");
			} else {
				String s = String.format("Sie haben es mit %d Punkten leider nicht in den Highscore geschafft.", optionalScore.get().getPoints());
				optionalScore = Optional.ofNullable(null);
				positionText = new Label(s);
			}
		}
		tv = new TableView<>();
		addColumns();
		List<HighScoreEntry> tmplist = game.getHighScore();
		if (optionalScore.isPresent()) {
			Game.addHighScore(tmplist, optionalScore.get());
		}
		tv.getItems().addAll(tmplist);
		if (positionText != null) {
			getChildren().addAll(positionText);
		}
		if (optionalScore.isPresent()) {
			getChildren().addAll(namelbl, name);
			name.setText(optionalScore.get().getName());
		}
		Button close = new Button("Schließen");
		close.setOnMouseClicked(e -> {
			if (optionalScore.isPresent()) {
				optionalScore.get().setName(name.getText());
				game.addHighScore(optionalScore.get());
				UIConstants.gameManager.saveGame(game);
			}
			stage.close();
		});
		close.setAlignment(Pos.CENTER_RIGHT);
		getChildren().addAll(tv, close);
	}
	
	private void addColumns() {
		TableColumn<HighScoreEntry,Long> ti = new TableColumn<>();
		ti.setStyle("-fx-alignment: CENTER-RIGHT;");
		ti.setText("Rang");
		ti.setCellValueFactory(p -> getCellInt(p)); // new PropertyValueFactory<HighScoreEntry, Integer>("rang"));
		tv.getColumns().add(ti);
		TableColumn<HighScoreEntry,String> tc = new TableColumn<>();
		tc.setText("Name");
		tc.setStyle("-fx-alignment: CENTER-RIGHT;");
		tc.setCellValueFactory(new PropertyValueFactory<HighScoreEntry, String>("name"));
		tv.getColumns().add(tc);
		ti = new TableColumn<>();
		ti.setStyle("-fx-alignment: CENTER-RIGHT;");
		ti.setText("Punkte");
		ti.setCellValueFactory(new PropertyValueFactory<HighScoreEntry, Long>("points"));
		tv.getColumns().add(ti);
		ti = new TableColumn<>();
		ti.setText("Schritte");
		ti.setStyle("-fx-alignment: BASELINE_CENTER");
		ti.setCellValueFactory(new PropertyValueFactory<HighScoreEntry, Long>("steps"));
		tv.getColumns().add(ti);
		tc = new TableColumn<>();
		tc.setText("Zeit");
		tc.setCellValueFactory(new PropertyValueFactory<HighScoreEntry, String>("time"));
		tv.getColumns().add(tc);
		
	}
	
	private ObservableValue<Long> getCellInt(TableColumn.CellDataFeatures<HighScoreEntry,Long> cdf) {
		SimpleObjectProperty<Long> prop = new SimpleObjectProperty();
		prop.set(cdf.getValue().getRang());
		return prop;
	}
	
	private TableCell<HighScoreEntry, String> cellFactory(TableColumn<HighScoreEntry, String> cf) {
		TableCell<HighScoreEntry, String> cell = new TableCell<>();
		
		return cell;
	}
	
	public static void showHighScore(IDistribute distribute, Game game, HighScoreEntry hse) {
		HighScoreDialog hsd = new HighScoreDialog(distribute, game, hse);
		hsd.scene.getWindow().sizeToScene();
		hsd.stage.show();
	}
}
