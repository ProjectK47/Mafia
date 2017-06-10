package hyperbox.mafia.animation;

public class CoolDown {
	
	
	
	private int coolDownTicks;
	
	private int currentCoolDown;
	
	
	
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
		if(currentCoolDown > 0)
			currentCoolDown --;
	}
	
	
	public void executeIfReady(Runnable task) {
		
		if(currentCoolDown <= 0) {
			task.run();
			
			currentCoolDown = coolDownTicks;
		}
	}
	
	
	
	
	
	
	public void resetCurrentCoolDown() {
		currentCoolDown = coolDownTicks;
	}
	
	
	public void rushCurrentCoolDown() {
		currentCoolDown = 0;
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
	
	

}
