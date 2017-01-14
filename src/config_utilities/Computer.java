package config_utilities;

public class Computer {
	
	private String name;
	private String ip;
	private String user;
	private String macLan;
	private String macWlan;

	private boolean reachable;
	private boolean reachabilityChecked;
	private boolean thisPC;
	
	
	public Computer(){
		this.reachabilityChecked = false;
	}
	
	public Computer (String name, String macLan, String user){
		this();
		this.name = name;
		this.macLan = macLan;
		this.user = user;
	}
	
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
	public boolean isReachabilityChecked() {
		return reachabilityChecked;
	}
	public void setReachabilityChecked(boolean reachabilityChecked) {
		this.reachabilityChecked = reachabilityChecked;
	}
	public boolean isThisPC() {
		return thisPC;
	}
	public void setThisPC(boolean thisPC) {
		this.thisPC = thisPC;
	}
	
	
	@Override
	public String toString() {
		return "Computer [name=" + name + ", macLan=" + macLan + ", user=" + user + ", reachable=" + reachable + "]";
	}
	
	
	//TODO
	

}
