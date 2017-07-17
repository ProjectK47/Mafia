package hyperbox.mafia.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;

import hyperbox.mafia.animation.CoolDown;
import hyperbox.mafia.core.Game;
import hyperbox.mafia.net.PacketResetGame;
import hyperbox.mafia.ui.TextElement;
import hyperbox.mafia.ui.UIAnchor;

public class GameStateInGameEnd extends GameState {
	
	
	public static final Color SURVIVED_BACKGROUND_COLOR_TOP = new Color(204, 255, 153, 200);
	public static final Color SURVIVED_BACKGROUND_COLOR_BOTTOM = new Color(255, 71, 26, 230);
	
	public static final Color DEAD_BACKGROUND_COLOR_TOP = new Color(204, 255, 153, 200);
	public static final Color DEAD_BACKGROUND_COLOR_BOTTOM = new Color(0, 153, 51, 230);
	
	public static final String MAFIA_STATUS_SURVIVED_TEXT = "The Mafia Killed Everyone!";
	public static final String MAFIA_STATUS_DEAD_TEXT = "The Mafia is Dead!";
	
	public static final int NEW_GAME_COOL_DOWN_TIME = 600;

	
	
	private GameStateInGame gameStateInGame;
	
	
	private TextElement mafiaStatusElement;
	
	private TextElement mafiaNameElement;
	private TextElement doctorNameElement;
	
	private TextElement roundElement;
	
	private CoolDown newGameCoolDown;
	private TextElement newGameTimeElement;
	
	
	
	
	@Override
	protected void onEnable(Game game) {
		gameStateInGame = game.getGameStateManager().getGameStateInGame();
		
		
		String mafiaStatusText;
		
		if(gameStateInGame.getPlayers().get(gameStateInGame.getMafiaUsername()).getAliveState() == 1)
			mafiaStatusText = MAFIA_STATUS_SURVIVED_TEXT;
		else
			mafiaStatusText = MAFIA_STATUS_DEAD_TEXT;
		
		mafiaStatusElement = new TextElement(0, 60, UIAnchor.CENTER, UIAnchor.NEGATIVE, UIAnchor.CENTER, UIAnchor.NEGATIVE, mafiaStatusText, 50, new Color(255, 223, 163));
		
		
		
		mafiaNameElement = new TextElement(0, 55, UIAnchor.CENTER, UIAnchor.PARENT, UIAnchor.CENTER, UIAnchor.NEGATIVE, 
				gameStateInGame.getMafiaUsername() + " was the Mafia", 30, new Color(255, 255, 204));
		
		mafiaNameElement.setParent(mafiaStatusElement);
		
		
		doctorNameElement = new TextElement(0, 35, UIAnchor.CENTER, UIAnchor.PARENT, UIAnchor.CENTER, UIAnchor.NEGATIVE, 
				gameStateInGame.getDoctorUsername() + " was the Doctor", 30, new Color(255, 255, 204));
		
		doctorNameElement.setParent(mafiaNameElement);
		
		
		int round = game.getGameStateManager().getGameStateInGameElimination().getCurrentRound();
		roundElement = new TextElement(0, 70, UIAnchor.CENTER, UIAnchor.PARENT, UIAnchor.CENTER, UIAnchor.NEGATIVE, 
				"The game reached Elimination Round #" + round, 20, new Color(255, 255, 204));
		
		roundElement.setParent(doctorNameElement);
		
		
		newGameCoolDown = new CoolDown(NEW_GAME_COOL_DOWN_TIME);
		newGameTimeElement = new TextElement(0, -30, UIAnchor.CENTER, UIAnchor.POSITIVE, UIAnchor.CENTER, UIAnchor.POSITIVE, grabNewGameTimeText(), 33, new Color(255, 223, 163));
		
		
		
		gameStateInGame.resetStatusText();
		gameStateInGame.resetTipText();
	}
	

	@Override
	protected void onDisable(Game game) {
		
	}

	
	
	
	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		
		
		newGameCoolDown.tick();
		
		
		newGameCoolDown.executeIfReady(() -> {
			if(gameStateInGame.isPlayerStoryteller()) {
				gameStateInGame.resetGame(game);
				
				PacketResetGame resetPacket = new PacketResetGame();
				gameStateInGame.getClient().sendPacket(resetPacket);
			}
			
		}, true);
		
		
		newGameTimeElement.setText(grabNewGameTimeText());
	}

	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		if(!isEnabled)
			return;
		
		
		Point2D gradientStart = new Point2D.Float(0, -game.getHeight() / 2);
		Point2D gradientEnd = new Point2D.Float(0, game.getHeight() / 2);
		float[] gradientDist = {0f, 1f};
		Color[] gradientColorsSurvived = {SURVIVED_BACKGROUND_COLOR_TOP, SURVIVED_BACKGROUND_COLOR_BOTTOM};
		Color[] gradientColorsDead = {DEAD_BACKGROUND_COLOR_TOP, DEAD_BACKGROUND_COLOR_BOTTOM};
		
		
		LinearGradientPaint gradientPaint;
		
		if(gameStateInGame.getPlayers().get(gameStateInGame.getMafiaUsername()).getAliveState() == 1)
			gradientPaint = new LinearGradientPaint(gradientStart, gradientEnd, gradientDist, gradientColorsSurvived);
		else
			gradientPaint = new LinearGradientPaint(gradientStart, gradientEnd, gradientDist, gradientColorsDead);
				
		g.setPaint(gradientPaint);
		g.fillRect(-game.getWidth() / 2, -game.getHeight() / 2, game.getWidth(), game.getHeight());
		g.setPaint(null);
		
		
		
		mafiaStatusElement.render(g, game);
		
		mafiaNameElement.render(g, game);
		doctorNameElement.render(g, game);
		
		roundElement.render(g, game);
		
		newGameTimeElement.render(g, game);
	}

	
	
	
	
	private String grabNewGameTimeText() {
		int coolDownSeconds = newGameCoolDown.grabCurrentCoolDownSeconds();
		
		String text = "New game starting in " + coolDownSeconds + " second(s)...";
		
		
		return text;
	}
	
}
