package hyperbox.mafia.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioClip {
	
	
	private Clip clip;
	
	
	
	public AudioClip(String filePath, float volumeAdjustment) {
		
		try {
			clip = AudioSystem.getClip();
			
			InputStream in = getClass().getClassLoader().getResourceAsStream(filePath);
			BufferedInputStream bufferedIn = new BufferedInputStream(in);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
			
			clip.open(audioIn);
			
			
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(volumeAdjustment);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}	
		
	}
	
	

	
	
	public void playAudio() {
		clip.setFramePosition(0);
		clip.start();
	}
	
	
}
