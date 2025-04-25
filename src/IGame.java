public interface IGame {
    void playTurn();
    int getCurrentPlayer();
    int getLastRoll();
    int[] getPlayerPositions();
}
