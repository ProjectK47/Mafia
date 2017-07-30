package hyperbox.mafia.window;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import hyperbox.mafia.core.Game;

public class Window {

	
	
	private JFrame frame;
	
	private Game game;
	
	
	
	public Window(int width, int height, String title, boolean resizable) {
		frame = new JFrame();
		game = new Game();
		
		
		frame.setMinimumSize(new Dimension(width, height));
		frame.setSize(width, height);
		
		frame.setTitle(title);
		frame.setResizable(resizable);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		frame.add(game);
		
		frame.setLocationRelativeTo(null);
	}
	
	
	
	
	
	public void showWindow() {
		frame.setVisible(true);
		game.requestFocus();
		
		game.init();
	}
	
	
	
	public void setWindowIcon(BufferedImage icon) {
		frame.setIconImage(icon);
	}
	
}
