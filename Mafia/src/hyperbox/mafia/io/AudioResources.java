package hyperbox.mafia.io;

import hyperbox.mafia.audio.AudioClip;

public class AudioResources {

	
	
	public static final String AUDIO_RESOURCES_PATH = "hyperbox/mafia/resources/audio/";
	
	
	
	public static AudioClip buttonClick;
	
	
	
	public static void loadResources() {
		buttonClick = new AudioClip(AUDIO_RESOURCES_PATH + "buttonClick.wav", -1);
	}
	
}
