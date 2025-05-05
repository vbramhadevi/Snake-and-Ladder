import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameView implements IGameView {
	private JFrame frame;
	private JPanel controlPanel;
	private JPanel middlePanel;
	private JPanel rightPanel;
	private JPanel playerInfoPanel;
	private JPanel soundThemePanel;
	private JPanel historyPanel;
	private JLabel statusLabel;
	private JLabel diceLabel;
	private JButton rollDiceButton;
	private JLabel soundIconLabel;
	private JLabel themeIconLabel;
	private JLabel player1Label;
	private JLabel player2Label;
	private JTextArea historyTextArea;
	private boolean isSoundOn;
	private boolean isDarkMode;
	private IBoardView boardView;
	private IGameController controller;

	public GameView(IBoardView boardView, IGameController controller) {
		this.boardView = boardView;
		this.controller = controller;
		isSoundOn = true;
		isDarkMode = false;

		frame = new JFrame("Snake and Ladder Game");
		frame.setSize(1150, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		statusLabel = new JLabel("Player 1's Turn", SwingConstants.CENTER);
		statusLabel.setFont(new Font("Arial", Font.BOLD, 18));

		diceLabel = new JLabel("Dice: 🎲", SwingConstants.CENTER);
		diceLabel.setFont(new Font("Dialog", Font.PLAIN, 36));

		rollDiceButton = new JButton("🎲 Roll Dice");

		controlPanel = new JPanel(new GridLayout(1, 3));
		controlPanel.add(statusLabel);
		controlPanel.add(diceLabel);
		controlPanel.add(rollDiceButton);

		// Board
		frame.add(boardView.getBoardPanel(), BorderLayout.WEST);

		// Player info and history
		middlePanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		playerInfoPanel = new JPanel(new GridLayout(1, 2));
		playerInfoPanel.setPreferredSize(new Dimension(300, 50));
		player1Label = new JLabel("<html>P1: <font color=\"blue\">🚶</font> on 1</html>", SwingConstants.CENTER);
		player1Label.setFont(new Font("Arial", Font.PLAIN, 25));
		player2Label = new JLabel("<html>P2: <font color=\"red\">🚶</font> on 1</html>", SwingConstants.CENTER);
		player2Label.setFont(new Font("Arial", Font.PLAIN, 25));
		playerInfoPanel.add(player1Label);
		playerInfoPanel.add(player2Label);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0.1;
		middlePanel.add(playerInfoPanel, gbc);

		// History panel
		historyTextArea = new JTextArea(15, 25);
		historyTextArea.setEditable(false);
		historyTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane historyScrollPane = new JScrollPane(historyTextArea);
		historyPanel = new JPanel(new BorderLayout());
		historyPanel.add(historyScrollPane, BorderLayout.CENTER);
		gbc.gridy = 1;
		gbc.weighty = 0.9;
		gbc.fill = GridBagConstraints.BOTH;
		middlePanel.add(historyPanel, gbc);

		frame.add(middlePanel, BorderLayout.CENTER);

		// Right panel for sound/theme
		rightPanel = new JPanel(new BorderLayout());
		rightPanel.setPreferredSize(new Dimension(100, 600));

		soundThemePanel = new JPanel(new GridLayout(2, 1, 10, 10));
		soundIconLabel = new JLabel("🔊", SwingConstants.CENTER);
		soundIconLabel.setFont(new Font("Dialog", Font.PLAIN, 50));
		soundIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		soundIconLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.toggleSound();
				animateIcon(soundIconLabel);
			}
		});
		soundThemePanel.add(soundIconLabel);

		themeIconLabel = new JLabel("🌞", SwingConstants.CENTER);
		themeIconLabel.setFont(new Font("Dialog", Font.PLAIN, 50));
		themeIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		themeIconLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.toggleTheme();
				animateIcon(themeIconLabel);
			}
		});
		soundThemePanel.add(themeIconLabel);
		rightPanel.add(soundThemePanel, BorderLayout.NORTH);

		frame.add(rightPanel, BorderLayout.EAST);
		frame.add(controlPanel, BorderLayout.NORTH);

		frame.setVisible(true);
	}

	@Override
	public void updateStatus(String text) {
		statusLabel.setText(text);
	}

	@Override
	public void updateDice(int roll) {
		String[] diceFaces = { "", "⚀ (1)", "⚁ (2)", "⚂ (3)", "⚃ (4)", "⚄ (5)", "⚅ (6)" };
		diceLabel.setText("Dice: " + diceFaces[roll]);
	}

	@Override
	public void enableRollButton(boolean enabled) {
		rollDiceButton.setEnabled(enabled);
	}

	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(frame, message);
	}

	@Override
	public void animateCell(JLabel cell, Color color) {
		String originalText = cell.getText();
		String cellNumber = originalText.contains("<html>") ? originalText.replaceAll("<html>(\\d+).*", "$1")
				: originalText;
		int cellNum = Integer.parseInt(cellNumber);
		Color originalFg = cell.getForeground();
		Timer timer = new Timer(100, null);
		final int[] count = { 0 };
		timer.addActionListener(e -> {
			String iconText = originalText.contains("<html>") ? originalText.replaceFirst("<html>\\d+", "") : "";
			if (count[0] % 2 == 0) {
				cell.setText("<html><font color=\"" + toHex(color) + "\">" + cellNum + "</font>" + iconText);
			} else {
				cell.setText("<html><font color=\"" + toHex(originalFg) + "\">" + cellNum + "</font>" + iconText);
			}
			cell.repaint();
			count[0]++;
			if (count[0] >= 6) {
				timer.stop();
				cell.setText(originalText);
			}
		});
		timer.start();
	}

	@Override
	public void updateSoundIcon(boolean soundOn) {
		isSoundOn = soundOn;
		soundIconLabel.setText(soundOn ? "🔊" : "🔇");
	}

	@Override
	public void updateTheme(boolean darkMode) {
		isDarkMode = darkMode;
		Color startBgColor = !darkMode ? Color.BLACK : Color.WHITE;
		Color endBgColor = darkMode ? Color.BLACK : Color.WHITE;

		Color startPanelBg = !darkMode ? new Color(40, 40, 40) : new Color(230, 230, 230);
		Color endPanelBg = darkMode ? new Color(40, 40, 40) : new Color(230, 230, 230);

		Color startFgColor = !darkMode ? Color.WHITE : Color.BLACK;
		Color endFgColor = darkMode ? Color.WHITE : Color.BLACK;

		Timer timer = new Timer(20, null);
		final int steps = 20;
		final int[] currentStep = { 0 };

		timer.addActionListener(e -> {
			float ratio = (float) currentStep[0] / steps;

			Color bgColor = interpolateColor(startBgColor, endBgColor, ratio);
			Color panelBgColor = interpolateColor(startPanelBg, endPanelBg, ratio);
			Color fgColor = interpolateColor(startFgColor, endFgColor, ratio);

			frame.getContentPane().setBackground(bgColor);
			controlPanel.setBackground(panelBgColor);
			middlePanel.setBackground(panelBgColor);
			rightPanel.setBackground(panelBgColor);
			playerInfoPanel.setBackground(panelBgColor);
			soundThemePanel.setBackground(panelBgColor);
			historyPanel.setBackground(panelBgColor);

			statusLabel.setForeground(fgColor);
			diceLabel.setForeground(fgColor);
			rollDiceButton.setForeground(fgColor);
			rollDiceButton.setBackground(darkMode ? Color.DARK_GRAY : UIManager.getColor("Button.background"));

			player1Label.setForeground(fgColor);
			player2Label.setForeground(fgColor);
			soundIconLabel.setForeground(fgColor);
			themeIconLabel.setForeground(fgColor);
			historyTextArea.setForeground(fgColor);
			historyTextArea.setBackground(darkMode ? new Color(50, 50, 50) : Color.WHITE);

			boardView.updateTheme(darkMode, bgColor, fgColor);

			currentStep[0]++;
			if (currentStep[0] > steps) {
				timer.stop();
				themeIconLabel.setText(darkMode ? "🌙" : "🌞");
				boardView.updateBoard(controller.getPlayerPositions());
			}
		});

		timer.start();
	}

	@Override
	public void addRollDiceListener(ActionListener listener) {
		rollDiceButton.addActionListener(listener);
	}

	@Override
	public boolean isDarkMode() {
		return isDarkMode;
	}

	@Override
	public void updatePlayerPositions(int[] playerPositions) {
		player1Label.setText("<html>P1: <font color=\"blue\">🚶</font> on " + playerPositions[0] + "</html>");
		player2Label.setText("<html>P2: <font color=\"red\">🚶</font> on " + playerPositions[1] + "</html>");
	}

	public JTextArea getHistoryTextArea() {
		return historyTextArea;
	}

	private Color interpolateColor(Color start, Color end, float ratio) {
		int red = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
		int green = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
		int blue = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));
		return new Color(red, green, blue);
	}

	private String toHex(Color color) {
		return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
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
}