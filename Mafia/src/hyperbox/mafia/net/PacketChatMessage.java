package hyperbox.mafia.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import hyperbox.mafia.ui.ChatMessage;

public class PacketChatMessage extends Packet {
	
	
	private ChatMessage message;
	
	
	
	public PacketChatMessage(ChatMessage message) {
		super(PacketID.CHAT_MESSAGE);
		
		this.message = message;
	}
	
	
	
	public PacketChatMessage(DataInputStream in) throws IOException {
		super(in, PacketID.CHAT_MESSAGE);
	}


	
	
	@Override
	protected void readPacket(DataInputStream in) throws IOException {
		String sender = in.readUTF();
		String message = in.readUTF();
		boolean isBold = in.readBoolean();
		
		this.message = new ChatMessage(sender, message, isBold);
	}


	@Override
	protected void onWritePacket(DataOutputStream out) throws IOException {
		out.writeUTF(message.getSender());
		out.writeUTF(message.getMessage());
		
		out.writeBoolean(message.isBold());
	}

	
	
	

	
	
	public ChatMessage getMessage() {
		return message;
	}

}
