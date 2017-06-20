package hyperbox.mafia.ui;

public class ChatMessage {

	
	private String sender;
	private String message;
	
	private boolean isBold;
	
	
	
	public ChatMessage(String sender, String message, boolean isBold) {
		this.sender = sender;
		this.message = message;
		
		this.isBold = isBold;
	}
	
	
	
	
	
	public String grabFormattedMessage() {
		String formattedMessage = sender + "> " + message;
		
		return formattedMessage;
	}
	
	
	
	
	public String getSender() {
		return sender;
	}
	
	
	public String getMessage() {
		return message;
	}
	
	
	public boolean isBold() {
		return isBold;
	}
	
}
