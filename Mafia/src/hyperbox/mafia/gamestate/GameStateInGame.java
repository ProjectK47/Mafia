package hyperbox.mafia.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import hyperbox.mafia.animation.CoolDown;
import hyperbox.mafia.client.GameClient;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Player;
import hyperbox.mafia.entity.PlayerLocal;
import hyperbox.mafia.entity.PlayerRemote;
import hyperbox.mafia.io.Settings;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.net.PacketPlayerDisconnect;
import hyperbox.mafia.net.PacketPlayerProfile;
import hyperbox.mafia.ui.TextElement;
import hyperbox.mafia.ui.UIAnchor;

public class GameStateInGame extends GameState {

	
	public static final float SPAWN_X = 0f;
	public static final float SPAWN_Y = 0F;
	
	public static final Color STATUS_ELEMENT_COLOR_ONE = new Color(255, 204, 153);
	public static final Color STATUS_ELEMENT_COLOR_TWO = new Color(255, 243, 230);
	public static final int STATUS_ELEMENT_COLOR_COOL_DOWN = 60;
	
	
	private PacketPlayerProfile profile;
	private PlayerLocal player;
	private GameClient client;
	
	private TextElement statusElement;
	private CoolDown statusElementColorCoolDown;
	
	
	private HashMap<String, Player> players;
	private boolean hasReceivedPlayers;
	private boolean isLoginCheckDone;
	
	
	
	@Override
	protected void onEnable(Game game) {
		Settings settings = game.getGameStateManager().getGameStateMenu().getSettings();
		
		String ip = settings.grabValueString("connectIp");
		int port = settings.grabValueInt("connectPort");
		
		String username = settings.grabValueString("username");
		
		
		profile = new PacketPlayerProfile(username);
		
		client = new GameClient(ip, port, profile);
		client.startClient();
		
		
		statusElement = new TextElement(0, -10, UIAnchor.CENTER, UIAnchor.POSITIVE, UIAnchor.CENTER, UIAnchor.POSITIVE, "", 30, STATUS_ELEMENT_COLOR_ONE);
		statusElementColorCoolDown = new CoolDown(STATUS_ELEMENT_COLOR_COOL_DOWN);
		
		
		players = new HashMap<String, Player>();
		hasReceivedPlayers = false;
		isLoginCheckDone = false;
	}

	
	
	@Override
	protected void onDisable(Game game) {
		
	}

	
	
	
	
	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		
		
		if(client.isConnected()) {
			handlePlayerPackets();
		}
		
		
		
		if(!client.isConnected() && client.wasExceptionCaught()) {
			game.getGameStateManager().disableAllStates(game);
			game.getGameStateManager().getGameStateMenu().enable(game);
			
			return;
		}
		
		
		
		for(String username : players.keySet())
			players.get(username).tick(game);
		
		
		
		statusElementColorCoolDown.tick();
		
		statusElementColorCoolDown.executeIfReady(() -> {
			if(statusElement.getColor() == STATUS_ELEMENT_COLOR_ONE)
				statusElement.setColor(STATUS_ELEMENT_COLOR_TWO);
			else
				statusElement.setColor(STATUS_ELEMENT_COLOR_ONE);
		});
		
		
		////
		
		
		if(!hasReceivedPlayers) {
			if(!client.hasReceivedBaseData() || players.size() != client.getInitialPlayerCount())
				return;
			
			
			for(String username : players.keySet()) {
				Player player = players.get(username);
				
				
				if(player instanceof PlayerRemote) {
					PlayerRemote playerRemote = (PlayerRemote) player;
					
					if(!playerRemote.hasReceivedUpdate())
						return;
				}
			}
			
			
			hasReceivedPlayers = true;
		}
		
		
		
		//All data is guaranteed to have been received from here on////
		if(!isLoginCheckDone) {
			boolean isPlayerNotReady = false;
			
			for(String username : players.keySet()) {
				Player player = players.get(username);
				
				if(player instanceof PlayerLocal)
					continue;
				
				
				if(player.getAliveState() == 0) {
					isPlayerNotReady = true;
					break;
				}
			}
			
			
			
			if(isPlayerNotReady || players.size() <= 1)
				game.getGameStateManager().getGameStateInGamePrepare().enable(game);
			else
				player.setAliveState((byte) -1);
			
			
			isLoginCheckDone = true;
		}
	}

	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		if(!isEnabled)
			return;
		
		
		game.getCamera().translateToCamera(g, game);
		game.getCamera().translateCameraShake(g);
		
		
		for(String username : players.keySet())
			players.get(username).render(g, game);
		
		
		game.getCamera().translateNoCameraShake(g);
		game.getCamera().translateFromCamera(g);
		
		
		statusElement.render(g, game);
	}

	
	
	
	
	private void handlePlayerPackets() {
		client.forEachReceivedPacket((Packet packet) -> {
			
			if(packet.getID() == PacketID.PLAYER_PROFILE) {
				PacketPlayerProfile profilePacket = (PacketPlayerProfile) packet;
				
				if(!profilePacket.getUsername().equals(profile.getUsername())) {
					players.put(profilePacket.getUsername(), new PlayerRemote(profilePacket));
					
				} else {
					player = new PlayerLocal(SPAWN_X, SPAWN_Y, profile);
					players.put(profile.getUsername(), player);
				}
				
				
				packet.disposePacket();
				
				
			} else if(packet.getID() == PacketID.PLAYER_DISCONNECT) {
				PacketPlayerDisconnect disconnectPacket = (PacketPlayerDisconnect) packet;
				players.remove(disconnectPacket.getUsername());
				
				packet.disposePacket();
			}
		});
	}
	
	
	
	
	
	protected void setStatusText(String text) {
		statusElement.setText("| " + text + " |");
	}
	
	
	protected void resetStatusText() {
		statusElement.setText("");
	}
	
	
	
	
	
	
	protected PacketPlayerProfile getProfile() {
		return profile;
	}
	
	
	protected PlayerLocal getPlayer() {
		return player;
	}
	
	
	public GameClient getClient() {
		return client;
	}
	
	
	protected HashMap<String, Player> getPlayers() {
		return players;
	}
	
	
}
