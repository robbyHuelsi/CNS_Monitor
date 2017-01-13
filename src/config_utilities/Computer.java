package config_utilities;

import java.util.Vector;

public class Computer {
	
	private String name;
	private String mac;
	private String user;
	private boolean reachable;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public boolean isReachable() {
		return reachable;
	}
	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}
	
	public Computer (String name, String mac, String user){
		this.name = name;
		this.mac = mac;
		this.user = user;
	}
	@Override
	public String toString() {
		return "Computer [name=" + name + ", mac=" + mac + ", user=" + user + ", reachable=" + reachable + "]";
	}
	
	
	//TODO
	

}
