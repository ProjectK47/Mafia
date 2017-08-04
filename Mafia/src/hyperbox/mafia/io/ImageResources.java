package hyperbox.mafia.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageResources {

	
	public static final String IMAGE_RESOURCES_PATH = "hyperbox/mafia/resources/images/";
	
	
	public static BufferedImage button;
	public static BufferedImage buttonHover;
	public static BufferedImage buttonPressed;
	
	public static BufferedImage textBoxLeft;
	public static BufferedImage textBoxRight;
	public static BufferedImage textBoxLeftHover;
	public static BufferedImage textBoxRightHover;
	public static BufferedImage textBoxLeftFocused;
	public static BufferedImage textBoxRightFocused;
	
	public static BufferedImage iconBox;
	public static BufferedImage iconBoxSelected;
	
	public static BufferedImage listBox;
	public static BufferedImage speakerIcon;
	
	public static BufferedImage grassTile;
	public static BufferedImage dirtTile;
	public static BufferedImage waterTileOne;
	public static BufferedImage waterTileTwo;
	
	public static BufferedImage grassBladesStill;
	public static BufferedImage grassBladesLeft;
	public static BufferedImage grassBladesRight;
	
	public static BufferedImage[][] players;
	
	public static BufferedImage notReadyIcon;
	public static BufferedImage readyIcon;
	
	public static BufferedImage sleepIcon;
	
	public static BufferedImage standardMap;
	
	public static BufferedImage projectK47Logo;
	
	
	
	
	public static void loadResources() {
		try {
			button = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "button.png"));
			buttonHover = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "buttonHover.png"));
			buttonPressed = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "buttonPressed.png"));
			
			textBoxLeft = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "textBoxLeft.png"));
			textBoxRight = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "textBoxRight.png"));
			textBoxLeftHover = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "textBoxLeftHover.png"));
			textBoxRightHover = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "textBoxRightHover.png"));
			textBoxLeftFocused = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "textBoxLeftFocused.png"));
			textBoxRightFocused = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "textBoxRightFocused.png"));
			
			iconBox = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "iconBox.png"));
			iconBoxSelected = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "iconBoxSelected.png"));
			
			listBox = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "listBox.png"));
			speakerIcon = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "speakerIcon.png"));
			
			grassTile = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "grassTile.png"));
			dirtTile = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "dirtTile.png"));
			waterTileOne = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "waterTileOne.png"));
			waterTileTwo = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "waterTileTwo.png"));
			
			grassBladesStill = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "grassBladesStill.png"));
			grassBladesLeft = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "grassBladesLeft.png"));
			grassBladesRight = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "grassBladesRight.png"));
			
			
			players = new BufferedImage[3][12];
			
			for(int i = 0; i < players.length; i ++) {
				String playerPathPrefix = "players/player" + (i + 1);
				
				players[i][0] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "FrontStill.png"));
				players[i][1] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "FrontWalk1.png"));
				players[i][2] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "FrontWalk2.png"));
				////
				players[i][3] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "BackStill.png"));
				players[i][4] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "BackWalk1.png"));
				players[i][5] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "BackWalk2.png"));
				////
				players[i][6] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "RightStill.png"));
				players[i][7] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "RightWalk1.png"));
				players[i][8] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "RightWalk2.png"));
				////
				players[i][9] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "LeftStill.png"));
				players[i][10] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "LeftWalk1.png"));
				players[i][11] = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + playerPathPrefix + "LeftWalk2.png"));
			}
			
			
			
			notReadyIcon = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "notReadyIcon.png"));
			readyIcon = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "readyIcon.png"));
			
			sleepIcon = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "sleepIcon.png"));
			
			standardMap = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "standardMap.png"));
			
			projectK47Logo = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "projectK47Logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
}
