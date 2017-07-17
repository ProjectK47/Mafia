package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketEliminationNextStage extends Packet {

	
	
	public PacketEliminationNextStage() {
		super(PacketID.ELIMINATION_NEXT_STAGE);
	}

	
	public PacketEliminationNextStage(DataInputStream in) throws IOException {
		super(in, PacketID.ELIMINATION_NEXT_STAGE);
	}


	
	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		
	}
	

}
