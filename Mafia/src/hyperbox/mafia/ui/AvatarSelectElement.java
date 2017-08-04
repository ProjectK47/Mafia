package hyperbox.mafia.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import hyperbox.mafia.animation.CoolDown;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.input.MouseInput;
import hyperbox.mafia.io.FontResources;
import hyperbox.mafia.io.ImageResources;

public class AvatarSelectElement extends UIElement {

	
	public static final float ICON_BOX_SCALE = 1.2f;
	public static final float ICON_BOX_SPACE = 4.5f;
	
	public static final int ICON_ANIMATION_COOL_DOWN = 12;
	
	public static final String LABEL_TEXT = "Avatar";
	public static final Color LABEL_TEXT_COLOR = new Color(255, 255, 242);
	public static final float LABEL_TEXT_SIZE = 3.75f;
	public static final float LABEL_TEXT_X_SPACE = 2f;
	public static final float LABEL_TEXT_Y_SPACE = 1.75f;
	
	
	private float scale;
	private int selectedAvatar;
	
	private BufferedImage[][] icons;
	
	private int hoveringIcon = -1;
	
	private CoolDown iconAnimationCoolDown = new CoolDown(ICON_ANIMATION_COOL_DOWN, true);
	private boolean isFirstAnimationStage = true;
	
	
	public AvatarSelectElement(int x, int y, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY,
			float scale, int selectedAvatar) {
		
		super(x, y, 0, 0, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, 1);
		
		
		this.scale = scale;
		this.selectedAvatar = selectedAvatar;
		
		icons = ImageResources.players;
	}

	

	@Override
	public void onTick(Game game) {
		if(width == 0 || height == 0) {
			width = (int) (ImageResources.iconBox.getWidth() * ICON_BOX_SCALE * scale);
			height = (int) (icons.length * ImageResources.iconBox.getHeight() * ICON_BOX_SCALE * scale + Math.max(icons.length - 1, 0) * ICON_BOX_SPACE * scale);
		}
		
		
		
		//Hovering icon////
		hoveringIcon = -1;
		
		
		int mouseX = MouseInput.grabScreenMouseX(game);
		int mouseY = MouseInput.grabScreenMouseY(game);
		
		BufferedImage iconBox = ImageResources.iconBox;
		
		
		int boxY = this.grabAnchoredY(game) - (height / 2);
		
		for(int i = 0; i < icons.length; i ++) {
			int boxX = this.grabAnchoredX(game) - (width / 2);
			
			if(mouseX >= boxX && mouseX <= boxX + iconBox.getWidth() * ICON_BOX_SCALE * scale)
				if(mouseY >= boxY && mouseY <= boxY + iconBox.getHeight() * ICON_BOX_SCALE * scale)
					hoveringIcon = i;
					
				
			boxY += iconBox.getHeight() * ICON_BOX_SCALE * scale + ICON_BOX_SPACE * scale;
		}
		
		
		
		if(hoveringIcon != -1) {
			//Selected icon////
			if(MouseInput.wasPrimaryClicked(mousePriority))
				selectedAvatar = hoveringIcon;
			
			
			//Icon hover animation////
			iconAnimationCoolDown.tick();
			
			iconAnimationCoolDown.executeIfReady(() -> {
				isFirstAnimationStage = !isFirstAnimationStage;
			});
			
		} else {
			iconAnimationCoolDown.rushCurrentCoolDown();
			isFirstAnimationStage = true;
		}
	}
	
	

	@Override
	public void render(Graphics2D g, Game game) {
		int currentY = this.grabAnchoredY(game) - (height / 2);
		
		
		//Label////
		g.setFont(FontResources.mainFontBold.deriveFont(LABEL_TEXT_SIZE * scale));
		g.setColor(LABEL_TEXT_COLOR);
		
		
		int labelWidth = g.getFontMetrics().stringWidth(LABEL_TEXT);
		
		int labelX = this.grabAnchoredX(game) - (labelWidth / 2);
		
		if(this.screenAnchorX == UIAnchor.NEGATIVE)
			labelX += labelWidth / 2 + width / 2 - LABEL_TEXT_X_SPACE * scale;
		else if(this.screenAnchorX == UIAnchor.POSITIVE)
			labelX -= labelWidth / 2 - width / 2 + LABEL_TEXT_X_SPACE * scale;
		
		
		g.drawString(LABEL_TEXT, labelX, currentY - (LABEL_TEXT_Y_SPACE * scale));
		
		
		
		for(int i = 0; i < icons.length; i ++) {
			//Icon box////
			BufferedImage iconBox;
			
			if(i != selectedAvatar)
				iconBox = ImageResources.iconBox;
			else
				iconBox = ImageResources.iconBoxSelected;
			
			
			int iconBoxHeight = (int) (iconBox.getHeight() * ICON_BOX_SCALE * scale);
			
			
			//Icon////
			BufferedImage icon;
			
			if(i != hoveringIcon) {
				icon = icons[i][0];
				
			} else {
				if(isFirstAnimationStage)
					icon = icons[i][1];
				else
					icon = icons[i][2];
			}
			
			
			//Render icons////
			g.drawImage(iconBox, this.grabAnchoredX(game) - (width / 2), currentY,
					(int) (iconBox.getWidth() * ICON_BOX_SCALE * scale), iconBoxHeight, null);
			
			
			g.drawImage(icon, this.grabAnchoredX(game) - (int) (icon.getWidth() * scale / 2), (int) (currentY + iconBoxHeight / 2 - icon.getHeight() * scale / 2),
					(int) (icon.getWidth() * scale), (int) (icon.getHeight() * scale), null);
			
			
			currentY += iconBox.getHeight() * ICON_BOX_SCALE * scale + ICON_BOX_SPACE * scale;
		}
	}

	
	
	
	public float getScale() {
		return scale;
	}
	
	
	public int getSelectedAvatar() {
		return selectedAvatar;
	}
	
}
