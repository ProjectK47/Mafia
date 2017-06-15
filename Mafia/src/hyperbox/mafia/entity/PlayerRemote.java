package hyperbox.mafia.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import hyperbox.mafia.client.GameClient;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.net.PacketPlayerProfile;
import hyperbox.mafia.net.PacketPlayerTallyUpdate;
import hyperbox.mafia.net.PacketPlayerUpdate;
import hyperbox.mafia.utils.NumberUtils;

public class PlayerRemote extends Player {
	
	
	public static final Color NAME_TAG_COLOR = new Color(255, 255, 230);
	
	public static final float POSITION_LERP_MUL = 0.6f;

	
	private float goalX;
	private float goalY;
	
	private boolean hasReceivedUpdate = false;
	
	
	
	public PlayerRemote(PacketPlayerProfile profile) {
		super(0, 0, profile, NAME_TAG_COLOR);
		
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
					
					
					//Alive state////
					aliveState = updatePacket.getAliveState();
					
					
					//Tally count////
					tallyCount = updatePacket.getTallyCount();
					
					
					packet.disposePacket();
					hasReceivedUpdate = true;
				}
			}
		});
	}

	
	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		
	}
	
	
	
	
	@Override
	public void incrementTally(Game game) {
		byte oldTally = tallyCount;
		
		super.incrementTally(game);
		sendUpdateTallyPacket(oldTally, game);
	}
	
	@Override
	public void decrementTally(Game game) {
		byte oldTally = tallyCount;
		
		super.decrementTally(game);
		sendUpdateTallyPacket(oldTally, game);
	}
	
	@Override
	public void resetTally(Game game) {
		byte oldTally = tallyCount;
		
		super.resetTally(game);
		sendUpdateTallyPacket(oldTally, game);
	}
	
	@Override
	public void disableTally(Game game) {
		byte oldTally = tallyCount;
		
		super.disableTally(game);
		sendUpdateTallyPacket(oldTally, game);
	}
	
	
	
	private void sendUpdateTallyPacket(byte oldTally, Game game) {
		byte tallyChange = (byte) (tallyCount - oldTally);
		
		PacketPlayerTallyUpdate packet = new PacketPlayerTallyUpdate(profile.getUsername(), tallyChange);
		game.getGameStateManager().getGameStateInGame().getClient().sendPacket(packet);
	}
	
	
	
	
	public boolean hasReceivedUpdate() {
		return hasReceivedUpdate;
	}
	

}
