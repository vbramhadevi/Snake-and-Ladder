import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
	public static void playSound(String soundFileName) {
		try {
			URL url = SoundPlayer.class.getResource(soundFileName);
			if (url == null) {
				System.err.println("Sound file not found: " + soundFileName);
				return;
			}
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}