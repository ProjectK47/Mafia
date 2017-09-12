package hyperbox.mafia.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Player;
import hyperbox.mafia.entity.PlayerRemote;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketEliminationChoice;
import hyperbox.mafia.net.PacketEliminationNextStage;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.ui.ButtonElement;
import hyperbox.mafia.ui.ChatMessage;
import hyperbox.mafia.ui.TextElement;
import hyperbox.mafia.ui.UIAnchor;

public class GameStateInGameElimination extends GameState {


	private GameStateInGame gameStateInGame;
	
	
	private ButtonElement nextStageButton;
	private TextElement currentRoundElement;
	
	private String mafiaChoiceUsername;
	private String doctorChoiceUsername;
	
	private int currentRound;
	private boolean arePlayersVoting;
	
	private String votedPlayerUsername;
	
	private boolean hasDocMessageBeenSaid;
	
	
	
	
	
	@Override
	protected void onEnable(Game game) {
		gameStateInGame = game.getGameStateManager().getGameStateInGame();
		
		
		nextStageButton = new ButtonElement(15, 15, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, 4.5f, "Next Stage", () -> {
			moveToNextStage(game, true);
		});
		
		
		currentRoundElement = new TextElement(-20, 125, UIAnchor.POSITIVE, UIAnchor.NEGATIVE, UIAnchor.POSITIVE, UIAnchor.NEGATIVE, "", 16, new Color(255, 255, 225));
		
		currentRound = 0;
		
		
		hasDocMessageBeenSaid = false;
		
		
		if(gameStateInGame.isPlayerStoryteller())
			gameStateInGame.getSoundEffectsElement().setIsDisabled(false);
		
		
		resetElimination(game);
	}
	

	@Override
	protected void onDisable(Game game) {
		
	}

	
	
	
	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		
		
		nextStageButton.tick(game);
		currentRoundElement.tick(game);
		
		
		gameStateInGame.getClient().forEachReceivedPacket((Packet packet) -> {
			if(packet.getID() == PacketID.ELIMINATION_CHOICE) {
				PacketEliminationChoice choicePacket = (PacketEliminationChoice) packet;	
				setEliminationChoice(choicePacket.getUsername(), choicePacket.isMafiaChoice(), game);
					
				packet.disposePacket();
				
				
			} else if(packet.getID() == PacketID.ELIMINATION_NEXT_STAGE) {
				moveToNextStage(game, false);
				
				packet.disposePacket();
			}
		});
		
		
		
		
		HashMap<String, Player> players = gameStateInGame.getPlayers();
		
		
		
		//Kill voted player////
		int numOfPlayers = 0;
		int numOfVotes = 0;
		
		byte highestVotedPlayerVotes = -1;
		ArrayList<String> highestVotedPlayerUsernames = new ArrayList<String>();
		
		
		for(String username : players.keySet()) {
			Player player = players.get(username);
			
			if(player.getAliveState() != 1 || player.getProfile().getUsername().equals(gameStateInGame.getStorytellerUsername()))
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
			
			
			if(votedUsername.equals(gameStateInGame.getProfile().getUsername())) {
				gameStateInGame.getPlayer().explodeToSpectator(game);
				
				ChatMessage outMessage = new ChatMessage("Game", votedUsername + " has been voted out!", true);
				gameStateInGame.getChatElement().addMessage(outMessage, true, game);
				
				moveToNextStage(game, true);
			}	
		}
		
		
		
		
		
		//Enable End state////
		boolean isPlayerAlive = false;
		
		for(String username : players.keySet()) {
			Player player = players.get(username);
			String playerUsername = player.getProfile().getUsername();
			
			
			if(playerUsername.equals(gameStateInGame.getStorytellerUsername()) || playerUsername.equals(gameStateInGame.getMafiaUsername()))
				continue;
			
			
			if(player.getAliveState() == 1) {
				isPlayerAlive = true;
				break;
			}
		}
		
		
		if(!isPlayerAlive || players.get(gameStateInGame.getMafiaUsername()).getAliveState() != 1) {
			this.disable(game);
			game.getGameStateManager().getGameStateInGameEnd().enable(game);
		}
		
		
		
		
		//Doctor dead message////
		Player doctor = players.get(gameStateInGame.getDoctorUsername());
		
		if(doctor.getAliveState() != 1 && !hasDocMessageBeenSaid) {
			
			if(gameStateInGame.isPlayerStoryteller()) {
				ChatMessage doctorDeadMessage = new ChatMessage("Game", "The Doctor is dead! You may announce who they were if you wish.", true);
				gameStateInGame.getChatElement().addMessage(doctorDeadMessage, false, game);
			}	
			
			
			hasDocMessageBeenSaid = true;
		}
	}

	
	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		if(!isEnabled)
			return;
		
		
		nextStageButton.render(g, game);
		currentRoundElement.render(g, game);
	}
	
	
	
	
	
	private void resetElimination(Game game) {
		mafiaChoiceUsername = null;
		doctorChoiceUsername = null;
		
		
		currentRound ++;
		currentRoundElement.setText("Elimination Round: " + currentRound);
		
		arePlayersVoting = false;
		
		votedPlayerUsername = null;
		
		
		
		if(gameStateInGame.isPlayerStoryteller()) {
			gameStateInGame.setStatusText("Tell the Mafia to awaken and choose someone to kill, then go back to sleep >:D");	
			nextStageButton.setIsDisabled(true);
			
		} else {
			gameStateInGame.setStatusText("Follow the Storyteller's instructions. Don't wake up unless instructed to!");
			nextStageButton.setIsHidden(true);
		}
		
		
		
		
		//Enable Mafia choosing////
		if(gameStateInGame.isPlayerMafia())
			for(String username : gameStateInGame.getPlayers().keySet()) {
				Player player = gameStateInGame.getPlayers().get(username);
				
				if(player instanceof PlayerRemote &&
						!player.getProfile().getUsername().equals(gameStateInGame.getStorytellerUsername()) &&
						player.getAliveState() == 1) {
					
					
					player.enableSelection(() -> {
						String chosenUsername = player.getProfile().getUsername();
						
						sendEliminationChoice(chosenUsername, true, game);
						
						ChatMessage mafiaMessage = new ChatMessage("Game", "You chose to kill " + chosenUsername + "!", true);
						gameStateInGame.getChatElement().addMessage(mafiaMessage, false, game);
					}, true);
				}
			}
		
		
		
		ChatMessage roundMessage = new ChatMessage("Game", "Elimination Round #" + currentRound + " has begun.", true);
		gameStateInGame.getChatElement().addMessage(roundMessage, false, game);
	}
	
	
	
	
	private void moveToNextStage(Game game, boolean shouldSendPacket) {
		if(!arePlayersVoting) {
			//Enable player vote stage////
			
			if(!gameStateInGame.isPlayerStoryteller()) {
				if(gameStateInGame.getPlayer().getAliveState() == 1) {
					HashMap<String, Player> players = gameStateInGame.getPlayers();
					
					for(String username : players.keySet()) {
						Player player = players.get(username);
						
						if(player.getAliveState() != 1 || player.getProfile().getUsername().equals(gameStateInGame.getStorytellerUsername()))
							continue;
						
						
						player.enableSelection(() -> {
							player.incrementTally(game);
							
							if(votedPlayerUsername != null)
								players.get(votedPlayerUsername).decrementTally(game);
							
							votedPlayerUsername = player.getProfile().getUsername();
						}, true);
					}
					
					
					gameStateInGame.getPlayer().resetTally(game);
				}
				
				
			} else {
				gameStateInGame.setStatusText("Tell the others to vote on who they think the Mafia is.");
				nextStageButton.setIsDisabled(true);
			}
			
			
			arePlayersVoting = true;
			
			
		} else {
			//Start next round////
			
			for(String username : gameStateInGame.getPlayers().keySet()) {
				Player player = gameStateInGame.getPlayers().get(username);
				
				player.disableSelection();
			}
			
			gameStateInGame.getPlayer().disableTally(game);
			
			
			resetElimination(game);
		}
		
		
		
		if(shouldSendPacket) {
			PacketEliminationNextStage stagePacket = new PacketEliminationNextStage();
			gameStateInGame.getClient().sendPacket(stagePacket);
		}
	}
	

	
	
	
	private void sendEliminationChoice(String eliminatedUsername, boolean isMafiaChoice, Game game) {
		for(String playerUsername : gameStateInGame.getPlayers().keySet())
			gameStateInGame.getPlayers().get(playerUsername).disableSelection();
		
		
		PacketEliminationChoice choicePacket = new PacketEliminationChoice(eliminatedUsername, isMafiaChoice);
		gameStateInGame.getClient().sendPacket(choicePacket);
		
		setEliminationChoice(eliminatedUsername, isMafiaChoice, game);
	}
	
	
	
	private void setEliminationChoice(String eliminatedUsername, boolean isMafiaChoice, Game game) {
		if(isMafiaChoice) {
			mafiaChoiceUsername = eliminatedUsername;
			
			
			if(gameStateInGame.isPlayerStoryteller()) {
				ChatMessage storytellerMessage = new ChatMessage("Game", "The Mafia (" + gameStateInGame.getMafiaUsername() +
						") has chosen to kill " + mafiaChoiceUsername + ".", true);
				
				gameStateInGame.getChatElement().addMessage(storytellerMessage, false, game);
			}
			
			
			
			if(gameStateInGame.getPlayers().get(gameStateInGame.getDoctorUsername()).getAliveState() == 1) {
				if(gameStateInGame.isPlayerStoryteller())
					gameStateInGame.setStatusText("Now tell the Doctor to awaken and choose someone to save, then go back to sleep.");
				
				
				//Enable Doctor choosing////
				if(gameStateInGame.isPlayerDoctor())
					for(String username : gameStateInGame.getPlayers().keySet()) {
						Player player = gameStateInGame.getPlayers().get(username);
						
						if(!player.getProfile().getUsername().equals(gameStateInGame.getStorytellerUsername()) &&
								player.getAliveState() == 1) {
							
							
							player.enableSelection(() -> {
								String chosenUsername = player.getProfile().getUsername();
								
								sendEliminationChoice(chosenUsername, false, game);
								
								ChatMessage doctorMessage = new ChatMessage("Game", "You chose to save " + chosenUsername + "!", true);
								gameStateInGame.getChatElement().addMessage(doctorMessage, false, game);
							}, true);
						}
					}
				
			} else {
				enableStoryStage();
			}
			
			
		} else {
			doctorChoiceUsername = eliminatedUsername;
			
			
			if(gameStateInGame.isPlayerStoryteller()) {
				ChatMessage storytellerMessage = new ChatMessage("Game", "The Doctor (" + gameStateInGame.getDoctorUsername() +
						") has chosen to save " + doctorChoiceUsername + ".", true);
				
				gameStateInGame.getChatElement().addMessage(storytellerMessage, false, game);
			}
			
			
			//Enable story stage////
			enableStoryStage();
		}
	}
	
	
	
	private void enableStoryStage() {
		if(gameStateInGame.isPlayerStoryteller()) {
			gameStateInGame.setStatusText("Great, now tell everyone to awaken so you can tell your story and kill/save the chosen players.");
			nextStageButton.setIsDisabled(false);
		}
	}
	
	
	
	
	
	public int getCurrentRound() {
		return currentRound;
	}
	
}
