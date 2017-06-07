package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;

public abstract class GameState {

	
	
	protected boolean isEnabled = false;
	

	
	
	public void enable() {
		if(isEnabled)
			return;
		
		
		isEnabled = true;
		
		onEnable();
	}
	
	
	public void disable() {
		if(!isEnabled)
			return;
		
		
		isEnabled = false;
		
		onDisable();
	}
	
	
	
	
	protected void tick(Game game) {
		
		onTick(game);
	}
	
	
	protected void render(Graphics2D g, Game game) {
		
		onRender(g, game);
	}
	
	
	
	protected abstract void onEnable();
	protected abstract void onDisable();
	
	protected abstract void onTick(Game game);
	protected abstract void onRender(Graphics2D g, Game game);
	
	
	
	
	
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
}
