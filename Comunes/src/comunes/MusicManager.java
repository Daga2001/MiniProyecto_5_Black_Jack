/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 *          David Alberto Guzman Ardila - 1942789
 *          Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */

package comunes;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

// TODO: Auto-generated Javadoc
/**
 * The following class was made to manage every sound effect or music for any project.
 *
 * @author David, Miguel, Diego
 */
public class MusicManager {
	
	/** The audio in. */
	private AudioInputStream audioIn;
	
	/** The background music. */
	private Clip cardFlip, earnedCash, lostCash, goodBye, bell, backgroundMusic;
	
	/** The sounds. */
	private ArrayList<Clip> sounds;
	
	/**
	 * Instantiates a new music manager.
	 */
	public MusicManager(){
		
		sounds = new ArrayList<Clip>();
	
		//Import Audio
		
		try {
			audioIn = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("cardFlip.wav"));
			cardFlip = AudioSystem.getClip();
			cardFlip.open(audioIn);
			sounds.add(cardFlip);
			
			audioIn = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("earnedMoney.wav"));
			earnedCash = AudioSystem.getClip();
			earnedCash.open(audioIn);
			sounds.add(earnedCash);
			
			audioIn = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("fail.wav"));
			lostCash = AudioSystem.getClip();
			lostCash.open(audioIn);
			sounds.add(lostCash);
			
			audioIn = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("goodBye.wav"));
			goodBye = AudioSystem.getClip();
			goodBye.open(audioIn);
			sounds.add(goodBye);
			
			audioIn = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("bell.wav"));
			bell = AudioSystem.getClip();
			bell.open(audioIn);
			sounds.add(bell);
			
			audioIn = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("pokerMusic.wav"));
			backgroundMusic = AudioSystem.getClip();
			backgroundMusic.open(audioIn);
			
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Audio file not found!", "Error!", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	/**
	 * Plays music according to the given index.
	 *
	 * @param index the index
	 */
	public void playMusic(int index) {
		sounds.get(index).setFramePosition(0);
		sounds.get(index).start();
	}
	
	/**
	 * Stops music according to the given index.
	 *
	 * @param index the index
	 */
	public void stopMusic(int index) {
		sounds.get(index).stop();
	}
	
	/**
	 * Starts the background music of the game.
	 */
	public void startBackgroundMusic() {
		backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	/**
	 * Stops the background music of the game.
	 */
	public void stopBackgroundMusic() {
		backgroundMusic.stop();
	}
	
}
