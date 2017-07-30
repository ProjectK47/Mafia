package hyperbox.mafia.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import java.io.File;

import hyperbox.mafia.core.Game;
import hyperbox.mafia.io.ImageResources;
import hyperbox.mafia.io.Settings;
import hyperbox.mafia.ui.ButtonElement;
import hyperbox.mafia.ui.ImageElement;
import hyperbox.mafia.ui.SplashElement;
import hyperbox.mafia.ui.SplashImage;
import hyperbox.mafia.ui.TextBoxElement;
import hyperbox.mafia.ui.TextElement;
import hyperbox.mafia.ui.UIAnchor;

public class GameStateMenu extends GameState {

	
	public static final Color BACKGROUND_COLOR_TOP = new Color(204, 255, 153, 150);
	public static final Color BACKGROUND_COLOR_BOTTOM = new Color(255, 153, 51, 230);
	
	public static final String COPYRIGHT_TEXT = "Copyright © 2017 GeneralBrody8 under the GNU GPL v3.0";
	
	public static final String SETTINGS_FILE_PATH = "mafia-settings.xml";
	public static final String SETTINGS_COMMENT = "This is the settings file for Mafia. It is not meant to be read/edited by humans, "
			+ "so I recommend closing it. Thank you :D";
	
	
	
	private Settings settings;
	
	
	private TextElement titleElement;
	
	private TextBoxElement connectIpElement;
	private TextBoxElement connectPortElement;
	private ButtonElement connectButtonElement;
	
	private TextBoxElement hostPortElement;
	private ButtonElement hostButtonElement;
	
	private TextElement copyrightElement;
	private ImageElement k47LogoElement;
	
	private TextBoxElement usernameElement;
	
	
	private SplashElement splashElement;
	
	
	
	public GameStateMenu(Game game) {
		settings = new Settings(new File(SETTINGS_FILE_PATH), SETTINGS_COMMENT);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			writeSettings();
		}));
		
		
		
		titleElement = new TextElement(0, 10, UIAnchor.CENTER, UIAnchor.NEGATIVE, UIAnchor.CENTER, UIAnchor.NEGATIVE, "Mafia!", 125, new Color(185, 155, 100));
		
		
		
		connectIpElement = new TextBoxElement(-100, 240, UIAnchor.CENTER, UIAnchor.NEGATIVE, UIAnchor.POSITIVE, UIAnchor.CENTER,
				30, settings.grabValue("connectIp", "localhost"), "IP Address", UIAnchor.POSITIVE, 30, TextBoxElement.ALL_CHARS);
		
		connectPortElement = new TextBoxElement(100, 240, UIAnchor.CENTER, UIAnchor.NEGATIVE, UIAnchor.NEGATIVE, UIAnchor.CENTER,
				30, settings.grabValue("connectPort", "21147"), "Port", UIAnchor.NEGATIVE, 5, TextBoxElement.NUMBER_CHARS);
		
		connectButtonElement = new ButtonElement(0, 240, UIAnchor.CENTER, UIAnchor.NEGATIVE, UIAnchor.CENTER, UIAnchor.NEGATIVE, 3.5f, "Connect", () -> {
			writeSettings();
			
			if(!connectIpElement.isEmpty() && !connectPortElement.isEmpty()) {
				this.disable(game);
				game.getGameStateManager().getGameStateInGame().enable(game);
			}
		});
		
		
		
		hostPortElement = new TextBoxElement(-100, -100, UIAnchor.CENTER, UIAnchor.POSITIVE, UIAnchor.CENTER, UIAnchor.CENTER,
				30, settings.grabValue("hostPort", "21147"), "Port", UIAnchor.CENTER, 5, TextBoxElement.NUMBER_CHARS);
		
		hostButtonElement = new ButtonElement(100, -100, UIAnchor.CENTER, UIAnchor.POSITIVE, UIAnchor.CENTER, UIAnchor.CENTER, 3.5f, "Host", () -> {
			writeSettings();
			
			if(!hostPortElement.isEmpty()) {
				Game.startGameServer(Integer.parseInt(hostPortElement.getText()), false);
				hostButtonElement.setIsDisabled(true);
			}
		});
		
		
		copyrightElement = new TextElement(10, -10, UIAnchor.NEGATIVE, UIAnchor.POSITIVE, UIAnchor.NEGATIVE, UIAnchor.POSITIVE, COPYRIGHT_TEXT, 15f, Color.WHITE);
		
		k47LogoElement = new ImageElement(-15, -15, UIAnchor.POSITIVE, UIAnchor.POSITIVE, UIAnchor.POSITIVE, UIAnchor.POSITIVE, ImageResources.projectK47Logo, 0.05f);
		
		
		usernameElement = new TextBoxElement(30, 50, UIAnchor.NEGATIVE, UIAnchor.CENTER, UIAnchor.NEGATIVE, UIAnchor.CENTER,
				25, settings.grabValue("username", "Player"), "Username", UIAnchor.NEGATIVE, 15, TextBoxElement.ALL_CHARS);
		
		
		
		splashElement = new SplashElement(new SplashImage(ImageResources.projectK47Logo, 0.25f, Color.BLACK));
	}
	
	
	
	
	@Override
	protected void onEnable(Game game) {
		
	}

	
	@Override
	protected void onDisable(Game game) {
		
	}

	
	
	@Override
	protected void onTick(Game game) {
		if(!isEnabled)
			return;
		
		
		titleElement.tick(game);
		
		connectIpElement.tick(game);
		connectPortElement.tick(game);
		connectButtonElement.tick(game);
		
		hostPortElement.tick(game);
		hostButtonElement.tick(game);
		
		usernameElement.tick(game);
		
		
		splashElement.tick(game);
	}

	
	
	@Override
	protected void onRender(Graphics2D g, Game game) {
		if(!isEnabled)
			return;
		
		
		Point2D gradientStart = new Point2D.Float(0, -game.getHeight() / 2);
		Point2D gradientEnd = new Point2D.Float(0, game.getHeight() / 2);
		float[] gradientDist = {0f, 1f};
		Color[] gradientColors = {BACKGROUND_COLOR_TOP, BACKGROUND_COLOR_BOTTOM};
		
		LinearGradientPaint gradientPaint = new LinearGradientPaint(gradientStart, gradientEnd, gradientDist, gradientColors);
		
		g.setPaint(gradientPaint);
		g.fillRect(-game.getWidth() / 2, -game.getHeight() / 2, game.getWidth(), game.getHeight());
		g.setPaint(null);
		
		
		
		titleElement.render(g, game);
		
		connectIpElement.render(g, game);
		connectPortElement.render(g, game);
		connectButtonElement.render(g, game);
		
		hostPortElement.render(g, game);
		hostButtonElement.render(g, game);
		
		copyrightElement.render(g, game);
		k47LogoElement.render(g, game);
		
		usernameElement.render(g, game);
		
		
		splashElement.render(g, game);
	}

	
	
	
	
	
	private void writeSettings() {
		settings.setValue("connectIp", connectIpElement.getText());
		settings.setValue("connectPort", connectPortElement.getText());
		
		settings.setValue("hostPort", hostPortElement.getText());
		
		settings.setValue("username", usernameElement.getText());
		
		
		settings.writeToFile();
	}
	
	
	
	
	
	public Settings getSettings() {
		return settings;
	}
	
}
