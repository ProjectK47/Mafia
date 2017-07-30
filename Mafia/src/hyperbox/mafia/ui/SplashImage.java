package hyperbox.mafia.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SplashImage {

	
	private BufferedImage image;
	private float scale;
	
	private Color backgroundColor;
	
	
	public SplashImage(BufferedImage image, float scale, Color backgroundColor) {
		this.image = image;
		this.scale = scale;
		
		this.backgroundColor = backgroundColor;
	}

	

	
	public BufferedImage getImage() {
		return image;
	}


	public float getScale() {
		return scale;
	}


	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
}
