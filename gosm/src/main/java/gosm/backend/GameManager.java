package gosm.backend;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GameManager {

	private Set<Game> games = new HashSet<>();
	private Game selected = null;
	
	public GameManager() {
		selected = Game.emptyGame();
		games.add(selected);
	}
	
	public Game getSelected() {
		return selected;
	}
	
	public void setSelected(Game game) {
		if (games.contains(game)) {
			selected = game;
		} else {
			throw new IllegalArgumentException("game not in list");
		}
	}
	
	public boolean addGame(String name, int width, int height, String dataStr) {
		int[][] data = new int[width][height];
		String[] v = dataStr.split(",");
		if (v.length != width * height) {
			throw new IllegalArgumentException("invalid array length");
		}
		int index = 0;
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
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
				data[x][y] = key;
				index++;
			}
		}
		games.add(new Game(name, width, height, false, data));
		return true;
	}
	
	public List<Game> getGames() {
		return games.stream().filter(g -> !g.isEmpty()).sorted((g1, g2) -> {
			return g1.getName().compareTo(g2.getName());
		}).collect(Collectors.toList());
	}
}
