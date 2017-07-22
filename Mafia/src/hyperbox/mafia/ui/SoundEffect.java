package hyperbox.mafia.ui;

import hyperbox.mafia.audio.AudioClip;

public class SoundEffect {
	
	
	private String name;
	private AudioClip audioClip;
	
	
	
	public SoundEffect(String name, AudioClip audioClip) {
		this.name = name;
		this.audioClip = audioClip;
	}
	
	
	
	
	public String getName() {
		return name;
	}
	
	
	public AudioClip getAudioClip() {
		return audioClip;
	}

}
