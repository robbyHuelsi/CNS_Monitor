package cns_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			System.out.println(getMac(ipAdress.toString().substring(1)));
			
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
	
	
	public String getMACAddress(String ip){ 
        String str = ""; 
        String macAddress = ""; 
        try { 
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip); 
            InputStreamReader ir = new InputStreamReader(p.getInputStream()); 
            LineNumberReader input = new LineNumberReader(ir); 
            for (int i = 1; i <100; i++) { 
                str = input.readLine(); 
                if (str != null) { 
                    if (str.indexOf("MAC Address") > 1) { 
                        macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length()); 
                        break; 
                    } 
                } 
            } 
        } catch (IOException e) { 
            e.printStackTrace(System.out); 
        } 
        return macAddress; 
    }
	
	private String getMac(String ip) {
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
	                System.out.println("Found");
	                System.out.println("MAC: " + m.group(0));
	                return m.group(0);
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
