package hyperbox.mafia.entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.input.MouseInput;
import hyperbox.mafia.io.AudioResources;
import hyperbox.mafia.io.FontResources;
import hyperbox.mafia.io.ImageResources;
import hyperbox.mafia.net.PacketPlayerProfile;

public abstract class Player extends Entity {
	
	
	public static final float NAME_TAG_SIZE = 15f;
	public static final int NAME_TAG_HEIGHT = 20;
	
	public static final float DEAD_TRANSPARENCY = 0.4f;
	
	public static final int READY_ICON_SIZE = 25;
	public static final int READY_ICON_HEIGHT = 30;
	
	public static final float HOVER_TRANSPARENCY = 0.75f;
	
	public static final float TALLY_TEXT_SIZE = 19f;
	public static final Color TALLY_TEXT_COLOR = new Color(255, 140, 26);
	public static final int TALLY_TEXT_X_SPACE = 4;
	public static final int TALLY_TEXT_Y_SPACE = 40;

	
	protected PacketPlayerProfile profile;
	protected Color nameTagColor;
	
	protected byte animationStage = 0;
	protected byte direction = 0;
	
	protected byte aliveState = 0;
	
	protected Runnable selectRunnable = null;
	protected boolean isHovering = false;
	
	protected byte tallyCount = -1;
	
	
	
	public Player(float x, float y, PacketPlayerProfile profile, Color nameTagColor) {
		super(x, y, 70, 140,
				ImageResources.player1FrontStill, ImageResources.player1FrontWalk1, ImageResources.player1FrontWalk2,
				ImageResources.player1BackStill, ImageResources.player1BackWalk1, ImageResources.player1BackWalk2,
				ImageResources.player1RightStill, ImageResources.player1RightWalk1, ImageResources.player1RightWalk2,
				ImageResources.player1LeftStill, ImageResources.player1LeftWalk1, ImageResources.player1LeftWalk2);
		
		
		this.profile = profile;
		this.nameTagColor = nameTagColor;
	}

	
	
	
	@Override
	public void tick(Game game) {
		isHovering = false;
		
		if(selectRunnable != null) {
			int mouseX = MouseInput.grabWorldMouseX(game);
			int mouseY = MouseInput.grabWorldMouseY(game);
			
			if(mouseX >= x - (width / 2) && mouseX <= x + (width / 2))
				if(mouseY >= y - height && mouseY <= y)
					isHovering = true;
		}
		
		
		if(isHovering) {
			if(MouseInput.wasPrimaryClicked()) {
				selectRunnable.run();
				
				AudioResources.selectionClick.playAudio();
			}
		}
		
		
		
		onTick(game);
	}

	
	@Override
	public void render(Graphics2D g, Game game) {
		Composite normalComposite = null;
		
		if(!isHovering) {
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
		
		
		//Sprite////
		g.drawImage(images[animationStage + (direction * 3)], (int) x - width / 2, (int) y - height, width, height, null);
		
		
		if(isHovering)
			g.setComposite(normalComposite);
		
		
		
		//Name tag////
		g.setFont(FontResources.mainFontBold.deriveFont(NAME_TAG_SIZE));
		g.setColor(nameTagColor);
		
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
		
		
		
		if(aliveState == -1)
			g.setComposite(normalComposite);
		
		
		
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
	
	
	
	
	
	
	public void enableSelection(Runnable runnable) {
		selectRunnable = runnable;
	}
	
	public void disableSelection() {
		selectRunnable = null;
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
	
	
	
	public PacketPlayerProfile getProfile() {
		return profile;
	}
	
	
	public byte getAliveState() {
		return aliveState;
	}
	
	public void setAliveState(byte aliveState) {
		this.aliveState = aliveState;
	}
	
	
	public byte getTallyCount() {
		return tallyCount;
	}
	
}
