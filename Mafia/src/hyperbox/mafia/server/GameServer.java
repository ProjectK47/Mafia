package hyperbox.mafia.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketPlayerProfile;

public class GameServer implements Runnable {

	
	
	private int port;
	
	
	private Thread serverThread;
	private ServerSocket serverSocket;
	
	private List<GameServerClient> clients = Collections.synchronizedList(new ArrayList<GameServerClient>());
	
	
	
	public GameServer(int port) {
		this.port = port;
		
		
		serverThread = new Thread(this);
		
		serverThread.setName("ServerThread");
		serverThread.setDaemon(true);
	}
	
	
	
	
	
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			
			
			while(true) {
				Socket socket = serverSocket.accept();
				DataInputStream in = new DataInputStream(socket.getInputStream());
				
				Packet.readID(in);
				PacketPlayerProfile profile = new PacketPlayerProfile(in);
				
				
				boolean isUsernamePresent = false;
				
				for(GameServerClient client : clients)
					if(client.getProfile().getUsername().equalsIgnoreCase(profile.getUsername())) {
						isUsernamePresent = true;
						
						break;
					}
				
				if(isUsernamePresent) {
					socket.close();
					continue;
				}
				
					
				
				GameServerClient client = new GameServerClient(socket, profile, this);
				clients.add(client);
				
				client.start();
			}
			
		} catch (IOException e) {
			System.out.println("Exception in server thread. Closing server.");
			closeServer();
		}
	}
	
	
	
	
	
	protected void broadcastPacket(Packet packet, GameServerClient sender) {
		for(GameServerClient client : clients) {
			if(!client.getProfile().getUsername().equals(sender.getProfile().getUsername()))
				client.sendPacket(packet);
		}
	}
	
	
	protected synchronized void removeDisconnectedClient(GameServerClient clientToRemove) {
		clients.remove(clientToRemove);
	}
	
	
	protected List<GameServerClient> getClients() {
		return clients;
	}
	
	
	
	
	
	public void startServer() {
		serverThread.start();
	}
	
	
	
	public void closeServer() {
		if(serverSocket != null)
			if(!serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		
		
		for(GameServerClient client : clients)
			client.closeConnection();
	}
	

	
	
	public int getPort() {
		return port;
	}

}
