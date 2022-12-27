package gosm.ui;

import java.util.Timer;
import java.util.TimerTask;

import gosm.backend.Game;
import gosm.backend.HighScoreEntry;
import gosm.ui.score.HighScoreDialog;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class BottomControls extends HBox {
	
	private static final String TIME_FORMAT = "00:00:00";
	private static final String ZERO_PTS = "0000";
	private static final String PROP_GAMERNAME = "name";
	private static final int LHEIGHT = 26;
	private Timer timer;
	private IDistribute distribute;
	private TextField gamerName;
	private Label lblSteps;
	private Label lblTime;
	private Label lblPoints;
	private Label lblCompl;
	private Button hscoreBtn;
	private boolean running = false;
	private int steps = 0;
	private long startMs = 0;
	private long points = 0;
	private long fixedTime = 0;
	private long complexity = 0;
	
	public BottomControls(IDistribute distribute) {
		this.distribute = distribute;
		setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER);
		this.setSpacing(UIConstants.STD_SPACING);
		this.setPadding(new Insets(UIConstants.STD_PADDING));
		gamerName = new TextField();
		gamerName.setPromptText("Spieler");
		gamerName.setText(distribute.getProp(PROP_GAMERNAME));
		gamerName.textProperty().addListener((observable, oldValue, newValue) -> {
			distribute.setProp(PROP_GAMERNAME, newValue);
		});
		InfoBox<TextField> ib = new InfoBox<>("Spielername", gamerName, 150);
		lblSteps = new Label(ZERO_PTS);
		lblSteps.setAlignment(Pos.BASELINE_CENTER);
		lblSteps.setMinHeight(LHEIGHT);
		InfoBox<Label> ibSteps = new InfoBox<>("Schritte", lblSteps,100);
		lblTime = new Label(TIME_FORMAT);
		lblTime.setAlignment(Pos.BASELINE_CENTER);
		lblTime.setMinHeight(LHEIGHT);
		InfoBox<Label> ibTime = new InfoBox<>("Zeit", lblTime,100);
		lblPoints = new Label(ZERO_PTS);
		lblPoints.setAlignment(Pos.BASELINE_CENTER);
		lblPoints.setMinHeight(LHEIGHT);
		InfoBox<Label> ibPoints = new InfoBox<>("Punkte", lblPoints,100);
		lblCompl = new Label("-");
		lblCompl.setAlignment(Pos.BASELINE_CENTER);
		lblCompl.setMinHeight(LHEIGHT);
		InfoBox<Label> ibComplex = new InfoBox<>("Komplexität", lblCompl,100);
		
		Region reg = new Region();
		HBox.setHgrow(reg, Priority.ALWAYS);
		hscoreBtn = UIConstants.createIconButton("star", "Highscore"); //new Button("Highscore");
		hscoreBtn.setOnMouseClicked(e -> {
			HighScoreDialog.showHighScore(distribute, UIConstants.gameManager.getSelected(), null);
		});
		getChildren().addAll(ib, ibSteps, ibTime, ibPoints, ibComplex, reg, hscoreBtn);
		initTimer();
	}

	public void startGame(Game gm) {
		running = true;
		points = 0;
		steps = 0;
		fixedTime = 0;
		startMs = System.currentTimeMillis();
		complexity = gm.getComplexity();
	}

	public void finishGame() {
		points = calculatePoints();
		if (running && points > 0) {
			fixedTime = (System.currentTimeMillis() - startMs) / 1000;
			HighScoreEntry hsr = new HighScoreEntry(gamerName.getText(), points, steps, fixedTime);
			HighScoreDialog.showHighScore(distribute, UIConstants.gameManager.getSelected(), hsr);
		}
		running = false;
	}
	
	public void stopGame() {
		
	}
	
	public void initTimer() {
		timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	    	
	    	@Override
	        public void run() {
	    		updateFields();
	    		
	        }
	    }, 1000L,1000L);
	}
	
	private void updateFields() {
		Platform.runLater(() -> {
			if (running) {
				fixedTime = (System.currentTimeMillis() - startMs) / 1000;
    			points = calculatePoints();
    			if (points <= 0) {
    				distribute.stopGame();
    				points = 0;
    			}
    		} 
			lblSteps.setText(String.format("%04d", steps));
			lblTime.setText(String.format("%02d:%02d:%02d", fixedTime / 3600, (fixedTime / 60) % 60, fixedTime % 60));
			lblPoints.setText(String.format("%04d", points));
			lblCompl.setText(String.valueOf(complexity));
		});
	}
	
	private long calculatePoints() {
		if (running) {
			long tm = (System.currentTimeMillis() - startMs) / 1000;
			return complexity * 6 - steps * 10 - tm;
		} else {
			return 0;
		}
	}

	public void closeApplication() {
		timer.cancel();
	}

	public boolean isRunning() {
		return running;
	}

	public void gridStep() {
		steps++;
	}

	public void gameSet(Game game) {
		complexity = game.getComplexity();
	}
}
