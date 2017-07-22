package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketSoundEffect extends Packet {

	
	private int soundEffectEntry;
	
	
	
	public PacketSoundEffect(int soundEffectEntry) {
		super(PacketID.SOUND_EFFECT);
		
		this.soundEffectEntry = soundEffectEntry;
	}
	
	
	public PacketSoundEffect(DataInputStream in) throws IOException {
		super(in, PacketID.SOUND_EFFECT);
	}


	
	
	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		this.soundEffectEntry = in.readInt();
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeInt(soundEffectEntry);
	}

	
	
	
	public int getSoundEffectEntry() {
		return soundEffectEntry;
	}

}
