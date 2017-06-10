package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import hyperbox.mafia.client.GameClient;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Entity;
import hyperbox.mafia.entity.Player;
import hyperbox.mafia.entity.PlayerLocal;
import hyperbox.mafia.entity.PlayerRemote;
import hyperbox.mafia.io.Settings;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.net.PacketPlayerDisconnect;
import hyperbox.mafia.net.PacketPlayerProfile;

public class GameStateInGame extends GameState {

	
	
	private PacketPlayerProfile profile;
	private GameClient client;
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	
	
	@Override
	protected void onEnable(Game game) {
		Settings settings = game.getGameStateManager().getGameStateMenu().getSettings();
		
		String ip = settings.grabValueString("connectIp");
		int port = settings.grabValueInt("connectPort");
		
		String username = settings.grabValueString("username");
		
		
		profile = new PacketPlayerProfile(username);
		
		client = new GameClient(ip, port, profile);
		client.startClient();
	}

	
	
	@Override
	protected void onDisable(Game game) {
		
	}

	
	
	
	
	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		
		if(client.isConnected()) {
			
			client.forEachReceivedPacket((Packet packet) -> {
				if(packet.getID() == PacketID.PLAYER_PROFILE) {
					PacketPlayerProfile packetPlayerProfile = (PacketPlayerProfile) packet;
					System.out.println(packetPlayerProfile.getUsername());
					
					if(packetPlayerProfile.getUsername().equals(profile.getUsername()))
						entities.add(new PlayerLocal(0, 0, profile));
					else
						entities.add(new PlayerRemote(packetPlayerProfile));
					
					
					packet.disposePacket();
					
					
				} else if(packet.getID() == PacketID.PLAYER_DISCONNECT) {
					PacketPlayerDisconnect packetPlayerDisconnect = (PacketPlayerDisconnect) packet;
					
					Iterator<Entity> it = entities.iterator();
					while(it.hasNext()) {
						Player player = (Player) it.next();
						
						if(player.getProfile().getUsername().equals(packetPlayerDisconnect.getUsername()))
							it.remove();
					}
					
					
					packet.disposePacket();
				}
			});
		}
		
		
		
		if(!client.isConnected() && client.wasExceptionCaught()) {
			game.getGameStateManager().disableAllStates(game);
			game.getGameStateManager().getGameStateMenu().enable(game);
		}
		
		
		
		for(Entity entity : entities)
			entity.tick(game);
	}

	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		if(!isEnabled)
			return;
		
		
		game.getCamera().translateToCamera(g, game);
		game.getCamera().translateCameraShake(g);
		
		
		for(Entity entity : entities)
			entity.render(g, game);
		
		
		game.getCamera().translateNoCameraShake(g);
		game.getCamera().translateFromCamera(g);
	}

	
	
	
	
	public GameClient getClient() {
		return client;
	}
	
	
}
