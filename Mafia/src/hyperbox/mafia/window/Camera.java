package hyperbox.mafia.window;

import java.awt.Graphics2D;
import java.util.Random;

import hyperbox.mafia.core.Game;

public class Camera {
	
	
	private int x = 0;
	private int y = 0;
	
	
	private float cameraShakeValue = 0;
	private float cameraShakeDecrease = 0;
	
	private int cameraShakeX = 0;
	private int cameraShakeY = 0;
	
	
	private Random random = new Random();
	
	
	
	public void tick() {
		cameraShakeValue -= cameraShakeDecrease;
		
		if(cameraShakeValue < 0)
			cameraShakeValue = 0;
		
		
		cameraShakeX = grabRandomCameraShake();
		cameraShakeY = grabRandomCameraShake();
	}
	
	
	
	
	public void translateToCamera(Graphics2D g, Game game) {
		g.translate(-x, -y);
	}
	
	
	public void translateFromCamera(Graphics2D g) {
		g.translate(x, y);
	}
	
	
	
	public void translateCameraShake(Graphics2D g) {
		g.translate(cameraShakeX, cameraShakeY);
	}
	
	
	public void translateNoCameraShake(Graphics2D g) {
		g.translate(-cameraShakeX, -cameraShakeY);
	}
	
	
	
	
	public void applyCameraShake(float cameraShakeValue, float cameraShakeDecrease) {
		this.cameraShakeValue = cameraShakeValue;
		this.cameraShakeDecrease = cameraShakeDecrease;
	}
	

	
	
	private int grabRandomCameraShake() {
		return (random.nextInt(20) - 10) * (int) cameraShakeValue;
	}
	
	
	
	
	public int getX() {
		return x;
	}
	
	
	public int getY() {
		return y;
	}
	
}
