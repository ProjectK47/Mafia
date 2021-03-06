package hyperbox.mafia.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.input.MouseInput;
import hyperbox.mafia.io.FontResources;
import hyperbox.mafia.io.ImageResources;

public class ListElement extends UIElement {
	
	
	public static final float LIST_TEXT_SIZE = 2.5f;
	public static final float LIST_ENTRY_INITIAL_X_SPACE = 3.6f;
	public static final float LIST_ENTRY_INITIAL_Y_SPACE = 6f;
	public static final float LIST_ENTRY_Y_SPACE = 3.6f;
	
	public static final Color LIST_TEXT_EVEN_COLOR = new Color(255, 255, 204);
	public static final Color LIST_TEXT_ODD_COLOR = new Color(227, 227, 184);
	public static final Color LIST_TEXT_SELECTED_COLOR = new Color(51, 214, 255);
	
	public static final float LIST_ICON_SCALE = 0.175f;
	public static final float LIST_ICON_X_SPACE = 6.75f;
	
	public static final int LIST_MAX_ENTRIES = 7;
	
	public static final float SCROLL_BUTTON_SCALE = 0.25f;
	
	
	public static final float LABEL_TEXT_SIZE = 2.5f;
	public static final Color LABEL_TEXT_COLOR = new Color(255, 255, 204);
	public static final float LABEL_TEXT_X_SPACE = 1.5f;
	public static final float LABEL_TEXT_Y_SPACE = 1f;
	
	
	public static final float SCROLL_INDICATOR_X_SPACE = 0.8f;
	public static final float SCROLL_INDICATOR_Y_SPACE = 2.5f;
	public static final float SCROLL_INDICATOR_WIDTH = 2f;
	
	public static final Color SCROLL_INDICATOR_COLOR = new Color(237, 237, 190);
	
	
	
	protected float scale;
	protected String labelText;
	protected BufferedImage listIcon;
	protected boolean isDisabled;
	
	
	private ImageElement boxElement;
	
	private ButtonElement scrollDownButton;
	private ButtonElement scrollUpButton;
	
	
	protected ArrayList<ListEntry> entries = new ArrayList<ListEntry>();
	private int entryHeight;
	
	private int startEntry = 0;
	protected int selectedEntry = -1;
	
	
	
	public ListElement(int x, int y, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY,
			float scale, String labelText, BufferedImage listIcon, boolean isDisabled) {
		
		super(x, y, 0, 0, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, 1);
		
		this.scale = scale;
		this.labelText = labelText;
		this.listIcon = listIcon;
		
		
		boxElement = new ImageElement(x, y, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, ImageResources.listBox, scale);
		
		scrollDownButton = new ButtonElement((int) (10.5f * scale), (int) (18.5 * scale), UIAnchor.PARENT, UIAnchor.PARENT, UIAnchor.CENTER, UIAnchor.CENTER, 
				SCROLL_BUTTON_SCALE * scale, "\\/", () -> {
					
					scrollList(1);
		});
		
		scrollDownButton.setParent(boxElement);
		
		
		scrollUpButton = new ButtonElement((int) (10.5f * scale), (int) -(18.5 * scale), UIAnchor.PARENT, UIAnchor.PARENT, UIAnchor.CENTER, UIAnchor.CENTER, 
				SCROLL_BUTTON_SCALE * scale, "/\\", () -> {
					
					scrollList(-1);
		});
		
		scrollUpButton.setParent(boxElement);
		
		
		setIsDisabled(isDisabled);
	}

	
	
	
	@Override
	public void onTick(Game game) {
		if(width == 0 || height == 0) {
			width = boxElement.getWidth();
			height = boxElement.getHeight();
		}
		
		
		
		if(isDisabled)
			return;
		
		
		
		scrollDownButton.tick(game);
		scrollUpButton.tick(game);
		
		
		
		int mouseX = MouseInput.grabScreenMouseX(game);
		int mouseY = MouseInput.grabScreenMouseY(game);
		
		
		//Select entry////
		selectedEntry = -1;
		
		for(int i = 0; i < LIST_MAX_ENTRIES; i ++) {
			int logicI = i + startEntry;
			
			if(logicI >= entries.size() || logicI < 0)
				continue;
			
			
			int entryX = (int) (this.grabAnchoredX(game) - (width / 2) + LIST_ENTRY_INITIAL_X_SPACE * scale);
			int entryY = (int) (this.grabAnchoredY(game) - (height / 2) + (LIST_ENTRY_INITIAL_Y_SPACE * scale) - entryHeight + (LIST_ENTRY_Y_SPACE * i * scale));
			
			if(mouseX >= entryX && mouseX <= entryX + width - (int) (LIST_ENTRY_INITIAL_X_SPACE * scale * 2))
				if(mouseY >= entryY && mouseY <= entryY + entryHeight)
					selectedEntry = logicI;
		}
		
			
		
		//Scrolling////
		if(mouseX >= this.grabAnchoredX(game) - (width / 2) && mouseX <= this.grabAnchoredX(game) + (width / 2))
			if(mouseY >= this.grabAnchoredY(game) - (height / 2) && mouseY <= this.grabAnchoredY(game) + (height / 2))
				scrollList(MouseInput.getAmountScrolled());
		
		
		
		
		//Run entry////
		if(selectedEntry != -1)
			if(MouseInput.wasPrimaryClicked(this.mousePriority)) {
				ListEntry entry = entries.get(selectedEntry);
				
				if(entry.getOnClick() != null)
					entry.getOnClick().run();
			}
	}

	
	
	@Override
	public void render(Graphics2D g, Game game) {
		if(isDisabled)
			return;
		
		
		boxElement.render(g, game);
		
		scrollDownButton.render(g, game);
		scrollUpButton.render(g, game);
		
		
		
		//List text////
		g.setFont(FontResources.mainFontBold.deriveFont(LIST_TEXT_SIZE * scale));
		entryHeight = g.getFontMetrics().getHeight();
		
		
		for(int i = 0; i < entries.size(); i ++) {
			
			if(i < startEntry)
				continue;
			
			
			int renderI = i - startEntry;
			
			if(renderI >= LIST_MAX_ENTRIES)
				break;
			
			
			ListEntry entry = entries.get(i);
			
			
			if(i != selectedEntry || entry.getOnClick() == null) {
				if(i % 2 == 0)
					g.setColor(LIST_TEXT_EVEN_COLOR);
				else
					g.setColor(LIST_TEXT_ODD_COLOR);
				
			} else
				g.setColor(LIST_TEXT_SELECTED_COLOR);
			
			
			g.drawString(entry.getText(), this.grabAnchoredX(game) - (width / 2) + (LIST_ENTRY_INITIAL_X_SPACE * scale), 
					this.grabAnchoredY(game) - (height / 2) + (LIST_ENTRY_INITIAL_Y_SPACE * scale) + (LIST_ENTRY_Y_SPACE * renderI * scale));
			
			
			
			//Icon////
			if(i == selectedEntry && listIcon != null && entry.getOnClick() != null) {
				int iconWidth = (int) (listIcon.getWidth() * LIST_ICON_SCALE * scale);
				int iconHeight = (int) (listIcon.getHeight() * LIST_ICON_SCALE * scale);
				
				g.drawImage(listIcon, (int) (this.grabAnchoredX(game) + (width / 2) - (LIST_ICON_X_SPACE * scale)),
						MouseInput.grabScreenMouseY(game) - (iconHeight / 2), iconWidth, iconHeight, null);
			}
		}
		
		
		
		//Label////
		g.setFont(FontResources.mainFontBold.deriveFont(LABEL_TEXT_SIZE * scale));
		g.setColor(LABEL_TEXT_COLOR);
		
		g.drawString(labelText, this.grabAnchoredX(game) - (width / 2) + (LABEL_TEXT_X_SPACE * scale), this.grabAnchoredY(game) - (height / 2) - (LABEL_TEXT_Y_SPACE * scale));
		
		
		
		
		//Scroll indicator////
		g.setColor(SCROLL_INDICATOR_COLOR);
		
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke(SCROLL_INDICATOR_WIDTH));
		
		
		int maxStartValue = entries.size() - LIST_MAX_ENTRIES;
		float indicatorScale = (float) startEntry / maxStartValue;
		
		if(indicatorScale > 0) {
			int indicatorHeight = (int) (height - (SCROLL_INDICATOR_Y_SPACE * scale));
			
			g.drawLine((int) (this.grabAnchoredX(game) + (width / 2) + (SCROLL_INDICATOR_X_SPACE * scale)), this.grabAnchoredY(game) - (indicatorHeight / 2),
					(int) (this.grabAnchoredX(game) + (width / 2) + (SCROLL_INDICATOR_X_SPACE * scale)), 
					(int) (this.grabAnchoredY(game) - (indicatorHeight / 2) + (indicatorHeight * indicatorScale)));
		}
		
		
		g.setStroke(oldStroke);
	}

	
	
	
	private void scrollList(int amountToScroll) {
		if(startEntry + amountToScroll < 0)
			startEntry = 0;
		else if(entries.size() - startEntry - amountToScroll < LIST_MAX_ENTRIES)
			startEntry = entries.size() - LIST_MAX_ENTRIES;
		else
			startEntry += amountToScroll;
	}
	
	
	
	
	public void addEntry(ListEntry entry) {
		entries.add(entry);
	}
	
	
	
	
	
	public float getScale() {
		return scale;
	}
	
	
	public String getLabelText() {
		return labelText;
	}
	
	
	public BufferedImage getListIcon() {
		return listIcon;
	}
	
	
	public boolean isDisabled() {
		return isDisabled;
	}
	
	public void setIsDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
		
		
		if(isDisabled) {
			this.shouldAllowClickThrough = true;
			
			scrollDownButton.setIsHidden(true);
			scrollUpButton.setIsHidden(true);
			
		} else {
			this.shouldAllowClickThrough = false;
			
			scrollDownButton.setIsHidden(false);
			scrollUpButton.setIsHidden(false);
		}
	}
	
}
