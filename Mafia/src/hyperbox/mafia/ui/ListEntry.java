package hyperbox.mafia.ui;

public class ListEntry {
	
	
	private String text;
	private Runnable onClick;
	
	
	
	public ListEntry(String text, Runnable onClick) {
		this.text = text;
		this.onClick = onClick;
	}

	
	public ListEntry(String text) {
		this(text, null);
	}
	
	
	
	
	public String getText() {
		return text;
	}
	
	public Runnable getOnClick() {
		return onClick;
	}
	
}
