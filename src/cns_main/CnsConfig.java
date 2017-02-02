package cns_main;

import config_utilities.*;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Vector;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import java.io.FileInputStream;


public class CnsConfig {
	/*
	 * Model with all the data
	 * reads data from json config file
	 * implemented as singleton
	 * Should inlude all the data from the config file
	 * Guess two methods (read/write config) are all we need here
	 * 
	 */

	private CnsGui gui;
	private static CnsConfig instance = null;
	private File file;

	private Vector<Computer> all_computers = new Vector<Computer>();
	private Vector<Module> all_modules = new Vector<Module>();
	
	private CnsConfig() {}

	public Vector<Computer> getAll_computers() {
		return all_computers;
	}
	
	public Vector<Module> getAll_modules() {
		return all_modules;
	}
	
	public Module getModule(int module_num){
		return all_modules.get(module_num);
	}
	
	public Module getModule(String moduleName){
		for (Module module : all_modules) {
			if (module.getName().toLowerCase().equals(moduleName.toLowerCase())) {
				return module;
			}
		}
		return null;
	} 

	public static CnsConfig getInstance() {
		if (instance == null){
			instance = new CnsConfig();

		}
		return instance;
	}
	
	public void setGui(CnsGui gui){
		this.gui = gui;
	}
	
	public File getFile(){
		return file;
	}

	public boolean load(String path){
		File file = new File(path);
		return load(file);
	}
	
	public boolean load(File file){
		this.file = file;
		//System.out.println("load wurde gedruekt!");
		JSONParser parser = new JSONParser();
		try{
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;

			all_computers.removeAllElements();
			all_modules.removeAllElements();
			
			// read Computers
			JSONArray computers = (JSONArray) jsonObject.get("Computers");
			Iterator<JSONObject> iterator = computers.iterator();
			while(iterator.hasNext()){
				JSONObject json_computer = iterator.next();
				String name = (String) json_computer.get("Name");
				String macLan = (String) json_computer.get("MacLan").toString().toUpperCase().replaceAll("-", ":");
				String macWlan = (String) json_computer.get("MacWlan").toString().toUpperCase().replaceAll("-", ":");
				String user = (String) json_computer.get("User");
				Computer computer = new Computer(name, macLan, macWlan, user);
				if (gui.getNetworkMonitor().isThatAMacAddressOnOfThisComputer(macLan, macWlan)) {
					computer.setThisPC(true);
				}
				all_computers.addElement(computer);
				
				//System.out.println(all_computers.lastElement());
			}

			// read Modules
			JSONArray modules = (JSONArray) jsonObject.get("Modules");
			iterator = modules.iterator();
			
			all_modules.removeAllElements();
			
			while(iterator.hasNext()){
				JSONObject json_module = iterator.next();
				String name = (String) json_module.get("Name");
				String computer_string = (String) json_module.get("Computer");
				Computer computer = null;
				int port = Integer.parseInt( (String) json_module.get("Port"));
				String command = (String) json_module.get("Command");
				for (Computer computer_temp : all_computers){
					if (computer_temp.getName().equals(computer_string))
						computer = computer_temp;
				}
				all_modules.addElement(new Module(this, name, port, computer));
				if (!command.isEmpty())
					all_modules.lastElement().setCommand(command);
				//System.out.println(all_modules.lastElement());
			}

			
			gui.setTotalTitle(file.getName());
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR occured while Parsing config file!");
			return false;
		}
		//try(FileInputStream inputStream = new FileInputStream("foo.txt")) {     
		//    String everything = IOUtils.toString(inputStream);
		// do something with everything string
		//}
		//return true;
	}

	public boolean write(){

		return false;
	}
	
	public Computer getThisComputer(){
		if (!all_computers.isEmpty()) {
			for (Computer computer : all_computers) {
				if (computer.isThisPC()) {
					return computer;
				}
			}
		}
		return null;
	}
	
	public void resetAllComputersIps(){
		for (Computer computer : all_computers) {
			computer.setIpLan("");
			computer.setIpWlan("");
		}
	}
	
	public void setAllComputersReachabilityChecked(boolean checked){
		for (Computer computer : all_computers) {
			computer.setReachabilityChecked(checked);
		}
	}
	
	public String parseCommand(String command){
		try {
			while (command.contains("#(")) {
				System.out.println(command);
				String ref = command.substring(command.indexOf("#("));
				ref = ref.substring(0, ref.indexOf(")") + 1);
				String source = parseRecursive(ref.substring(2, ref.length()-1), this);
				do {
					command = command.replace(ref, source);
				} while (command.contains(ref));
			}
			//System.out.println("Commando parsing done: " + command);
		} catch (Exception e) {
			//System.out.println("Commando parsing failed: " + command);
		}
		return command;
	}
	
	private String parseRecursive(String ref, Object parent){
		String[] refSubstrs = ref.split("\\.");
		Object obj = null;
			 
		// Objekt finden
		if (parent.getClass().equals(this.getClass())) {
			if (refSubstrs[0].toLowerCase().equals("c")) {
				obj = all_computers;
			} else if(refSubstrs[0].toLowerCase().equals("m")) {
				obj = all_modules;
			}
		}else if(parent instanceof Vector){
			// Wenn Vector, dann gebe gesuchtes Objekt des Vectors zurück
			Vector v = (Vector) parent;
			try {
				//if numbers
				obj = v.get(Integer.parseInt(refSubstrs[0]));
			} catch (Exception e) {
				//if not a number
				//try to find string in list
				for (Object o : v) {
					if ((o instanceof Computer && ((Computer) o).getName().toLowerCase().equals(refSubstrs[0].toLowerCase()))
					 || (o instanceof Module && ((Module) o).getName().toLowerCase().equals(refSubstrs[0].toLowerCase()))) {
						obj = o;
						break;
					}
				}
			}
		}else if(parent instanceof Computer){
			obj = ((Computer) parent).getParamByName(refSubstrs[0]);
		}else if(parent instanceof Module){
			obj = ((Module) parent).getParamByName(refSubstrs[0]);
		}
		
		
		if (refSubstrs.length == 1) {
			// Rücksprungbedingung
			// Wenn der hinterste Teil der Referenz erreicht ist, gebe Wert zurück
			// Davor Objekt in String umwandeln
			if (obj == null) {
				return "null";
			}else if (obj instanceof String) {
				return (String)obj;
			}else if(obj instanceof Integer){
				return Integer.toString((Integer)obj);
			}else if(obj instanceof Boolean){
				return ((Boolean)obj?"true":"false");
			}else{
				return obj.toString();
			}
		} else {
			//Gehe zum nächsten Part der Referenz und nehme obj mit.
			return parseRecursive(ref.substring(ref.indexOf('.') + 1), obj);
		}
	}
}
