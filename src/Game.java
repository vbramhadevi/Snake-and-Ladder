import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

public class Game implements IGame {
    private int[] playerPositions = {1, 1};
    private int currentPlayer = 0;
    private int lastRoll = 0;
    private final Map<Integer, Integer> snakes;
    private final Map<Integer, Integer> ladders;
    private final Random rand = new Random();
    private final IBoard board;

    private boolean snakeEncountered = false;
    private boolean ladderEncountered = false;

    public Game(Map<Integer, Integer> snakes, Map<Integer, Integer> ladders, IBoard board) {
        this.snakes = snakes;
        this.ladders = ladders;
        this.board = board;
    }

    public void playTurn() {
        lastRoll = rand.nextInt(6) + 1;
        int pos = playerPositions[currentPlayer] + lastRoll;

        snakeEncountered = false;
        ladderEncountered = false;

        if (pos > 100) {
            // skip move
        } else {
            if (snakes.containsKey(pos)) {
                JOptionPane.showMessageDialog(null, "Player " + (currentPlayer + 1) + " bitten by snake!");
                pos = snakes.get(pos);
                snakeEncountered = true;
            } else if (ladders.containsKey(pos)) {
                JOptionPane.showMessageDialog(null, "Player " + (currentPlayer + 1) + " climbed a ladder!");
                pos = ladders.get(pos);
                ladderEncountered = true;
            }
            playerPositions[currentPlayer] = pos;
        }

        currentPlayer = (currentPlayer + 1) % 2;
    }

    public boolean isSnakeEncountered() {
        return snakeEncountered;
    }

    public boolean isLadderEncountered() {
        return ladderEncountered;
    }

    public int getLastRoll() {
        return lastRoll;
    }

    public int[] getPlayerPositions() {
        return playerPositions;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }
}
