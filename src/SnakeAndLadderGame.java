import javax.swing.*;

public class SnakeAndLadderGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IBoardModel boardModel = new BoardModel();
            IBoardView boardView = new BoardView(boardModel);
            MainMenu mainMenu = new MainMenu();
            GameController controller = new GameController(null, null, boardView, boardModel, mainMenu);
        });
    }
}