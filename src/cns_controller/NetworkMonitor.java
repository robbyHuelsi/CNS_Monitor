package cns_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Vector;
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
	
	private int countCheckedIps;
	
	public NetworkMonitor(CnsConfig config, CnsGui gui){
		this.config = config;
		this.gui = gui;
	}
	
	public NetworkMonitor(CnsConfig config){
		this.config = config;
	}
	
	public void setGui(CnsGui gui){
		this.gui = gui;
	}
	
	public boolean startCompleteNetworkCheck(){
		config.resetAllComputersIps();
		config.setAllComputersReachabilityChecked(false);
		checkInternetConnection();
		
		String ip = getOwnIpAddress();
		System.out.println("This IP: " + ip);
		String mac = getOwnMacAddress();
		
		// Set own IP and write into right Computer for LAN or WLAN
		Computer thisComputer = config.getThisComputer();
		System.out.println("This MAC: " + mac);
		if (thisComputer != null) {
			if (thisComputer.getMacLan().equals(mac)) {
				thisComputer.setIpLan(ip);
				System.out.println("This MAC is for LAN");
				gui.getComputerTable().updateUI();
			}else if (thisComputer.getMacWlan().equals(mac)) {
				thisComputer.setIpWlan(ip);
				gui.getComputerTable().updateUI();
				System.out.println("This MAC is for WLAN");
			}
		}
		
		String subnet = ip.substring(ip.indexOf("/")+1, ip.lastIndexOf("."));
		System.out.println("Subnet: " + subnet);
		checkAllIpAdressesAsynchronously(subnet);
		
		gui.getModuleTable().updateUI();

		return true;
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
		Vector<Integer> checkedIps = new Vector<Integer>();
		
		for (int i=1;i<255;i++){
			Integer lastOctet = i;
			String host = subnet + "." + lastOctet;		
			new Thread(new Runnable() {
			    public void run() {
			    	checkIsReachable(host, timeout + lastOctet); //Damit nach timeout nicht alle gleichzeitig auf checkedIps zugreifen, timeout für jedes Element 1ms länger
			    	checkedIps.add(lastOctet);
			    	System.out.println(checkedIps.size());
			    	if(checkedIps.size() >= 254){
			    		//Alle IP-Addressen wurden abgefragt
						config.setAllComputersReachabilityChecked(true);
						gui.updateView_NetworkCheck(true);
					}
			    }
			}).start();
		}
		
	}
	
	private void checkIsReachable(String host, int timeout) {
		try {
			InetAddress ipAdress = InetAddress.getByName(host);
			if (ipAdress.isReachable(timeout)){
				String mac = getMacAddress(host);
				for (Computer computer : config.getAll_computers()) {
					if (!computer.getMacLan().isEmpty() && mac.equals(computer.getMacLan())) {
						// Die die Mac-Adresse des Hosts stimmt mit der eines Computers ueberein.
						// Diese Computer ist per LAN verbunden.
						// Es kann sein, wenn der PC mit WLAN UND LAN verbunden ist, dass die MAC vom LAN zurueckgegeben wird.
						// In diesem Fall kann nicht eindeutig bestimmt werden, welche Mac zu LAN oder WLAN gehoert. Daher wird die Adresse, wie zuerst reagierte, als LAN und die zweite als WLAN gespeichert.
						// ==> Sollte der PC schneller ueber WLAN als aber LAN reagierte, ist die Reihenfolge falsch.
						if (computer.getIpLan().isEmpty()) {
							computer.setIpLan(host);
							computer.setReachabilityChecked(true);
							System.out.println(host + ": " + mac + " (" + computer.getName() + " via LAN)");
						}else if (computer.getIpWlan().isEmpty()) {
							computer.setIpWlan(host);
							computer.setReachabilityChecked(true);
							System.out.println(host + ": " + mac + " (" + computer.getName() + " via WLAN)");
						}
						gui.updateView_NetworkCheck(false);
						
						return;
					}
					if (!computer.getMacWlan().isEmpty() && mac.equals(computer.getMacWlan())) {
						// Die die Mac-Adresse des Hosts stimmt mit der eines Computers überein.
						// Diese Computer ist per LAN verbunden.
						if (computer.getIpWlan().isEmpty()) {
							computer.setIpWlan(host);
							//computer.setReachableWlan(true);
							System.out.println(host + ": " + mac + " (" + computer.getName() + " via WLAN)");
						}else if (computer.getIpLan().isEmpty()) {
							computer.setIpLan(host);
							//computer.setReachableLan(true);
							System.out.println(host + ": " + mac + " (" + computer.getName() + " via LAN)");
						}
						gui.updateView_NetworkCheck(false);
						return;
					}
				}
				System.out.println(host + ": " + mac + " (unknown)");
			}
		} catch (IOException e) {
			return;
		}
	}
	
	
	public boolean isThatAMacAddressOnOfThisComputer(String macLan, String macWlan){
		try {
			Vector<String> macs = new Vector<String>();
			
			Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
			while(networks.hasMoreElements()) {
				NetworkInterface network = networks.nextElement();
				byte[] mac = network.getHardwareAddress();
				
				if(mac != null) {
					//System.out.print("Current MAC address : ");
					
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
					}
					//System.out.println(sb.toString());
					macs.add(sb.toString());
				}
			}
			
			if(!macs.isEmpty()){
				if (macLan != null && !macLan.isEmpty() && macs.contains(macLan)) {
					return true;
				}else if(macWlan != null && !macWlan.isEmpty() && macs.contains(macWlan)){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		} catch (SocketException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public String getMacAddress(String ip) {
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

	public String getOwnIpAddress(){
		// Find OS and set command according to OS
	    String OS = System.getProperty("os.name").toLowerCase();
		if (OS.contains("win") || OS.contains("mac")) {
	        // Windows and Mac
			try {
				String ip = InetAddress.getLocalHost().toString();
				return ip.substring(ip.indexOf("/")+1);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
		else{
			try {
				Socket s = new Socket("google.com", 80);
				String ip = s.getLocalAddress().getHostAddress().toString();
				s.close();
				return ip.substring(ip.indexOf("/")+1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
	}
	
	public String getOwnMacAddress(){
		try {
			NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] mac = network.getHardwareAddress();
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			
			String[] macAry = sb.toString().replaceAll("-", ":").split("\\:",-1);
        	String macString = "";
        	for (String seg : macAry) {
				if (seg.length() == 1) {
					seg = "0" + seg;
				}
				macString += seg.toUpperCase() + ":";
			}
            return macString.substring(0, macString.length()-1);

		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "";
		} catch (SocketException e){
			e.printStackTrace();
			return "";
		}
	}
	
}

