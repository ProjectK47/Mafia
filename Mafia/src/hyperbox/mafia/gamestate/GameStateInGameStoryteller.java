package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Random;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Player;

public class GameStateInGameStoryteller extends GameState {

	
	
	private GameStateInGame gameStateInGame;
	
	private String votedPlayerUsername = null;
	
	
	private Random random = new Random();
	
	
	
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
			player.disableTally(game);
		}
	}

	
	
	
	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		
		int numOfPlayers = 0;
		int numOfVotes = 0;
		
		byte highestVotedPlayerVotes = -1;
		String highestVotedPlayerUsername = null;
		
		
		for(String username : gameStateInGame.getPlayers().keySet()) {
			Player player = gameStateInGame.getPlayers().get(username);
			
			if(player.getAliveState() != 1)
				continue;
			
			
			numOfPlayers ++;
			numOfVotes += player.getTallyCount();
			
			if(player.getTallyCount() > highestVotedPlayerVotes ||
					(player.getTallyCount() == highestVotedPlayerVotes && random.nextBoolean())) {
				
				highestVotedPlayerVotes = player.getTallyCount();
				highestVotedPlayerUsername = player.getProfile().getUsername();
			}
		}
		
		
		
		if(numOfVotes >= numOfPlayers) {
			System.out.println(highestVotedPlayerUsername);
		}
	}

	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		
	}

}
