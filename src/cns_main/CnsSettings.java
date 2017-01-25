package cns_main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Vector;

import config_utilities.Computer;

public class CnsSettings {

	private Vector<String> recentOpenConfig = new Vector<String>();
	private Vector<CnsPassword> passwords = new Vector<CnsPassword>();
	
	public CnsSettings(){
		if (load()) {
			System.out.println("Settings loaded");
		}
	}
	
	public Vector<String> getRecentOpenConfig(){
		return recentOpenConfig;
	}
	
	public boolean addRecentOpenConfig(String newPath){
		recentOpenConfig.add(0, newPath);
		if (recentOpenConfig.size() > 1) {
			for (int i = 1; i < recentOpenConfig.size(); i++) {
				if (recentOpenConfig.get(i).equals(newPath)) {
					recentOpenConfig.remove(i);
					i--;
				}
			}
		}
		return save();
	}
	
	public boolean removeAllRecentOpenConfig(){
		recentOpenConfig.removeAllElements();
		return save();
	}
	
	public Vector<CnsPassword> getPasswords(){
		return passwords;
	}
	
	public String getPassword(Computer computer){
		for (CnsPassword password : passwords) {
			if (password.getMac().equals(computer.getMac()) && password.getUser().equals(computer.getUser())) {
				return password.getPassword();
			}
		}
		return null;
	}
	
	public boolean addPassword(Computer computer, String pass){
		if (computer.getMacLan() != null && !computer.getMacLan().isEmpty()) {
			boolean found = false;
			for (CnsPassword password : passwords) {
				if (password.getMac().equals(computer.getMacLan()) && password.getUser().equals(computer.getUser())) {
					found = true;
					password.setPassword(pass);
				}
			}
			if (!found) {
				passwords.add(new CnsPassword(computer.getMacLan(), computer.getUser(), pass));
			}
		}
		
		if (computer.getMacWlan() != null && !computer.getMacWlan().isEmpty()) {
			boolean found = false;
			for (CnsPassword password : passwords) {
				if (password.getMac().equals(computer.getMacWlan()) && password.getUser().equals(computer.getUser())) {
					found = true;
					password.setPassword(pass);
				}
			}
			if (!found) {
				passwords.add(new CnsPassword(computer.getMacWlan(), computer.getUser(), pass));
			}
		}
		return save();
	}
	
	public boolean removeAllPasswords(){
		passwords.removeAllElements();
		return save();
	}
	
	public boolean load(){
		String path = getPath();
		File f = new File(path);
		if(f==null || !f.exists() || f.isDirectory()) { 
		    System.out.println("No Settings-File found");
		    return false;
		}
		
		ObjectInputStream objectinputstream = null;
		try {
			FileInputStream streamIn = new FileInputStream(path);
		    objectinputstream = new ObjectInputStream(streamIn);

		    Object obj= null;
		      // lese ein objekt nach dem anderen aus dem inputstream. das letzte 
		      // object, welches gelesen wird, ist null. dieses muss allerdings explizit
		      // geschrieben worden sein; andernfalls wird eine EOFException geworfen.
		      if ( (obj= objectinputstream.readObject()) != null ){
		    	  recentOpenConfig = ((Vector<String>) obj);
		      }
		      if ( (obj= objectinputstream.readObject()) != null ){
		    	  passwords = ((Vector<CnsPassword>) obj);
			  }
		    System.out.println("Opening done");
		    return true;
		} catch (Exception e) {
			System.err.println("Opening failed");
		    e.printStackTrace();
		    return false;
		} finally {
		    if(objectinputstream != null){
		        try {
					objectinputstream .close();
				} catch (IOException e) {
					
				}
		    } 
		}
	}
	
	public boolean save(){
		try{
			File f = new File(getPath());
			
			FileOutputStream fout = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
		
			oos.writeObject(recentOpenConfig);
			System.out.println(passwords);
			oos.writeObject(passwords);
			oos.writeObject( null );
			oos.flush();
			oos.close();
			System.out.println("Saving done");
			return true;
		}catch(Exception ex){
			System.err.println("Saving failed");
			ex.printStackTrace();
			return false;
		}
	}
	
	private String getPath(){
		String path;
		//String OS = System.getProperty("os.name").toLowerCase();
		//System.out.println(OS);
		/*if (OS.contains("win")) {
			return "c:\\cnsMonitor\\settings.cns";
		}else if(OS.equals("mac os x")){
			return System.getProperty ("user.home") + "/cnsMonitor/settings.cns";
		}else{
			return "/home/cnsMonitor/settings.cns";
		}*/
		path = System.getProperty ("user.home") + "/settings.cns";
		//path = "settings.cns";
		return path;
	}
	
}
