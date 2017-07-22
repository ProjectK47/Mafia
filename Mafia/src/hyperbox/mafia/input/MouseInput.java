package hyperbox.mafia.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import hyperbox.mafia.core.Game;

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {

	
	
	private static boolean isPrimaryPressed = false;
	private static boolean isSecondaryPressed = false;
	private static boolean isMiddlePressed = false;
	
	private static int mouseX = 0;
	private static int mouseY = 0;
	
	private static int amountScrolled = 0;
	
	
	private static boolean wasPrimaryClicked = false;
	private static boolean wasSecondaryClicked = false;
	private static boolean wasMiddleClicked = false;
	
	
	
	
	@Override
	public void mousePressed(MouseEvent event) {
		if(event.getButton() == MouseEvent.BUTTON1) {
			isPrimaryPressed = true;
			
		} else if(event.getButton() == MouseEvent.BUTTON2) {
			isMiddlePressed = true;
			
		} else if(event.getButton() == MouseEvent.BUTTON3) {
			isSecondaryPressed = true;
		}
	}
	

	@Override
	public void mouseReleased(MouseEvent event) {
		if(event.getButton() == MouseEvent.BUTTON1) {
			isPrimaryPressed = false;
			wasPrimaryClicked = true;
			
		} else if(event.getButton() == MouseEvent.BUTTON2) {
			isMiddlePressed = false;
			wasMiddleClicked = true;
			
		} else if(event.getButton() == MouseEvent.BUTTON3) {
			isSecondaryPressed = false;
			wasSecondaryClicked = true;
		}
	}
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent event) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent event) {
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		
	}

	
	
	
	
	@Override
	public void mouseMoved(MouseEvent event) {
		mouseX = event.getX();
		mouseY = event.getY();
	}
	
	
	@Override
	public void mouseDragged(MouseEvent event) {
		
	}

	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		int notches = event.getWheelRotation();
		
		amountScrolled += notches;
	}
	
	

	
	
	public static void tick() {
		wasPrimaryClicked = false;
		wasSecondaryClicked = false;
		wasMiddleClicked = false;
		
		amountScrolled = 0;
	}
	
	
	
	
	public static int grabWorldMouseX(Game game) {
		return (int) (mouseX + game.getCamera().getX() - (game.getWidth() / 2));
	}
	
	public static int grabWorldMouseY(Game game) {
		return (int) (mouseY + game.getCamera().getY() - (game.getHeight() / 2));
	}
	
	
	public static int grabScreenMouseX(Game game) {
		return mouseX - (game.getWidth() / 2);
	}
	
	public static int grabScreenMouseY(Game game) {
		return mouseY - (game.getHeight() / 2);
	}
	
	
	
	
	
	public static boolean isPrimaryPressed() {
		return isPrimaryPressed;
	}
	
	public static boolean isSecondaryPressed() {
		return isSecondaryPressed;
	}
	
	public static boolean isMiddlePressed() {
		return isMiddlePressed;
	}
	
	
	
	public static boolean wasPrimaryClicked() {
		return wasPrimaryClicked;
	}
	
	public static boolean wasSecondaryClicked() {
		return wasSecondaryClicked;
	}
	
	public static boolean wasMiddleClicked() {
		return wasMiddleClicked;
	}
	
	
	
	public static float getMouseX() {
		return mouseX;
	}
	
	public static float getMouseY() {
		return mouseY;
	}
	
	
	public static int getAmountScrolled() {
		return amountScrolled;
	}
	

}