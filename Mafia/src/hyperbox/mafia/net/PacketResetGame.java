package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketResetGame extends Packet {

	
	public PacketResetGame() {
		super(PacketID.RESET_GAME);
	}
	
	
	public PacketResetGame(DataInputStream in) throws IOException {
		super(in, PacketID.RESET_GAME);
	}


	
	
	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		
	}

	

}
