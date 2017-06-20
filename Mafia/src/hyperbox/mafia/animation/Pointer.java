package hyperbox.mafia.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import hyperbox.mafia.core.Game;

public class Pointer {
	
	
	public static final float POINTER_STAGE_SPEED = 0.09f;
	
	public static final Color POINTER_COLOR = new Color(255, 255, 230);
	public static final float POINTER_THINKNESS = 2.5f;
	
	
	private float startX;
	private float startY;
	
	private float targetX;
	private float targetY;
	
	
	private float pointerStage = 0f;
	private boolean hasFinished = false;
	
	
	
	public Pointer(float startX, float startY, float targetX, float targetY) {
		this.startX = startX;
		this.startY = startY;
		
		this.targetX = targetX;
		this.targetY = targetY;
	}
	
	
	

	public void tick(Game game) {
		if(pointerStage < 1)
			pointerStage += POINTER_STAGE_SPEED;
		
		if(pointerStage > 1)
			pointerStage = 1f;
		
		
		if(pointerStage == 1)
			hasFinished = true;
	}

	
	public void render(Graphics2D g, Game game) {
		int pointOneX = (int) (startX + (targetX - startX) * pointerStage);
		int pointOneY = (int) (startY + (targetY - startY) * pointerStage);
		
		
		
		Stroke oldStroke = g.getStroke();
		
		
		int colorAlpha = (int) (255 - 255 * pointerStage);
		
		g.setColor(new Color(POINTER_COLOR.getRed(), POINTER_COLOR.getGreen(), POINTER_COLOR.getBlue(), colorAlpha));
		g.setStroke(new BasicStroke(POINTER_THINKNESS));
		
		g.drawLine(pointOneX, pointOneY, (int) startX, (int) startY);
		
		
		g.setStroke(oldStroke);
	}
	
	
	
	
	
	
	public float getStartX() {
		return startX;
	}
	
	public float getStartY() {
		return startY;
	}
	
	
	public float getTargetX() {
		return targetX;
	}
	
	public float getTargetY() {
		return targetY;
	}
	
	
	public boolean hasFinished() {
		return hasFinished;
	}

}
