package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;

import hyperbox.mafia.client.GameClient;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.io.Settings;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.net.PacketPlayerProfile;

public class GameStateInGame extends GameState {

	
	
	private GameClient client;
	
	
	
	@Override
	protected void onEnable(Game game) {
		Settings settings = game.getGameStateManager().getGameStateMenu().getSettings();
		
		String ip = settings.grabValueString("connectIp");
		int port = settings.grabValueInt("connectPort");
		
		String username = settings.grabValueString("username");
		
		
		PacketPlayerProfile profile = new PacketPlayerProfile(username);
		
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
					
					packet.disposePacket();
				}
			});
		}
		
		
		
		if(!client.isConnected() && client.wasExceptionCaught()) {
			game.getGameStateManager().disableAllStates(game);
			game.getGameStateManager().getGameStateMenu().enable(game);
		}
	}

	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		if(!isEnabled)
			return;
		
		
		game.getCamera().translateToCamera(g, game);
		game.getCamera().translateCameraShake(g);
		
		
		
		game.getCamera().translateNoCameraShake(g);
		game.getCamera().translateFromCamera(g);
	}

}
