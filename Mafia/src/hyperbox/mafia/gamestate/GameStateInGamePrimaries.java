package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Player;
import hyperbox.mafia.entity.PlayerRemote;
import hyperbox.mafia.ui.ChatMessage;

public class GameStateInGamePrimaries extends GameState {

	
	private GameStateInGame gameStateInGame;
	
	
	
	@Override
	protected void onEnable(Game game) {
		gameStateInGame = game.getGameStateManager().getGameStateInGame();
		
		
		if(gameStateInGame.isPlayerStoryteller()) {
			gameStateInGame.setStatusText("Tell the others to fall asleep so you can choose the Mafia, then choose once they have done so.");
			gameStateInGame.setTipText("You may kill any players that peek or point to/poke others when they shouldn't.");
			
			
			for(String username : gameStateInGame.getPlayers().keySet()) {
				Player player = gameStateInGame.getPlayers().get(username);
				
				if(player instanceof PlayerRemote && player.getAliveState() == 1) {
					player.enableSelection(() -> {
						
						if(!gameStateInGame.hasMafiaBeenChosen()) {
							gameStateInGame.setMafiaUsername(player.getProfile().getUsername());
							gameStateInGame.setStatusText("Great, now tell the others you're choosing the Doctor, then do so.");
							
							ChatMessage mafiaMessage = new ChatMessage("Game", "You chose " + player.getProfile().getUsername() + " to be the Mafia!", true);
							gameStateInGame.getChatElement().addMessage(mafiaMessage, false, game);
							
							
						} else {
							gameStateInGame.setDoctorUsername(player.getProfile().getUsername());
							
							ChatMessage doctorMessage = new ChatMessage("Game", "You chose " + player.getProfile().getUsername() + " to be the Doctor!", true);
							gameStateInGame.getChatElement().addMessage(doctorMessage, false, game);
						}
					}, false);
					
					
					player.setAreStateActionsAllowed(true);
				}
			}
			
			
			gameStateInGame.getResetGameButton().setIsHidden(false);
			
			
		} else {
			gameStateInGame.setStatusText("Fall asleep and let the Storyteller choose the Mafia and Doctor.");
			gameStateInGame.setTipText("Don't peek or point to/poke the others unless instructed to! *Cough...*");
		}
		
		
		
		if(gameStateInGame.getPlayer().getAliveState() == 1) {
			gameStateInGame.getPlayer().setIsSleepingAllowed(true);
			gameStateInGame.getPlayer().setIsPointingEnabled(true);
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
		
		
		
		if(gameStateInGame.hasDoctorBeenChosen()) {
			this.disable(game);
			game.getGameStateManager().getGameStateInGameElimination().enable(game);
		}
	}

	
	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		
	}

	
}
