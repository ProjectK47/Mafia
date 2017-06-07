package hyperbox.mafia.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.input.MouseInput;
import hyperbox.mafia.io.AudioResources;
import hyperbox.mafia.io.ImageResources;

public class ButtonElement extends UIElement {

	
	
	public static final Color TEXT_COLOR = new Color(255, 255, 204);
	public static final float TEXT_BOUNDARY_BUFFER = 6f;
	
	
	
	protected float scale;
	protected String text;
	
	protected Runnable onPress;
	
	
	protected ImageElement defaultImageElement;
	protected ImageElement hoverImageElement;
	protected ImageElement pressedImageElement;
	
	protected TextElement textElement;
	
	
	
	protected boolean isHovering = false;
	
	
	
	
	public ButtonElement(int x, int y, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY, 
			float scale, String text, Runnable onPress) {
		
		super(x, y, (int) (ImageResources.button.getWidth() * scale), (int) (ImageResources.button.getHeight() * scale), 
				screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY);
		
		
		this.scale = scale;
		this.text = text;
		
		this.onPress = onPress;
		
		
		defaultImageElement = new ImageElement(x, y, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, ImageResources.button, scale);
		hoverImageElement = new ImageElement(x, y, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, ImageResources.buttonHover, scale);
		pressedImageElement = new ImageElement(x, y, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, ImageResources.buttonPressed, scale);
	}

	
	
	
	@Override
	public void tick(Game game) {
		int screenX = this.grabAnchoredX(game) - (width / 2);
		int screenY = this.grabAnchoredY(game) - (height / 2);
		
		int mouseX = MouseInput.grabScreenMouseX(game);
		int mouseY = MouseInput.grabScreenMouseY(game);
		
		
		isHovering = false;
		
		if(mouseX >= screenX && mouseX <= screenX + width)
			if(mouseY >= screenY && mouseY <= screenY + height)
				isHovering = true;
		
		
		
		if(isHovering && MouseInput.wasPrimaryClicked()) {
			onPress.run();
			
			AudioResources.buttonClick.playAudio();
		}
	}

	
	
	@Override
	public void render(Graphics2D g, Game game) {
		
		if(textElement == null) {
			float textSize = TextElement.grabMaxTextSize(text, width - (int) (TEXT_BOUNDARY_BUFFER * scale), height - (int) (TEXT_BOUNDARY_BUFFER * scale), g);
			
			textElement = new TextElement(0, 0, UIAnchor.PARENT, UIAnchor.PARENT, UIAnchor.CENTER, UIAnchor.CENTER, text, textSize, TEXT_COLOR);
			textElement.setParent(this);
		}
		
		
		
		
		if(isHovering) {
			if(MouseInput.isPrimaryPressed())
				pressedImageElement.render(g, game);
			else
				hoverImageElement.render(g, game);
			
		} else {
			defaultImageElement.render(g, game);
		}
		
		
		
		textElement.render(g, game);
	}

	
	
	
	
	
	public float getScale() {
		return scale;
	}
	
	
	public String getText() {
		return text;
	}
	
}
