package gosm.backend;

public class Game {

	public static final int MIN_WIDTH = 4;
	public static final int MAX_WIDTH = 8;
	
	private final String name;
	private final int width;
	private final int height;
	private final boolean empty;
	
	private final int[][] data;
	
	/*
	 * 
	 */
	public Game(String name, int width, int height, boolean empty, int[][] data) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.data = data;
		this.empty = empty;
		System.out.println("Len : " + data.length);
		check();
	}
	
	public Game(String name, int width, int height, int[][] data) {
		this(name, width, height, false, data);
	}
	
	
	
	public String getName() {
		return name;
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
		if (height != data[0].length) {
			throw new IllegalArgumentException("array does not fit height");
		}
		if (width != data.length) {
			throw new IllegalArgumentException("array does not fit width");
		}
	}

	public static Game emptyGame() {
		int w = 6;
		int[][] data = new int[w][w - 1];
		return new Game("Leer", w, w - 1, true, data);
	}
}
