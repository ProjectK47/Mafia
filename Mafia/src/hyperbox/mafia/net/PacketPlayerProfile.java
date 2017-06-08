package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerProfile extends Packet {

	
	private String username;
	
	
	
	public PacketPlayerProfile(String username) {
		super(PacketID.PLAYER_PROFILE);
		
		this.username = username;
	}

	
	public PacketPlayerProfile(DataInputStream in) throws IOException {
		super(in, PacketID.PLAYER_PROFILE);
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
