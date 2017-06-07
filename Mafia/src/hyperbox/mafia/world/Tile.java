package hyperbox.mafia.world;

import java.awt.image.BufferedImage;

import hyperbox.mafia.animation.CoolDown;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.io.ImageResources;

public enum Tile {

	
	
	GRASS(0, 255, 0, 255, -1, ImageResources.grassTile),
	DIRT(0, 0, 0, 255, -1, ImageResources.dirtTile),
	WATER(255, 255, 255, 0, 60, ImageResources.waterTileOne, ImageResources.waterTileTwo);
	
	
	
	private int mapImageR;
	private int mapImageG;
	private int mapImageB;
	private int mapImageA;
	
	private BufferedImage[] images;
	
	private CoolDown animationCoolDown = null;
	
	
	private int currentImageIndex = 0;
	
	
	private Tile(int mapImageR, int mapImageG, int mapImageB, int mapImageA, int animationTicks, BufferedImage... images) {
		this.mapImageR = mapImageR;
		this.mapImageG = mapImageG;
		this.mapImageB = mapImageB;
		this.mapImageA = mapImageA;
		
		this.images = images;
		
		if(animationTicks > 0)
			animationCoolDown = new CoolDown(animationTicks);
	}

	
	
	
	public void tick(Game game) {
		if(animationCoolDown == null)
			return;
		
		
		animationCoolDown.tick();
		
		animationCoolDown.executeIfReady(() -> {
			if(currentImageIndex + 1 < images.length)
				currentImageIndex ++;
			else
				currentImageIndex = 0;
		});
	}
	
	

	
	public int getMapImageR() {
		return mapImageR;
	}

	public int getMapImageG() {
		return mapImageG;
	}

	public int getMapImageB() {
		return mapImageB;
	}

	public int getMapImageA() {
		return mapImageA;
	}
	
	
	public BufferedImage[] getImages() {
		return images;
	}
	
	
	public int getCurrentImageIndex() {
		return currentImageIndex;
	}
	
}
