package hyperbox.mafia.animation;

import hyperbox.mafia.core.Game;

public class CoolDown {
	
	
	
	private int coolDownTicks;
	
	private int currentCoolDown;
	private boolean isLocked = false;
	
	
	
	public CoolDown(int coolDownTicks) {
		this.coolDownTicks = coolDownTicks;	
		
		currentCoolDown = coolDownTicks;
	}
	
	
	public CoolDown(int coolDownTicks, boolean shouldStartCompleted) {
		this.coolDownTicks = coolDownTicks;	
		
		if(!shouldStartCompleted)
			currentCoolDown = coolDownTicks;
		else
			currentCoolDown = 0;
	}
	
	
	
	
	public void tick() {
		if(currentCoolDown > 0 && !isLocked)
			currentCoolDown --;
	}
	
	
	
	public void executeIfReady(Runnable task) {
		executeIfReady(task, false);
	}
	
	
	public void executeIfReady(Runnable task, boolean shouldLock) {
		
		if(currentCoolDown <= 0) {
			task.run();
			
			currentCoolDown = coolDownTicks;
			
			
			if(shouldLock)
				isLocked = true;
		}
	}
	
	
	
	
	
	
	public void resetCurrentCoolDown() {
		currentCoolDown = coolDownTicks;
	}
	
	
	public void rushCurrentCoolDown() {
		currentCoolDown = 0;
	}
	
	
	
	public int grabCurrentCoolDownSeconds() {
		int seconds = currentCoolDown / Game.TARGET_TPS + 1;
		
		return seconds;
	}
	
	
	
	public int getCoolDownTicks() {
		return coolDownTicks;
	}
	
	
	public void setCoolDownTicks(int coolDownTicks) {
		this.coolDownTicks = coolDownTicks;
	}
	
	
	
	public int getCurrentCoolDown() {
		return currentCoolDown;
	}
	
	
	public boolean isLocked() {
		return isLocked;
	}
	
	

}
