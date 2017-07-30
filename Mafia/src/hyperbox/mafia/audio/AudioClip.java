package hyperbox.mafia.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioClip {
	
	
	private String filePath;
	private float volumeAdjustment;
	
	private boolean shouldPreloadAudio;
	
	
	private Clip clip;
	private FloatControl volume;
	
	
	
	public AudioClip(String filePath, float volumeAdjustment) {
		this.filePath = filePath;
		this.volumeAdjustment = volumeAdjustment;
		
		this.shouldPreloadAudio = true;
		
		
		loadClip();
	}
	
	
	public AudioClip(String filePath, float volumeAdjustment, boolean shouldPreloadAudio) {
		this.filePath = filePath;
		this.volumeAdjustment = volumeAdjustment;
		
		this.shouldPreloadAudio = shouldPreloadAudio;
		
		
		if(shouldPreloadAudio)
			loadClip();
	}

	
	
	
	private void loadClip() {
		try {
			clip = AudioSystem.getClip();
			
			InputStream in = getClass().getClassLoader().getResourceAsStream(filePath);
			BufferedInputStream bufferedIn = new BufferedInputStream(in);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
			
			clip.open(audioIn);
			
			
			volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	
	
	
	public void playAudio() {
		playAudio(0);
	}
	
	
	public void playAudio(float volumeDelta) {
		boolean isPlaying = false;
		
		if(clip != null)
			if(clip.isActive())
				isPlaying = true;
		
		
		if(!shouldPreloadAudio && !isPlaying) {
			loadClip();
			
			clip.addLineListener((LineEvent event) -> {
				if(event.getType() == LineEvent.Type.STOP)
					clip.close();
			});
		}
		
		
		float newVolume = Math.max(Math.min(volumeAdjustment + volumeDelta, volume.getMaximum()), volume.getMinimum());
		volume.setValue(newVolume);
		
		clip.setFramePosition(0);
		clip.start();
	}


	
	
	public String getFilePath() {
		return filePath;
	}


	public float getVolumeAdjustment() {
		return volumeAdjustment;
	}


	public boolean isShouldPreloadAudio() {
		return shouldPreloadAudio;
	}
	
}
