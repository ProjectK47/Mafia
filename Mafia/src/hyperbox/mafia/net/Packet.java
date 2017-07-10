package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet {

	
	
	private PacketID id;
	
	private boolean isDisposed = false;
	
	
	
	public Packet(PacketID id) {
		this.id = id;
	}
	
	
	public Packet(DataInputStream in, PacketID id) throws IOException {
		this.id = id;
		
		readPacket(in);
	}
	
	
	
	
	public void writePacket(DataOutputStream out) throws IOException {
		writeID(out);
		onWritePacket(out);
		
		out.flush();
	}
	
	
	
	
	private void writeID(DataOutputStream out) throws IOException {
		out.write(id.getID());
	}
	
	
	
	protected abstract void readPacket(DataInputStream in) throws IOException;
	protected abstract void onWritePacket(DataOutputStream out) throws IOException;
	
	
	
	
	
	
	public static byte readID(DataInputStream in) throws IOException {
		int b = in.read();
		
		if(b == -1)
			throw new IOException("Read returned -1");
		
		
		return (byte) b;
	}
	
	
	
	
	
	public static Packet readPacketByID(DataInputStream in, byte packetID) throws IOException {
		Packet packet = null;
		
		
		if(packetID == PacketID.PLAYER_PROFILE.getID())
			packet = new PacketPlayerProfile(in);
		else if(packetID == PacketID.PLAYER_UPDATE.getID())
			packet = new PacketPlayerUpdate(in);
		else if(packetID == PacketID.PLAYER_DISCONNECT.getID())
			packet = new PacketPlayerDisconnect(in);
		else if(packetID == PacketID.PLAYER_TALLY_UPDATE.getID())
			packet = new PacketPlayerTallyUpdate(in);
		else if(packetID == PacketID.CHOOSE_PRIMARY.getID())
			packet = new PacketChoosePrimary(in);
		else if(packetID == PacketID.SPAWN_POINTER.getID())
			packet = new PacketSpawnPointer(in);
		else if(packetID == PacketID.CHAT_MESSAGE.getID())
			packet = new PacketChatMessage(in);
		else if(packetID == PacketID.ELIMINATION_CHOICE.getID())
			packet = new PacketEliminationChoice(in);
		else if(packetID == PacketID.PLAYER_STATE_ACTION.getID())
			packet = new PacketPlayerStateAction(in);
		else if(packetID == PacketID.ELIMINATION_ROUND_COMPLETE.getID())
			packet = new PacketEliminationRoundComplete(in);
		
		
		return packet;
	}
	
	
	
	
	public void disposePacket() {
		isDisposed = true;
	}
	
	
	
	public PacketID getID() {
		return id;
	}
	
	
	public boolean isDisposed() {
		return isDisposed;
	}
	
}
