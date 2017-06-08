package hyperbox.mafia.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import hyperbox.mafia.net.Packet;
import hyperbox.mafia.net.PacketPlayerProfile;
import hyperbox.mafia.net.PacketRunnable;

public class GameClient implements Runnable {

	
	
	private String ip;
	private int port;
	
	private PacketPlayerProfile playerProfile;
	
	
	private Thread gameClientThread;
	
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	
	private List<Packet> receivedPackets = Collections.synchronizedList(new ArrayList<Packet>());
	
	private boolean wasExceptionCaught = false;
	
	
	
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
			socket = new Socket(ip, port);
			
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			
			playerProfile.writePacket(out);
			

			
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
		Iterator<Packet> iterator = receivedPackets.iterator();
		
		while(iterator.hasNext()) {
			Packet packet = iterator.next();
			
			
			runnable.run(packet);
			
			if(packet.isDisposed())
				iterator.remove();
		}
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
	
	
	
	public boolean wasExceptionCaught() {
		return wasExceptionCaught;
	}
	
	
	
	public String getIP() {
		return ip;
	}
	
	
	public int getPort() {
		return port;
	}
	
}