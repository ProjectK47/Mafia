package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketEliminationChoice extends Packet {

	
	private String username;
	private boolean isMafiaChoice;
	
	
	
	public PacketEliminationChoice(String username, boolean isMafiaChoice) {
		super(PacketID.ELIMINATION_CHOICE);
		
		this.username = username;
		this.isMafiaChoice = isMafiaChoice;
	}
	
	
	public PacketEliminationChoice(DataInputStream in) throws IOException {
		super(in, PacketID.ELIMINATION_CHOICE);
	}

	
	

	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		this.username = in.readUTF();
		this.isMafiaChoice = in.readBoolean();
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		out.writeBoolean(isMafiaChoice);
	}

	
	
	
	public String getUsername() {
		return username;
	}
	
	
	public boolean isMafiaChoice() {
		return isMafiaChoice;
	}

}
