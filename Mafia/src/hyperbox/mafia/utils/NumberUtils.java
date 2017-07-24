package hyperbox.mafia.utils;

import java.util.Random;

public class NumberUtils {

	
	
	private static Random random = new Random();
	
	
	
	public static int randomIntInRange(int min, int max) {
		int integer = random.nextInt(max - min + 1) + min;
		
		return integer;
	}
	
	
	
	public static boolean booleanFromProbability(float probability) {
		float randFloat = random.nextFloat();
		
		if(probability >= randFloat)
			return true;
		
		
		return false;
	}
	
	
	
	public static float lerp(float currentValue, float goal, float speed) {
		float finalValue = currentValue;
		
		
		if(goal > currentValue) {
			finalValue += speed;
			
			if(finalValue > goal)
				finalValue = goal;
			
		} else if(goal < currentValue) {
			finalValue -= speed;
			
			if(finalValue < goal)
				finalValue = goal;
		}
		
		
		return finalValue;
	}
	
	
	
	public static float sineWave(float x, float scale) {
		float sin = (float) (Math.sin(Math.toRadians(x)) * scale);
		
		return sin;
	}
	
	
	
	public static float distance(float x1, float y1, float x2, float y2) {
		float xLength = x2 - x1;
		float yLength = y2 - y1;
		
		float distance = (float) Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
		
		
		return distance;
	}
	
}
