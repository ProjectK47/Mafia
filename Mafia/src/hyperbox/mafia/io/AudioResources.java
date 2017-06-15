package hyperbox.mafia.io;

import hyperbox.mafia.audio.AudioClip;

public class AudioResources {

	
	
	public static final String AUDIO_RESOURCES_PATH = "hyperbox/mafia/resources/audio/";
	
	
	
	public static AudioClip buttonClick;
	public static AudioClip selectionClick;
	
	
	
	public static void loadResources() {
		buttonClick = new AudioClip(AUDIO_RESOURCES_PATH + "buttonClick.wav", -1);
		selectionClick = new AudioClip(AUDIO_RESOURCES_PATH + "selectionClick.wav", -2);
	}
	
}
