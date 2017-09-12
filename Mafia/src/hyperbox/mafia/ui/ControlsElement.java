package hyperbox.mafia.ui;

public class ControlsElement extends ListElement {

	
	
	
	public ControlsElement(int x, int y, UIAnchor screenAnchorX, UIAnchor screenAnchorY, UIAnchor elementAnchorX, UIAnchor elementAnchorY, float scale) {
		super(x, y, screenAnchorX, screenAnchorY, elementAnchorX, elementAnchorY, scale, "Controls", null, false);
		
		addControls();
	}
	
	
	
	private void addControls() {
		this.addEntry(new ListEntry("Move up: W"));
		this.addEntry(new ListEntry("Move down: S"));
		this.addEntry(new ListEntry("Move left: A"));
		this.addEntry(new ListEntry("Move right: D"));
		
		this.addEntry(new ListEntry("Sleep: Space"));
		
		this.addEntry(new ListEntry("Point: L-Mouse"));
		this.addEntry(new ListEntry("Poke: R-Mouse"));
		
		this.addEntry(new ListEntry("Explode player: X"));
		this.addEntry(new ListEntry("Save player: Y"));
		
		this.addEntry(new ListEntry("Chat: Enter"));
		
		this.addEntry(new ListEntry("Exit game: Sft+Esc"));
		//this.addEntry(new ListEntry("Skip splash: Spc/Esc"));
	}
	
}
