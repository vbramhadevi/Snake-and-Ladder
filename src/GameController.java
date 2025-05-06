import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class GameController implements IGameController {
    private IGameModel gameModel;
    private IGameView gameView;
    private IBoardView boardView;
    private IBoardModel boardModel;
    private MainMenu mainMenu;
    private boolean isSoundOn;

    public GameController(IGameModel gameModel, IGameView gameView, IBoardView boardView, IBoardModel boardModel, MainMenu mainMenu) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.boardView = boardView;
        this.boardModel = boardModel;
        this.mainMenu = mainMenu;
        this.isSoundOn = true;
        if (mainMenu != null) {
            setupMainMenuListeners();
        }
    }

    @Override
    public void setGameView(IGameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void handleRollDice() {
        if (gameModel.isComputerTurn()) {
            gameView.enableRollButton(false);
            Timer timer = new Timer(1000, e -> {
                performPlayerTurn();
                if (!gameModel.isComputerTurn()) {
                    gameView.enableRollButton(true);
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            performPlayerTurn();
        }
    }

    private void performPlayerTurn() {
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
        if (gameModel.isComputerTurn()) {
            handleRollDice(); // Trigger computer turn
        }
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

    @Override
    public void restartGame() {
        gameModel.reset();
        int[] playerPositions = gameModel.getPlayerPositions();
        boardView.updateBoard(playerPositions);
        gameView.updatePlayerPositions(playerPositions);
        gameView.updateDice(0);
        gameView.updateStatus("Player 1's Turn");
        gameView.enableRollButton(true);
        if (gameView instanceof GameView) {
            JTextArea historyTextArea = ((GameView) gameView).getHistoryTextArea();
            historyTextArea.setText("");
            historyTextArea.append("Game restarted\n");
        }
    }

    @Override
    public void newGame() {
        if (gameView instanceof GameView) {
            ((GameView) gameView).close();
        }
        mainMenu = new MainMenu();
        setupMainMenuListeners();
    }

    private void setupMainMenuListeners() {
        mainMenu.addHumanVsHumanListener(() -> startGame(GameModel.GameMode.HUMAN_VS_HUMAN));
        mainMenu.addHumanVsComputerListener(() -> startGame(GameModel.GameMode.HUMAN_VS_COMPUTER));
    }

    private void startGame(GameModel.GameMode gameMode) {
        mainMenu.close();
        gameModel = new GameModel(boardModel, gameMode);
        gameView = new GameView(boardView, this);
        setGameView(gameView);
        gameView.addRollDiceListener(e -> handleRollDice());
        int[] initialPositions = gameModel.getPlayerPositions();
        boardView.updateBoard(initialPositions);
        gameView.updatePlayerPositions(initialPositions);
        if (gameView instanceof GameView) {
            JTextArea historyTextArea = ((GameView) gameView).getHistoryTextArea();
            historyTextArea.append("Game started (" + (gameMode == GameModel.GameMode.HUMAN_VS_HUMAN ? "Human vs Human" : "Human vs Computer") + ")\n");
            historyTextArea.append("P1: on 1\n");
            historyTextArea.append("P2: on 1\n");
        }
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