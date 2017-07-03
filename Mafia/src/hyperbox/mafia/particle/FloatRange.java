package hyperbox.mafia.particle;

import java.util.Random;

public class FloatRange {

	
	private float min;
	private float max;
	
	private static Random random = new Random();
	
	
	
	public FloatRange(float min, float max) {
		this.min = min;
		this.max = max;
	}

	
	
	
	public float randomFloatInRange() {
		float value = random.nextFloat() * (max - min) + min;
		
		return value;
	}
	
	

	
	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}


	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}
	
}
