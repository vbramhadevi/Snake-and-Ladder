import javax.swing.*;
import java.awt.*;

public class BoardView implements IBoardView {
	private JPanel boardPanel;
	private BoardRenderer renderer;
	private IBoardModel boardModel;
	private static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);
	private static final Font PLAYER_FONT = new Font("Dialog", Font.PLAIN, 16);

	public BoardView(IBoardModel boardModel) {
		this.boardModel = boardModel;
		initializeBoardPanel();
	}

	private void initializeBoardPanel() {
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
				JLabel cell = boardModel.getCell(val);
				cell.setBounds(col * 60, row * 60, 60, 60);
				cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				cell.setFont(DEFAULT_FONT);
				cell.setHorizontalAlignment(SwingConstants.CENTER);
				boardPanel.add(cell);
			}
			leftToRight = !leftToRight;
		}

		renderer = new BoardRenderer(boardModel);
		boardPanel.add(renderer);
	}

	@Override
	public JPanel getBoardPanel() {
		return boardPanel;
	}

	@Override
	public void updateBoard(int[] playerPositions) {
		for (int i = 1; i <= 100; i++) {
			JLabel cell = boardModel.getCell(i);
			cell.setText(String.valueOf(i));
			cell.setFont(DEFAULT_FONT);
		}
		if (playerPositions[0] == playerPositions[1] && playerPositions[0] != 0) {
			JLabel cell = boardModel.getCell(playerPositions[0]);
			cell.setText("<html>" + playerPositions[0]
					+ " <font color=\"blue\">🚶</font><font color=\"red\">🚶</font></html>");
			cell.setFont(PLAYER_FONT);
		} else {
			for (int p = 0; p < 2; p++) {
				if (playerPositions[p] != 0) {
					JLabel cell = boardModel.getCell(playerPositions[p]);
					cell.setText("<html>" + playerPositions[p]
							+ (p == 0 ? " <font color=\"blue\">🚶</font>" : " <font color=\"red\">🚶</font>")
							+ "</html>");
					cell.setFont(PLAYER_FONT);
				}
			}
		}
		renderer.repaint();
	}

	@Override
	public JLabel getCell(int cellNum) {
		return boardModel.getCell(cellNum);
	}

	@Override
	public void updateTheme(boolean darkMode, Color bgColor, Color fgColor) {
		boardPanel.setBackground(bgColor);
		for (int i = 1; i <= 100; i++) {
			JLabel cell = boardModel.getCell(i);
			cell.setBackground(bgColor);
			cell.setForeground(fgColor);
			cell.setBorder(BorderFactory.createLineBorder(fgColor));
		}
	}
}