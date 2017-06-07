package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;
import java.util.LinkedList;

import hyperbox.mafia.core.Game;

public class GameStateManager {

	
	private GameStateMenu gameStateMenu;
	
	
	private LinkedList<GameState> gameStates = new LinkedList<GameState>();
	
	
	
	
	public GameStateManager() {
		gameStateMenu = new GameStateMenu();
		
		
		addGameStatesToList();
	}
	
	
	
	
	
	public void tick(Game game) {
		for(GameState state : gameStates)
			state.tick(game);
	}
	
	
	public void render(Graphics2D g, Game game) {
		for(GameState state : gameStates)
			state.render(g, game);
	}
	
	
	
	
	private void addGameStatesToList() {
		gameStates.add(gameStateMenu);
	}
	
	
	
	
	
	public GameStateMenu getGameStateMenu() {
		return gameStateMenu;
	}
	
}
