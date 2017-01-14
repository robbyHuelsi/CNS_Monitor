package cns_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cns_main.CnsConfig;
import cns_main.CnsGui;
import config_utilities.Computer;

public class NetworkMonitor {
	/*
	 * This class can monitor the network
	 * gets the information from the config
	 * updates the config 
	 * 
	 */
	
	private CnsConfig config;
	private CnsGui gui;
	
	
	
	public NetworkMonitor(CnsConfig config, CnsGui gui){
		this.config = config;
		this.gui = gui;
	}
	
	public NetworkMonitor(CnsConfig config){
		this.config = config;
	}
	
	public void addGui(CnsGui gui){
		this.gui = gui;
	}
	
	public boolean startCompleteNetworkCheck(){
		
		checkInternetConnection();
		
		String ip = getOwnIpAddress();
		String subnet = ip.substring(ip.indexOf("/")+1, ip.lastIndexOf("."));
		System.out.println("Subnet: " + subnet);
		checkAllIpAdressesAsynchronously(subnet);

		return true;
	}
	
	public String getOwnIpAddress(){
		try {
			return InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public String getOwnMacAddress(){
		return getMac(getOwnIpAddress());
	}
	
	public boolean checkInternetConnection(){
		int timeout = 2000;
		String[] hosts = {"https://www.google.de","https://www.wikipedia.org","https://www.github.com"};
		InetAddress address;
		for (String host : hosts) {
			try {
				address = InetAddress.getByName(host);
				if (address.isReachable(timeout)) {
					System.out.println("Internt connected");
					return true;
				}else{
					System.out.println(host + " not available");
				}
			} catch (IOException e) {}
		}
		System.out.println("No Internet connection");
		return false;
	}
	
	private void checkAllIpAdressesAsynchronously(String subnet){
		int timeout = 2000;
		
		for (int i=1;i<255;i++){
			String host = subnet + "." + i;			
			new Thread(new Runnable() {
			    public void run() {
			    	checkIsReachable(host, timeout);
			    }
			}).start();
		}
		
	}
	
	private void checkIsReachable(String host, int timeout) {
		try {
			InetAddress ipAdress = InetAddress.getByName(host);
			if (ipAdress.isReachable(timeout)){
				System.out.println(host + ": " + getMac(host));
				String mac = getMac(host);
				for (Computer computer : config.getAll_computers()) {
					if (mac.equals(computer.getMacLan())) {
						computer.setIp(host);
						computer.setReachable(true);
						System.out.println("reachable");
						gui.getComputerTable().updateUI();
						return;
					}
				}
			}
		} catch (IOException e) {
			return;
		}
	}
	
	public String getMac(String ip) {
		Pattern macpt = null;
		
	    // Find OS and set command according to OS
	    String OS = System.getProperty("os.name").toLowerCase();

	    String[] cmd;
	    if (OS.contains("win")) {
	        // Windows
	        macpt = Pattern
	                .compile("[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+");
	        String[] a = { "arp", "-a", ip };
	        cmd = a;
	    } else {
	        // Mac OS X, Linux
	        macpt = Pattern
	                .compile("[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+");
	        String[] a = { "arp", ip };
	        cmd = a;
	    }

	    try {
	        // Run command
	        Process p = Runtime.getRuntime().exec(cmd);
	        p.waitFor();
	        // read output with BufferedReader
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                p.getInputStream()));
	        String line = reader.readLine();

	        // Loop trough lines
	        while (line != null) {
	            Matcher m = macpt.matcher(line);

	            // when Matcher finds a Line then return it as result
	            if (m.find()) {
	                //System.out.println("MAC found");
	            	String[] macAry = m.group(0).replaceAll("-", ":").split("\\:",-1);
	            	String mac = "";
	            	for (String seg : macAry) {
						if (seg.length() == 1) {
							seg = "0" + seg;
						}
						mac += seg.toUpperCase() + ":";
					}
	                return mac.substring(0, mac.length()-1);
	            }

	            line = reader.readLine();
	        }

	    } catch (IOException e1) {
	        e1.printStackTrace();
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }

	    // Return empty string if no MAC is found
	    return "";
	}

}
