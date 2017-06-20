package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketChoosePrimary extends Packet {
	
	
	public static final byte PRIMARY_TYPE_STORYTELLER = 0;
	public static final byte PRIMARY_TYPE_MAFIA = 1;
	public static final byte PRIMARY_TYPE_DOCTOR = 2;
	
	
	private String username;
	private byte primaryType;
	
	
	
	public PacketChoosePrimary(String username, byte primaryType) {
		super(PacketID.CHOOSE_PRIMARY);
		
		this.username = username;
		this.primaryType = primaryType;
	}
	
	
	public PacketChoosePrimary(DataInputStream in) throws IOException {
		super(in, PacketID.CHOOSE_PRIMARY);
	}

	
	

	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		this.username = in.readUTF();
		this.primaryType = in.readByte();
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		out.writeByte(primaryType);
	}

	
	
	
	
	public String getUsername() {
		return username;
	}
	
	
	public byte getPrimaryType() {
		return primaryType;
	}

}
