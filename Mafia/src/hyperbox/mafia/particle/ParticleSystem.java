package hyperbox.mafia.particle;

import java.awt.Color;
import java.util.Random;

import hyperbox.mafia.core.Game;

public class ParticleSystem {

	
	private FloatRange directionXRange;
	private FloatRange directionYRange;
	
	private FloatRange speedRange;
	
	private IntRange sizeRange;
	
	private IntRange lifetimeRange;
	
	private IntRange spreadRange;
	private IntRange countRange;
	
	
	private Random random = new Random();
	
	
	
	public ParticleSystem(FloatRange directionXRange, FloatRange directionYRange, FloatRange speedRange, IntRange sizeRange, IntRange lifetimeRange,
			IntRange spreadRange, IntRange countRange) {
		
		this.directionXRange = directionXRange;
		this.directionYRange = directionYRange;
		
		this.speedRange = speedRange;
		
		this.sizeRange = sizeRange;
		
		this.lifetimeRange = lifetimeRange;
		
		this.spreadRange = spreadRange;
		this.countRange = countRange;
	}


	
	
	public void emitParticles(float x, float y, Game game, Color... colors) {
		
		int count = countRange.randomIntInRange();
		
		
		for(int i = 0; i < count; i ++) {
			
			float speed = speedRange.randomFloatInRange();
			
			float velocityX = directionXRange.randomFloatInRange() * speed;
			float velocityY = directionYRange.randomFloatInRange() * speed;
			
			int size = sizeRange.randomIntInRange();
			
			int lifetime = lifetimeRange.randomIntInRange();
			
			
			int spreadX = spreadRange.randomIntInRange();
			int spreadY = spreadRange.randomIntInRange();
			
			if(random.nextBoolean())
				spreadX = -spreadX;
			
			if(random.nextBoolean())
				spreadY = -spreadY;
			
			
			Color color = colors[random.nextInt(colors.length)];
			
			
			
			Particle particle = new Particle(x + spreadX, y + spreadY, velocityX, velocityY, size, color, lifetime);
			game.getGameStateManager().getGameStateInGame().addParticle(particle);
		}
	}
	
	
	

	public FloatRange getDirectionXRange() {
		return directionXRange;
	}

	public FloatRange getDirectionYRange() {
		return directionYRange;
	}


	public FloatRange getSpeedRange() {
		return speedRange;
	}


	public IntRange getSizeRange() {
		return sizeRange;
	}


	public IntRange getLifetimeRange() {
		return lifetimeRange;
	}


	public IntRange getSpreadRange() {
		return spreadRange;
	}

	public IntRange getCountRange() {
		return countRange;
	}
	
}
