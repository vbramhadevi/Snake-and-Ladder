public interface IGameModel {
	void playTurn();

	int getCurrentPlayer();

	boolean isSnakeEncountered();

	boolean isLadderEncountered();

	int getLastRoll();

	int[] getPlayerPositions();

	boolean checkWinner(int playerIndex);
	
	void reset();

	boolean isComputerTurn();
}