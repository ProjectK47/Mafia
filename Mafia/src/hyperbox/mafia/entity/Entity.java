package hyperbox.mafia.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import hyperbox.mafia.core.Game;

public abstract class Entity {

	
	
	protected float x;
	protected float y;
	
	protected int width;
	protected int height;
	
	protected BufferedImage[] images;
	
	
	
	public Entity(float x, float y, int width, int height, BufferedImage... images) {
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		this.images = images;
	}
	
	
	
	
	public abstract void tick(Game game);
	public abstract void render(Graphics2D g, Game game);
	
	
	
	
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	
	public int getWidth() {
		return width;
	}
		
	public int getHeight() {
		return height;
	}
	
	
}
