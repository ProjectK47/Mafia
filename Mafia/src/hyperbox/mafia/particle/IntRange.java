package hyperbox.mafia.particle;

import java.util.Random;

public class IntRange {
	
	
	private int min;
	private int max;
	
	private static Random random = new Random();
	
	
	
	public IntRange(int min, int max) {
		this.min = min;
		this.max = max;
	}

	
	
	
	public int randomIntInRange() {
		int value = random.nextInt(max - min + 1) + min;
		
		return value;
	}
	
	
	

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}


	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
}
