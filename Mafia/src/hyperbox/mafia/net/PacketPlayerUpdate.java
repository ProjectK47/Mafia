package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerUpdate extends Packet {
	
	
	private String username;
	
	private float x;
	private float y;
	
	private int animationStage;
	private int direction;
	

	
	public PacketPlayerUpdate(String username, float x, float y, int animationStage, int direction) {
		super(PacketID.PLAYER_UPDATE);
		
		
		this.username = username;
		
		this.x = x;
		this.y = y;
		
		this.animationStage = animationStage;
		this.direction = direction;
	}
	
	
	public PacketPlayerUpdate(DataInputStream in) throws IOException {
		super(in, PacketID.PLAYER_UPDATE);
	}

	
	
	

	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		this.username = in.readUTF();
		
		this.x = in.readFloat();
		this.y = in.readFloat();
		
		this.animationStage = in.readInt();
		this.direction = in.readInt();
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		
		out.writeFloat(x);
		out.writeFloat(y);
		
		out.writeInt(animationStage);
		out.writeInt(direction);
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
	
	
	public int getAnimationStage() {
		return animationStage;
	}
	
	public int getDirection() {
		return direction;
	}

}
