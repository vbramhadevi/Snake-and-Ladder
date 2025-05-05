public interface IGameController {
	void handleRollDice();

	void toggleSound();

	void toggleTheme();
	
	void restartGame();
	
	void newGame();

	void setGameView(IGameView gameView);

	int[] getPlayerPositions();
}