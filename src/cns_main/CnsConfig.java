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

	private static CnsConfig instance = null;
	private String file_path = "C:\\Users\\Philipp\\workspace\\CNS_Monitor\\src\\config2.json";
	private File file;

	private Vector<Computer> all_computers = new Vector<Computer>();
	private Vector<Module> all_modules = new Vector<Module>();

	public Vector<Computer> getAll_computers() {
		return all_computers;
	}
	public Vector<Module> getAll_modules() {
		return all_modules;
	}

	private CnsConfig() {}

	public static CnsConfig getInstance() {
		if (instance == null){
			instance = new CnsConfig();

		}
		return instance;
	}

	public boolean load(File file){
		this.file_path = file.getAbsolutePath();
		this.file = file;
		//System.out.println("load wurde gedruekt!");
		JSONParser parser = new JSONParser();
		try{
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;

			// read Computers
			JSONArray computers = (JSONArray) jsonObject.get("Computers");
			Iterator<JSONObject> iterator = computers.iterator();
			while(iterator.hasNext()){
				JSONObject json_computer = iterator.next();
				String name = (String) json_computer.get("Name");
				String mac = (String) json_computer.get("MacLan");
				String user = (String) json_computer.get("User");
				all_computers.addElement(new Computer( name , mac , user));
				//System.out.println(all_computers.lastElement());
			}

			// read Modules
			JSONArray modules = (JSONArray) jsonObject.get("Modules");
			iterator = modules.iterator();
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
				all_modules.addElement(new Module(name, port, computer));
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
}
