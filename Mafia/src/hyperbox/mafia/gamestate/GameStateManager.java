package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;
import java.util.LinkedList;

import hyperbox.mafia.core.Game;

public class GameStateManager {

	
	private GameStateMenu gameStateMenu;
	private GameStateInGame gameStateInGame;
	private GameStateInGamePrepare gameStateInGamePrepare;
	private GameStateInGameStoryteller gameStateInGameStoryteller;
	private GameStateInGamePrimaries gameStateInGamePrimaries;
	private GameStateInGameElimination gameStateInGameElimination;
	private GameStateInGameEnd gameStateInGameEnd;
	
	
	private LinkedList<GameState> gameStates = new LinkedList<GameState>();
	
	
	
	
	public GameStateManager(Game game) {
		gameStateMenu = new GameStateMenu(game);
		gameStateInGame = new GameStateInGame();
		gameStateInGamePrepare = new GameStateInGamePrepare(game);
		gameStateInGameStoryteller = new GameStateInGameStoryteller();
		gameStateInGamePrimaries = new GameStateInGamePrimaries();
		gameStateInGameElimination = new GameStateInGameElimination(game);
		gameStateInGameEnd = new GameStateInGameEnd();
		
		
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
	
	
	public void disableAllStatesExcept(Game game, GameState... exceptions) {
		for(GameState state : gameStates) {
			boolean isException = false;
			
			for(GameState exception : exceptions)
				if(state == exception) {
					isException = true;
					break;
				}
			
			
			if(!isException)
				state.disable(game);
		}
	}
	
	
	
	
	private void addGameStatesToList() {
		gameStates.add(gameStateMenu);
		gameStates.add(gameStateInGame);
		gameStates.add(gameStateInGamePrepare);
		gameStates.add(gameStateInGameStoryteller);
		gameStates.add(gameStateInGamePrimaries);
		gameStates.add(gameStateInGameElimination);
		gameStates.add(gameStateInGameEnd);
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
	
	
	public GameStateInGamePrimaries getGameStateInGamePrimaries() {
		return gameStateInGamePrimaries;
	}
	
	
	public GameStateInGameElimination getGameStateInGameElimination() {
		return gameStateInGameElimination;
	}
	
	
	public GameStateInGameEnd getGameStateInGameEnd() {
		return gameStateInGameEnd;
	}
	
}
