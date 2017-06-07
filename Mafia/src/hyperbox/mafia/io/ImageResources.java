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
			
			standardMap = ImageIO.read(ImageResources.class.getClassLoader().getResourceAsStream(IMAGE_RESOURCES_PATH + "standardMap.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
}
