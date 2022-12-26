package gosm.backend;

public class HighScoreEntry {

	private String name;
	transient private long rang;
	private final long points;
	private final long steps;
	private final long seconds;
	
	public HighScoreEntry(long points, long steps, long seconds) {
		this.rang = 0;
		this.points = points;
		this.steps = steps;
		this.seconds = seconds;
	}
	
	public HighScoreEntry(String name, long points, long steps, long seconds) {
		this.name = name;
		this.points = points;
		this.steps = steps;
		this.seconds = seconds;
	}
	
	public HighScoreEntry(String savedStr) {
		String[] s = savedStr.split(",");
		this.name = s[0];
		this.points = Long.parseLong(s[1]);
		this.steps = Long.parseLong(s[2]);
		this.seconds = Long.parseLong(s[3]);
	}

	public String getName() {
		return name;
	}

	public HighScoreEntry setName(String name) {
		this.name = name;
		return this;
	}

	public long getRang() {
		return rang;
	}

	public void setRang(long rang) {
		this.rang = rang;
	}

	public long getPoints() {
		return points;
	}

	public long getSteps() {
		return steps;
	}

	public long getSeconds() {
		return seconds;
	}
	
	public String getTime() {
		return String.format("%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60,seconds % 60);
	}

	public String saveStr() {
		return String.format("%s,%d,%d,%d", name, points, steps, seconds);
	}
	
	
}
