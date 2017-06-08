package hyperbox.mafia.gamestate;

import java.awt.Graphics2D;

import hyperbox.mafia.core.Game;

public abstract class GameState {

	
	
	protected boolean isEnabled = false;
	

	
	
	public void enable(Game game) {
		if(isEnabled)
			return;
		
		
		isEnabled = true;
		
		onEnable(game);
	}
	
	
	public void disable(Game game) {
		if(!isEnabled)
			return;
		
		
		isEnabled = false;
		
		onDisable(game);
	}
	
	
	
	
	protected void tick(Game game) {
		
		onTick(game);
	}
	
	
	protected void render(Graphics2D g, Game game) {
		
		onRender(g, game);
	}
	
	
	
	protected abstract void onEnable(Game game);
	protected abstract void onDisable(Game game);
	
	protected abstract void onTick(Game game);
	protected abstract void onRender(Graphics2D g, Game game);
	
	
	
	
	
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
}
