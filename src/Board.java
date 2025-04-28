import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Board extends AbstractBoard {
	private JPanel boardPanel;

	public Board() {
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
		boardPanel = new JPanel(null);
		boardPanel.setPreferredSize(new Dimension(600, 600));
		boardPanel.setBackground(Color.WHITE);

		boolean leftToRight = true;
		int num = 100;
		for (int row = 0; row < 10; row++) {
			int[] rowNumbers = new int[10];
			for (int col = 0; col < 10; col++) {
				rowNumbers[col] = num--;
			}
			if (!leftToRight) {
				for (int i = 0; i < 5; i++) {
					int tmp = rowNumbers[i];
					rowNumbers[i] = rowNumbers[9 - i];
					rowNumbers[9 - i] = tmp;
				}
			}
			for (int col = 0; col < 10; col++) {
				int val = rowNumbers[col];
				JLabel cell = new JLabel(String.valueOf(val), SwingConstants.CENTER);
				cell.setBounds(col * 60, row * 60, 60, 60);
				cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				cells[val] = cell;
				boardPanel.add(cell);
			}
			leftToRight = !leftToRight;
		}

		drawPanel = new DrawPanel(snakes, ladders);
		boardPanel.add(drawPanel);
	}

	@Override
	public JPanel getBoardPanel() {
		return boardPanel;
	}
}
