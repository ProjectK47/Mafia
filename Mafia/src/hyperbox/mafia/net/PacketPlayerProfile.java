package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerProfile extends Packet {

	
	private String username;
	private int avatar;
	
	
	
	public PacketPlayerProfile(String username, int avatar) {
		super(PacketID.PLAYER_PROFILE);
		
		this.username = username;
		this.avatar = avatar;
	}

	
	public PacketPlayerProfile(DataInputStream in) throws IOException {
		super(in, PacketID.PLAYER_PROFILE);
	}


	
	
	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		this.username = in.readUTF();
		this.avatar = in.readInt();
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		out.writeInt(avatar);
	}

	
	
	
	
	public String getUsername() {
		return username;
	}
	
	
	public int getAvatar() {
		return avatar;
	}
	
}
