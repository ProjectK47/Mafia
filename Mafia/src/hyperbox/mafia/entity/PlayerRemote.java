package hyperbox.mafia.entity;

import java.awt.Graphics2D;

import hyperbox.mafia.client.GameClient;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.net.PacketPlayerProfile;
import hyperbox.mafia.net.PacketPlayerUpdate;
import hyperbox.mafia.utils.NumberUtils;

public class PlayerRemote extends Player {
	
	
	public static final float POSITION_LERP_MUL = 0.6f;

	
	private float goalX;
	private float goalY;
	
	
	
	public PlayerRemote(PacketPlayerProfile profile) {
		super(0, 0, profile);
		
		this.goalX = x;
		this.goalY = y;
	}

	
	
	
	@Override
	protected void onTick(Game game) {
		GameClient client = game.getGameStateManager().getGameStateInGame().getClient();
		
		
		client.forEachReceivedPacket((Packet packet) -> {
			if(packet.getID() == PacketID.PLAYER_UPDATE) {
				
				PacketPlayerUpdate updatePacket = (PacketPlayerUpdate) packet;
				
				
				if(updatePacket.getUsername().equals(profile.getUsername())) {
					
					//Move////
					goalX = updatePacket.getX();
					goalY = updatePacket.getY();
					
					float diffX = Math.abs(goalX - x);
					float diffY = Math.abs(goalY - y);
					
					x = NumberUtils.lerp(x, goalX, diffX * POSITION_LERP_MUL);
					y = NumberUtils.lerp(y, goalY, diffY * POSITION_LERP_MUL);
					
					
					//Animation////
					animationStage = updatePacket.getAnimationStage();
					direction = updatePacket.getDirection();
					
					
					packet.disposePacket();
				}
			}
		});
	}

	
	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		
	}
	

}
