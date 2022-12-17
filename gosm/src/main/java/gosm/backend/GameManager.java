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
	
	public List<Game> getGames() {
		return games.stream().filter(g -> !g.isEmpty()).sorted((g1, g2) -> {
			return g1.getName().compareTo(g2.getName());
		}).collect(Collectors.toList());
	}
}
