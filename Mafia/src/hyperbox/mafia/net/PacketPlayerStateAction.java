package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerStateAction extends Packet {

	
	private String username;
	private boolean isExplodeAction;
	
	
	
	public PacketPlayerStateAction(String username, boolean isExplodeAction) {
		super(PacketID.PLAYER_STATE_ACTION);
		
		this.username = username;
		this.isExplodeAction = isExplodeAction;
	}
	
	
	public PacketPlayerStateAction(DataInputStream in) throws IOException {
		super(in, PacketID.PLAYER_STATE_ACTION);
	}


	
	
	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		this.username = in.readUTF();
		this.isExplodeAction = in.readBoolean();
	}

	
	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		out.writeBoolean(isExplodeAction);
	}

	
	
	
	public String getUsername() {
		return username;
	}
	
	
	public boolean isExplodeAction() {
		return isExplodeAction;
	}

}
