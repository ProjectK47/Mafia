package hyperbox.mafia.particle;

import java.awt.Color;
import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;

public class Particle {

	
	private float x;
	private float y;
	
	private float velocityX;
	private float velocityY;
	
	private int size;
	private Color color;
	
	private int lifetime;
	
	
	private int currentLifetime = 0;
	private boolean isParticleDead = false;
	
	
	
	public Particle(float x, float y, float velocityX, float velocityY, int size, Color color, int lifetime) {
		this.x = x;
		this.y = y;
		
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		
		this.size = size;
		this.color = color;
		
		this.lifetime = lifetime;
	}


	
	
	public void tick(Game game) {
		x += velocityX;
		y += velocityY;
		
		
		currentLifetime ++;
		
		if(currentLifetime > lifetime)
			isParticleDead = true;
	}
	
	
	public void render(Graphics2D g, Game game) {
		g.setColor(color);
		
		g.fillRect((int) x, (int) y, size, size);
	}
	
	
	

	
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	

	public float getVelocityX() {
		return velocityX;
	}

	public float getVelocityY() {
		return velocityY;
	}


	public int getSize() {
		return size;
	}

	public Color getColor() {
		return color;
	}


	public int getLifetime() {
		return lifetime;
	}


	public int getCurrentLifetime() {
		return currentLifetime;
	}
	
	public boolean isParticleDead() {
		return isParticleDead;
	}
	
}
