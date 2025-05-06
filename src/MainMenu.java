import javax.swing.*;
import java.awt.*;

public class MainMenu {
	private JFrame frame;
	private JButton humanVsHumanButton;
	private JButton humanVsComputerButton;

	public MainMenu() {
		frame = new JFrame("Snake and Ladder - Select Mode");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout(10, 10));

		frame.getContentPane().setBackground(new Color(173, 216, 230));

		JLabel titleLabel = new JLabel("Snake and Ladder", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(Color.BLACK);
		frame.add(titleLabel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		buttonPanel.setBackground(new Color(173, 216, 230));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		humanVsHumanButton = new JButton("👨👨 Human vs Human");
        humanVsComputerButton = new JButton("👨💾 Human vs Computer");
        
//        humanVsHumanButton = new JButton("Human vs Human", new ImageIcon("resources/human_vs_human.png"));
//        humanVsComputerButton = new JButton("Human vs Computer", new ImageIcon("resources/human_vs_computer.png"));
        
		humanVsHumanButton.setFont(new Font("Arial", Font.PLAIN, 18));
		humanVsComputerButton.setFont(new Font("Arial", Font.PLAIN, 18));
		humanVsHumanButton.setHorizontalAlignment(SwingConstants.CENTER);
		humanVsComputerButton.setHorizontalAlignment(SwingConstants.CENTER);

		buttonPanel.add(humanVsHumanButton);
		buttonPanel.add(humanVsComputerButton);

		frame.add(buttonPanel, BorderLayout.CENTER);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void addHumanVsHumanListener(Runnable listener) {
		humanVsHumanButton.addActionListener(e -> listener.run());
	}

	public void addHumanVsComputerListener(Runnable listener) {
		humanVsComputerButton.addActionListener(e -> listener.run());
	}

	public void close() {
		frame.dispose();
	}
}