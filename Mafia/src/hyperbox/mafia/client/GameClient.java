package hyperbox.mafia.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketPlayerProfile;
import hyperbox.mafia.net.PacketRunnable;

public class GameClient implements Runnable {
	
	
	public static final int CONNECT_TIMEOUT_SECONDS = 10;
	
	
	
	private String ip;
	private int port;
	
	private PacketPlayerProfile playerProfile;
	
	
	private Thread gameClientThread;
	
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	private int initialPlayerCount;
	
	
	private List<Packet> receivedPackets = Collections.synchronizedList(new ArrayList<Packet>());
	
	private boolean wasExceptionCaught = false;
	private boolean hasReceivedBaseData = false;
	
	
	
	public GameClient(String ip, int port, PacketPlayerProfile playerProfile) {
		this.ip = ip;
		this.port = port;
		
		this.playerProfile = playerProfile;
		
		
		gameClientThread = new Thread(this);
		
		gameClientThread.setName("GameClientThread");
		gameClientThread.setDaemon(true);
	}
	
	
	
	
	
	@Override
	public void run() {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), CONNECT_TIMEOUT_SECONDS * 1000);
			
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			
			out.writeUTF(Game.VERSION);	
			playerProfile.writePacket(out);
			
			initialPlayerCount = in.readInt();
			
			
			hasReceivedBaseData = true;
			
			
			while(true) {
				byte packetID = Packet.readID(in);
				Packet packet = Packet.readPacketByID(in, packetID);
				
				receivedPackets.add(packet);
			}
			
		} catch (IOException e) {
			System.out.println("Exception in client thread. Closing client.");
			
			wasExceptionCaught = true;
			closeClient();
		}
	}

	
	
	
	public void sendPacket(Packet packet) {
		if(!isConnected())
			return;
		
		
		try {
			packet.writePacket(out);
		} catch (IOException e) {
			System.out.println("Error when writing packet to server.");
		}
	}
	
	
	
	public void forEachReceivedPacket(PacketRunnable runnable) {
		ArrayList<Packet> packetsToRemove = new ArrayList<Packet>();
		
		
		for(int i = 0; i < receivedPackets.size(); i ++) {
			Packet packet = receivedPackets.get(i);
			
			runnable.run(packet);
			
			if(packet.isDisposed())
				packetsToRemove.add(packet);
		}
		
		
		receivedPackets.removeAll(packetsToRemove);
	}
	
	
	
	
	public void startClient() {
		gameClientThread.start();
	}
	
	
	
	public void closeClient() {
		if(socket != null)
			if(!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
	}
	
	
	
	public boolean isConnected() {
		if(socket != null)
			if(socket.isConnected() && !socket.isClosed())
				return true;
		
		
		return false;
	}
	
	
	
	public void clearPackets() {
		receivedPackets.clear();
	}
		
	
	
	
	public boolean wasExceptionCaught() {
		return wasExceptionCaught;
	}
	
	
	public boolean hasReceivedBaseData() {
		return hasReceivedBaseData;
	}
	
	
	
	public String getIP() {
		return ip;
	}
	
	
	public int getPort() {
		return port;
	}
	
	
	public int getInitialPlayerCount() {
		return initialPlayerCount;
	}
	
}
