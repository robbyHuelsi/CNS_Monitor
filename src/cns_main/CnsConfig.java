package cns_main;

import config_utilities.*;
import java.util.Vector;


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
	
	private Vector<Computer> all_computers = new Vector<Computer>();
	
	public Vector<Computer> getAll_computers() {
		return all_computers;
	}

	private CnsConfig() {}
	
	public static CnsConfig getInstance() {
		if (instance == null){
			instance = new CnsConfig();
			
		}
		return instance;
	}
	
	public boolean load(){
		//TODO Load json config file 
		System.out.println("load wurde gedruekt!");
		return true;
	}
	
	public boolean write(){
		
		return false;
	}
}
