package hyperbox.mafia.entity;

import java.awt.Graphics2D;
import java.util.Random;

import hyperbox.mafia.animation.CoolDown;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.io.ImageResources;
import hyperbox.mafia.utils.NumberUtils;

public class GrassBlades extends Entity {
	
	
	public static final int ANIMATION_UPDATE_MIN_TICKS = 150;
	public static final int ANIMATION_UPDATE_MAX_TICKS = 900;
	

	
	private CoolDown animationCoolDown;
	
	private int currentImageIndex = 0;
	
	
	private Random random = new Random();
	
	
	
	public GrassBlades(float x, float y) {
		super(x, y, 60, 60, ImageResources.grassBladesStill, ImageResources.grassBladesLeft, ImageResources.grassBladesRight);
		
		animationCoolDown = new CoolDown(NumberUtils.randomIntInRange(ANIMATION_UPDATE_MIN_TICKS, ANIMATION_UPDATE_MAX_TICKS));
	}
	
	
	

	@Override
	public void tick(Game game) {
		animationCoolDown.tick();
		
		
		animationCoolDown.executeIfReady(() -> {
			if(currentImageIndex == 0) {
				
				if(random.nextBoolean())
					currentImageIndex = 1;
				else
					currentImageIndex = 2;
				
			} else {
				currentImageIndex = 0;
			}
		});
	}

	
	
	@Override
	public void render(Graphics2D g, Game game) {
		g.drawImage(images[currentImageIndex], (int) x - (width / 2), (int) y - height, width, height, null);
	}

	
}
