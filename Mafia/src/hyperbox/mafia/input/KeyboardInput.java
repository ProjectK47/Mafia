package hyperbox.mafia.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyboardInput implements KeyListener {

	
	
	public static final int NUM_OF_KEYS = 68836;
	
	
	private static boolean[] keysDown = new boolean[NUM_OF_KEYS];
	private static boolean[] keysChecked = new boolean[NUM_OF_KEYS];
	
	private static ArrayList<Character> charsTyped = new ArrayList<Character>();
	
	
	private static boolean isPromptActive = false;
	
	
	
	
	public void keyPressed(KeyEvent event) {
		keysDown[event.getKeyCode()] = true;
	}

	
	
	public void keyReleased(KeyEvent event) {
		int keyCode = event.getKeyCode();
		
		
		keysDown[keyCode] = false;
		keysChecked[keyCode] = false;
	}
	
	
	
	public void keyTyped(KeyEvent event) {
		charsTyped.add(event.getKeyChar());
	}
	
	
	
	
	
	public static void tick() {
		charsTyped.clear();
	}
	
	
	
	
	
	public static boolean isKeyDown(int keyCode, boolean isPrompt) {
		if(isPromptActive && !isPrompt)
			return false;
		
		return keysDown[keyCode];
	}
	
	
	
	public static boolean wasKeyTyped(int keyCode, boolean isPrompt) {
		if(isPromptActive && !isPrompt)
			return false;
		
		
		if(keysDown[keyCode] && !keysChecked[keyCode]) {
			keysChecked[keyCode] = true;
			
			return true;
		}
		
		
		return false;
	}
	
	
	
	public static ArrayList<Character> getCharsTyped() {
		return charsTyped;
	}
	
	
	
	
	public static void setIsPromptActive(boolean isPromptActive) {
		KeyboardInput.isPromptActive = isPromptActive;
	}
	
	
	
	
}