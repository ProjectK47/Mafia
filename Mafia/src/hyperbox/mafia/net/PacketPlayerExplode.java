package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerExplode extends Packet {

	
	private String username;
	
	
	
	public PacketPlayerExplode(String username) {
		super(PacketID.PLAYER_EXPLODE);
		
		this.username = username;
	}
	
	
	public PacketPlayerExplode(DataInputStream in) throws IOException {
		super(in, PacketID.PLAYER_EXPLODE);
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
