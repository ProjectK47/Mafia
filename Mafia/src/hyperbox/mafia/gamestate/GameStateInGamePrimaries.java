package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Player;
import hyperbox.mafia.entity.PlayerRemote;

public class GameStateInGamePrimaries extends GameState {

	
	private GameStateInGame gameStateInGame;
	
	
	
	@Override
	protected void onEnable(Game game) {
		gameStateInGame = game.getGameStateManager().getGameStateInGame();
		
		
		if(gameStateInGame.isPlayerStoryteller()) {
			gameStateInGame.setStatusText("Tell the Others to Fall Asleep, then Choose the Mafia Once They Have Done So");
			
			
			for(String username : gameStateInGame.getPlayers().keySet()) {
				Player player = gameStateInGame.getPlayers().get(username);
				
				if(player instanceof PlayerRemote && player.getAliveState() == 1)
					player.enableSelection(() -> {
						
						if(!gameStateInGame.hasMafiaBeenChosen()) {
							gameStateInGame.setMafiaUsername(player.getProfile().getUsername());
							gameStateInGame.setStatusText("Great, Now Choose the Doctor");
							
						} else {
							gameStateInGame.setDoctorUsername(player.getProfile().getUsername());
						}
					});
			}
			
			
		} else {
			gameStateInGame.setStatusText("Fall Asleep and Let the Storyteller Choose the Mafia");
		}
	}

	
	
	@Override
	protected void onDisable(Game game) {
		for(String username : gameStateInGame.getPlayers().keySet()) {
			Player player = gameStateInGame.getPlayers().get(username);
			
			player.disableSelection();
		}
	}
	
	
	

	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		
		if(!gameStateInGame.isPlayerStoryteller() && gameStateInGame.hasMafiaBeenChosen())
			gameStateInGame.setStatusText("Don't Awake yet, the Storyteller Is Now Choosing the Doctor");
		
		
		
		
		if(gameStateInGame.hasDoctorBeenChosen()) {
			System.out.println("Done!");
		}
	}

	
	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		
	}

	
}
