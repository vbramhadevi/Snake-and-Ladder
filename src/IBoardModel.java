import javax.swing.*;
import java.util.Map;

public interface IBoardModel {
	void initializeBoard();

	JLabel getCell(int cellNum);

	Map<Integer, Integer> getSnakes();

	Map<Integer, Integer> getLadders();
}