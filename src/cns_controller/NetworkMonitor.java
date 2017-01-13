package cns_controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import cns_main.CnsConfig;
import config_utilities.Computer;

public class NetworkMonitor {
	/*
	 * This class can monitor the network
	 * gets the information from the config
	 * updates the config 
	 * 
	 */
	
	private CnsConfig config;
	
	public NetworkMonitor(CnsConfig config){
		this.config = config;
	}
	
	public void getIpAdresses(){
		int timeout = 20;
		String subnet = "192.168.188";
		Vector<InetAddress> ipAdresses = new Vector<InetAddress>();
		for (int i=1;i<255;i++){
			String host = subnet + "." + i;
			try {
				InetAddress ipAdress = InetAddress.getByName(host);
				if (ipAdress.isReachable(timeout)){
					ipAdresses.add(ipAdress);
					System.out.println(host + " is reachable");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("Looking for " + host + "failed");
			}
		}
		
		for (InetAddress ipAdress : ipAdresses) {
			try {
				NetworkInterface network = NetworkInterface.getByInetAddress(ipAdress);
				byte[] mac = network.getHardwareAddress();
				System.out.print("MAC address for " + ipAdress.toString() + ": " + mac.toString());
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public boolean all_computers_reachable(){
		//TODO check if all computer reachable
		getIpAdresses();
		Vector<Computer> all_computers = config.getAll_computers();
		for (Computer computer : all_computers){
			// TODO check availablity of computer
			computer.setReachable(true);
		}
		System.out.println("all computers reachable, dummy");
		return true;
	}
	

}
