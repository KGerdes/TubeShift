package gosm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import gosm.backend.Game;
import gosm.backend.HighScoreEntry;
import org.junit.Assert;

public class HighScoreEntryTest {

	
	@Test
	public void sortTest01() {
		List<HighScoreEntry> list = new ArrayList<>();
		list.add(new HighScoreEntry("B", 100, 100, 100).setDoneTime(1));
		list.add(new HighScoreEntry("B", 100, 100, 100).setDoneTime(0));
		list.add(new HighScoreEntry("A", 100, 100, 100).setDoneTime(0));
		list.add(new HighScoreEntry("A", 100, 100, 99).setDoneTime(0));
		list.add(new HighScoreEntry("A", 100, 99, 100).setDoneTime(0));
		list.add(new HighScoreEntry("A", 101, 100, 100).setDoneTime(0));
		Collections.sort(list);
		Assert.assertEquals("[A;101;100;100;0, A;100;99;100;0, A;100;100;99;0, A;100;100;100;0, B;100;100;100;0, B;100;100;100;1]", list.toString());
	}
	
	@Test
	public void rankTest01() {
		Game g = new Game("test", 4, 4, new int[4][4]);
		for (int i=0;i<=10;i++) {
			g.addHighScore(new HighScoreEntry("A", 100 - i * 2, 100, 100).setDoneTime(0));
		}
		Assert.assertEquals(4, g.ranked(new HighScoreEntry("A", 85, 100, 100).setDoneTime(0)));
	}
}
