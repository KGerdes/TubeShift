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
	private long complexity = 0;
	
	public BottomControls(IDistribute distribute) {
		this.distribute = distribute;
		setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER);
		this.setSpacing(UIConstants.STD_SPACING);
		this.setPadding(new Insets(UIConstants.STD_PADDING));
		Label tmp = new Label("Spieler :");
		tmp.setMinHeight(LHEIGHT);
		tmp.setAlignment(Pos.CENTER_LEFT);
		gamerName = new TextField();
		gamerName.setPromptText("Spieler");
		gamerName.setText(distribute.getProp(PROP_GAMERNAME));
		gamerName.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Text changed: " + newValue);
			distribute.setProp(PROP_GAMERNAME, newValue);
		});
		Label tmp2 = new Label("Steps :");
		tmp2.setMinHeight(LHEIGHT);
		tmp2.setAlignment(Pos.CENTER_LEFT);
		lblSteps = new Label("-");
		lblSteps.setAlignment(Pos.CENTER_LEFT);
		lblSteps.minWidth(80);
		lblSteps.setMinHeight(LHEIGHT);
		Label tmp3 = new Label("Zeit :");
		tmp3.setMinHeight(LHEIGHT);
		tmp3.setAlignment(Pos.CENTER_LEFT);
		lblTime = new Label("-");
		lblTime.setAlignment(Pos.CENTER_LEFT);
		lblTime.minWidth(80);
		lblTime.setMinHeight(LHEIGHT);
		Label tmp4 = new Label("Punkte :");
		tmp4.setMinHeight(LHEIGHT);
		tmp4.setAlignment(Pos.CENTER_LEFT);
		lblPoints = new Label("-");
		lblPoints.setAlignment(Pos.CENTER_LEFT);
		lblPoints.minWidth(80);
		lblPoints.setMinHeight(LHEIGHT);
		Label tmp5 = new Label("Komplexität :");
		tmp5.setMinHeight(LHEIGHT);
		tmp5.setAlignment(Pos.CENTER_LEFT);
		lblCompl = new Label("-");
		lblCompl.setAlignment(Pos.CENTER_LEFT);
		lblCompl.minWidth(80);
		lblCompl.setMinHeight(LHEIGHT);
		Region reg = new Region();
		HBox.setHgrow(reg, Priority.ALWAYS);
		hscoreBtn = new Button("Highscore");
		hscoreBtn.setOnMouseClicked(e -> {
			HighScoreDialog.showHighScore(distribute, UIConstants.gameManager.getSelected(), null);
		});
		getChildren().addAll(tmp, gamerName, tmp2, lblSteps, tmp3, lblTime, tmp4, lblPoints, tmp5, lblCompl, reg, hscoreBtn);
		initTimer();
	}

	public void startGame(Game gm) {
		running = true;
		points = 0;
		steps = 0;
		startMs = System.currentTimeMillis();
		complexity = gm.getComplexity();
	}

	public void finishGame() {
		points = calculatePoints();
		if (running && points > 0) {
			long tm = (System.currentTimeMillis() - startMs) / 1000;
			HighScoreEntry hsr = new HighScoreEntry(gamerName.getText(), points, steps, tm);
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
				long tm = (System.currentTimeMillis() - startMs) / 1000;
    			lblSteps.setText(String.valueOf(steps));
    			lblTime.setText(String.format("%02d:%02d:%02d", tm / 3600, (tm / 60) % 60, tm % 60));
    			points = calculatePoints();
    			if (points <= 0) {
    				distribute.stopGame();
    				points = 0;
    			}
    			lblPoints.setText(String.valueOf(points));
    		} else {
    			lblSteps.setText("-");
    			lblTime.setText("-");
    			lblPoints.setText("-");
    		}
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
