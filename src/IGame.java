public interface IGame {
	void playTurn();

	int getCurrentPlayer();

	boolean isSnakeEncountered();

	boolean isLadderEncountered();

	int getLastRoll();

	int[] getPlayerPositions();
}
