public interface IGameController {
	void handleRollDice();

	void toggleSound();

	void toggleTheme();

	void setGameView(IGameView gameView);

	int[] getPlayerPositions();
}