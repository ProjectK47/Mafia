package hyperbox.mafia.core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import hyperbox.mafia.gamestate.GameStateManager;
import hyperbox.mafia.input.KeyboardInput;
import hyperbox.mafia.input.MouseInput;
import hyperbox.mafia.io.AudioResources;
import hyperbox.mafia.io.FontResources;
import hyperbox.mafia.io.ImageResources;
import hyperbox.mafia.server.GameServer;
import hyperbox.mafia.window.Camera;
import hyperbox.mafia.window.Window;
import hyperbox.mafia.world.Map;

public class Game extends Canvas implements Runnable {

	
	public static final int WIDTH = 960;
	public static final int HEIGHT = 540;
	
	public static final String TITLE = "Mafia";
	public static final String VERSION = "1.0.2";
	
	public static final boolean RESIZABLE = true;
	
	public static final int TARGET_FPS = 60;
	public static final int TARGET_TPS = 30;
	
	
	private static final long serialVersionUID = 3805123262062170593L;

	
	////
	
	private int currentTick = 0;
	
	////
	
	private static Window window;
	
	
	private Thread gameThread;
	private GameStateManager gameStateManager;
	
	private Camera camera;
	
	private Map map;
	
	
	
	public static void main(String[] args) {
		boolean shouldRunMacFix = true;
		
		
		if(args.length >= 2) {
			if(args[0].equalsIgnoreCase("-server")) {
				int port = Integer.parseInt(args[1]);
				
				startGameServer(port, true);
				return;
				
			} else if(args[0].equalsIgnoreCase("-useMacFix")) {
				shouldRunMacFix = Boolean.parseBoolean(args[1]);
			}
		}
		
		
		if(shouldRunMacFix)
			disableMacAccentMenu();
		
		window = new Window(WIDTH, HEIGHT, TITLE + " - " + VERSION, RESIZABLE);
		window.showWindow();
	}
	
	
	
	
	
	public void init() {
		gameThread = new Thread(this);
		
		gameThread.setDaemon(true);
		gameThread.setName("GameThread");
		
		
		ImageResources.loadResources();
		AudioResources.loadResources();
		FontResources.loadResources();
		
		
		Random random = new Random();
		int randInt = random.nextInt(3);
		
		BufferedImage icon;
		
		if(randInt == 0)
			icon = ImageResources.grassBladesStill;
		else if(randInt == 1)
			icon = ImageResources.grassBladesLeft;
		else
			icon = ImageResources.grassBladesRight;
		
		
		window.setWindowIcon(icon);
		
		
		
		this.addKeyListener(new KeyboardInput());
		
		MouseInput mouseInput = new MouseInput();
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
		this.addMouseWheelListener(mouseInput);
		
		
		gameStateManager = new GameStateManager(this);
		gameStateManager.getGameStateMenu().enable(this);
		
		
		camera = new Camera();
		
		map = new Map(ImageResources.standardMap);
		
		
		
		
		gameThread.start();
	}





	@Override
	public void run() {
		long lastTime = System.nanoTime();
		
		double nsRender = 1000000000 / TARGET_FPS;
		double deltaRender = 0;
		
		double nsTick = 1000000000 / TARGET_TPS;
		double deltaTick = 0;
		
		int ticks = 0;
		int frames = 0;
		int iterations = 0;
		long timer = System.currentTimeMillis();
		
		
		
		while(true) {
			long now = System.nanoTime();
			
			deltaTick += (now - lastTime) / nsTick;
			deltaRender += (now - lastTime) / nsRender;
			
			lastTime = now;
			
			if(deltaTick >= 1) {
				tick();
				deltaTick --;
				ticks ++;
			}
			
			
			if(deltaRender >= 1) {
				render();
				deltaRender --;
				frames ++;
			}
			
			
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames + " TPS: " + ticks + " IPS: " + iterations);
				timer += 1000;
				ticks = 0;
				frames = 0;
				iterations = 0;
			}
			
			
			
			try {
				int timeToTick = (int) ((1 - deltaTick) * nsTick / 1000000);
				int timeToRender = (int) ((1 - deltaRender) * nsRender / 1000000);
				
				int timeToSleep = timeToTick > timeToRender ? timeToRender : timeToTick;
				Thread.sleep(Math.max(1, timeToSleep));
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			
			iterations ++;
		}
	}
	
	
	
	
	
	
	private void tick() {
		camera.tick();
		map.tick(this);
		
		gameStateManager.tick(this);
		
		
		KeyboardInput.tick();
		MouseInput.tick(this);
		
		
		currentTick ++;
	}
	
	
	
	
	private void render() {
		BufferStrategy buffer = this.getBufferStrategy();
		
		if(buffer == null) {
			this.createBufferStrategy(3);
			
			return;
		}
		
		
		Graphics2D g = (Graphics2D) buffer.getDrawGraphics();
		
		g.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		
		////
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		
		g.translate(this.getWidth() / 2, this.getHeight() / 2);
		
		
		
		camera.translateToCamera(g, this);
		camera.translateCameraShake(g);
		
		map.render(g, this);
		
		camera.translateNoCameraShake(g);
		camera.translateFromCamera(g);
		
		
		gameStateManager.render(g, this);
		
		////
		
		g.dispose();
		buffer.show();
	}
	
	
	
	
	
	
	public boolean areCoordsOnScreen(float worldX, float worldY) {
		int screenX = (int) worldX - camera.getX();
		int screenY = (int) worldY - camera.getY();
		
		
		if(screenX >= -this.getWidth() / 2 && screenX <= this.getWidth() / 2)
			if(screenY >= -this.getHeight() / 2 && screenY <= this.getHeight() / 2) {
				return true;
			}
		
		
		return false;
	}
	
	
	
	
	public static void startGameServer(int port, boolean shouldJoin) {
		GameServer server = new GameServer(port);
		server.startServer();
		
		if(shouldJoin)
			server.joinThread();
	}
	
	
	
	
	
	
	private static void disableMacAccentMenu() {
		String osName = System.getProperty("os.name").toLowerCase();
		
		
		if(osName.contains("mac")) {
			
			Process checkProcess = runCommand("defaults read NSGlobalDomain ApplePressAndHoldEnabled");
			BufferedReader procIn = new BufferedReader(new InputStreamReader(checkProcess.getInputStream()));
			
			
			String input = null;
			
			try {
				input = procIn.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			
			boolean propertyActive = true;
			
			if(input != null)
				if(input.toLowerCase().contains("0"))
					propertyActive = false;
			
			
			if(propertyActive) {
				runCommand("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool false");
				
				Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					runCommand("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool true");
				}));
			}
		}
	}
	
	
	
	private static Process runCommand(String command) {
		Process process = null;
		
		try {
			process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		return process;
	}
	
	
	
	
	public int getCurrentTick() {
		return currentTick;
	}
	
	
	
	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}
	
	
	public Camera getCamera() {
		return camera;
	}
	
	
	public Map getMap() {
		return map;
	}
	
	
}
