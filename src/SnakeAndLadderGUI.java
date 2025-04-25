import javax.swing.*;
import java.awt.*;

public class SnakeAndLadderGUI extends JFrame {
    private final IGame game;
    private final IBoard board;
    private final JButton rollDiceButton;
    private final JLabel statusLabel;
    private final JLabel diceLabel;

    public SnakeAndLadderGUI() {
        setTitle("Snake and Ladder Game");
        setSize(620, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        board = new Board();
        game = new Game(((Board) board).snakes, ((Board) board).ladders, board);

        diceLabel = new JLabel("Dice: 🎲", SwingConstants.CENTER);
        diceLabel.setFont(new Font("Dialog", Font.PLAIN, 36));

        statusLabel = new JLabel("Player 1's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));

        rollDiceButton = new JButton("🎲 Roll Dice");
        rollDiceButton.addActionListener(e -> {
            game.playTurn();
            
            int lastRoll = game.getLastRoll();
            String[] diceFaces = {"", "⚀ (1)", "⚁ (2)", "⚂ (3)", "⚃ (4)", "⚄ (5)", "⚅ (6)"};
            diceLabel.setText("Dice: " + diceFaces[lastRoll]);

            updateBoard();

            int winner = checkWinner();
            if (winner != -1) {
                JOptionPane.showMessageDialog(this, "🎉 Player " + (winner + 1) + " Wins!");
                rollDiceButton.setEnabled(false);
            }

            statusLabel.setText("Player " + (game.getCurrentPlayer() + 1) + "'s Turn");
        });

        JPanel controlPanel = new JPanel(new GridLayout(1, 3));
        controlPanel.add(statusLabel);
        controlPanel.add(diceLabel);
        controlPanel.add(rollDiceButton);

        add(controlPanel, BorderLayout.NORTH);
        add(board.getBoardPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private void updateBoard() {
        int[] positions = game.getPlayerPositions();
        for (int i = 1; i <= 100; i++) {
            JLabel cell = board.getCell(i);
            cell.setText(String.valueOf(i));
            cell.setOpaque(false);
        }

        for (int p = 0; p < 2; p++) {
            JLabel cell = board.getCell(positions[p]);
            cell.setText(cell.getText() + " P" + (p + 1));
            cell.setOpaque(true);
            cell.setBackground(p == 0 ? Color.CYAN : Color.PINK);
        }

        ((Board) board).getDrawPanel().repaint();
    }

    private int checkWinner() {
        int[] pos = game.getPlayerPositions();
        for (int i = 0; i < pos.length; i++) {
            if (pos[i] == 100) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
    	//new SnakeAndLadderGUI();
    	SwingUtilities.invokeLater(() -> new SnakeAndLadderGUI());
    }
}
