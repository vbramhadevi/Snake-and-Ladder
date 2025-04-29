import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class BoardModel implements IBoardModel {
	private JLabel[] cells = new JLabel[101];
	private Map<Integer, Integer> snakes;
	private Map<Integer, Integer> ladders;

	public BoardModel() {
		snakes = new HashMap<>();
		ladders = new HashMap<>();
		initializeSnakesAndLadders();
		initializeBoard();
	}

	private void initializeSnakesAndLadders() {
		snakes.put(99, 54);
		snakes.put(70, 55);
		snakes.put(52, 42);
		snakes.put(25, 2);
		snakes.put(95, 72);

		ladders.put(6, 25);
		ladders.put(11, 40);
		ladders.put(60, 85);
		ladders.put(46, 90);
		ladders.put(17, 69);
	}

	@Override
	public void initializeBoard() {
		for (int i = 1; i <= 100; i++) {
			cells[i] = new JLabel(String.valueOf(i), SwingConstants.CENTER);
		}
	}

	@Override
	public JLabel getCell(int cellNum) {
		return cells[cellNum];
	}

	@Override
	public Map<Integer, Integer> getSnakes() {
		return snakes;
	}

	@Override
	public Map<Integer, Integer> getLadders() {
		return ladders;
	}
}