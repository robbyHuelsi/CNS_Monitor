package config_utilities;

import java.util.Vector;

public class Computer {
	
	private String name;
<<<<<<< HEAD
	private String mac;
	private String user;
=======
	private String macLan;
	private String macWlan;
	private String ip;
>>>>>>> network
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
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getMacLan() {
		return macLan;
	}
	public void setMacLan(String macLan) {
		this.macLan = macLan;
	}
	public String getMacWlan() {
		return macWlan;
	}
	public void setMacWlan(String macWlan) {
		this.macWlan = macWlan;
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
