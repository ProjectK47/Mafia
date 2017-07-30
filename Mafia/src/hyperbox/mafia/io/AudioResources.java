package hyperbox.mafia.io;

import hyperbox.mafia.audio.AudioClip;

public class AudioResources {

	
	
	public static final String AUDIO_RESOURCES_PATH = "hyperbox/mafia/resources/audio/";
	public static final String SOUND_EFFECTS_PATH = AUDIO_RESOURCES_PATH + "soundEffects/";
	
	
	
	public static AudioClip buttonClick;
	public static AudioClip selectionClick;
	public static AudioClip notificationClick;
	
	public static AudioClip playerExplosion;
	public static AudioClip playerSave;
	
	public static AudioClip playerWalkOne;
	public static AudioClip playerWalkTwo;
	
	////
	
	public static AudioClip airplaneEffect;
	public static AudioClip blasterEffect;
	public static AudioClip bombDropEffect;
	public static AudioClip bowAndArrowEffect;
	public static AudioClip carCrashEffect;
	public static AudioClip carHornEffect;
	public static AudioClip catMeowEffect;
	public static AudioClip dogBarkEffect;
	public static AudioClip dramatic1Effect;
	public static AudioClip dramatic2Effect;
	public static AudioClip explosionEffect;
	public static AudioClip fireEffect;
	public static AudioClip glassBreakingEffect;
	public static AudioClip gunshotEffect;
	public static AudioClip iAmYourFatherEffect;
	public static AudioClip iLikeTurtlesEffect;
	public static AudioClip lightsaberEffect;
	public static AudioClip lionRoarEffect;
	public static AudioClip macStartupEffect;
	public static AudioClip papersEffect;
	public static AudioClip rocketLaunchEffect;
	public static AudioClip swordClashEffect;
	public static AudioClip swordDrawEffect;
	public static AudioClip treeFallingEffect;
	public static AudioClip typingEffect;
	public static AudioClip useTheForceEffect;
	public static AudioClip wilhelmScreamEffect;
	public static AudioClip windowsXPStartupEffect;
	public static AudioClip wolfHowlEffect;
	public static AudioClip yeeEffect;
	public static AudioClip youShallNotPassEffect;
	
	
	
	public static void loadResources() {
		buttonClick = new AudioClip(AUDIO_RESOURCES_PATH + "buttonClick.wav", -1);
		selectionClick = new AudioClip(AUDIO_RESOURCES_PATH + "selectionClick.wav", -2);
		notificationClick = new AudioClip(AUDIO_RESOURCES_PATH + "notificationClick.wav", -3);
		
		playerExplosion = new AudioClip(AUDIO_RESOURCES_PATH + "playerExplosion.wav", 5);
		playerSave = new AudioClip(AUDIO_RESOURCES_PATH + "playerSave.wav", -1);
		
		playerWalkOne = new AudioClip(AUDIO_RESOURCES_PATH + "playerWalkOne.wav", -9);
		playerWalkTwo = new AudioClip(AUDIO_RESOURCES_PATH + "playerWalkTwo.wav", -9);
		
		////
		
		airplaneEffect = new AudioClip(SOUND_EFFECTS_PATH + "airplane.wav", 1, false);
		blasterEffect = new AudioClip(SOUND_EFFECTS_PATH + "blaster.wav", 0, false);
		bombDropEffect = new AudioClip(SOUND_EFFECTS_PATH + "bombDrop.wav", 4, false);
		bowAndArrowEffect = new AudioClip(SOUND_EFFECTS_PATH + "bowAndArrow.wav", 0, false);
		carCrashEffect = new AudioClip(SOUND_EFFECTS_PATH + "carCrash.wav", 0, false);
		carHornEffect = new AudioClip(SOUND_EFFECTS_PATH + "carHorn.wav", 0, false);
		catMeowEffect = new AudioClip(SOUND_EFFECTS_PATH + "catMeow.wav", 0, false);
		dogBarkEffect = new AudioClip(SOUND_EFFECTS_PATH + "dogBark.wav", 0, false);
		dramatic1Effect = new AudioClip(SOUND_EFFECTS_PATH + "dramatic1.wav", -1, false);
		dramatic2Effect = new AudioClip(SOUND_EFFECTS_PATH + "dramatic2.wav", -1, false);
		explosionEffect = new AudioClip(SOUND_EFFECTS_PATH + "explosion.wav", 0, false);
		fireEffect = new AudioClip(SOUND_EFFECTS_PATH + "fire.wav", -2, false);
		glassBreakingEffect = new AudioClip(SOUND_EFFECTS_PATH + "glassBreaking.wav", 0, false);
		gunshotEffect = new AudioClip(SOUND_EFFECTS_PATH + "gunshot.wav", -5, false);
		iAmYourFatherEffect = new AudioClip(SOUND_EFFECTS_PATH + "iAmYourFather.wav", 5, false);
		iLikeTurtlesEffect = new AudioClip(SOUND_EFFECTS_PATH + "iLikeTurtles.wav", 6, false);
		lightsaberEffect = new AudioClip(SOUND_EFFECTS_PATH + "lightsaber.wav", -1, false);
		lionRoarEffect = new AudioClip(SOUND_EFFECTS_PATH + "lionRoar.wav", 0, false);
		macStartupEffect = new AudioClip(SOUND_EFFECTS_PATH + "macStartup.wav", 2, false);
		papersEffect = new AudioClip(SOUND_EFFECTS_PATH + "papers.wav", 3, false);
		rocketLaunchEffect = new AudioClip(SOUND_EFFECTS_PATH + "rocketLaunch.wav", -2, false);
		swordClashEffect = new AudioClip(SOUND_EFFECTS_PATH + "swordClash.wav", 2, false);
		swordDrawEffect = new AudioClip(SOUND_EFFECTS_PATH + "swordDraw.wav", 6, false);
		treeFallingEffect = new AudioClip(SOUND_EFFECTS_PATH + "treeFalling.wav", -1, false);
		typingEffect = new AudioClip(SOUND_EFFECTS_PATH + "typing.wav", 5, false);
		useTheForceEffect = new AudioClip(SOUND_EFFECTS_PATH + "useTheForce.wav", 5, false);
		wilhelmScreamEffect = new AudioClip(SOUND_EFFECTS_PATH + "wilhelmScream.wav", 0, false);
		windowsXPStartupEffect = new AudioClip(SOUND_EFFECTS_PATH + "windowsXPStartup.wav", 2, false);
		wolfHowlEffect = new AudioClip(SOUND_EFFECTS_PATH + "wolfHowl.wav", 5, false);
		yeeEffect = new AudioClip(SOUND_EFFECTS_PATH + "yee.wav", -4, false);
		youShallNotPassEffect = new AudioClip(SOUND_EFFECTS_PATH + "youShallNotPass.wav", 3, false);
	}
	
}
