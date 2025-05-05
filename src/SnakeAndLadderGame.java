import javax.swing.*;

public class SnakeAndLadderGame {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			IBoardModel boardModel = new BoardModel();
			IBoardView boardView = new BoardView(boardModel);
			IGameModel gameModel = new GameModel(boardModel);
			IGameController controller = new GameController(gameModel, null, boardView, boardModel);
			IGameView gameView = new GameView(boardView, controller);
			controller.setGameView(gameView);
			gameView.addRollDiceListener(e -> controller.handleRollDice());
			int[] initialPositions = controller.getPlayerPositions();
			boardView.updateBoard(initialPositions);
			gameView.updatePlayerPositions(initialPositions);
			JTextArea historyTextArea = ((GameController) controller).getHistoryTextArea();
			if (historyTextArea != null) {
				historyTextArea.append("Game started\n");
				historyTextArea.append("P1: on 1\n");
				historyTextArea.append("P2: on 1\n");
			}
		});
	}
}