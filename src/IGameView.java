import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JLabel;

public interface IGameView {
	void updateStatus(String text);

	void updateDice(int roll);

	void enableRollButton(boolean enabled);

	void showMessage(String message);

	void animateCell(JLabel cell, Color color);

	void updateSoundIcon(boolean soundOn);

	void updateTheme(boolean darkMode);

	void addRollDiceListener(ActionListener listener);

	boolean isDarkMode();

	void updatePlayerPositions(int[] playerPositions);
}