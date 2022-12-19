package gosm.ui;

import gosm.backend.Game;

public interface IDistribute {

	public void startNewGame(Game game);

	public void restartGame();
	
	public void setGameState(boolean running);
	
	public boolean isRunning();
}
