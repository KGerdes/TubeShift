package gosm.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import gosm.ui.IDistribute;

public class GameManager {

	
	private static final String DATA = "data";

	private static final String HEIGHT = "height";

	private static final String WIDTH = "width";

	private static final String NAME = "name";

	private static final String UUID = "uuid";

	private static final String SCORE = "score";

	private static final String CRLF = "\r\n";
	
	private IDistribute distribute;
	private Map<String, Game> games = new HashMap<>();
	private Game selected = null;
	
	public GameManager(IDistribute distribute) {
		this.distribute = distribute;
		selected = Game.emptyGame();
		games.put(selected.getKey(), selected);
	}
	
	public Game getSelected() {
		return selected;
	}
	
	public void setSelected(Game game) {
		if (game != null) {
			if (games.containsKey(game.getKey())) {
				selected = game;
			} else {
				throw new IllegalArgumentException("game not in list");
			}
		}
	}
	
	public boolean addGame(String name, int width, int height, String dataStr) {
		int[][] data = new int[height][width];
		String[] v = dataStr.split(",");
		if (v.length != width * height) {
			throw new IllegalArgumentException("invalid array length");
		}
		int index = 0;
		for (int y=0;y<height;y++) {
			for (int x=0;x<width;x++) {
				int key = 0;
				if (v[index].indexOf('t') >= 0) {
					key |= 1;
				}
				if (v[index].indexOf('r') >= 0) {
					key |= 2;
				}
				if (v[index].indexOf('b') >= 0) {
					key |= 4;
				}
				if (v[index].indexOf('l') >= 0) {
					key |= 8;
				}
				data[y][x] = key;
				index++;
			}
		}
		Game g = new Game(name, width, height, false, data);
		games.put(g.getKey(), g);
		return true;
	}
	
	public List<Game> getGames() {
		return games.values().stream().filter(g -> !g.isEmpty()).sorted((g1, g2) -> {
			return g1.getName().compareTo(g2.getName());
		}).collect(Collectors.toList());
	}

	public void loadGames(String workingDir) {
		File f = new File(distribute.getWorkingFile(null));
		
		String[] files = f.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".game");
			}
			
		});
		for (String fname : files) {
			Properties p = new Properties();
			try {
				p.load(new FileInputStream(distribute.getWorkingFile(fname)));
				Game g = loadFromProps(p);
				updateGameInCache(g);
			} catch (Exception e) {
				throw new TubeShiftException(e.getMessage(), e);
			}
			
		}
		
	}
	
	private Game loadFromProps(Properties p) {
		String uuid = p.getProperty(UUID);
		String name = p.getProperty(NAME);
		int width = Integer.parseInt(p.getProperty(WIDTH));
		int height = Integer.parseInt(p.getProperty(HEIGHT));
		int[][] tdata = new int[width][height];
		String[] dstr = p.getProperty(DATA).split(",");
		int index = 0;
		for (int col=0;col<width;col++) {
			for (int row=0;row<height;row++) {
				tdata[row][col] = Integer.parseInt(dstr[index]);
				index++;
			}
		}
		Game game = new Game(uuid,name,width,height,false,tdata);
		String s = "";
		int val = 1;
		do {
			s = p.getProperty(SCORE + val);
			if (s != null) {
				game.addHighScore(new HighScoreEntry(s));
			}
			val++;
		} while (s != null);
		return game;
	}

	public void updateGameInCache(Game game) {
		games.put(game.getKey(), game);
	}

	public void saveGame(Game game) {
		updateGameInCache(game);
		StringBuffer sb = saveString(game);
		String fname = distribute.getWorkingFile(game.getKey() + ".game");
		File f = new File(fname);
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(sb.toString().getBytes());
			fos.close();
		} catch (Exception e) {
			throw new TubeShiftException(e.getMessage(), e);
		}
	}
	
	private StringBuffer saveString(Game game) {
		StringBuffer sb = new StringBuffer();
		sb.append(UUID).append("=").append(game.getKey()).append(CRLF);
		sb.append(NAME).append("=").append(game.getName()).append(CRLF);
		sb.append(WIDTH).append("=").append(game.getWidth()).append(CRLF);
		sb.append(HEIGHT).append("=").append(game.getHeight()).append(CRLF);
		sb.append(DATA).append("=").append(dataStr(game)).append(CRLF);
		int val = 1;
		for (HighScoreEntry hse : game.getHighScore()) {
			sb.append(SCORE + val + "=").append(hse.saveStr()).append(CRLF);
			val++;
		}
		return sb;
	}
	
	private String dataStr(Game game) {
		StringBuffer sb = new StringBuffer();
		for (int col=0;col<game.getWidth();col++) {
			for (int row=0;row<game.getHeight();row++) {
				sb.append((col > 0 || row > 0) ? "," : "").append(game.getData(col, row));
			}
		}
		return sb.toString();
	}

	public Game getGameByUUID(String guuid) {
		return games.get(guuid);
	}
}
