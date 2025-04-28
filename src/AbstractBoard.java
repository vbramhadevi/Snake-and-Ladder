import javax.swing.*;
import java.util.Map;

public abstract class AbstractBoard implements IBoard {
	protected JLabel[] cells = new JLabel[101];
	protected DrawPanel drawPanel;
	protected Map<Integer, Integer> snakes;
	protected Map<Integer, Integer> ladders;

	@Override
	public JLabel getCell(int cellNum) {
		return cells[cellNum];
	}

	public DrawPanel getDrawPanel() {
		return drawPanel;
	}
}
