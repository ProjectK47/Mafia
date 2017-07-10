package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketEliminationRoundComplete extends Packet {

	
	
	public PacketEliminationRoundComplete() {
		super(PacketID.ELIMINATION_ROUND_COMPLETE);
	}

	
	public PacketEliminationRoundComplete(DataInputStream in) throws IOException {
		super(in, PacketID.ELIMINATION_ROUND_COMPLETE);
	}


	
	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		
	}
	

}
