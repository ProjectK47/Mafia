package hyperbox.mafia.animation;

public class CoolDown {
	
	
	
	private int coolDownTicks;
	
	private int currentCoolDown;
	
	
	
	public CoolDown(int coolDownTicks) {
		this.coolDownTicks = coolDownTicks;	
		
		currentCoolDown = coolDownTicks;
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
