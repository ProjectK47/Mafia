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
	
	public static BufferedImage grassTile;
	public static BufferedImage dirtTile;
	public static BufferedImage waterTileOne;
	public static BufferedImage waterTileTwo;
	
	public static BufferedImage grassBladesStill;
	public static BufferedImage grassBladesLeft;
	public static BufferedImage grassBladesRight;
	
	public static BufferedImage player1FrontStill;
	public static BufferedImage player1FrontWalk1;
	public static BufferedImage player1FrontWalk2;
	////
	public static BufferedImage player1BackStill;
	public static BufferedImage player1BackWalk1;
	public static BufferedImage player1BackWalk2;
	////
	public static BufferedImage player1RightStill;
	public static BufferedImage player1RightWalk1;
	public static BufferedImage player1RightWalk2;
	////
	public static BufferedImage player1LeftStill;
	public static BufferedImage player1LeftWalk1;
	public static BufferedImage player1LeftWalk2;
	
	public static BufferedImage standardMap;
	
	
	
	
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
			
			grassTile = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "grassTile.png"));
			dirtTile = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "dirtTile.png"));
			waterTileOne = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "waterTileOne.png"));
			waterTileTwo = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "waterTileTwo.png"));
			
			grassBladesStill = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "grassBladesStill.png"));
			grassBladesLeft = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "grassBladesLeft.png"));
			grassBladesRight = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "grassBladesRight.png"));
			
			player1FrontStill = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1FrontStill.png"));
			player1FrontWalk1 = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1FrontWalk1.png"));
			player1FrontWalk2 = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1FrontWalk2.png"));
			////
			player1BackStill = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1BackStill.png"));
			player1BackWalk1 = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1BackWalk1.png"));
			player1BackWalk2 = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1BackWalk2.png"));
			////
			player1RightStill = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1RightStill.png"));
			player1RightWalk1 = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1RightWalk1.png"));
			player1RightWalk2 = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1RightWalk2.png"));
			////
			player1LeftStill = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1LeftStill.png"));
			player1LeftWalk1 = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1LeftWalk1.png"));
			player1LeftWalk2 = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "player1LeftWalk2.png"));
			
			standardMap = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "standardMap.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
}
