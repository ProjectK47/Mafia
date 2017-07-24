package hyperbox.mafia.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import hyperbox.mafia.animation.CoolDown;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.io.AudioResources;
import hyperbox.mafia.io.FontResources;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketChatMessage;
import hyperbox.mafia.net.PacketID;

public class ChatElement extends UIElement {

	
	public static final int MAX_MESSAGES = 20;
	public static final int MAX_MESSAGE_LENGTH = 100;
	
	public static final float TEXT_SCALE = 0.85f;
	public static final Color TEXT_COLOR = Color.WHITE;
	public static final float TEXT_SPACE = 1.25f;
	public static final float TEXT_BASE_HEIGHT = 2f;
	
	public static final int LAST_MESSAGE_TRANSPARENCY = 125;
	
	public static final float VISIBLE_STAGE_DECREASE = 0.04f;
	public static final int VISIBLE_STAGE_TIME = 300;
	
	
	private float size;
	
	private TextBoxElement inputElement;
	
	
	private ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
	
	private boolean isInVisibleStage = false;
	private float visibleStage = 0f;
	private CoolDown visibleStageCoolDown = new CoolDown(VISIBLE_STAGE_TIME);
	
	
	
	public ChatElement(int x, int y, float size, String localUserUsername, Game game) {	
		super(x, y, 0, 0, UIAnchor.NEGATIVE, UIAnchor.POSITIVE, UIAnchor.NEGATIVE, UIAnchor.POSITIVE, true);
		
		
		this.size = size;
		
		inputElement = new TextBoxElement(x, y, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY,
				size, "", "Chat", UIAnchor.NEGATIVE, MAX_MESSAGE_LENGTH, TextBoxElement.ALL_CHARS,
				() -> {
					if(!inputElement.isEmpty()) {
						ChatMessage message = new ChatMessage(localUserUsername, inputElement.getText(), false);
						
						addMessage(message, true, game);
					}
					
					
					inputElement.clearText();
					inputElement.removeFocus();
				}, true);
	}

	
	
	
	@Override
	public void onTick(Game game) {
		width = inputElement.getWidth();
		height = inputElement.getHeight();
		
		inputElement.tick(game);
		
		
		game.getGameStateManager().getGameStateInGame().getClient().forEachReceivedPacket((Packet packet) -> {
			if(packet.getID() == PacketID.CHAT_MESSAGE) {
				PacketChatMessage messagePacket = (PacketChatMessage) packet;
				
				addMessage(messagePacket.getMessage(), false, game);
				
				
				packet.disposePacket();
			}
		});
		
		
		
		
		if(inputElement.isFocused()) {
			resetVisibleStage();
			
		} else if(!inputElement.isFocused() && isInVisibleStage) {
			visibleStageCoolDown.tick();
			
			visibleStageCoolDown.executeIfReady(() -> {
				isInVisibleStage = false;
			});
		}
		
		
		if(!isInVisibleStage) {
			if(visibleStage > 0)
				visibleStage -= VISIBLE_STAGE_DECREASE;
			
			if(visibleStage < 0)
				visibleStage = 0f;
		}
	}

	
	
	@Override
	public void render(Graphics2D g, Game game) {
		inputElement.render(g, game);
		
		
		
		if(visibleStage <= 0)
			return;
		
		
		for(int i = 0; i < messages.size(); i ++) {
			ChatMessage message = messages.get(messages.size() - 1 - i);
			
			
			int transparency = 255;
			
			if(i == MAX_MESSAGES - 1)
				transparency = LAST_MESSAGE_TRANSPARENCY;
			
			g.setColor(new Color(TEXT_COLOR.getRed(), TEXT_COLOR.getGreen(), TEXT_COLOR.getBlue(), (int) (transparency * visibleStage)));
			
			
			if(!message.isBold())
				g.setFont(FontResources.mainFont.deriveFont(TEXT_SCALE * size));
			else
				g.setFont(FontResources.mainFontBold.deriveFont(TEXT_SCALE * size));
			
			
			String messageText = message.grabFormattedMessage();
			
			g.drawString(messageText, this.grabAnchoredX(game) - (width / 2), this.grabAnchoredY(game) - (TEXT_BASE_HEIGHT * size) - (TEXT_SPACE * size * (i + 1)));
		}
	}

	
	
	
	
	
	public void addMessage(ChatMessage message, boolean shareMessage, Game game) {
		messages.add(message);
		
		if(messages.size() > MAX_MESSAGES)
			messages.remove(0);
			
		
		if(shareMessage) {
			PacketChatMessage messagePacket = new PacketChatMessage(message);
			game.getGameStateManager().getGameStateInGame().getClient().sendPacket(messagePacket);
		}
		
		
		
		resetVisibleStage();
		
		AudioResources.notificationClick.playAudio();
	}
	
	
	
	
	private void resetVisibleStage() {
		isInVisibleStage = true;
		visibleStage = 1f;
	}
	
	
	
	
	
	public float getSize() {
		return size;
	}
	
}
