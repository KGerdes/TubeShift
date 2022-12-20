package gosm.backend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GameManager {

	private Map<String, Game> games = new HashMap<>();
	private Game selected = null;
	
	public GameManager() {
		selected = Game.emptyGame();
		games.put(selected.getKey(), selected);
	}
	
	public Game getSelected() {
		return selected;
	}
	
	public void setSelected(Game game) {
		if (games.containsKey(game.getKey())) {
			selected = game;
		} else {
			throw new IllegalArgumentException("game not in list");
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
}
