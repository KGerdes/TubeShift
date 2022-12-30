package gosm.ui;

import gosm.backend.Game;
import gosm.backend.GameState;
import javafx.scene.paint.Color;

public interface IDistribute {
	
	public static final String RGB_BACKGROUND_PROP = Bitmapper.class.getName() + ".background";
	public static final String RGB_TUBES_PROP = Bitmapper.class.getName() + ".tube";
	public static final String RGB_FRAME_PROP = Bitmapper.class.getName() + ".frame";

	public void startNewGame(Game game);

	public void restartGame();
	
	public void setGameState(GameState running);
	
	public boolean isRunning();

	public void stopGame();

	public void gameChanged(Game game);

	public String getWorkingFile(String string);

	public void gridChanged();

	public String getProp(String string);

	public void setProp(String string, String newValue);

	public Game getGame();

	public void pauseGame();

	public void changeImageColors(Color background, Color tubes, Color frame);
}
