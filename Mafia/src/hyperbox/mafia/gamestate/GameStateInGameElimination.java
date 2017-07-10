package hyperbox.mafia.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Player;
import hyperbox.mafia.entity.PlayerRemote;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketEliminationChoice;
import hyperbox.mafia.net.PacketEliminationRoundComplete;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.ui.ButtonElement;
import hyperbox.mafia.ui.ChatMessage;
import hyperbox.mafia.ui.TextElement;
import hyperbox.mafia.ui.UIAnchor;

public class GameStateInGameElimination extends GameState {


	private GameStateInGame gameStateInGame;
	
	
	private ButtonElement roundCompleteButton;
	private TextElement currentRoundElement;
	
	private String mafiaChoiceUsername;
	private String doctorChoiceUsername;
	
	private int currentRound;
	
	
	
	@Override
	protected void onEnable(Game game) {
		gameStateInGame = game.getGameStateManager().getGameStateInGame();
		
		
		roundCompleteButton = new ButtonElement(15, 15, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, 4.5f, "Round Complete", () -> {
			resetElimination(game);
			
			PacketEliminationRoundComplete roundPacket = new PacketEliminationRoundComplete();
			gameStateInGame.getClient().sendPacket(roundPacket);
		});
		
		
		currentRoundElement = new TextElement(-20, 150, UIAnchor.POSITIVE, UIAnchor.NEGATIVE, UIAnchor.POSITIVE, UIAnchor.NEGATIVE, "", 16, new Color(255, 255, 225));
		
		currentRound = 0;
		
		
		resetElimination(game);
	}
	

	@Override
	protected void onDisable(Game game) {
		
	}

	
	
	
	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		
		
		roundCompleteButton.tick(game);
		currentRoundElement.tick(game);
		
		
		gameStateInGame.getClient().forEachReceivedPacket((Packet packet) -> {
			if(packet.getID() == PacketID.ELIMINATION_CHOICE) {
				PacketEliminationChoice choicePacket = (PacketEliminationChoice) packet;	
				setEliminationChoice(choicePacket.getUsername(), choicePacket.isMafiaChoice(), game);
					
				packet.disposePacket();
				
				
			} else if(packet.getID() == PacketID.ELIMINATION_ROUND_COMPLETE) {
				resetElimination(game);
				
				packet.disposePacket();
			}
		});
	}

	
	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		if(!isEnabled)
			return;
		
		
		roundCompleteButton.render(g, game);
		currentRoundElement.render(g, game);
	}
	
	
	
	
	
	private void resetElimination(Game game) {
		mafiaChoiceUsername = null;
		doctorChoiceUsername = null;
		
		
		currentRound ++;
		currentRoundElement.setText("Elimination Round: " + currentRound);
		
		
		
		if(gameStateInGame.isPlayerStoryteller()) {
			gameStateInGame.setStatusText("Tell the Mafia to awaken and choose someone to kill, then go back to sleep >:D");	
			roundCompleteButton.setIsDisabled(true);
			
		} else {
			gameStateInGame.setStatusText("Follow the Storyteller's instructions. Don't wake up unless instructed to!");
			roundCompleteButton.setIsHidden(true);
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
					});
				}
			}
		
		
		
		ChatMessage roundMessage = new ChatMessage("Game", "Elimination Round #" + currentRound + " has begun.", true);
		gameStateInGame.getChatElement().addMessage(roundMessage, false, game);
	}

	
	
	
	private void sendEliminationChoice(String eliminatedUsername, boolean isMafiaChoice, Game game) {
		PacketEliminationChoice choicePacket = new PacketEliminationChoice(eliminatedUsername, isMafiaChoice);
		gameStateInGame.getClient().sendPacket(choicePacket);
		
		setEliminationChoice(eliminatedUsername, isMafiaChoice, game);
		
		
		for(String playerUsername : gameStateInGame.getPlayers().keySet())
			gameStateInGame.getPlayers().get(playerUsername).disableSelection();
	}
	
	
	
	private void setEliminationChoice(String eliminatedUsername, boolean isMafiaChoice, Game game) {
		if(isMafiaChoice) {
			mafiaChoiceUsername = eliminatedUsername;
			
			
			if(gameStateInGame.isPlayerStoryteller()) {
				ChatMessage storytellerMessage = new ChatMessage("Game", "The Mafia (" + gameStateInGame.getMafiaUsername() +
						") has chosen to kill " + mafiaChoiceUsername + ".", true);
				
				gameStateInGame.getChatElement().addMessage(storytellerMessage, false, game);
				
				gameStateInGame.setStatusText("Now tell the Doctor to awaken and choose someone to save, then go back to sleep.");
			}
			
			
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
						});
					}
				}
			
			
		} else {
			doctorChoiceUsername = eliminatedUsername;
			
			
			if(gameStateInGame.isPlayerStoryteller()) {
				ChatMessage storytellerMessage = new ChatMessage("Game", "The Doctor (" + gameStateInGame.getDoctorUsername() +
						") has chosen to save " + doctorChoiceUsername + ".", true);
				
				gameStateInGame.getChatElement().addMessage(storytellerMessage, false, game);
				
				
				
				//Enable story stage////
				gameStateInGame.setStatusText("Great, now tell everyone to awaken so you can tell your story and kill/save the chosen players.");
				roundCompleteButton.setIsDisabled(false);
			}
		}
	}
	
	
}
