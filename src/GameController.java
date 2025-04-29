import java.awt.Color;

import javax.swing.JTextArea;

public class GameController implements IGameController {
	private IGameModel gameModel;
	private IGameView gameView;
	private IBoardView boardView;
	private boolean isSoundOn;

	public GameController(IGameModel gameModel, IGameView gameView, IBoardView boardView) {
		this.gameModel = gameModel;
		this.gameView = gameView;
		this.boardView = boardView;
		this.isSoundOn = true;
	}

	@Override
	public void setGameView(IGameView gameView) {
		this.gameView = gameView;
	}

	@Override
	public void handleRollDice() {
		int currentPlayer = gameModel.getCurrentPlayer();
		int previousPosition = gameModel.getPlayerPositions()[currentPlayer];
		gameModel.playTurn();
		int lastRoll = gameModel.getLastRoll();
		int newPosition = gameModel.getPlayerPositions()[currentPlayer];

		// Log combined dice roll and movement
		String historyEntry = "Player " + (currentPlayer + 1) + " rolled " + lastRoll + " and moved to " + newPosition;
		appendHistory(historyEntry);

		gameView.updateDice(lastRoll);
		int[] playerPositions = gameModel.getPlayerPositions();
		boardView.updateBoard(playerPositions);
		gameView.updatePlayerPositions(playerPositions);

		if (gameModel.isSnakeEncountered()) {
			historyEntry = "Player " + (currentPlayer + 1) + " hit a snake, moved to " + newPosition;
			appendHistory(historyEntry);
			gameView.showMessage("Player " + (currentPlayer + 1) + " bitten by snake!");
			gameView.animateCell(boardView.getCell(newPosition), Color.RED);
			if (isSoundOn) {
				SoundPlayer.playSound("/snake.wav");
			}
		} else if (gameModel.isLadderEncountered()) {
			historyEntry = "Player " + (currentPlayer + 1) + " climbed a ladder, moved to " + newPosition;
			appendHistory(historyEntry);
			gameView.showMessage("Player " + (currentPlayer + 1) + " climbed a ladder!");
			gameView.animateCell(boardView.getCell(newPosition), Color.GREEN);
			if (isSoundOn) {
				SoundPlayer.playSound("/ladder.wav");
			}
		}

		for (int i = 0; i < 2; i++) {
			if (gameModel.checkWinner(i)) {
				historyEntry = "Player " + (i + 1) + " wins!";
				appendHistory(historyEntry);
				gameView.showMessage("🎉 Player " + (i + 1) + " Wins!");
				gameView.enableRollButton(false);
				return;
			}
		}

		gameView.updateStatus("Player " + (gameModel.getCurrentPlayer() + 1) + "'s Turn");
	}

	@Override
	public void toggleSound() {
		isSoundOn = !isSoundOn;
		gameView.updateSoundIcon(isSoundOn);
		appendHistory("Sound " + (isSoundOn ? "enabled" : "disabled"));
	}

	@Override
	public void toggleTheme() {
		gameView.updateTheme(!gameView.isDarkMode());
		appendHistory("Theme switched to " + (gameView.isDarkMode() ? "dark" : "light") + " mode");
	}

	@Override
	public int[] getPlayerPositions() {
		return gameModel.getPlayerPositions();
	}

	private void appendHistory(String entry) {
		if (gameView instanceof GameView) {
			JTextArea historyTextArea = ((GameView) gameView).getHistoryTextArea();
			if (historyTextArea != null) {
				historyTextArea.append(entry + "\n");
				historyTextArea.setCaretPosition(historyTextArea.getDocument().getLength());
			}
		}
	}

	public JTextArea getHistoryTextArea() {
		return ((GameView) gameView).getHistoryTextArea();
	}
}