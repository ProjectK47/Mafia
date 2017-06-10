package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerDisconnect extends Packet {

	
	private String username;
	
	
	
	public PacketPlayerDisconnect(String username) {
		super(PacketID.PLAYER_DISCONNECT);
		
		this.username = username;
	}
	
	
	public PacketPlayerDisconnect(DataInputStream in) throws IOException {
		super(in, PacketID.PLAYER_DISCONNECT);
	}

	
	

	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		this.username = in.readUTF();
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeUTF(username);
	}

	
	
	
	
	public String getUsername() {
		return username;
	}

}
