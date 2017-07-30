package hyperbox.mafia.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import hyperbox.mafia.client.GameClient;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.input.MouseInput;
import hyperbox.mafia.io.AudioResources;
import hyperbox.mafia.io.FontResources;
import hyperbox.mafia.io.ImageResources;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.net.PacketSoundEffect;

public class SoundEffectsElement extends UIElement {
	
	
	public static final float LIST_TEXT_SIZE = 2.5f;
	public static final float LIST_ENTRY_INITIAL_X_SPACE = 3.6f;
	public static final float LIST_ENTRY_INITIAL_Y_SPACE = 6f;
	public static final float LIST_ENTRY_Y_SPACE = 3.6f;
	
	public static final Color LIST_TEXT_EVEN_COLOR = new Color(255, 255, 204);
	public static final Color LIST_TEXT_ODD_COLOR = new Color(227, 227, 184);
	public static final Color LIST_TEXT_SELECTED_COLOR = new Color(51, 214, 255);
	
	public static final float LIST_SPEAKER_SCALE = 0.175f;
	public static final float LIST_SPEAKER_X_SPACE = 6.75f;
	
	public static final int LIST_MAX_ENTRIES = 7;
	
	public static final float SCROLL_BUTTON_SCALE = 0.25f;
	
	
	public static final String LABEL_TEXT = "Sound Effects";
	public static final float LABEL_TEXT_SIZE = 2.5f;
	public static final Color LABEL_TEXT_COLOR = new Color(255, 255, 204);
	public static final float LABEL_TEXT_X_SPACE = 1.5f;
	public static final float LABEL_TEXT_Y_SPACE = 1f;
	
	
	public static final float SCROLL_INDICATOR_X_SPACE = 0.8f;
	public static final float SCROLL_INDICATOR_Y_SPACE = 2.5f;
	public static final float SCROLL_INDICATOR_WIDTH = 2f;
	
	public static final Color SCROLL_INDICATOR_COLOR = new Color(237, 237, 190);
	
	
	
	private float scale;
	private boolean isDisabled;
	
	
	private ImageElement boxElement;
	
	private ButtonElement scrollDownButton;
	private ButtonElement scrollUpButton;
	
	
	private ArrayList<SoundEffect> soundEffects = new ArrayList<SoundEffect>();
	private int entryHeight;
	
	private int startEntry = 0;
	private int selectedEntry = -1;
	
	
	
	public SoundEffectsElement(int x, int y, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY, float scale, boolean isDisabled) {
		super(x, y, 0, 0, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, 1);
		
		this.scale = scale;
		
		
		boxElement = new ImageElement(x, y, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, ImageResources.listBox, scale);
		
		scrollDownButton = new ButtonElement((int) (10.5f * scale), (int) (18.5 * scale), UIAnchor.PARENT, UIAnchor.PARENT, UIAnchor.CENTER, UIAnchor.CENTER, 
				SCROLL_BUTTON_SCALE * scale, "\\/", () -> {
					
					scrollList(1);
		});
		
		scrollDownButton.setParent(boxElement);
		
		
		scrollUpButton = new ButtonElement((int) (10.5f * scale), (int) -(18.5 * scale), UIAnchor.PARENT, UIAnchor.PARENT, UIAnchor.CENTER, UIAnchor.CENTER, 
				SCROLL_BUTTON_SCALE * scale, "/\\", () -> {
					
					scrollList(-1);
		});
		
		scrollUpButton.setParent(boxElement);
		
		
		setIsDisabled(isDisabled);
		
		
		addSoundEffects();
	}

	
	
	
	@Override
	public void onTick(Game game) {
		if(width == 0 || height == 0) {
			width = boxElement.getWidth();
			height = boxElement.getHeight();
		}
		
		
		
		GameClient client = game.getGameStateManager().getGameStateInGame().getClient();
		
		
		//Remote playing////
		client.forEachReceivedPacket((Packet packet) -> {
			if(packet.getID() == PacketID.SOUND_EFFECT) {
				PacketSoundEffect soundEffectPacket = (PacketSoundEffect) packet;
				
				SoundEffect soundEffect = soundEffects.get(soundEffectPacket.getSoundEffectEntry());
				soundEffect.getAudioClip().playAudio();
				
				
				packet.disposePacket();
			}
		});
		
		
		
		if(isDisabled)
			return;
		
		
		
		scrollDownButton.tick(game);
		scrollUpButton.tick(game);
		
		
		
		int mouseX = MouseInput.grabScreenMouseX(game);
		int mouseY = MouseInput.grabScreenMouseY(game);
		
		
		//Select entry////
		selectedEntry = -1;
		
		for(int i = 0; i < LIST_MAX_ENTRIES; i ++) {
			int logicI = i + startEntry;
			
			if(logicI >= soundEffects.size() || logicI < 0)
				continue;
			
			
			int entryX = (int) (this.grabAnchoredX(game) - (width / 2) + LIST_ENTRY_INITIAL_X_SPACE * scale);
			int entryY = (int) (this.grabAnchoredY(game) - (height / 2) + (LIST_ENTRY_INITIAL_Y_SPACE * scale) - entryHeight + (LIST_ENTRY_Y_SPACE * i * scale));
			
			if(mouseX >= entryX && mouseX <= entryX + width - (int) (LIST_ENTRY_INITIAL_X_SPACE * scale * 2))
				if(mouseY >= entryY && mouseY <= entryY + entryHeight)
					selectedEntry = logicI;
		}
		
			
		
		//Scrolling////
		if(mouseX >= this.grabAnchoredX(game) - (width / 2) && mouseX <= this.grabAnchoredX(game) + (width / 2))
			if(mouseY >= this.grabAnchoredY(game) - (height / 2) && mouseY <= this.grabAnchoredY(game) + (height / 2))
				scrollList(MouseInput.getAmountScrolled());
		
		
		
		
		//Local playing////
		if(selectedEntry != -1)
			if(MouseInput.wasPrimaryClicked(this.mousePriority)) {
				soundEffects.get(selectedEntry).getAudioClip().playAudio();
				
				PacketSoundEffect soundEffectPacket = new PacketSoundEffect(selectedEntry);
				client.sendPacket(soundEffectPacket);
			}
	}

	
	
	@Override
	public void render(Graphics2D g, Game game) {
		if(isDisabled)
			return;
		
		
		boxElement.render(g, game);
		
		scrollDownButton.render(g, game);
		scrollUpButton.render(g, game);
		
		
		
		//List text////
		g.setFont(FontResources.mainFontBold.deriveFont(LIST_TEXT_SIZE * scale));
		entryHeight = g.getFontMetrics().getHeight();
		
		
		for(int i = 0; i < soundEffects.size(); i ++) {
			
			if(i < startEntry)
				continue;
			
			
			int renderI = i - startEntry;
			
			if(renderI >= LIST_MAX_ENTRIES)
				break;
			
			
			SoundEffect soundEffect = soundEffects.get(i);
			
			
			if(i != selectedEntry) {
				if(i % 2 == 0)
					g.setColor(LIST_TEXT_EVEN_COLOR);
				else
					g.setColor(LIST_TEXT_ODD_COLOR);
				
			} else
				g.setColor(LIST_TEXT_SELECTED_COLOR);
			
			
			g.drawString(soundEffect.getName(), this.grabAnchoredX(game) - (width / 2) + (LIST_ENTRY_INITIAL_X_SPACE * scale), 
					this.grabAnchoredY(game) - (height / 2) + (LIST_ENTRY_INITIAL_Y_SPACE * scale) + (LIST_ENTRY_Y_SPACE * renderI * scale));
			
			
			
			//Speaker icon////
			if(i == selectedEntry) {
				int speakerWidth = (int) (ImageResources.speakerIcon.getWidth() * LIST_SPEAKER_SCALE * scale);
				int speakerHeight = (int) (ImageResources.speakerIcon.getHeight() * LIST_SPEAKER_SCALE * scale);
				
				g.drawImage(ImageResources.speakerIcon, (int) (this.grabAnchoredX(game) + (width / 2) - (LIST_SPEAKER_X_SPACE * scale)),
						MouseInput.grabScreenMouseY(game) - (speakerHeight / 2), speakerWidth, speakerHeight, null);
			}
		}
		
		
		
		//Label////
		g.setFont(FontResources.mainFontBold.deriveFont(LABEL_TEXT_SIZE * scale));
		g.setColor(LABEL_TEXT_COLOR);
		
		g.drawString(LABEL_TEXT, this.grabAnchoredX(game) - (width / 2) + (LABEL_TEXT_X_SPACE * scale), this.grabAnchoredY(game) - (height / 2) - (LABEL_TEXT_Y_SPACE * scale));
		
		
		
		
		//Scroll indicator////
		g.setColor(SCROLL_INDICATOR_COLOR);
		
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke(SCROLL_INDICATOR_WIDTH));
		
		
		int maxStartValue = soundEffects.size() - LIST_MAX_ENTRIES;
		float indicatorScale = (float) startEntry / maxStartValue;
		
		if(indicatorScale > 0) {
			int indicatorHeight = (int) (height - (SCROLL_INDICATOR_Y_SPACE * scale));
			
			g.drawLine((int) (this.grabAnchoredX(game) + (width / 2) + (SCROLL_INDICATOR_X_SPACE * scale)), this.grabAnchoredY(game) - (indicatorHeight / 2),
					(int) (this.grabAnchoredX(game) + (width / 2) + (SCROLL_INDICATOR_X_SPACE * scale)), 
					(int) (this.grabAnchoredY(game) - (indicatorHeight / 2) + (indicatorHeight * indicatorScale)));
		}
		
		
		g.setStroke(oldStroke);
	}

	
	
	
	
	private void addSoundEffects() {
		soundEffects.add(new SoundEffect("Airplane", AudioResources.airplaneEffect));
		soundEffects.add(new SoundEffect("Blaster", AudioResources.blasterEffect));
		soundEffects.add(new SoundEffect("Bomb Drop", AudioResources.bombDropEffect));
		soundEffects.add(new SoundEffect("Bow and Arrow", AudioResources.bowAndArrowEffect));
		soundEffects.add(new SoundEffect("Car Crash", AudioResources.carCrashEffect));
		soundEffects.add(new SoundEffect("Car Horn", AudioResources.carHornEffect));
		soundEffects.add(new SoundEffect("Cat Meow", AudioResources.catMeowEffect));
		soundEffects.add(new SoundEffect("Dog Bark", AudioResources.dogBarkEffect));
		soundEffects.add(new SoundEffect("Dramatic 1", AudioResources.dramatic1Effect));
		soundEffects.add(new SoundEffect("Dramatic 2", AudioResources.dramatic2Effect));
		soundEffects.add(new SoundEffect("Explosion", AudioResources.explosionEffect));
		soundEffects.add(new SoundEffect("Fire", AudioResources.fireEffect));
		soundEffects.add(new SoundEffect("Glass Breaking", AudioResources.glassBreakingEffect));
		soundEffects.add(new SoundEffect("Gunshot", AudioResources.gunshotEffect));
		soundEffects.add(new SoundEffect("I Am Your Father", AudioResources.iAmYourFatherEffect));
		soundEffects.add(new SoundEffect("I Like Turtles", AudioResources.iLikeTurtlesEffect));
		soundEffects.add(new SoundEffect("Lightsaber", AudioResources.lightsaberEffect));
		soundEffects.add(new SoundEffect("Lion Roar", AudioResources.lionRoarEffect));
		soundEffects.add(new SoundEffect("Mac Startup", AudioResources.macStartupEffect));
		soundEffects.add(new SoundEffect("Papers", AudioResources.papersEffect));
		soundEffects.add(new SoundEffect("Rocket Launch", AudioResources.rocketLaunchEffect));
		soundEffects.add(new SoundEffect("Sword Clash", AudioResources.swordClashEffect));
		soundEffects.add(new SoundEffect("Sword Draw", AudioResources.swordDrawEffect));
		soundEffects.add(new SoundEffect("Tree Falling", AudioResources.treeFallingEffect));
		soundEffects.add(new SoundEffect("Typing", AudioResources.typingEffect));
		soundEffects.add(new SoundEffect("Use the Force", AudioResources.useTheForceEffect));
		soundEffects.add(new SoundEffect("Wilhelm Scream", AudioResources.wilhelmScreamEffect));
		soundEffects.add(new SoundEffect("Windows XP Startup", AudioResources.windowsXPStartupEffect));
		soundEffects.add(new SoundEffect("Wolf Howl", AudioResources.wolfHowlEffect));
		soundEffects.add(new SoundEffect("Yee", AudioResources.yeeEffect));
		soundEffects.add(new SoundEffect("You Shall Not Pass", AudioResources.youShallNotPassEffect));
	}
	
	
	
	private void scrollList(int amountToScroll) {
		if(startEntry + amountToScroll < 0)
			startEntry = 0;
		else if(soundEffects.size() - startEntry - amountToScroll < LIST_MAX_ENTRIES)
			startEntry = soundEffects.size() - LIST_MAX_ENTRIES;
		else
			startEntry += amountToScroll;
	}
	
	
	
	
	
	public float getScale() {
		return scale;
	}
	
	
	public boolean isDisabled() {
		return isDisabled;
	}
	
	public void setIsDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
		
		
		if(isDisabled) {
			this.shouldAllowClickThrough = true;
			
			scrollDownButton.setIsHidden(true);
			scrollUpButton.setIsHidden(true);
			
		} else {
			this.shouldAllowClickThrough = false;
			
			scrollDownButton.setIsHidden(false);
			scrollUpButton.setIsHidden(false);
		}
	}
	
}
