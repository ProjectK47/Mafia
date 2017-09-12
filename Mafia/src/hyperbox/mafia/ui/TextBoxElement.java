package hyperbox.mafia.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.input.KeyboardInput;
import hyperbox.mafia.input.MouseInput;
import hyperbox.mafia.io.FontResources;
import hyperbox.mafia.io.ImageResources;

public class TextBoxElement extends UIElement {

	
	public static final Color BOX_TEXT_COLOR = new Color(255, 255, 204);
	public static final Color LABEL_TEXT_COLOR = new Color(255, 255, 242);
	
	public static final float LABEL_TEXT_SCALE = 0.65f;
	public static final float LABEL_TEXT_SPACE = 1.2f;
	
	public static final float SIDE_IMAGES_SCALE = 0.055f;
	public static final float SIDE_IMAGES_SPACE = 0.025f;
	
	public static final String MARKER = "_";
	public static final String BLANK_FILLER = "  ";
	
	public static final String ALL_CHARS = "abcdefghijklmnopqrstuvwxyz 1234567890 `~!@#$%^&*()-_+=[{]}\\|;:'\",<.>/?";
	public static final String NUMBER_CHARS = "1234567890";
	
	
	private float size;
	
	private String labelText;
	private UIAnchor labelAnchorX;
	
	private int maxBoxTextLength;
	private String allowedChars;
	
	private TextElement boxTextElement;
	
	
	private Runnable enterTask = null;
	private boolean shouldEnterFocus = false;
	
	
	private boolean isHovering = false;
	private boolean isFocused = false;
	
	private int markerPosition = 0;
	
	
	
	public TextBoxElement(int x, int y, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY,
			float size, String boxText, String labelText, UIAnchor labelAnchorX, int maxBoxTextLength, String allowedChars) {
		
		super(x, y, 0, 0, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, 1);
		
		
		this.size = size;
		
		this.labelText = labelText;
		this.labelAnchorX = labelAnchorX;
		
		this.maxBoxTextLength = maxBoxTextLength;
		this.allowedChars = allowedChars;
		
		boxTextElement = new TextElement(x, y, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, boxText, size, BOX_TEXT_COLOR);
	}

	
	public TextBoxElement(int x, int y, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY,
			float size, String boxText, String labelText, UIAnchor labelAnchorX, int maxBoxTextLength, String allowedChars,
			Runnable enterTask, boolean shouldEnterFocus) {
		
		this(x, y, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, size, boxText, labelText, labelAnchorX, maxBoxTextLength, allowedChars);
		
		this.enterTask = enterTask;
		this.shouldEnterFocus = shouldEnterFocus;
	}
	
	
	
	
	@Override
	public void onTick(Game game) {
		width = boxTextElement.getWidth();
		height = boxTextElement.getHeight();
		
		
		
		//Set hovering////
		int mouseX = MouseInput.grabScreenMouseX(game);
		int mouseY = MouseInput.grabScreenMouseY(game);
		
		
		isHovering = false;
		
		if(mouseX >= this.grabAnchoredX(game) - (width / 2) && mouseX <= this.grabAnchoredX(game) + (width / 2))
			if(mouseY >= this.grabAnchoredY(game) - (height / 2) && mouseY <= this.grabAnchoredY(game) + (height / 2))
				isHovering = true;
		
		
		
		//Set focused////
		if(MouseInput.wasPrimaryClicked(this.mousePriority)) {
			if(isHovering) {
				addFocus();
				
			} else {
				removeFocus();
			}
		}
		
		

		//Enter task////
		if(KeyboardInput.wasKeyTyped(KeyEvent.VK_ENTER, true)) {
			if(shouldEnterFocus && !isFocused)
				addFocus();
			else if(enterTask != null && isFocused)
				enterTask.run();
		}
		
		
		
		
		if(!isFocused && boxTextElement.getText().trim().equals(""))
			boxTextElement.setText(BLANK_FILLER);
		
		
		
		////
		
		
		if(!isFocused)
			return;
		
		
		
		//Move marker////
		if(KeyboardInput.wasKeyTyped(KeyEvent.VK_LEFT, true) && markerPosition - 1 >= -boxTextElement.getText().length())
			markerPosition --;
		
		if(KeyboardInput.wasKeyTyped(KeyEvent.VK_RIGHT, true) && markerPosition + 1 <= 0)
			markerPosition ++;
		
		
		//Backspace////
		if(KeyboardInput.wasKeyTyped(KeyEvent.VK_BACK_SPACE, true) && boxTextElement.getText().length() - 1 >= 0 && markerPosition > -boxTextElement.getText().length()) {
			String currentText = boxTextElement.getText();
			
			String newTextLeft = currentText.substring(0, currentText.length() + markerPosition - 1);
			String newTextRight = currentText.substring(currentText.length() + markerPosition, currentText.length());
			
			boxTextElement.setText(newTextLeft + newTextRight);
		}
		
		
		
		//Type////
		ArrayList<Character> charsTyped = new ArrayList<Character>(KeyboardInput.getCharsTyped());
		
		
		
		//Paste from clipboard////
		if(KeyboardInput.wasKeyTyped(KeyEvent.VK_V, true) && KeyboardInput.isKeyDown(KeyEvent.VK_CONTROL, true)) {
			String content = null;
			
			
			try {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				content = (String) clipboard.getData(DataFlavor.stringFlavor);
				
			} catch (UnsupportedFlavorException | IOException e) {
				System.out.println("Error when pasting text into TextBoxElement.");
			}
			
			
			if(content != null)
				for(char c : content.toCharArray())
					charsTyped.add(c);
		}
		
		
		
		//Add typed/pasted chars to text box////
		for(char c : charsTyped) {
			char cLower = Character.toLowerCase(c);
			
			if(allowedChars.indexOf(cLower) >= 0 && boxTextElement.getText().length() + 1 <= maxBoxTextLength)
				insertBoxText(Character.toString(c));
		}
	}

	
	@Override
	public void render(Graphics2D g, Game game) {
		String normalText = null;
		
		if(isFocused) {
			normalText = boxTextElement.getText();
			
			insertBoxText(MARKER);
		}
		
		
		boxTextElement.render(g, game);
		
		
		if(isFocused)
			boxTextElement.setText(normalText);
		
		
		
		BufferedImage imageLeft;
		BufferedImage imageRight;
		
		if(isFocused) {
			imageLeft = ImageResources.textBoxLeftFocused;
			imageRight = ImageResources.textBoxRightFocused;
			
		} else if(isHovering) {
			imageLeft = ImageResources.textBoxLeftHover;
			imageRight = ImageResources.textBoxRightHover;
			
		} else {
			imageLeft = ImageResources.textBoxLeft;
			imageRight = ImageResources.textBoxRight;
		}
		
		
		g.drawImage(imageLeft,
				(int) (boxTextElement.grabAnchoredX(game) - boxTextElement.getWidth() / 2 - SIDE_IMAGES_SPACE * size - imageLeft.getWidth() / 2 * SIDE_IMAGES_SCALE * size),
				(int) (boxTextElement.grabAnchoredY(game) - imageLeft.getHeight() / 2 * SIDE_IMAGES_SCALE * size),
				(int) (imageLeft.getWidth() * SIDE_IMAGES_SCALE * size),
				(int) (imageLeft.getHeight() * SIDE_IMAGES_SCALE * size), null);
		
		g.drawImage(imageRight,
				(int) (boxTextElement.grabAnchoredX(game) + boxTextElement.getWidth() / 2 + SIDE_IMAGES_SPACE * size - imageLeft.getWidth() / 2 * SIDE_IMAGES_SCALE * size),
				(int) (boxTextElement.grabAnchoredY(game) - imageLeft.getHeight() / 2 * SIDE_IMAGES_SCALE * size),
				(int) (imageRight.getWidth() * SIDE_IMAGES_SCALE * size),
				(int) (imageRight.getHeight() * SIDE_IMAGES_SCALE * size), null);
		
		
		
		
		g.setFont(FontResources.mainFontBold.deriveFont(LABEL_TEXT_SCALE * size));
		g.setColor(LABEL_TEXT_COLOR);
		
		int labelTextWidth = g.getFontMetrics().stringWidth(labelText);
		
		
		int labelX = this.grabAnchoredX(game) - (labelTextWidth / 2);
		
		if(labelAnchorX == UIAnchor.NEGATIVE) {
			labelX -= (width / 2) - (labelTextWidth / 2);
			
		} else if(labelAnchorX == UIAnchor.POSITIVE) {
			labelX += (width / 2) - (labelTextWidth / 2);
		}
		
		
		g.drawString(labelText, labelX, (this.grabAnchoredY(game) - LABEL_TEXT_SPACE * size));
	}
	
	
	
	
	
	private void insertBoxText(String text) {
		String currentText = boxTextElement.getText();
		
		String newTextLeft = currentText.substring(0, currentText.length() + markerPosition);
		String newTextRight = currentText.substring(currentText.length() + markerPosition, currentText.length());
		
		boxTextElement.setText(newTextLeft + text + newTextRight);
	}
	
	
	
	
	
	public void removeFocus() {
		isFocused = false;
		
		KeyboardInput.setIsPromptActive(false);
		markerPosition = 0;
	}
	
	
	private void addFocus() {
		isFocused = true;
		
		KeyboardInput.setIsPromptActive(true);
		
		if(boxTextElement.getText().equals(BLANK_FILLER))
			boxTextElement.setText("");
	}
	
	
	
	public boolean isEmpty() {
		String text = boxTextElement.getText();
		
		if(text.equals("") || text.equals(BLANK_FILLER) || text.trim().equals(""))
			return true;
		
		
		return false;
	}
	
	
	public void clearText() {
		boxTextElement.setText("");
	}
	
	
	
	
	
	
	public String getText() {
		return boxTextElement.getText();
	}
	
	
	
	public float getSize() {
		return size;
	}
	
	
	public String getLabelText() {
		return labelText;
	}
	
	
	public int getMaxBoxTextLength() {
		return maxBoxTextLength;
	}
	
	
	public String getAllowedChars() {
		return allowedChars;
	}
	
	
	public boolean isFocused() {
		return isFocused;
	}

	
}
