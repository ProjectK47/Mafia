package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.ui.ButtonElement;
import hyperbox.mafia.ui.ControlsElement;
import hyperbox.mafia.ui.UIAnchor;

public class GameStateInGamePrepare extends GameState {

	
	private GameStateInGame gameStateInGame;
	
	
	private ButtonElement readyButton;
	private ButtonElement spectateButton;
	
	private ButtonElement exitButton;
	
	private ControlsElement controlsElement;
	
	
	
	public GameStateInGamePrepare(Game game) {
		readyButton = new ButtonElement(-125, 60, UIAnchor.CENTER, UIAnchor.NEGATIVE, UIAnchor.POSITIVE, UIAnchor.NEGATIVE, 4, "Ready", () -> {
			gameStateInGame.getPlayer().setAliveState((byte) 1);
		});

		
		spectateButton = new ButtonElement(125, 60, UIAnchor.CENTER, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, 4, "Spectate", () -> {
			gameStateInGame.getPlayer().setAliveState((byte) -1);
		});
		
		
		
		exitButton = new ButtonElement(15, 15, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, 2.75f, "Exit", () -> {
			gameStateInGame.exitToMenu(game);
		});
		
		
		
		controlsElement = new ControlsElement(-20, 0, UIAnchor.POSITIVE, UIAnchor.CENTER, UIAnchor.POSITIVE, UIAnchor.CENTER, 5.5f);
	}
	
	
	
	
	@Override
	protected void onEnable(Game game) {
		gameStateInGame = game.getGameStateManager().getGameStateInGame();
		
		
		gameStateInGame.setStatusText("Please choose to either Spectate or be Ready.");
		gameStateInGame.setTipText("You need a minimum of " + GameStateInGame.MINIMUM_NUMBER_OF_PLAYERS + " players to start the game.");
	}

	
	@Override
	protected void onDisable(Game game) {
		
	}

	
	
	
	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		readyButton.tick(game);
		spectateButton.tick(game);
		
		exitButton.tick(game);
		
		controlsElement.tick(game);
		
		
		
		if(gameStateInGame.shouldGameStart(false)) {
			this.disable(game);
			game.getGameStateManager().getGameStateInGameStoryteller().enable(game);
			
			gameStateInGame.setHasGameStarted(true);
		}
	}
	

	@Override
	protected void onRender(Graphics2D g, Game game) {
		if(!isEnabled)
			return;
		
		
		game.getCamera().translateToCamera(g, game);
		game.getCamera().translateCameraShake(g);
		
		
		for(String username : gameStateInGame.getPlayers().keySet())
			gameStateInGame.getPlayers().get(username).renderReadyIcon(g, game);
		
		
		game.getCamera().translateNoCameraShake(g);
		game.getCamera().translateFromCamera(g);
		
		
		
		readyButton.render(g, game);
		spectateButton.render(g, game);
		
		exitButton.render(g, game);
		
		controlsElement.render(g, game);
	}

	
}
