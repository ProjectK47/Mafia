package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Player;

public class GameStateInGameStoryteller extends GameState {

	
	
	private GameStateInGame gameStateInGame;
	
	private String votedPlayerUsername = null;
	
	
	
	@Override
	protected void onEnable(Game game) {
		gameStateInGame = game.getGameStateManager().getGameStateInGame();
		
		gameStateInGame.setStatusText("Please Vote on Who Should Be the Storyteller");
		
		
		
		if(gameStateInGame.getPlayer().getAliveState() == 1) {
			HashMap<String, Player> players = gameStateInGame.getPlayers();
			
			for(String username : players.keySet()) {
				Player player = players.get(username);
				
				if(player.getAliveState() != 1)
					continue;
				
				
				player.enableSelection(() -> {
					player.incrementTally(game);
					
					if(votedPlayerUsername != null)
						players.get(votedPlayerUsername).decrementTally(game);
					
					
					votedPlayerUsername = player.getProfile().getUsername();
				});
			}
			
			
			gameStateInGame.getPlayer().resetTally(game);
		}
	}

	
	@Override
	protected void onDisable(Game game) {
		for(String username : gameStateInGame.getPlayers().keySet()) {
			Player player = gameStateInGame.getPlayers().get(username);
			
			player.disableSelection();
		}
		
		gameStateInGame.getPlayer().disableTally(game);
	}

	
	
	
	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		
		int numOfPlayers = 0;
		int numOfVotes = 0;
		
		byte highestVotedPlayerVotes = -1;
		ArrayList<String> highestVotedPlayerUsernames = new ArrayList<String>();
		
		
		for(String username : gameStateInGame.getPlayers().keySet()) {
			Player player = gameStateInGame.getPlayers().get(username);
			
			if(player.getAliveState() != 1)
				continue;
			
			
			numOfPlayers ++;
			numOfVotes += player.getTallyCount();
			
			if(player.getTallyCount() >= highestVotedPlayerVotes) {
				if(player.getTallyCount() > highestVotedPlayerVotes)
					highestVotedPlayerUsernames.clear();
				
				highestVotedPlayerVotes = player.getTallyCount();
				highestVotedPlayerUsernames.add(player.getProfile().getUsername());
			}
		}
		
		
		
		if(numOfVotes >= numOfPlayers && highestVotedPlayerUsernames.size() == 1) {
			String votedUsername = highestVotedPlayerUsernames.get(0);
			
			if(votedUsername.equals(gameStateInGame.getProfile().getUsername()))
				gameStateInGame.setStorytellerUsername(votedUsername, game);
		}
		
		
		
		if(gameStateInGame.hasStorytellerBeenChosen()) {
			this.disable(game);
			game.getGameStateManager().getGameStateInGamePrimaries().enable(game);
		}
	}

	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		
	}

}
