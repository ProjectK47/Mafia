package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerUpdate extends Packet {
	
	
	private String username;
	
	private float x;
	private float y;
	
	private byte animationStage;
	private byte direction;
	
	private byte aliveState;
	
	private boolean isSleeping;
	
	private byte tallyCount;
	

	
	public PacketPlayerUpdate(String username, float x, float y, byte animationStage, byte direction, byte aliveState, boolean isSleeping, byte tallyCount) {
		super(PacketID.PLAYER_UPDATE);
		
		
		this.username = username;
		
		this.x = x;
		this.y = y;
		
		this.animationStage = animationStage;
		this.direction = direction;
		
		this.aliveState = aliveState;
		
		this.isSleeping = isSleeping;
		
		this.tallyCount = tallyCount;
	}
	
	
	public PacketPlayerUpdate(DataInputStream in) throws IOException {
		super(in, PacketID.PLAYER_UPDATE);
	}

	
	
	

	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		this.username = in.readUTF();
		
		this.x = in.readFloat();
		this.y = in.readFloat();
		
		this.animationStage = in.readByte();
		this.direction = in.readByte();
		
		this.aliveState = in.readByte();
		
		this.isSleeping = in.readBoolean();
		
		this.tallyCount = in.readByte();
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		
		out.writeFloat(x);
		out.writeFloat(y);
		
		out.writeByte(animationStage);
		out.writeByte(direction);
		
		out.writeByte(aliveState);
		
		out.writeBoolean(isSleeping);
		
		out.writeByte(tallyCount);
	}

	
	
	
	
	
	public String getUsername() {
		return username;
	}
	
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	
	public byte getAnimationStage() {
		return animationStage;
	}
	
	public byte getDirection() {
		return direction;
	}
	
	
	public byte getAliveState() {
		return aliveState;
	}
	
	
	public boolean isSleeping() {
		return isSleeping;
	}
	
	
	public byte getTallyCount() {
		return tallyCount;
	}

}
