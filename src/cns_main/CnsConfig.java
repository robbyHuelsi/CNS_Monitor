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

	public static CnsConfig getInstance() {
		if (instance == null){
			instance = new CnsConfig();

		}
		return instance;
	}
	
	public void setGui(CnsGui gui){
		this.gui = gui;
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
				System.out.println("command of "+name+": "+command);
				for (Computer computer_temp : all_computers){
					if (computer_temp.getName().equals(computer_string))
						computer = computer_temp;
				}
				all_modules.addElement(new Module(name, port, computer));
				if (!command.isEmpty())
					all_modules.lastElement().setCommand(command);
				//System.out.println(all_modules.lastElement());
			}

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
}
