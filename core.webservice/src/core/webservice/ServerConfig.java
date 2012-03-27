package core.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

public class ServerConfig {
	
	private String bpFile="";
	
	private Properties settings = new Properties();
	
	public boolean load(){
		Location configDir = Platform.getConfigurationLocation();
		System.out.println(configDir.getURL());

		bpFile = configDir.getURL().getPath().substring(1) + "server.config";
		
		File f = new File(bpFile);
		if (!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		try {
			settings.clear();
			
			FileInputStream fis = new FileInputStream(f);
			
			settings.load(fis);
			
			fis.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	public boolean save(){
		Location configDir = Platform.getConfigurationLocation();
		System.out.println(configDir.getURL());

		bpFile = configDir.getURL().getPath().substring(1) + "server.config";
		
		File f = new File(bpFile);
		if (!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			settings.store(fos, "");
			fos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void setProperty(String key,String value){
		settings.setProperty(key, value);
	}
	
	public String getProperty(String key){
		return settings.getProperty(key);
	}
	
}
