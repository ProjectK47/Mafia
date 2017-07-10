package hyperbox.mafia.net;

public enum PacketID {

	
	
	PLAYER_PROFILE((byte) 0),
	PLAYER_UPDATE((byte) 1),
	PLAYER_DISCONNECT((byte) 2),
	PLAYER_TALLY_UPDATE((byte) 3),
	CHOOSE_PRIMARY((byte) 4),
	SPAWN_POINTER((byte) 5),
	CHAT_MESSAGE((byte) 6),
	ELIMINATION_CHOICE((byte) 7),
	PLAYER_STATE_ACTION((byte) 8),
	ELIMINATION_ROUND_COMPLETE((byte) 9);
	
	
	
	private byte id;
	
	
	private PacketID(byte id) {
		this.id = id;
	}
	
	
	
	public byte getID() {
		return id;
	}
	
}
