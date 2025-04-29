import javax.swing.*;

public interface IBoardView {
	JPanel getBoardPanel();

	void updateBoard(int[] playerPositions);

	JLabel getCell(int cellNum);

	void updateTheme(boolean darkMode, java.awt.Color bgColor, java.awt.Color fgColor);
}