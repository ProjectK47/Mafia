package hyperbox.mafia.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

	
	private File file;
	private String comment;
	
	private Properties properties;
	
	
	
	public Settings(File file, String comment) {
		this.file = file;
		this.comment = comment;
		
		this.properties = new Properties();
		
		
		if(file.exists() && !file.isDirectory())
			loadFromFile();
	}
	
	
	
	
	
	
	public String grabValue(String key, String defaultValue) {
		String value = properties.getProperty(key, defaultValue);
		
		return value;
	}
	
	
	public int grabValue(String key, int defaultValue) {
		int value = Integer.parseInt(properties.getProperty(key, Integer.toString(defaultValue)));
		
		return value;
	}
	
	
	
	
	public void setValue(String key, String value) {
		properties.setProperty(key, value);
	}
	
	
	public void setValue(String key, int value) {
		properties.setProperty(key, Integer.toString(value));
	}
	
	
	
	
	
	
	public void writeToFile() {
		try(FileOutputStream out = new FileOutputStream(file)) {
			
			properties.storeToXML(out, comment);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	
	
	private void loadFromFile() {
		try(FileInputStream in = new FileInputStream(file)) {
			
			properties.loadFromXML(in);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	
	
	
	
	
	public String getComment() {
		return comment;
	}
	
	
}
