package hyperbox.mafia.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketPlayerProfile;

public class GameServerClient extends Thread {
	
	
	
	private Socket socket;
	private PacketPlayerProfile profile;
	
	private GameServer gameServer;
	
	
	private DataInputStream in;
	private DataOutputStream out;
	
	private String ip;
	
	
	
	public GameServerClient(Socket socket, PacketPlayerProfile profile, GameServer gameServer) {
		this.socket = socket;
		this.profile = profile;
		
		this.gameServer = gameServer;
		
		
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		ip = socket.getRemoteSocketAddress().toString();
		
		
		this.setName("GameServerClientThread" + " - " + profile.getUsername() + " - " + ip);
		this.setDaemon(true);
	}

	
	
	
	@Override
	public void run() {
		try {
			
			List<GameServerClient> clients = gameServer.getClients();
			
			out.writeInt(clients.size());
			
			for(GameServerClient client : clients)
				sendPacket(client.getProfile());
			
			
			////
			
			
			while(true) {
				byte packetID = Packet.readID(in);
				Packet packet = Packet.readPacketByID(in, packetID);
				
				gameServer.broadcastPacket(packet, this);
			}
			
		} catch (IOException e) {
			System.out.println(profile.getUsername() + " - " + ip + " has disconnected.");
			
			gameServer.removeDisconnectedClient(this);
		}
	}
	
	
	
	
	public synchronized void sendPacket(Packet packet) {
		try {
			packet.writePacket(out);
		} catch (IOException e) {
			System.out.println("Error when writing packet to " + profile.getUsername() + " - " + ip);
		}
	}
	
	
	
	
	
	public void closeConnection() {
		if(socket.isClosed())
			return;
		
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	
	
	
	public Socket getSocket() {
		return socket;
	}
	
	
	public PacketPlayerProfile getProfile() {
		return profile;
	}
	
}
