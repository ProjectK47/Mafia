package hyperbox.mafia.core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import hyperbox.mafia.gamestate.GameStateManager;
import hyperbox.mafia.input.KeyboardInput;
import hyperbox.mafia.input.MouseInput;
import hyperbox.mafia.io.AudioResources;
import hyperbox.mafia.io.FontResources;
import hyperbox.mafia.io.ImageResources;
import hyperbox.mafia.window.Camera;
import hyperbox.mafia.window.Window;
import hyperbox.mafia.world.Map;

public class Game extends Canvas implements Runnable {

	
	public static final int WIDTH = 960;
	public static final int HEIGHT = 540;
	
	public static final String TITLE = "Mafia";
	public static final String VERSION = "1.0";
	
	public static final boolean RESIZABLE = true;
	
	public static final int TARGET_FPS = 60;
	public static final int TARGET_TPS = 30;
	
	
	private static final long serialVersionUID = 3805123262062170593L;

	////
	
	private static Window window;
	
	
	private Thread gameThread;
	private GameStateManager gameStateManager;
	
	private Camera camera;
	
	private Map map;
	
	
	
	
	public static void main(String[] args) {
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
		
		
		this.addKeyListener(new KeyboardInput());
		
		MouseInput mouseInput = new MouseInput();
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
		
		
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
				System.out.println("FPS: " + frames + " TPS: " + ticks);
				timer += 1000;
				ticks = 0;
				frames = 0;
			}
		}
	}
	
	
	
	
	
	
	private void tick() {
		camera.tick();
		map.tick(this);
		
		gameStateManager.tick(this);
		
		
		KeyboardInput.tick();
		MouseInput.tick();
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
		
		
		
		map.render(g, this);
		
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
	
	
	
	
	
	
	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}
	
	
	public Camera getCamera() {
		return camera;
	}
	
	
}
