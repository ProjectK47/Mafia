package hyperbox.mafia.ui;

import hyperbox.mafia.audio.AudioClip;
import hyperbox.mafia.client.GameClient;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.io.AudioResources;
import hyperbox.mafia.io.ImageResources;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.net.PacketSoundEffect;

public class SoundEffectsElement extends ListElement {
	
	
	private boolean isRemotePlay = false;
	
	
	
	public SoundEffectsElement(int x, int y, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY, float scale, Game game) {
		super(x, y, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, scale, "Sound Effects", ImageResources.speakerIcon, true);

		addSoundEffects(game);
	}

	
	
	
	@Override
	public void onTick(Game game) {
		super.onTick(game);
		
		
		GameClient client = game.getGameStateManager().getGameStateInGame().getClient();
		
		
		//Remote playing////
		client.forEachReceivedPacket((Packet packet) -> {
			if(packet.getID() == PacketID.SOUND_EFFECT) {
				isRemotePlay = true;
				
				PacketSoundEffect soundEffectPacket = (PacketSoundEffect) packet;
				
				ListEntry entry = entries.get(soundEffectPacket.getSoundEffectEntry());
				entry.getOnClick().run();
				
				isRemotePlay = false;
				
				
				packet.disposePacket();
			}
		});
	}
	
	
	
	private void addSoundEffects(Game game) {
		addSoundEffect("Airplane", AudioResources.airplaneEffect, game);
		addSoundEffect("Blaster", AudioResources.blasterEffect, game);
		addSoundEffect("Bomb Drop", AudioResources.bombDropEffect, game);
		addSoundEffect("Bow and Arrow", AudioResources.bowAndArrowEffect, game);
		addSoundEffect("Car Crash", AudioResources.carCrashEffect, game);
		addSoundEffect("Car Horn", AudioResources.carHornEffect, game);
		addSoundEffect("Cat Meow", AudioResources.catMeowEffect, game);
		addSoundEffect("Dog Bark", AudioResources.dogBarkEffect, game);
		addSoundEffect("Dramatic 1", AudioResources.dramatic1Effect, game);
		addSoundEffect("Dramatic 2", AudioResources.dramatic2Effect, game);
		addSoundEffect("Explosion", AudioResources.explosionEffect, game);
		addSoundEffect("Fire", AudioResources.fireEffect, game);
		addSoundEffect("Glass Breaking", AudioResources.glassBreakingEffect, game);
		addSoundEffect("Gunshot", AudioResources.gunshotEffect, game);
		addSoundEffect("I Am Your Father", AudioResources.iAmYourFatherEffect, game);
		addSoundEffect("I Like Turtles", AudioResources.iLikeTurtlesEffect, game);
		addSoundEffect("Lightsaber", AudioResources.lightsaberEffect, game);
		addSoundEffect("Lion Roar", AudioResources.lionRoarEffect, game);
		addSoundEffect("Mac Startup", AudioResources.macStartupEffect, game);
		addSoundEffect("Papers", AudioResources.papersEffect, game);
		addSoundEffect("Rocket Launch", AudioResources.rocketLaunchEffect, game);
		addSoundEffect("Sword Clash", AudioResources.swordClashEffect, game);
		addSoundEffect("Sword Draw", AudioResources.swordDrawEffect, game);
		addSoundEffect("Tree Falling", AudioResources.treeFallingEffect, game);
		addSoundEffect("Typing", AudioResources.typingEffect, game);
		addSoundEffect("Use the Force", AudioResources.useTheForceEffect, game);
		addSoundEffect("Wilhelm Scream", AudioResources.wilhelmScreamEffect, game);
		addSoundEffect("Windows XP Startup", AudioResources.windowsXPStartupEffect, game);
		addSoundEffect("Wolf Howl", AudioResources.wolfHowlEffect, game);
		addSoundEffect("Yee", AudioResources.yeeEffect, game);
		addSoundEffect("You Shall Not Pass", AudioResources.youShallNotPassEffect, game);
	}
	
	
	
	private void addSoundEffect(String name, AudioClip audioClip, Game game) {
		ListEntry entry = new ListEntry(name, () -> {
			audioClip.playAudio();
			
			if(!isRemotePlay) {
				PacketSoundEffect soundEffectPacket = new PacketSoundEffect(selectedEntry);
				game.getGameStateManager().getGameStateInGame().getClient().sendPacket(soundEffectPacket);
			}
		});
		
		this.addEntry(entry);
	}
	
}
