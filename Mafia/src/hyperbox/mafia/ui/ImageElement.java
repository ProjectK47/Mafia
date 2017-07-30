package hyperbox.mafia.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import hyperbox.mafia.core.Game;

public class ImageElement extends UIElement {
	
	
	
	protected BufferedImage image;
	protected float scale;
	
	

	public ImageElement(int x, int y, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY,
			BufferedImage image, float scale) {
		
		super(x, y, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, 0);
		
		
		this.image = image;
		this.scale = scale;
	}
	
	
	
	

	@Override
	public void onTick(Game game) {
		
	}

	
	@Override
	public void render(Graphics2D g, Game game) {
		g.drawImage(image, this.grabAnchoredX(game) - (width / 2), this.grabAnchoredY(game) - (height / 2), width, height, null);
	}
	
	
	
	
	
	
	public BufferedImage getImage() {
		return image;
	}
	
	
	public float getScale() {
		return scale;
	}

	
}
