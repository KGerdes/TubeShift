package gosm.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game {

	public static final int SCORE_MAX = 20;
	public static final int MIN_WIDTH = 4;
	public static final int MAX_WIDTH = 10;
	
	private final String key;
	private String name;
	private final int width;
	private final int height;
	private final boolean empty;
	
	private final int[][] data;
	private List<HighScoreEntry> highscore = new ArrayList<>();
	
	/*
	 * 
	 */
	public Game(String name, int width, int height, boolean empty, int[][] data) {
		this(UUID.randomUUID().toString(), name, width, height, empty, data);
	}
	
	/*
	 * 
	 */
	public Game(String uuid, String name, int width, int height, boolean empty, int[][] data) {
		this.key = uuid;
		this.name = name;
		this.width = width;
		this.height = height;
		this.data = data;
		this.empty = empty;
		check();
	}
	
	/**
	 * 
	 * @param name
	 * @param width
	 * @param height
	 * @param data
	 */
	public Game(String name, int width, int height, int[][] data) {
		this(name, width, height, false, data);
	}
	
	public String getKey() {
		return key;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isEmpty() {
		return empty;
	}

	private void check() {
		if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_WIDTH || height > MAX_WIDTH) {
			throw new IllegalArgumentException("Wrong width/height arguments in game");
		}
		if (height != data.length) {
			throw new IllegalArgumentException("array does not fit height");
		}
		if (width != data[0].length) {
			throw new IllegalArgumentException("array does not fit width");
		}
	}

	public static Game emptyGame() {
		int w = 6;
		int[][] data = new int[w - 1][w];
		return new Game("Leer", w, w - 1, true, data);
	}
	
	public static Game createBlankGame(int width, int height) {
		int[][] data = new int[width][height];
		return new Game("", width, height, false, data);
	}

	public int getItem(int col, int row) {
		return data[row][col];
	}

	public Game dup() {
		int[][] tdata = new int[height][width];
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				tdata[y][x] = data[y][x];
			}
		}
		return new Game(getName(), width, height, isEmpty(), tdata);
	}
	
	public Game mixit() {
		Random r = new Random(new Date().getTime());
		for (int i=0;i<200;i++) {
			if (i % 2 != 0) {
				shiftRow(r.nextInt(height), r.nextInt(2) == 1);
			} else {
				shiftColumn(r.nextInt(width), r.nextInt(2) == 1);
			}
		}
		return this;
	}
	
	public void shiftRow(int row, boolean up) {
		int store;
		if (!up) {
			int offset = width - 1;
			for (int i=0;i<width - 1;i++) {
				store = data[row][i];
				data[row][i] = data[row][offset % width];
				data[row][offset % width] = store;
				offset++;
			}
		} else {
			int offset = width;
			for (int i=width-1;i>0;i--) {
				store = data[row][i];
				data[row][i] = data[row][offset % width];
				data[row][offset % width] = store;
				offset--;
			}
		}
	}
	
	public void shiftColumn(int col, boolean up) {
		int store;
		if (!up) {
			int offset = height - 1;
			for (int i=0;i<height - 1;i++) {
				store = data[i][col];
				data[i][col] = data[offset % height][col];
				data[offset % height][col] = store;
				offset++;
			}
		} else {
			int offset = height;
			for (int i=height-1;i>0;i--) {
				store = data[i][col];
				data[i][col] = data[offset % height][col];
				data[offset % height][col] = store;
				offset--;
			}
		}
	}
	
	public boolean isFinished(Game comp) {
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				if (data[y][x] != comp.data[y][x]) {
					return false;
				}
			}
		}
		return true;
	}

	public String toString() {
		return name;
	}

	public void setData(int col, int row, int index) {
		data[row][col] = index;
	}
	
	public int getData(int col, int row) {
		return data[row][col];
	}

	public Game resize(int w, int h) {
		int[][] tdata = new int[h][w];
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				if (x < w && x < width && y < h && y < height) {
					tdata[y][x] = data[y][x];
				}
			}
		}
		return new Game(getKey(), getName(), w, h, isEmpty(), tdata);
	}

	public Game duplicate() {
		int[][] tdata = new int[height][width];
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				tdata[y][x] = data[y][x];
			}
		}
		return new Game(key,name,width,height,empty,tdata);
	}

	public long getComplexity() {
		Map<Integer, Integer> tst = new HashMap<>();
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				int count = tst.containsKey(data[y][x]) ? tst.get(data[y][x]) + 1 : 1;
				tst.put(data[y][x], count);
			}
		}
		int max = 0;
		for (Integer i : tst.values()) {
			max = Math.max(max, i);
		}
		double v = (width * height - max) * (Math.max(1.0,Math.sqrt(tst.size())) - 1);
		return Math.round(Math.sqrt(v) * 50.0);
	}

	public List<HighScoreEntry> getHighScore() {
		List<HighScoreEntry> tmp = highscore.stream()
				.sorted()
				.collect(Collectors.toList());
		int rang = 1;
		for (HighScoreEntry hse : tmp) {
			hse.setRang(rang++);
		}
		return tmp;
	}
	
	public static void addHighScore(List<HighScoreEntry> list, HighScoreEntry highScoreEntry) {
		if (highScoreEntry != null) {
			list.add(highScoreEntry);
		}
		Collections.sort(list);
		int rang = 1;
		for (HighScoreEntry hse : list) {
			hse.setRang(rang++);
		}
		while (list.size() > SCORE_MAX) {
			list.remove(SCORE_MAX);
		}
	}

	public void addHighScore(HighScoreEntry highScoreEntry) {
		addHighScore(highscore, highScoreEntry);
	}

	public int ranked(HighScoreEntry check) {
		int rank = 1;
		for (HighScoreEntry hse : highscore) {
			if (hse.compareTo(check) < 0) {
				rank++;
			}
		}
		if (rank > SCORE_MAX || check.getPoints() == 0) {
			return 0;
		}
		return rank;
	}

	public boolean hasHighscore() {
		return highscore.size() > 0;
	}

}
