import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeAndLadderGUI extends JFrame {
	private final IGame game;
	private final IBoard board;
	private final JButton rollDiceButton;
	private final JLabel statusLabel;
	private final JLabel diceLabel;

	private boolean isSoundOn = true;
	private boolean isDarkMode = false;

	private JLabel soundIconLabel;
	private JLabel themeIconLabel;

	private JPanel controlPanel;
	private JPanel soundThemePanel;

	public SnakeAndLadderGUI() {
		setTitle("Snake and Ladder Game");
		setSize(900, 750);
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
			String[] diceFaces = { "", "⚀ (1)", "⚁ (2)", "⚂ (3)", "⚃ (4)", "⚄ (5)", "⚅ (6)" };
			diceLabel.setText("Dice: " + diceFaces[lastRoll]);
			updateBoard();

			if (game.isSnakeEncountered()) {
				int[] positions = game.getPlayerPositions();
				animateCell(board.getCell(positions[(game.getCurrentPlayer() + 1) % 2]), Color.RED);
				if (isSoundOn) {
					SoundPlayer.playSound("/snake.wav");
				}
			} else if (game.isLadderEncountered()) {
				int[] positions = game.getPlayerPositions();
				animateCell(board.getCell(positions[(game.getCurrentPlayer() + 1) % 2]), Color.GREEN);
				if (isSoundOn) {
					SoundPlayer.playSound("/snake.wav");
				}
			}

			int winner = checkWinner();
			if (winner != -1) {
				JOptionPane.showMessageDialog(this, "🎉 Player " + (winner + 1) + " Wins!");
				rollDiceButton.setEnabled(false);
			}

			statusLabel.setText("Player " + (game.getCurrentPlayer() + 1) + "'s Turn");
		});

		controlPanel = new JPanel(new GridLayout(1, 3));
		controlPanel.add(statusLabel);
		controlPanel.add(diceLabel);
		controlPanel.add(rollDiceButton);

		soundThemePanel = new JPanel(new GridLayout(2, 1, 10, 10));
		soundThemePanel.setPreferredSize(new Dimension(100, 600));

		soundIconLabel = new JLabel("🔊", SwingConstants.CENTER);
		soundIconLabel.setFont(new Font("Dialog", Font.PLAIN, 50));
		soundIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		soundThemePanel.add(soundIconLabel);
		soundIconLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				toggleSound();
				animateIcon(soundIconLabel);
			}
		});

		themeIconLabel = new JLabel("🌞", SwingConstants.CENTER);
		themeIconLabel.setFont(new Font("Dialog", Font.PLAIN, 50));
		themeIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		soundThemePanel.add(themeIconLabel);
		themeIconLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				toggleTheme();
				animateIcon(themeIconLabel);
			}
		});

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(board.getBoardPanel(), BorderLayout.CENTER);
		centerPanel.add(soundThemePanel, BorderLayout.EAST);

		add(controlPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);

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
			if (pos[i] == 100)
				return i;
		}
		return -1;
	}

	private void animateCell(JLabel cell, Color flashColor) {
		Color original = cell.getBackground();
		Timer timer = new Timer(100, null);
		final int[] count = { 0 };
		timer.addActionListener(e -> {
			if (count[0] % 2 == 0) {
				cell.setBackground(flashColor);
			} else {
				cell.setBackground(original);
			}
			cell.setOpaque(true);
			cell.repaint();
			count[0]++;
			if (count[0] >= 6) {
				timer.stop();
			}
		});
		timer.start();
	}

	private void toggleSound() {
		isSoundOn = !isSoundOn;
		soundIconLabel.setText(isSoundOn ? "🔊" : "🔇");
	}

	private void toggleTheme() {
		boolean oldIsDarkMode = isDarkMode;
		isDarkMode = !isDarkMode;

		Color startBgColor = oldIsDarkMode ? Color.BLACK : Color.WHITE;
		Color endBgColor = isDarkMode ? Color.BLACK : Color.WHITE;

		Color startPanelBg = oldIsDarkMode ? new Color(40, 40, 40) : new Color(230, 230, 230);
		Color endPanelBg = isDarkMode ? new Color(40, 40, 40) : new Color(230, 230, 230);

		Color startCellColor = oldIsDarkMode ? new Color(30, 30, 30) : Color.WHITE;
		Color endCellColor = isDarkMode ? new Color(30, 30, 30) : Color.WHITE;

		Color startFgColor = oldIsDarkMode ? Color.WHITE : Color.BLACK;
		Color endFgColor = isDarkMode ? Color.WHITE : Color.BLACK;

		Timer timer = new Timer(20, null);
		final int steps = 20;
		final int[] currentStep = { 0 };

		timer.addActionListener(e -> {
			float ratio = (float) currentStep[0] / steps;

			// Interpolate colors
			Color bgColor = interpolateColor(startBgColor, endBgColor, ratio);
			Color panelBgColor = interpolateColor(startPanelBg, endPanelBg, ratio);
			Color cellBgColor = interpolateColor(startCellColor, endCellColor, ratio);
			Color fgColor = interpolateColor(startFgColor, endFgColor, ratio);

			getContentPane().setBackground(bgColor);
			board.getBoardPanel().setBackground(bgColor);

			controlPanel.setBackground(panelBgColor);
			soundThemePanel.setBackground(panelBgColor);

			statusLabel.setForeground(fgColor);
			diceLabel.setForeground(fgColor);
			rollDiceButton.setForeground(fgColor);
			rollDiceButton.setBackground(isDarkMode ? Color.DARK_GRAY : UIManager.getColor("Button.background"));

			soundIconLabel.setForeground(fgColor);
			themeIconLabel.setForeground(fgColor);

			for (int i = 1; i <= 100; i++) {
				JLabel cell = board.getCell(i);
				cell.setBackground(cellBgColor);
				cell.setForeground(fgColor);
				cell.setBorder(BorderFactory.createLineBorder(fgColor));
			}

			currentStep[0]++;
			if (currentStep[0] > steps) {
				timer.stop();
				themeIconLabel.setText(isDarkMode ? "🌙" : "🌞");
				updateBoard();
			}
		});

		timer.start();
	}

	private Color interpolateColor(Color start, Color end, float ratio) {
		int red = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
		int green = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
		int blue = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));
		return new Color(red, green, blue);
	}

	private void animateIcon(JLabel iconLabel) {
		Timer timer = new Timer(20, null);
		final float[] scale = { 1.0f };
		timer.addActionListener(new ActionListener() {
			private boolean enlarging = true;
			private int steps = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (enlarging) {
					scale[0] += 0.05f;
				} else {
					scale[0] -= 0.05f;
				}
				steps++;
				iconLabel.setFont(iconLabel.getFont().deriveFont(50f * scale[0]));
				if (steps == 4) {
					enlarging = false;
				}
				if (steps == 8) {
					iconLabel.setFont(new Font("Dialog", Font.PLAIN, 50));
					timer.stop();
				}
			}
		});
		timer.start();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(SnakeAndLadderGUI::new);
	}
}
