import javax.swing.JLabel;
import javax.swing.JPanel;

public interface IBoard {
    void initializeBoard();
    JLabel getCell(int cellNum);
    JPanel getBoardPanel();
}
