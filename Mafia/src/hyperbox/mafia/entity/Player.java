package hyperbox.mafia.entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import hyperbox.mafia.animation.Pointer;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.input.KeyboardInput;
import hyperbox.mafia.input.MouseInput;
import hyperbox.mafia.io.AudioResources;
import hyperbox.mafia.io.FontResources;
import hyperbox.mafia.io.ImageResources;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.net.PacketPlayerProfile;
import hyperbox.mafia.net.PacketPlayerStateAction;
import hyperbox.mafia.particle.FloatRange;
import hyperbox.mafia.particle.IntRange;
import hyperbox.mafia.particle.ParticleSystem;
import hyperbox.mafia.ui.ChatMessage;
import hyperbox.mafia.utils.NumberUtils;

public abstract class Player extends Entity {
	
	
	public static final float NAME_TAG_SIZE = 15f;
	public static final int NAME_TAG_HEIGHT = 20;
	
	public static final float DEAD_TRANSPARENCY = 0.4f;
	
	public static final int READY_ICON_SIZE = 25;
	public static final int READY_ICON_HEIGHT = 30;
	
	public static final float SLEEPING_ICON_SCALE = 2f;
	public static final int SLEEPING_ICON_HEIGHT_GAP = 15;
	public static final float SLEEPING_ICON_WAVE_SCALE = 5.5f;
	public static final float SLEEPING_ICON_WAVE_SPEED = 6f;
	
	public static final float HOVER_TRANSPARENCY = 0.75f;
	public static final int SELECT_POINTER_SECONDARY_DISTANCE_LIMIT = 175;
	
	
	public static final float STATE_ACTION_HOVER_TEXT_SIZE = 12f;
	
	public static final String EXPLODE_HOVER_TEXT = "Press 'X' to kill ";
	public static final int EXPLODE_HOVER_TEXT_HEIGHT_SPACE = 10;
	public static final Color EXPLODE_HOVER_TEXT_COLOR = new Color(255, 173, 153);
	
	public static final String SAVE_HOVER_TEXT = "Press 'Y' to save ";
	public static final int SAVE_HOVER_TEXT_HEIGHT_SPACE = -10;
	public static final Color SAVE_HOVER_TEXT_COLOR = new Color(153, 255, 204);
	
	
	public static final float TALLY_TEXT_SIZE = 19f;
	public static final Color TALLY_TEXT_COLOR = new Color(255, 140, 26);
	public static final int TALLY_TEXT_X_SPACE = 4;
	public static final int TALLY_TEXT_Y_SPACE = 40;
	
	public static final ParticleSystem EXPLODE_PARTICLE_SYSTEM = new ParticleSystem(
			new FloatRange(-1f, 1f), new FloatRange(-1f, -0.75f), 
			new FloatRange(2f, 20f),
			new IntRange(1, 10),
			new IntRange(10, 30),
			new IntRange(0, 5), new IntRange(200, 300));
	
	public static final ParticleSystem SAVED_PARTICLE_SYSTEM = new ParticleSystem(
			new FloatRange(-1f, 1f), new FloatRange(-1f, 1f), 
			new FloatRange(1f, 2f),
			new IntRange(1, 4),
			new IntRange(15, 60),
			new IntRange(0, 35), new IntRange(40, 50));

	
	protected PacketPlayerProfile profile;
	protected Color nameTagColor;
	
	protected byte animationStage = 0;
	protected byte direction = 0;
	
	protected byte aliveState;
	
	protected boolean isSleeping;
	protected float sleepingIconWaveX = 0f;
	
	protected ArrayList<Pointer> pointers = new ArrayList<Pointer>();
	
	protected boolean isHovering = false;
	
	protected Runnable selectRunnable;
	protected boolean isSelectPrimary;
	
	protected boolean areStateActionsAllowed;
	
	protected byte tallyCount;
	
	protected Color specialNameTagColor;
	
	
	
	public Player(float x, float y, PacketPlayerProfile profile, Color nameTagColor) {
		super(x, y, 70, 140,
				ImageResources.player1FrontStill, ImageResources.player1FrontWalk1, ImageResources.player1FrontWalk2,
				ImageResources.player1BackStill, ImageResources.player1BackWalk1, ImageResources.player1BackWalk2,
				ImageResources.player1RightStill, ImageResources.player1RightWalk1, ImageResources.player1RightWalk2,
				ImageResources.player1LeftStill, ImageResources.player1LeftWalk1, ImageResources.player1LeftWalk2);
		
		
		this.profile = profile;
		this.nameTagColor = nameTagColor;
		
		
		resetMetadata();
	}

	
	
	
	public void resetMetadata() {
		aliveState = 0;
		
		isSleeping = false;
		
		selectRunnable = null;
		isSelectPrimary = true;
		
		areStateActionsAllowed = false;
		
		tallyCount = -1;
		
		specialNameTagColor = null;
	}
	
	
	
	
	@Override
	public void tick(Game game) {
		
		//Sleeping icon////
		sleepingIconWaveX += SLEEPING_ICON_WAVE_SPEED;
		
		
		
		//Pointers////
		Iterator<Pointer> pointersIt = pointers.iterator();
		
		while(pointersIt.hasNext()) {
			Pointer pointer = pointersIt.next();
			
			
			pointer.tick(game);
			
			if(pointer.hasFinished())
				pointersIt.remove();
		}
		
		
		
		int mouseX = MouseInput.grabWorldMouseX(game);
		int mouseY = MouseInput.grabWorldMouseY(game);
		
		
		//Hover////
		isHovering = false;
		
		if(mouseX >= x - (width / 2) && mouseX <= x + (width / 2))
			if(mouseY >= y - height && mouseY <= y)
				isHovering = true;
		
		
		
		//Selection////
		if(isHovering && selectRunnable != null) {
			boolean shouldRun = false;
			
			
			if(isSelectPrimary) {
				if(MouseInput.wasPrimaryClicked(false))
					shouldRun = true;
				
			} else {
				PlayerLocal localPlayer = game.getGameStateManager().getGameStateInGame().getPlayer();
				
				if(MouseInput.wasSecondaryClicked(false))
					if(NumberUtils.distance(localPlayer.getX(), localPlayer.getY() - (localPlayer.getHeight() / 2), 
							mouseX, mouseY) <= SELECT_POINTER_SECONDARY_DISTANCE_LIMIT) {
						
						shouldRun = true;
					}
			}
			
			
			if(shouldRun) {
				selectRunnable.run();
				AudioResources.selectionClick.playAudio();
			}
		}
		
		
		
		//State actions////
		if(isHovering && areStateActionsAllowed) {
			if(KeyboardInput.wasKeyTyped(KeyEvent.VK_X, false))
				explodeToSpectator(game, true);
			
			
			if(KeyboardInput.wasKeyTyped(KeyEvent.VK_Y, false))
				showSavedSequence(game, true);
		}
		
		
		
		game.getGameStateManager().getGameStateInGame().getClient().forEachReceivedPacket((Packet packet) -> {
			if(packet.getID() == PacketID.PLAYER_STATE_ACTION) {
				
				PacketPlayerStateAction actionPacket = (PacketPlayerStateAction) packet;
				
				
				if(actionPacket.getUsername().equals(profile.getUsername())) {
					if(actionPacket.isExplodeAction())
						explodeToSpectator(game, false);
					else
						showSavedSequence(game, false);
					
					
					packet.disposePacket();
				}
			}
		});
		
		
		
		onTick(game);
	}

	
	@Override
	public void render(Graphics2D g, Game game) {
		Composite normalComposite = null;
		
		if(!isHovering || selectRunnable == null) {
			if(aliveState == -1) {
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DEAD_TRANSPARENCY);
				
				normalComposite = g.getComposite();
				g.setComposite(ac);
			}
			
		} else {
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HOVER_TRANSPARENCY);
			
			normalComposite = g.getComposite();
			g.setComposite(ac);
		}
		
		
		//Pointers////
		for(Pointer pointer : pointers)
			pointer.render(g, game);
		
		
		
		//Sprite////
		g.drawImage(images[animationStage + (direction * 3)], (int) x - width / 2, (int) y - height, width, height, null);
		
		
		if(isHovering && selectRunnable != null)
			g.setComposite(normalComposite);
		
		
		
		//Name tag////
		g.setFont(FontResources.mainFontBold.deriveFont(NAME_TAG_SIZE));
		
		if(specialNameTagColor == null)
			g.setColor(nameTagColor);
		else
			g.setColor(specialNameTagColor);
		
		String username = profile.getUsername();
		int tagWidth = g.getFontMetrics().stringWidth(username);
		
		g.drawString(profile.getUsername(), (int) x - tagWidth / 2, (int) y + NAME_TAG_HEIGHT);
		
		
		
		
		//Tally////
		if(tallyCount >= 0) {
			g.setFont(FontResources.mainFontBold.deriveFont(TALLY_TEXT_SIZE));
			g.setColor(TALLY_TEXT_COLOR);
			
			String tally = Byte.toString(tallyCount);
			int tallyWidth = g.getFontMetrics().stringWidth(tally);
			
			g.drawString(tally, (int) x - (width / 2) - tallyWidth - TALLY_TEXT_X_SPACE, (int) y - height + TALLY_TEXT_Y_SPACE);
		}
		
		
		
		
		//Sleep icon////
		if(isSleeping) {
			BufferedImage sleepIcon = ImageResources.sleepIcon;
			float sineWave = NumberUtils.sineWave(sleepingIconWaveX, SLEEPING_ICON_WAVE_SCALE);
			
			g.drawImage(sleepIcon, (int) (x - sleepIcon.getWidth() / 2 * SLEEPING_ICON_SCALE), (int) (y - height - SLEEPING_ICON_HEIGHT_GAP + sineWave),
					(int) (sleepIcon.getWidth() * SLEEPING_ICON_SCALE), (int) (sleepIcon.getHeight() * SLEEPING_ICON_SCALE), null);
		}
		
		
		
		
		if(aliveState == -1)
			g.setComposite(normalComposite);
		
		
		
		//State actions hover text////
		if(isHovering && areStateActionsAllowed) {
			g.setFont(FontResources.mainFontBold.deriveFont(STATE_ACTION_HOVER_TEXT_SIZE));
			
			
			g.setColor(EXPLODE_HOVER_TEXT_COLOR);
			
			String explodeTextString = EXPLODE_HOVER_TEXT + profile.getUsername();
			int explodeTextWidth = g.getFontMetrics().stringWidth(explodeTextString);
			
			g.drawString(explodeTextString, (int) x - explodeTextWidth / 2, (int) y - height - EXPLODE_HOVER_TEXT_HEIGHT_SPACE);
			
			
			g.setColor(SAVE_HOVER_TEXT_COLOR);
			
			String saveTextString = SAVE_HOVER_TEXT + profile.getUsername();
			int saveTextWidth = g.getFontMetrics().stringWidth(saveTextString);
			
			g.drawString(saveTextString, (int) x - saveTextWidth / 2, (int) y - height - SAVE_HOVER_TEXT_HEIGHT_SPACE);
		}
		
		
		
		onRender(g, game);
	}

	
	
	protected abstract void onTick(Game game);
	protected abstract void onRender(Graphics2D g, Game game);
	
	
	
	public void renderReadyIcon(Graphics2D g, Game game) {
		BufferedImage icon = null;
		
		if(aliveState == 0)
			icon = ImageResources.notReadyIcon;
		else if(aliveState == 1)
			icon = ImageResources.readyIcon;
		
		
		if(icon != null)
			g.drawImage(icon, (int) x - (READY_ICON_SIZE / 2), (int) y + READY_ICON_HEIGHT, READY_ICON_SIZE, READY_ICON_SIZE, null);
	}
	
	
	
	
	
	public boolean isPointOnPlayer(float pointX, float pointY) {
		if(pointX >= x - (width / 2) && pointX <= x + (width / 2))
			if(pointY >= y - height && pointY <= y)
				return true;
		
		
		return false;
	}
	
	
	
	
	
	
	
	public void explodeToSpectator(Game game) {
		explodeToSpectator(game, true);
	}
	
	
	private void explodeToSpectator(Game game, boolean shouldSendPacket) {
		aliveState = -1;
		areStateActionsAllowed = false;
		
		
		game.getCamera().applyCameraShake(2.5f, 0.075f);
		AudioResources.playerExplosion.playAudio();
		EXPLODE_PARTICLE_SYSTEM.emitParticles(x, y, game,
				new Color(240, 0, 0), new Color(255, 64, 0), new Color(255, 153, 0), new Color(89, 89, 89));
		
		ChatMessage deadMessage = new ChatMessage("Game", profile.getUsername() + " is now dead (a spectator)!", true);
		game.getGameStateManager().getGameStateInGame().getChatElement().addMessage(deadMessage, false, game);
		
		
		if(shouldSendPacket) {
			PacketPlayerStateAction actionPacket = new PacketPlayerStateAction(profile.getUsername(), true);
			game.getGameStateManager().getGameStateInGame().getClient().sendPacket(actionPacket);
		}
	}
	
	
	
	private void showSavedSequence(Game game, boolean shouldSendPacket) {
		game.getCamera().applyCameraShake(1.2f, 0.09f);
		AudioResources.playerSave.playAudio();
		SAVED_PARTICLE_SYSTEM.emitParticles(x, y - (height / 2), game,
				new Color(51, 255, 51), new Color(145, 255, 145), new Color(0, 150, 0), new Color(230, 184, 0));
		
		ChatMessage savedMessage = new ChatMessage("Game", profile.getUsername() + " nearly died, but was saved!", true);
		game.getGameStateManager().getGameStateInGame().getChatElement().addMessage(savedMessage, false, game);
		
		
		if(shouldSendPacket) {
			PacketPlayerStateAction actionPacket = new PacketPlayerStateAction(profile.getUsername(), false);
			game.getGameStateManager().getGameStateInGame().getClient().sendPacket(actionPacket);
		}
	}
	
	
	
	
	protected void spawnPointer(float targetX, float targetY, boolean isPrimaryPointer, Game game) {
		Pointer pointer = new Pointer(x, y - (height / 2), targetX, targetY, isPrimaryPointer);
		
		pointers.add(pointer);
	}
	
	
	
	
	public void enableSelection(Runnable runnable, boolean isPrimary) {
		selectRunnable = runnable;
		isSelectPrimary = isPrimary;
	}
	
	public void disableSelection() {
		selectRunnable = null;
		isSelectPrimary = true;
	}
	
	
	public void incrementTally(Game game) {
		tallyCount ++;
	}
	
	public void decrementTally(Game game) {
		tallyCount --;
	}
	
	public void resetTally(Game game) {
		tallyCount = 0;
	}
	
	public void disableTally(Game game) {
		tallyCount = -1;
	}
	
	
	public void setSpecialNameTagColor(Color specialNameTagColor) {
		this.specialNameTagColor = specialNameTagColor;
	}
	
	public void removeSpecialNameTagColor() {
		specialNameTagColor = null;
	}
	
	
	
	public PacketPlayerProfile getProfile() {
		return profile;
	}
	
	
	public byte getAliveState() {
		return aliveState;
	}
	
	public void setAliveState(byte aliveState) {
		this.aliveState = aliveState;
	}
	
	
	public boolean isSleeping() {
		return isSleeping;
	}
	
	
	public boolean areStateActionsAllowed() {
		return areStateActionsAllowed;
	}
	
	public void setAreStateActionsAllowed(boolean areStateActionsAllowed) {
		this.areStateActionsAllowed = areStateActionsAllowed;
	}
	
	
	public byte getTallyCount() {
		return tallyCount;
	}
	
}
