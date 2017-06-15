package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerTallyUpdate extends Packet {

	
	private String username;
	private byte tallyCountChange;
	
	
	
	public PacketPlayerTallyUpdate(String username, byte tallyCountChange) {
		super(PacketID.PLAYER_TALLY_UPDATE);
		
		this.username = username;
		this.tallyCountChange = tallyCountChange;
	}
	
	
	public PacketPlayerTallyUpdate(DataInputStream in) throws IOException {
		super(in, PacketID.PLAYER_TALLY_UPDATE);
	}

	
	

	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		this.username = in.readUTF();
		this.tallyCountChange = in.readByte();
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		out.writeByte(tallyCountChange);
	}

	
	
	
	
	public String getUsername() {
		return username;
	}
	
	
	public byte getTallyCountChange() {
		return tallyCountChange;
	}

}
