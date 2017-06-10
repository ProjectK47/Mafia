package hyperbox.mafia.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Entity;
import hyperbox.mafia.entity.GrassBlades;
import hyperbox.mafia.utils.NumberUtils;

public class Map {

	
	
	public static final int TILE_SIZE = 75;
	public static final float GRASS_BLADES_PROBABILITY = 0.25f;
	
	
	private int gridWidth;
	private int gridHeight;
	
	private Tile[][] tiles;
	
	
	private ArrayList<Entity> scenery = new ArrayList<Entity>();
	
	
	
	
	public Map(BufferedImage mapImage) {
		gridWidth = mapImage.getWidth();
		gridHeight = mapImage.getHeight();
		
		tiles = new Tile[gridWidth][gridHeight];
		
		
		for(int x = 0; x < mapImage.getWidth(); x ++)
			for(int y = 0; y < mapImage.getHeight(); y ++) {
				Color pixel = new Color(mapImage.getRGB(x, y), true);
				
				
				for(Tile tile : Tile.values())
					if(tile.getMapImageR() == pixel.getRed() && tile.getMapImageG() == pixel.getGreen() &&
							tile.getMapImageB() == pixel.getBlue() && tile.getMapImageA() == pixel.getAlpha()) {
						
						tiles[x][y] = tile;
						checkSpecialTile(x, y, tile);
					}
			}
	}
	
	
	
	
	
	
	public void tick(Game game) {
		Tile.WATER.tick(game);
		
		for(Entity entity : scenery)
			entity.tick(game);
	}
	
	
	
	
	public void render(Graphics2D g, Game game) {
		for(int x = 0; x < gridWidth; x ++)
			for(int y = 0; y < gridHeight; y ++) {
				
				Tile tile = tiles[x][y];
				
				int worldX = (x - gridWidth / 2) * TILE_SIZE;
				int worldY = (y - gridHeight / 2) * TILE_SIZE;
				
				
				if(game.areCoordsOnScreen(worldX, worldY) ||
						game.areCoordsOnScreen(worldX + TILE_SIZE, worldY + TILE_SIZE) ||
						game.areCoordsOnScreen(worldX, worldY + TILE_SIZE) ||
						game.areCoordsOnScreen(worldX + TILE_SIZE, worldY)) {
					
					
					BufferedImage currentImage = tile.getImages()[tile.getCurrentImageIndex()];
					
					g.drawImage(currentImage, worldX, worldY, TILE_SIZE, TILE_SIZE, null);
				}
			}
		
		
		
		for(Entity entity : scenery)
			entity.render(g, game);
	}
	
	
	
	
	
	public Tile grabTileAtCoords(float x, float y, Game game) {
		int gridX = (int) (x + gridWidth / 2 * TILE_SIZE) / TILE_SIZE;
		int gridY = (int) (y + gridHeight / 2 * TILE_SIZE) / TILE_SIZE;
		
		Tile tile = tiles[gridX][gridY];
		
		
		return tile;
	}
	
	
	
	
	
	
	private void checkSpecialTile(int x, int y, Tile tile) {
		if(tile == Tile.GRASS) {
			if(NumberUtils.booleanFromProbability(GRASS_BLADES_PROBABILITY)) {
				GrassBlades grassBlades = new GrassBlades(
						(x - gridWidth / 2) * TILE_SIZE + TILE_SIZE / 2,
						(y - gridHeight / 2) * TILE_SIZE + TILE_SIZE / 2);
				
				scenery.add(grassBlades);
			}
		}
	}
	
	
	
	
	
	
	public int getGridWidth() {
		return gridWidth;
	}
	
	public int getGridHeight() {
		return gridHeight;
	}
	
	
}
