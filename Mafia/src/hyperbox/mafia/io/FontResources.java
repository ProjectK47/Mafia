package hyperbox.mafia.io;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class FontResources {

	
	
	
	public static Font mainFont;
	public static Font mainFontBold;
	
	
	
	
	
	public static void loadResources() {
		try(InputStream mainFontIn = FontResources.class.getClassLoader().getResourceAsStream("hyperbox/mafia/resources/fonts/ubuntu.ttf");
				InputStream mainFontBoldIn = FontResources.class.getClassLoader().getResourceAsStream("hyperbox/mafia/resources/fonts/ubuntuBold.ttf")) {
			
			mainFont = Font.createFont(Font.TRUETYPE_FONT, mainFontIn);
			mainFontBold = Font.createFont(Font.TRUETYPE_FONT, mainFontBoldIn);
			
			
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			
			ge.registerFont(mainFont);
			ge.registerFont(mainFontBold);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	
}
