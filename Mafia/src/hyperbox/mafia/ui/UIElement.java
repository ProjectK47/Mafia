package hyperbox.mafia.ui;

import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.input.MouseInput;

public abstract class UIElement {

	
	
	protected int x;
	protected int y;
	
	protected int width;
	protected int height;
	
	
	protected UIAnchor screenAnchorX;
	protected UIAnchor screenAnchorY;
	
	protected UIAnchor elementAnchorX;
	protected UIAnchor elementAnchorY;
	
	
	protected boolean shouldBlockMouse;
	
	protected int lastTick;
	protected boolean shouldAllowClickThrough = false;
	
	
	protected UIElement parent = null;
	
	
	
	public UIElement(int x, int y, int width, int height, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY, boolean shouldBlockMouse) {
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		
		this.screenAnchorX = screenAnchorX;
		this.screenAnchorY = screenAnchorY;
		
		this.elementAnchorX = elementAnchorX;
		this.elementAnchorY = elementAnchorY;
		
		
		this.shouldBlockMouse = shouldBlockMouse;
	}


	
	
	
	public void tick(Game game) {
		MouseInput.removeElementMousingOver(this);
		
		
		if(shouldBlockMouse && !shouldAllowClickThrough) {
			int mouseX = MouseInput.grabScreenMouseX(game);
			int mouseY = MouseInput.grabScreenMouseY(game);
			
			int anchoredX = grabAnchoredX(game);
			int anchoredY = grabAnchoredY(game);
			
			
			boolean isMousingOver = false;
			
			if(mouseX >= anchoredX - (width / 2) && mouseX <= anchoredX + (width / 2))
				if(mouseY >= anchoredY - (height / 2) && mouseY <= anchoredY + (height / 2))
					isMousingOver = true;
					
			
			if(isMousingOver)
				MouseInput.addElementMousingOver(this);
			else
				MouseInput.removeElementMousingOver(this);
		}
		
		
		lastTick = game.getCurrentTick();
		
		
		onTick(game);
	}
	
	
	public abstract void onTick(Game game);
	public abstract void render(Graphics2D g, Game game);
	
	
	
	
	
	protected int grabAnchoredX(Game game) {
		int anchoredX = x;
		
		
		if(screenAnchorX == UIAnchor.POSITIVE)
			anchoredX += game.getWidth() / 2;
		else if(screenAnchorX == UIAnchor.NEGATIVE)
			anchoredX -= game.getWidth() / 2;
		else if(screenAnchorX == UIAnchor.PARENT && parent != null)
			anchoredX += parent.grabAnchoredX(game);
		
		if(elementAnchorX == UIAnchor.POSITIVE)
			anchoredX -= width / 2;
		else if(elementAnchorX == UIAnchor.NEGATIVE)
			anchoredX += width / 2;
		
		
		return anchoredX;
	}
	
	
	protected int grabAnchoredY(Game game) {
		int anchoredY = y;
		
		
		if(screenAnchorY == UIAnchor.POSITIVE)
			anchoredY += game.getHeight() / 2;
		else if(screenAnchorY == UIAnchor.NEGATIVE)
			anchoredY -= game.getHeight() / 2;
		else if(screenAnchorY == UIAnchor.PARENT && parent != null)
			anchoredY += parent.grabAnchoredY(game);
		
		if(elementAnchorY == UIAnchor.POSITIVE)
			anchoredY -= height / 2;
		else if(elementAnchorY == UIAnchor.NEGATIVE)
			anchoredY += height / 2;
		
		
		return anchoredY;
	}
	
	
	
	

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	
	public UIAnchor getScreenAnchorX() {
		return screenAnchorX;
	}

	public UIAnchor getScreenAnchorY() {
		return screenAnchorY;
	}
	
	
	public UIAnchor getElementAnchorX() {
		return elementAnchorX;
	}

	public UIAnchor getElementAnchorY() {
		return elementAnchorY;
	}
	
	
	public boolean shouldBlockMouse() {
		return shouldBlockMouse;
	}
	
	
	public int getLastTick() {
		return lastTick;
	}
	
	
	public boolean shouldAllowClickThrough() {
		return shouldAllowClickThrough;
	}
	
	public void setShouldAllowClickThrough(boolean shouldAllowClickThrough) {
		this.shouldAllowClickThrough = shouldAllowClickThrough;
	}
	
	
	public UIElement getParent() {
		return parent;
	}
	
	public void setParent(UIElement parent) {
		this.parent = parent;
	}
	
}
