package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Player;
import hyperbox.mafia.ui.ButtonElement;
import hyperbox.mafia.ui.UIAnchor;

public class GameStateInGamePrepare extends GameState {

	
	private GameStateInGame gameStateInGame;
	
	private ButtonElement readyButton;
	private ButtonElement spectateButton;
	
	
	
	public GameStateInGamePrepare(Game game) {
		readyButton = new ButtonElement(-125, 60, UIAnchor.CENTER, UIAnchor.NEGATIVE, UIAnchor.POSITIVE, UIAnchor.NEGATIVE, 4, "Ready", () -> {
			gameStateInGame.getPlayer().setAliveState((byte) 1);
		});

		
		spectateButton = new ButtonElement(125, 60, UIAnchor.CENTER, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, 4, "Spectate", () -> {
			gameStateInGame.getPlayer().setAliveState((byte) -1);
		});
	}
	
	
	
	
	@Override
	protected void onEnable(Game game) {
		gameStateInGame = game.getGameStateManager().getGameStateInGame();
		
		
		gameStateInGame.setStatusText("Please choose to either Spectate or be Ready");
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
		
		
		
		
		boolean arePlayersReady = true;
		
		for(String username : gameStateInGame.getPlayers().keySet()) {
			Player player = gameStateInGame.getPlayers().get(username);
			
			if(player.getAliveState() == 0) {
				arePlayersReady = false;
				break;
			}
		}
		
		
		if(arePlayersReady) {
			this.disable(game);
			game.getGameStateManager().getGameStateInGameStoryteller().enable(game);
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
	}

	
}
