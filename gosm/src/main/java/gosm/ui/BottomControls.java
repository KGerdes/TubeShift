package gosm.ui;

import java.util.Timer;
import java.util.TimerTask;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class BottomControls extends HBox {
	
	private Timer timer;
	private IDistribute distribute;
	private boolean running = false;
	private int steps = 0;
	private long startMs = 0;
	
	public BottomControls(IDistribute distribute) {
		this.distribute = distribute;
		setStyle(UIConstants.STD_PANE_COLOR_AND_BORDER);
		this.setSpacing(UIConstants.STD_SPACING);
		this.setPadding(new Insets(UIConstants.STD_PADDING));
		Button b = new Button("Test");
		getChildren().add(b);
		initTimer();
	}

	public void startGame() {
		running = true;
		steps = 0;
		startMs = System.currentTimeMillis();
	}

	public void finishGame() {
		running = false;
	}
	
	public void initTimer() {
		timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	    	
	    	@Override
	        public void run() {
	    		if (running) {
	    			// System.out.println("timer running " + (System.currentTimeMillis() - startMs));
	    		} else {
	    			// System.out.println("Timer not running");
	    		}
	        }
	    }, 1000L,1000L);
	}

	public void closeApplication() {
		timer.cancel();
		
	}

	public boolean isRunning() {
		return running;
	}
}
