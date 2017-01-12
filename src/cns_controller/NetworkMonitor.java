package cns_controller;

import java.util.Vector;

import cns_main.CnsConfig;
import config_utilities.Computer;

public class NetworkMonitor {
	
	private CnsConfig config;
	
	public NetworkMonitor(CnsConfig config){
		this.config = config;
	}
	
	public boolean all_computers_reachable(){
		//TODO check if all computer reachable
		
		Vector<Computer> all_computers = config.getAll_computers();
		for (Computer computer : all_computers){
			// TODO check availablity of computer
			computer.setReachable(true);
		}
		System.out.println("all computers reachable, dummy");
		return true;
	}
	

}
