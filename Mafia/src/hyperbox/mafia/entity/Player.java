package hyperbox.mafia.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.io.FontResources;
import hyperbox.mafia.io.ImageResources;
import hyperbox.mafia.net.PacketPlayerProfile;

public abstract class Player extends Entity {
	
	
	public static final float NAME_TAG_SIZE = 15f;
	public static final int NAME_TAG_HEIGHT = 20;
	public static final Color NAME_TAG_COLOR = new Color(255, 255, 230);

	
	protected PacketPlayerProfile profile;
	
	protected int animationStage = 0;
	protected int direction = 0;
	
	
	
	public Player(float x, float y, PacketPlayerProfile profile) {
		super(x, y, 70, 140,
				ImageResources.player1FrontStill, ImageResources.player1FrontWalk1, ImageResources.player1FrontWalk2,
				ImageResources.player1BackStill, ImageResources.player1BackWalk1, ImageResources.player1BackWalk2,
				ImageResources.player1RightStill, ImageResources.player1RightWalk1, ImageResources.player1RightWalk2,
				ImageResources.player1LeftStill, ImageResources.player1LeftWalk1, ImageResources.player1LeftWalk2);
		
		this.profile = profile;
	}

	
	
	
	@Override
	public void tick(Game game) {
		
		onTick(game);
	}

	
	@Override
	public void render(Graphics2D g, Game game) {
		g.drawImage(images[animationStage + (direction * 3)], (int) x - width / 2, (int) y - height, width, height, null);
		
		
		g.setFont(FontResources.mainFontBold.deriveFont(NAME_TAG_SIZE));
		g.setColor(NAME_TAG_COLOR);
		
		String username = profile.getUsername();
		int tagWidth = g.getFontMetrics().stringWidth(username);
		
		g.drawString(profile.getUsername(), (int) x - tagWidth / 2, (int) y + NAME_TAG_HEIGHT);
		
		
		onRender(g, game);
	}

	
	
	protected abstract void onTick(Game game);
	protected abstract void onRender(Graphics2D g, Game game);
	
	
	
	
	public PacketPlayerProfile getProfile() {
		return profile;
	}
	
}
