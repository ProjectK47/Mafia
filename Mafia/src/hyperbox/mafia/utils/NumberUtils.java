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
	
	
}
