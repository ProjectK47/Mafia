package hyperbox.mafia.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.sun.glass.events.KeyEvent;

import hyperbox.mafia.animation.CoolDown;
import hyperbox.mafia.client.GameClient;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.input.KeyboardInput;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.net.PacketPlayerProfile;
import hyperbox.mafia.net.PacketPlayerTallyUpdate;
import hyperbox.mafia.net.PacketPlayerUpdate;
import hyperbox.mafia.world.Tile;

public class PlayerLocal extends Player {
	
	
	public static final Color NAME_TAG_COLOR = new Color(128, 255, 255);
	
	public static final float VELOCITY_INCREASE = 1.25f;
	public static final float VELOCITY_DECREASE = 0.7f;
	public static final float TERMINAL_VELOCITY = 3.5f;
	
	
	private float velocityX = 0;
	private float velocityY = 0;
	
	private CoolDown animationCoolDown = new CoolDown(10, true);

	
	
	public PlayerLocal(float x, float y, PacketPlayerProfile profile) {
		super(x, y, profile, NAME_TAG_COLOR);
	}

	
	
	@Override
	protected void onTick(Game game) {
		
		//Move////
		if(KeyboardInput.isKeyDown(KeyEvent.VK_A, false)) {
			velocityX -= VELOCITY_INCREASE;
			
			if(velocityX < -TERMINAL_VELOCITY)
				velocityX = -TERMINAL_VELOCITY;
			
			direction = 3;
		}
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_D, false)) {
			velocityX += VELOCITY_INCREASE;
			
			if(velocityX > TERMINAL_VELOCITY)
				velocityX = TERMINAL_VELOCITY;
			
			direction = 2;
		}
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_W, false)) {
			velocityY -= VELOCITY_INCREASE;
			
			if(velocityY < -TERMINAL_VELOCITY)
				velocityY = -TERMINAL_VELOCITY;
			
			direction = 1;
		}
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_S, false)) {
			velocityY += VELOCITY_INCREASE;
			
			if(velocityY > TERMINAL_VELOCITY)
				velocityY = TERMINAL_VELOCITY;
			
			direction = 0;
		}
		
		
		
		//Animation////
		if(KeyboardInput.isKeyDown(KeyEvent.VK_W, false) || KeyboardInput.isKeyDown(KeyEvent.VK_S, false) ||
				KeyboardInput.isKeyDown(KeyEvent.VK_A, false) || KeyboardInput.isKeyDown(KeyEvent.VK_D, false)) {
			
			animationCoolDown.tick();
			
			animationCoolDown.executeIfReady(() -> {
				if(animationStage == 0 || animationStage == 2)
					animationStage = 1;
				else if(animationStage == 1)
					animationStage = 2;
			});
			
		} else {
			animationStage = 0;
			animationCoolDown.rushCurrentCoolDown();
		}
		
		
		
		
		//Decrease velocity////
		if(velocityX > 0) {
			if(velocityX - VELOCITY_DECREASE > 0)
				velocityX -= VELOCITY_DECREASE;
			else
				velocityX = 0;
			
		} else if(velocityX < 0) {
			if(velocityX + VELOCITY_DECREASE < 0)
				velocityX += VELOCITY_DECREASE;
			else
				velocityX = 0;
		}
		
		
		if(velocityY > 0) {
			if(velocityY - VELOCITY_DECREASE > 0)
				velocityY -= VELOCITY_DECREASE;
			else
				velocityY = 0;
			
		} else if(velocityY < 0) {
			if(velocityY + VELOCITY_DECREASE < 0)
				velocityY += VELOCITY_DECREASE;
			else
				velocityY = 0;
		}
		
		
		
		//Apply velocity////
		float lastX = x;
		float lastY = y;
		
		x += velocityX;
		y += velocityY;
		
		
		
		Tile tile = game.getMap().grabTileAtCoords(x, y, game);
		
		if(tile == Tile.WATER) {
			x = lastX;
			y = lastY;
		}
		
		
		
		
		
		
		
		//Packet updates////
		GameClient client = game.getGameStateManager().getGameStateInGame().getClient();
		
		
		client.forEachReceivedPacket((Packet packet) -> {
			
			//Tally count////
			if(packet.getID() == PacketID.PLAYER_TALLY_UPDATE) {
				PacketPlayerTallyUpdate tallyPacket = (PacketPlayerTallyUpdate) packet;
				
				if(tallyPacket.getUsername().equals(profile.getUsername()))
					tallyCount += tallyPacket.getTallyCountChange();
				
				
				packet.disposePacket();
			}
		});
		
		
		
		
		//Send update packet////
		PacketPlayerUpdate updatePacket = new PacketPlayerUpdate(profile.getUsername(), x, y, animationStage, direction, aliveState, tallyCount);
		client.sendPacket(updatePacket);
	}

	
	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		
	}

	
}
