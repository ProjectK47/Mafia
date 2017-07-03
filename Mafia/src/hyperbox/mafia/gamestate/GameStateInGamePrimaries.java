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
			gameStateInGame.setStatusText("Tell the others to fall asleep so you can choose the Mafia, then choose once they have done so");
			
			
			for(String username : gameStateInGame.getPlayers().keySet()) {
				Player player = gameStateInGame.getPlayers().get(username);
				
				if(player instanceof PlayerRemote && player.getAliveState() == 1)
					player.enableSelection(() -> {
						
						if(!gameStateInGame.hasMafiaBeenChosen()) {
							gameStateInGame.setMafiaUsername(player.getProfile().getUsername());
							gameStateInGame.setStatusText("Great, now tell the others you're choosing the Doctor, then do so");
							
						} else {
							gameStateInGame.setDoctorUsername(player.getProfile().getUsername());
						}
					});
			}
			
			
		} else {
			gameStateInGame.setStatusText("Fall asleep and let the Storyteller choose the Mafia and Doctor");
		}
		
		
		if(gameStateInGame.getPlayer().getAliveState() == 1)
			gameStateInGame.getPlayer().setIsPointingEnabled(true);
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
		
		
		
		if(gameStateInGame.hasDoctorBeenChosen()) {
			this.disable(game);
			game.getGameStateManager().getGameStateInGameElimination().enable(game);
		}
	}

	
	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		
	}

	
}
