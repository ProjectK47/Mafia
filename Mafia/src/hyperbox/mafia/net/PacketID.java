package hyperbox.mafia.net;

public enum PacketID {

	
	
	PLAYER_PROFILE((byte) 0),
	PLAYER_UPDATE((byte) 1),
	PLAYER_DISCONNECT((byte) 2),
	PLAYER_TALLY_UPDATE((byte) 3);
	
	
	
	private byte id;
	
	
	private PacketID(byte id) {
		this.id = id;
	}
	
	
	
	public byte getID() {
		return id;
	}
	
}
