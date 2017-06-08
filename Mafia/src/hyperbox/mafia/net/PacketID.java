package hyperbox.mafia.net;

public enum PacketID {

	
	
	PLAYER_PROFILE((byte) 0);
	
	
	
	private byte id;
	
	
	private PacketID(byte id) {
		this.id = id;
	}
	
	
	
	public byte getID() {
		return id;
	}
	
}
