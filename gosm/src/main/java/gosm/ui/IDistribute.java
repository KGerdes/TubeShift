package gosm.ui;

import gosm.backend.Game;
import gosm.backend.GameState;

public interface IDistribute {

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
}
