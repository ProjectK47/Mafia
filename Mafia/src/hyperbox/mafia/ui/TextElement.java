package hyperbox.mafia.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.io.FontResources;

public class TextElement extends UIElement {

	
	public static final float SHADOW_DISPLACMENT = 0.05f;
	
	
	protected String text;
	protected float size;
	
	protected Color color;
	
	
	private boolean updateBounds = false;
	
	
	
	public TextElement(int x, int y, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY,
			String text, float size, Color color) {
		
		super(x, y, 0, 0, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, 0);
		
		
		this.text = text;
		this.size = size;
		
		this.color = color;
	}

	
	
	
	@Override
	public void onTick(Game game) {
		
	}

	
	@Override
	public void render(Graphics2D g, Game game) {
		g.setFont(FontResources.mainFont.deriveFont(size));
		
		
		if(width == 0 || height == 0 || updateBounds) {
			FontRenderContext frc = g.getFontRenderContext();
			Rectangle2D bounds = g.getFont().getStringBounds(text, frc);
			
			width = (int) bounds.getWidth();
			height = (int) bounds.getHeight();
			
			
			updateBounds = false;
		}
		
		
		
		
		g.setColor(color.darker());
		g.drawString(text, this.grabAnchoredX(game) - (width / 2) + (int) (SHADOW_DISPLACMENT * size), this.grabAnchoredY(game) + (height / 4) + (int) (SHADOW_DISPLACMENT * size));
		
			
		g.setColor(color);
		g.drawString(text, this.grabAnchoredX(game) - (width / 2), this.grabAnchoredY(game) + (height / 4));
	}
	
	
	
	
	
	
	public static float grabMaxTextSize(String textToFit, int widthConstraint, int heightConstraint, Graphics2D g) {
		float textSize = 1;
		
		
		while(true) {
			g.setFont(FontResources.mainFont.deriveFont(textSize));
			
			FontRenderContext frc = g.getFontRenderContext();
			Rectangle2D bounds = g.getFont().getStringBounds(textToFit, frc);
			
			int textWidth = (int) bounds.getWidth();
			int textHeight = (int) bounds.getHeight();
			
			
			if(textWidth < widthConstraint && textHeight < heightConstraint)
				textSize ++;
			else
				break;
		}
		
		
		return textSize;
	}
	

	
	
	
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		
		updateBounds = true;
	}
	
	
	public float getSize() {
		return size;
	}
	
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	
}
