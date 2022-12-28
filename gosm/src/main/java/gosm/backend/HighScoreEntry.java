package gosm.backend;

import java.util.Date;

public class HighScoreEntry implements Comparable<HighScoreEntry> {

	private String name;
	transient private long rang;
	private final long points;
	private final long steps;
	private final long seconds;
	private long  doneTime;
	
	public HighScoreEntry(long points, long steps, long seconds) {
		this.rang = 0;
		this.points = points;
		this.steps = steps;
		this.seconds = seconds;
		this.doneTime = new Date().getTime();
	}
	
	public HighScoreEntry(String name, long points, long steps, long seconds) {
		this.name = name;
		this.points = points;
		this.steps = steps;
		this.seconds = seconds;
		this.doneTime = new Date().getTime();
	}
	
	public HighScoreEntry(String savedStr) {
		String[] s = savedStr.split(",");
		this.name = s[0];
		this.points = Long.parseLong(s[1]);
		this.steps = Long.parseLong(s[2]);
		this.seconds = Long.parseLong(s[3]);
		if (s.length > 4) {
			this.doneTime = Long.parseLong(s[4]);
		} else {
			this.doneTime = new Date().getTime();
		}
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
	
	public long getDoneTime() {
		return doneTime;
	}

	public HighScoreEntry setDoneTime(long doneTime) {
		this.doneTime = doneTime;
		return this;
	}

	public String getTime() {
		return String.format("%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60,seconds % 60);
	}

	public String saveStr() {
		return String.format("%s,%d,%d,%d,%d", name, points, steps, seconds, doneTime);
	}

	@Override
	public int compareTo(HighScoreEntry arg) {
		if (this.points != arg.points) {
			return (int)(arg.points - this.points);
		}
		if (this.steps != arg.steps) {
			return (int)(this.steps - arg.steps);
		}
		if (this.seconds != arg.seconds) {
			return (int)(this.seconds - arg.seconds);
		}
		if (this.doneTime != arg.doneTime) {
			return (int)(this.doneTime - arg.doneTime);
		}
		return this.name.compareTo(arg.name);
	}
	
	@Override
	public String toString() {
		return String.format("%s;%d;%d;%d;%d", name,points,steps,seconds, doneTime);
	}
	
	
}
