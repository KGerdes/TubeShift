package gosm.ui;

import gosm.backend.Game;

public interface IDistribute {

	public void startNewGame(Game game);

	public void restartGame();
	
	public void setGameState(boolean running);
	
	public boolean isRunning();

	public void stopGame();

	public void gameChanged(Game game);

	public String getWorkingFile(String string);

	public void gridChanged();

	public String getProp(String string);

	public void setProp(String string, String newValue);
}
