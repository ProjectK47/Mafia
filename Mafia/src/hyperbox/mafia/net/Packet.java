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