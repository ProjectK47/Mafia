package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketSpawnPointer extends Packet {

	
	private String username;
	
	private float targetX;
	private float targetY;
	
	private boolean isPrimaryPointer;
	
	
	
	public PacketSpawnPointer(String username, float targetX, float targetY, boolean isPrimaryPointer) {
		super(PacketID.SPAWN_POINTER);
		
		
		this.username = username;
		
		this.targetX = targetX;
		this.targetY = targetY;
		
		this.isPrimaryPointer = isPrimaryPointer;
	}
	
	
	public PacketSpawnPointer(DataInputStream in) throws IOException {
		super(in, PacketID.SPAWN_POINTER);
	}


	
	
	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		this.username = in.readUTF();
		
		this.targetX = in.readFloat();
		this.targetY = in.readFloat();
		
		this.isPrimaryPointer = in.readBoolean();
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		
		out.writeFloat(targetX);
		out.writeFloat(targetY);
		
		out.writeBoolean(isPrimaryPointer);
	}


	
	
	

	public String getUsername() {
		return username;
	}
	
	
	public float getTargetX() {
		return targetX;
	}

	public float getTargetY() {
		return targetY;
	}
	
	
	public boolean isPrimaryPointer() {
		return isPrimaryPointer;
	}

}
