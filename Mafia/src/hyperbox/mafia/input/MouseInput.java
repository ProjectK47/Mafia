package hyperbox.mafia.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.ui.UIElement;

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
	
	
	private static HashMap<UIElement, Integer> elementsMousingOver = new HashMap<UIElement, Integer>();
	private static int highestElementPriority = 0;
	
	
	
	
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
	
	

	
	
	public static void tick(Game game) {
		wasPrimaryClicked = false;
		wasSecondaryClicked = false;
		wasMiddleClicked = false;
		
		amountScrolled = 0;
		
		
		
		highestElementPriority = 0;
		
		
		Iterator<Map.Entry<UIElement, Integer>> it = elementsMousingOver.entrySet().iterator();
		
		while(it.hasNext()) {
			Map.Entry<UIElement, Integer> entry = it.next();
			
			if(game.getCurrentTick() - entry.getKey().getLastTick() > 1)
				it.remove();
			
			
			if(entry.getValue() > highestElementPriority)
				highestElementPriority = entry.getValue();
		}
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
	
	
	
	
	public static void addElementMousingOver(UIElement element, int priority) {
		if(!elementsMousingOver.containsKey(element))
			elementsMousingOver.put(element, priority);
	}
	
	public static void removeElementMousingOver(UIElement element) {
		if(elementsMousingOver.containsKey(element))
			elementsMousingOver.remove(element);
	}
	
	
	
	
	public static boolean isPrimaryPressed(int priority) {
		if(isPrimaryPressed && priority >= highestElementPriority)
			return true;
		
		return false;
	}
	
	public static boolean isSecondaryPressed(int priority) {
		if(isSecondaryPressed && priority >= highestElementPriority)
			return true;
		
		return false;
	}
	
	public static boolean isMiddlePressed(int priority) {
		if(isMiddlePressed && priority >= highestElementPriority)
			return true;
		
		return false;
	}
	
	
	
	public static boolean wasPrimaryClicked(int priority) {
		if(wasPrimaryClicked && priority >= highestElementPriority)
			return true;
		
		return false;
	}
	
	public static boolean wasSecondaryClicked(int priority) {
		if(wasSecondaryClicked && priority >= highestElementPriority)
			return true;
		
		return false;
	}
	
	public static boolean wasMiddleClicked(int priority) {
		if(wasMiddleClicked && priority >= highestElementPriority)
			return true;
		
		return false;
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