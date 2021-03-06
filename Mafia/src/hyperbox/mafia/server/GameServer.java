package hyperbox.mafia.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketPlayerDisconnect;
import hyperbox.mafia.net.PacketPlayerProfile;

public class GameServer implements Runnable {
	
	
	public static final int SOCKET_TIMEOUT_SECONDS = 10;

	
	
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
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		System.out.println("Server started on port: " + port);
		
		
		while(true) {
			Socket socket = null;
			
			try {
				socket = serverSocket.accept();
				socket.setSoTimeout(SOCKET_TIMEOUT_SECONDS * 1000);
			} catch (IOException e) {
				System.out.println("Exception in server thread when accepting connection. Closing server.");
				closeServer();
			}
			
			
			
			try {
				DataInputStream in = new DataInputStream(socket.getInputStream());
				String ip = socket.getRemoteSocketAddress().toString();
				
				
				//Check version////
				String version = in.readUTF();
				
				if(!version.equals(Game.VERSION)) {
					System.out.println(ip + " - client version '" + version + "' doesn't match server version '" + 
							Game.VERSION + "'. Disconnecting.");
					
					socket.close();
					continue;
				}
				
				
				//Read profile////
				Packet.readID(in);
				PacketPlayerProfile profile = new PacketPlayerProfile(in);
				
				
				//Check for username conflict////
				boolean isUsernamePresent = false;
				
				for(GameServerClient client : clients)
					if(client.getProfile().getUsername().equalsIgnoreCase(profile.getUsername())) {
						isUsernamePresent = true;
						
						break;
					}
				
				if(isUsernamePresent) {
					System.out.println(profile.getUsername() + " - " + ip + " - username already taken. Disconnecting.");
					
					socket.close();
					continue;
				}
				
					
				
				//Add client////
				GameServerClient client = new GameServerClient(socket, profile, this);
				clients.add(client);
				
				client.start();
				broadcastPacket(profile, client);
				
				System.out.println(profile.getUsername() + " - " + ip + " has logged in successfully!");
				
				
			} catch(IOException e) {
				System.out.println("Exception in server thread when verifying connection. Accepting new connection.");
				closeSocket(socket);
			}
		}
	}
	
	
	
	
	private void closeSocket(Socket socket) {
		if(!socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	
	
	
	
	protected void broadcastPacket(Packet packet, GameServerClient sender) {
		for(int i = 0; i < clients.size(); i ++) {
			GameServerClient client = clients.get(i);
			
			if(!client.getProfile().getUsername().equals(sender.getProfile().getUsername()))
				client.sendPacket(packet);
		}
	}
	
	
	protected synchronized void removeDisconnectedClient(GameServerClient clientToRemove) {
		clients.remove(clientToRemove);
		
		PacketPlayerDisconnect disconnectPacket = new PacketPlayerDisconnect(clientToRemove.getProfile().getUsername());
		broadcastPacket(disconnectPacket, clientToRemove);
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
	
	
	
	public void joinThread() {
		try {
			serverThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	
	
	
	public int getPort() {
		return port;
	}

}
