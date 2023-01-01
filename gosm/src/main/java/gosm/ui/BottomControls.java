package gosm.ui;

import java.util.Timer;
import java.util.TimerTask;

import gosm.backend.Game;
import gosm.backend.HighScoreEntry;
import gosm.backend.StringLocalization;
import gosm.ui.score.HighScoreDialog;
import gosm.ui.settings.SettingsDialog;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

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
	private Label lblRank;
	private Label lblCompl;
	private Button hscoreBtn;
	private Button settingsBtn;
	private InfoBox<TextField> ib;
	private InfoBox<Label> ibSteps;
	private InfoBox<Label> ibTime;
	private InfoBox<Label> ibPoints;
	private InfoBox<Label> ibRank;
	private InfoBox<Label> ibComplex;
	private boolean running = false;
	private boolean paused = false;
	private int steps = 0;
	private long startMs = 0;
	private long points = 0;
	private long rank = 0;
	private long fixedTime = 0;
	private long complexity = 0;
	
	public BottomControls(IDistribute distribute) {
		StringLocalization sl = UIConstants.getLocalization();
		this.distribute = distribute;
		setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER);
		this.setSpacing(UIConstants.STD_SPACING);
		this.setPadding(new Insets(UIConstants.STD_PADDING));
		gamerName = new TextField();
		gamerName.setPromptText("Spieler");
		gamerName.setText(distribute.getProp(PROP_GAMERNAME));
		gamerName.textProperty().addListener((observable, oldValue, newValue) -> 
			distribute.setProp(PROP_GAMERNAME, newValue)
		);
		ib = new InfoBox<>("", gamerName, 150);
		lblSteps = new Label(ZERO_PTS);
		lblSteps.setAlignment(Pos.BASELINE_CENTER);
		lblSteps.setMinHeight(LHEIGHT);
		ibSteps = new InfoBox<>("", lblSteps,100);
		lblTime = new Label(TIME_FORMAT);
		lblTime.setAlignment(Pos.BASELINE_CENTER);
		lblTime.setMinHeight(LHEIGHT);
		ibTime = new InfoBox<>("", lblTime,100);
		lblPoints = new Label(ZERO_PTS);
		lblPoints.setAlignment(Pos.BASELINE_CENTER);
		lblPoints.setMinHeight(LHEIGHT);
		ibPoints = new InfoBox<>("", lblPoints,100);
		lblRank = new Label("-");
		lblRank.setAlignment(Pos.BASELINE_CENTER);
		lblRank.setMinHeight(LHEIGHT);
		ibRank = new InfoBox<>("", lblRank,100);
		lblCompl = new Label("-");
		lblCompl.setAlignment(Pos.BASELINE_CENTER);
		lblCompl.setMinHeight(LHEIGHT);
		ibComplex = new InfoBox<>("", lblCompl,100);
		
		Region reg = new Region();
		HBox.setHgrow(reg, Priority.ALWAYS);
		hscoreBtn = UIConstants.createIconButton("star", sl.getByObject(this, "HighScore")); 
		hscoreBtn.setOnMouseClicked(
				e -> 
					HighScoreDialog.showHighScore(distribute, UIConstants.getGameManager().getSelected(), null)
				);
		settingsBtn = UIConstants.createIconButton("settings", sl.getByObject(this, "Options")); 
		settingsBtn.setOnMouseClicked(e -> 
			SettingsDialog.showSettings(distribute)
		);
		localize();
		getChildren().addAll(ib, ibSteps, ibTime, ibPoints, ibRank, ibComplex, reg, hscoreBtn, settingsBtn);
		initTimer();
		
	}
	
	public void localize() {
		StringLocalization sl = UIConstants.getLocalization();
		ib.setHeader(sl.getByObject(this, "PlayerName"));
		ibSteps.setHeader(sl.getByObject(this, "Steps"));
		ibTime.setHeader(sl.getByObject(this, "Time"));
		ibPoints.setHeader(sl.getByObject(this, "Points"));
		ibRank.setHeader(sl.getByObject(this, "Ranking"));
		ibComplex.setHeader(sl.getByObject(this, "Complexity"));
	}

	public void startGame(Game gm) {
		running = true;
		points = 0;
		steps = 0;
		fixedTime = 0;
		rank = 0;
		startMs = System.currentTimeMillis();
		complexity = gm.getComplexity();
	}

	public void finishGame() {
		points = calculatePoints();
		if (running && points > 0) {
			fixedTime = (System.currentTimeMillis() - startMs) / 1000;
			HighScoreEntry hsr = new HighScoreEntry(gamerName.getText(), points, steps, fixedTime);
			HighScoreDialog.showHighScore(distribute, UIConstants.getGameManager().getSelected(), hsr);
		}
		running = false;
	}
	
	public void stopGame() {
		running = false;
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
		if (running && !paused) {
			fixedTime = (System.currentTimeMillis() - startMs) / 1000;
			points = calculatePoints();
			HighScoreEntry hse = new HighScoreEntry(points, steps, fixedTime);
			rank = distribute.getGame().ranked(hse);
			if (points <= 0) {
				distribute.stopGame();
				points = 0;
			}
		} 
		Platform.runLater(() -> {
			lblSteps.setText(String.format("%04d", steps));
			lblTime.setText(String.format("%02d:%02d:%02d", fixedTime / 3600, (fixedTime / 60) % 60, fixedTime % 60));
			lblPoints.setText(String.format("%04d", points));
			lblRank.setText(rank > 0 ? String.format("%02d", rank) : "-");
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
		return running && !paused;
	}

	public void gridStep() {
		steps++;
	}

	public void gameSet(Game game) {
		complexity = game.getComplexity();
	}

	public void pauseGame(boolean toPause) {
		if (toPause) {
			updateFields();
		} else {
			startMs = System.currentTimeMillis() - (fixedTime * 1000);
		}
		paused = toPause;
	}
}
