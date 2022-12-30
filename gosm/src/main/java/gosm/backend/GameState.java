package gosm.backend;

public enum GameState {

	OFFLINE(),
	RUNNING(),
	PAUSED();

	public boolean isRunning() {
		return this == RUNNING;
	}
	
	public boolean isOffline() {
		return this == OFFLINE;
	}
	
	public boolean isPaused() {
		return this == PAUSED;
	}
}
