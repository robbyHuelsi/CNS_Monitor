package config_utilities;

import java.util.Vector;

public class Computer {
	
	private String name;
	private String ip;
	private boolean reachable;
	private Vector<Module> modules = new Vector<Module>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public boolean isReachable() {
		return reachable;
	}
	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}
	
	
	//TODO
	

}
