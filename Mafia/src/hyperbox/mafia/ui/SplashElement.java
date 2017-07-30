package hyperbox.mafia.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.sun.glass.events.KeyEvent;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.input.KeyboardInput;

public class SplashElement extends UIElement {
	
	
	public static final float STAGE_SPEED = 0.02f;
	public static final float SKIP_KEY_AMOUNT = 1f;

	
	private SplashImage[] images;
	
	private ImageElement[] imageElements;
	
	
	private int imageStage = 0;
	private int fadeStage = 0;
	
	private float stageProgress = 0f;
	
	
	private boolean isComplete = false;
	
	
	
	public SplashElement(SplashImage... images) {
		super(0, 0, 0, 0, UIAnchor.CENTER, UIAnchor.CENTER, UIAnchor.CENTER, UIAnchor.CENTER, 2);
		
		this.images = images;
		
		
		imageElements = new ImageElement[images.length];
		
		for(int i = 0; i < images.length; i ++) {
			SplashImage image = images[i];
			ImageElement imageElement = new ImageElement(0, 0, UIAnchor.CENTER, UIAnchor.CENTER, UIAnchor.CENTER, UIAnchor.CENTER, 
					image.getImage(), image.getScale());
			
			imageElements[i] = imageElement;
		}
	}
	
	

	@Override
	public void onTick(Game game) {
		width = game.getWidth();
		height = game.getHeight();
		
		
		if(isComplete)
			return;
		
		
		if(imageStage >= images.length && fadeStage == 2) {
			isComplete = true;
			
			this.shouldAllowClickThrough = true;
		}
		
		
		
		
		if(KeyboardInput.wasKeyTyped(KeyEvent.VK_ESCAPE, false) || KeyboardInput.wasKeyTyped(KeyEvent.VK_SPACE, false))
			stageProgress += SKIP_KEY_AMOUNT;
			
		
		stageProgress += STAGE_SPEED;
		
		if(stageProgress > 1) {
			stageProgress = 0f;
			
			if(fadeStage < 3) {
				fadeStage ++;
				
			} else {
				imageStage ++;
				fadeStage = 0;
			}
		}
	}

	
	
	@Override
	public void render(Graphics2D g, Game game) {
		if(isComplete)
			return;
		
		
		if(imageStage < images.length) {
			SplashImage image = images[imageStage];
			ImageElement imageElement = imageElements[imageStage];
			
			
			g.setColor(image.getBackgroundColor());
			g.fillRect(x - (game.getWidth() / 2), y - (game.getHeight() / 2), game.getWidth(), game.getHeight());
			
			imageElement.render(g, game);
		}
		
		
		
		float coverTransparency = 0;
		
		if(fadeStage == 0)
			coverTransparency = 1f;
		else if(fadeStage == 1)
			coverTransparency = 1f - stageProgress;
		else if(fadeStage == 2)
			coverTransparency = 0f;
		else if(fadeStage == 3)
			coverTransparency = stageProgress;
		
		
		Color coverColor = new Color(0f, 0f, 0f, coverTransparency);
		g.setColor(coverColor);
		
		g.fillRect(x - (game.getWidth() / 2), y - (game.getHeight() / 2), game.getWidth(), game.getHeight());
	}

}
