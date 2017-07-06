package hyperbox.mafia.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import hyperbox.mafia.animation.CoolDown;
import hyperbox.mafia.client.GameClient;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.entity.Player;
import hyperbox.mafia.entity.PlayerLocal;
import hyperbox.mafia.entity.PlayerRemote;
import hyperbox.mafia.io.Settings;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketChoosePrimary;
import hyperbox.mafia.net.PacketID;
import hyperbox.mafia.net.PacketPlayerDisconnect;
import hyperbox.mafia.net.PacketPlayerProfile;
import hyperbox.mafia.particle.Particle;
import hyperbox.mafia.ui.ChatElement;
import hyperbox.mafia.ui.ChatMessage;
import hyperbox.mafia.ui.TextElement;
import hyperbox.mafia.ui.UIAnchor;

public class GameStateInGame extends GameState {

	
	public static final float SPAWN_X = 0f;
	public static final float SPAWN_Y = 0F;
	
	public static final Color STATUS_ELEMENT_COLOR_ONE = new Color(255, 204, 153);
	public static final Color STATUS_ELEMENT_COLOR_TWO = new Color(255, 243, 230);
	public static final int STATUS_ELEMENT_COLOR_COOL_DOWN = 60;
	
	public static final Color STORYTELLER_NAME_TAG_COLOR = new Color(89, 0, 179);
	
	
	private PacketPlayerProfile profile;
	private PlayerLocal player;
	private GameClient client;
	
	private TextElement statusElement;
	private CoolDown statusElementColorCoolDown;
	
	private TextElement tipElement;
	
	private ChatElement chatElement;
	
	
	private HashMap<String, Player> players;
	private boolean hasReceivedPlayers;
	private boolean isLoginCheckDone;
	
	private ArrayList<Particle> particles;
	
	private String storytellerUsername;
	private String mafiaUsername;
	private String doctorUsername;
	
	
	
	@Override
	protected void onEnable(Game game) {
		Settings settings = game.getGameStateManager().getGameStateMenu().getSettings();
		
		String ip = settings.grabValueString("connectIp");
		int port = settings.grabValueInt("connectPort");
		
		String username = settings.grabValueString("username");
		
		
		profile = new PacketPlayerProfile(username);
		
		client = new GameClient(ip, port, profile);
		client.startClient();
		
		
		statusElement = new TextElement(0, -10, UIAnchor.CENTER, UIAnchor.POSITIVE, UIAnchor.CENTER, UIAnchor.POSITIVE, "", 20, STATUS_ELEMENT_COLOR_ONE);
		statusElementColorCoolDown = new CoolDown(STATUS_ELEMENT_COLOR_COOL_DOWN);
		
		tipElement = new TextElement(-20, 30, UIAnchor.POSITIVE, UIAnchor.NEGATIVE, UIAnchor.POSITIVE, UIAnchor.NEGATIVE, "", 19, new Color(255, 255, 204));
		
		chatElement = new ChatElement(15, -15, 20, username, game);
		
		
		players = new HashMap<String, Player>();
		hasReceivedPlayers = false;
		isLoginCheckDone = false;
		
		particles = new ArrayList<Particle>();
		
		storytellerUsername = null;
		mafiaUsername = null;
		doctorUsername = null;
	}

	
	
	@Override
	protected void onDisable(Game game) {
		
	}

	
	
	
	
	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		
		
		if(client.isConnected()) {
			handlePlayerPackets();
			handlePrimaryChoosePackets(game);
		}
		
		
		
		if(!client.isConnected() && client.wasExceptionCaught()) {
			game.getGameStateManager().disableAllStates(game);
			game.getGameStateManager().getGameStateMenu().enable(game);
			
			return;
		}
		
		
		
		
		for(String username : players.keySet())
			players.get(username).tick(game);
		
		
		
		Iterator<Particle> it = particles.iterator();
		
		while(it.hasNext()) {
			Particle particle = it.next();
			
			
			particle.tick(game);
			
			if(particle.isParticleDead())
				it.remove();
		}
		
		
		
		
		statusElementColorCoolDown.tick();
		
		statusElementColorCoolDown.executeIfReady(() -> {
			if(statusElement.getColor() == STATUS_ELEMENT_COLOR_ONE)
				statusElement.setColor(STATUS_ELEMENT_COLOR_TWO);
			else
				statusElement.setColor(STATUS_ELEMENT_COLOR_ONE);
		});
		
		
		tipElement.tick(game);
		
		chatElement.tick(game);
		
		
		////
		
		
		if(!hasReceivedPlayers) {
			if(!client.hasReceivedBaseData() || players.size() != client.getInitialPlayerCount())
				return;
			
			
			for(String username : players.keySet()) {
				Player player = players.get(username);
				
				
				if(player instanceof PlayerRemote) {
					PlayerRemote playerRemote = (PlayerRemote) player;
					
					if(!playerRemote.hasReceivedUpdate())
						return;
				}
			}
			
			
			hasReceivedPlayers = true;
		}
		
		
		
		//All data is guaranteed to have been received from here on////
		if(!isLoginCheckDone) {
			boolean isPlayerNotReady = false;
			
			for(String username : players.keySet()) {
				Player player = players.get(username);
				
				if(player instanceof PlayerLocal)
					continue;
				
				
				if(player.getAliveState() == 0) {
					isPlayerNotReady = true;
					break;
				}
			}
			
			
			
			if(isPlayerNotReady || players.size() <= 1)
				game.getGameStateManager().getGameStateInGamePrepare().enable(game);
			else
				player.setAliveState((byte) -1);
			
			
			isLoginCheckDone = true;
		}
	}

	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		if(!isEnabled)
			return;
		
		
		game.getCamera().translateToCamera(g, game);
		game.getCamera().translateCameraShake(g);
		
		
		for(String username : players.keySet())
			players.get(username).render(g, game);
		
		
		for(Particle particle : particles)
			particle.render(g, game);
		
		
		game.getCamera().translateNoCameraShake(g);
		game.getCamera().translateFromCamera(g);
		
		
		if(player != null)
			player.renderSleepBars(g, game);
		
		statusElement.render(g, game);
		tipElement.render(g, game);
		chatElement.render(g, game);
	}

	
	
	
	
	private void handlePlayerPackets() {
		client.forEachReceivedPacket((Packet packet) -> {
			
			if(packet.getID() == PacketID.PLAYER_PROFILE) {
				PacketPlayerProfile profilePacket = (PacketPlayerProfile) packet;
				
				if(!profilePacket.getUsername().equals(profile.getUsername())) {
					players.put(profilePacket.getUsername(), new PlayerRemote(profilePacket));
					
				} else {
					player = new PlayerLocal(SPAWN_X, SPAWN_Y, profile);
					players.put(profile.getUsername(), player);
				}
				
				
				packet.disposePacket();
				
				
			} else if(packet.getID() == PacketID.PLAYER_DISCONNECT) {
				PacketPlayerDisconnect disconnectPacket = (PacketPlayerDisconnect) packet;
				players.remove(disconnectPacket.getUsername());
				
				packet.disposePacket();
			}
		});
	}
	
	
	
	
	private void handlePrimaryChoosePackets(Game game) {
		client.forEachReceivedPacket((Packet packet) -> {
			
			if(packet.getID() == PacketID.CHOOSE_PRIMARY) {
				PacketChoosePrimary primaryPacket = (PacketChoosePrimary) packet;
				
				if(primaryPacket.getPrimaryType() == PacketChoosePrimary.PRIMARY_TYPE_STORYTELLER) {
					changeStoryteller(primaryPacket.getUsername(), game);
					
				} else if(primaryPacket.getPrimaryType() == PacketChoosePrimary.PRIMARY_TYPE_MAFIA) {
					mafiaUsername = primaryPacket.getUsername();
					
				} else if(primaryPacket.getPrimaryType() == PacketChoosePrimary.PRIMARY_TYPE_DOCTOR)
					doctorUsername = primaryPacket.getUsername();
				
				
				packet.disposePacket();
			}
		});
	}
	
	
	
	
	private void changeStoryteller(String storytellerUsername, Game game) {
		this.storytellerUsername = storytellerUsername;
		
		
		Player storyteller = players.get(storytellerUsername);
		storyteller.setSpecialNameTagColor(STORYTELLER_NAME_TAG_COLOR);
		
		ChatMessage storytellerMessage = new ChatMessage("Game", storytellerUsername + " is the Storyteller!", true);
		chatElement.addMessage(storytellerMessage, false, game);
	}
	
	
	
	
	
	protected void setStatusText(String text) {
		statusElement.setText("- " + text + " -");
	}
	
	protected void resetStatusText() {
		statusElement.setText("");
	}
	
	
	protected void setTipText(String text) {
		tipElement.setText("TIP: " + text);
	}
	
	protected void resetTipText() {
		tipElement.setText("");
	}
	
	
	
	
	public void addParticle(Particle particle) {
		particles.add(particle);
	}
	
	
	
	protected boolean isPlayerStoryteller() {
		if(storytellerUsername != null)
			if(player.getProfile().getUsername().equals(storytellerUsername))
				return true;
		
		
		return false;
	}	
	
	protected boolean isPlayerMafia() {
		if(mafiaUsername != null)
			if(player.getProfile().getUsername().equals(mafiaUsername))
				return true;
		
		
		return false;
	}
	
	protected boolean isPlayerDoctor() {
		if(doctorUsername != null)
			if(player.getProfile().getUsername().equals(doctorUsername))
				return true;
		
		
		return false;
	}
	
	
	
	
	protected boolean hasStorytellerBeenChosen() {
		if(storytellerUsername != null)
			return true;
		
		
		return false;
	}
	
	protected boolean hasMafiaBeenChosen() {
		if(mafiaUsername != null)
			return true;
		
		
		return false;
	}
	
	protected boolean hasDoctorBeenChosen() {
		if(doctorUsername != null)
			return true;
		
		
		return false;
	}
	
	
	
	
	
	protected PacketPlayerProfile getProfile() {
		return profile;
	}
	
	
	public PlayerLocal getPlayer() {
		return player;
	}
	
	
	public GameClient getClient() {
		return client;
	}
	
	
	public ChatElement getChatElement() {
		return chatElement;
	}
	
	
	public HashMap<String, Player> getPlayers() {
		return players;
	}
	
	
	
	protected String getStorytellerUsername() {
		return storytellerUsername;
	}
	
	protected void setStorytellerUsername(String storytellerUsername, Game game) {
		changeStoryteller(storytellerUsername, game);
		
		PacketChoosePrimary primaryPacket = new PacketChoosePrimary(storytellerUsername, PacketChoosePrimary.PRIMARY_TYPE_STORYTELLER);
		client.sendPacket(primaryPacket);
	}
	
	
	protected String getMafiaUsername() {
		return mafiaUsername;
	}
	
	protected void setMafiaUsername(String mafiaUsername) {
		this.mafiaUsername = mafiaUsername;
		
		PacketChoosePrimary primaryPacket = new PacketChoosePrimary(mafiaUsername, PacketChoosePrimary.PRIMARY_TYPE_MAFIA);
		client.sendPacket(primaryPacket);
	}
	
	
	protected String getDoctorUsername() {
		return doctorUsername;
	}
	
	protected void setDoctorUsername(String doctorUsername) {
		this.doctorUsername = doctorUsername;
		
		PacketChoosePrimary primaryPacket = new PacketChoosePrimary(doctorUsername, PacketChoosePrimary.PRIMARY_TYPE_DOCTOR);
		client.sendPacket(primaryPacket);
	}
	
}
