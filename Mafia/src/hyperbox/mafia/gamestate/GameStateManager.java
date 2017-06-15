package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;
import java.util.LinkedList;

import hyperbox.mafia.core.Game;

public class GameStateManager {

	
	private GameStateMenu gameStateMenu;
	private GameStateInGame gameStateInGame;
	private GameStateInGamePrepare gameStateInGamePrepare;
	private GameStateInGameStoryteller gameStateInGameStoryteller;
	
	
	private LinkedList<GameState> gameStates = new LinkedList<GameState>();
	
	
	
	
	public GameStateManager(Game game) {
		gameStateMenu = new GameStateMenu(game);
		gameStateInGame = new GameStateInGame();
		gameStateInGamePrepare = new GameStateInGamePrepare(game);
		gameStateInGameStoryteller = new GameStateInGameStoryteller();
		
		
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
	
	
	
	
	public void disableAllStates(Game game) {
		for(GameState state : gameStates)
			state.disable(game);
	}
	
	
	
	
	private void addGameStatesToList() {
		gameStates.add(gameStateMenu);
		gameStates.add(gameStateInGame);
		gameStates.add(gameStateInGamePrepare);
		gameStates.add(gameStateInGameStoryteller);
	}
	
	
	
	
	
	public GameStateMenu getGameStateMenu() {
		return gameStateMenu;
	}
	
	
	public GameStateInGame getGameStateInGame() {
		return gameStateInGame;
	}
	
	
	public GameStateInGamePrepare getGameStateInGamePrepare() {
		return gameStateInGamePrepare;
	}
	
	
	public GameStateInGameStoryteller getGameStateInGameStoryteller() {
		return gameStateInGameStoryteller;
	}
	
}
