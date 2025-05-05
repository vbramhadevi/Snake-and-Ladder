import javax.swing.*;
import java.util.Random;

public class GameModel implements IGameModel {
	private Player[] players;
	private int currentPlayerIndex;
	private int lastRoll;
	private boolean snakeEncountered;
	private boolean ladderEncountered;
	private final IBoardModel boardModel;
	private final Random rand;

	public GameModel(IBoardModel boardModel) {
		this.boardModel = boardModel;
		this.players = new Player[] { new Player(), new Player() };
		this.currentPlayerIndex = 0;
		this.rand = new Random();
	}

	@Override
	public void playTurn() {
		lastRoll = rand.nextInt(6) + 1;
		Player currentPlayer = players[currentPlayerIndex];
		int pos = currentPlayer.getPosition() + lastRoll;

		snakeEncountered = false;
		ladderEncountered = false;

		if (pos <= 100) {
			if (boardModel.getSnakes().containsKey(pos)) {
				pos = boardModel.getSnakes().get(pos);
				snakeEncountered = true;
			} else if (boardModel.getLadders().containsKey(pos)) {
				pos = boardModel.getLadders().get(pos);
				ladderEncountered = true;
			}
			currentPlayer.setPosition(pos);
		}

		currentPlayerIndex = (currentPlayerIndex + 1) % 2;
	}

	@Override
	public int getCurrentPlayer() {
		return currentPlayerIndex;
	}

	@Override
	public boolean isSnakeEncountered() {
		return snakeEncountered;
	}

	@Override
	public boolean isLadderEncountered() {
		return ladderEncountered;
	}

	@Override
	public int getLastRoll() {
		return lastRoll;
	}

	@Override
	public int[] getPlayerPositions() {
		return new int[] { players[0].getPosition(), players[1].getPosition() };
	}

	@Override
	public boolean checkWinner(int playerIndex) {
		return players[playerIndex].getPosition() == 100;
	}
	
	@Override
	public void reset() {
	    players = new Player[] { new Player(), new Player() };
	    currentPlayerIndex = 0;
	    lastRoll = 0;
	    snakeEncountered = false;
	    ladderEncountered = false;
	}
}