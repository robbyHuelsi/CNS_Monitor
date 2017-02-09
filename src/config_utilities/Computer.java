package config_utilities;

import cns_main.CnsSettings;

public class Computer {
	
	private String name;
	private String ipLan, ipWlan;
	private String user;
	private String macLan,macWlan;
	
	private CnsSettings settings;


	//private boolean reachableLan, reachableWlan;
	private boolean reachabilityChecked;
	private boolean thisPC;
	
	
	public Computer(){
		this.reachabilityChecked = false;
	}
	
	public Computer (String name, String macLan, String macWlan, String user, CnsSettings settings){
		this();
		this.name = name;
		this.macLan = macLan;
		this.macWlan = macWlan;
		this.user = user;
		this.settings = settings;
		ipLan = "";
		ipWlan = "";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIpLan() {
		return ipLan;
	}
	public void setIpLan(String ip) {
		this.ipLan = ip;
	}
	public String getIpWlan() {
		return ipWlan;
	}
	public void setIpWlan(String ip) {
		this.ipWlan = ip;
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
	
	public String getMac(){
		if (!macLan.isEmpty()) {
			return macLan;
		}else{
			return macWlan;
		}
	}
	
	public String getIp(){
		if (!ipLan.isEmpty()) {
			return ipLan;
		}else{
			return ipWlan;
		}
	}
	
	public String getIpInfoText(){
		if (ipLan != null && !ipLan.isEmpty() && ipWlan != null && !ipWlan.isEmpty()){
			return ipLan + " / (" + ipWlan + ")";
		}else if(ipLan != null && !ipLan.isEmpty()){
			return ipLan;
		}else if(ipWlan != null && !ipWlan.isEmpty()){
			return "(" + ipWlan + ")";
		}else{
			return "";
		}
	}
	
	public String getMacInfoText(){
		if (macLan != null && !macLan.isEmpty() && macWlan != null && !macWlan.isEmpty()){
			return macLan + " / (" + macWlan + ")";
		}else if(macLan != null && !macLan.isEmpty()){
			return macLan;
		}else if(macWlan != null && !macWlan.isEmpty()){
			return "(" + macWlan + ")";
		}else{
			return "";
		}
	}
	
	public String isReachableInfoText(){
		if (thisPC) {
			return "This Computer";
		}else if(!reachabilityChecked){
			return "";
		}else if(!ipLan.isEmpty() && !ipWlan.isEmpty()){
			return "LAN & WLAN";
		}else if(!ipLan.isEmpty()){
			return "LAN";
		}else if(!ipWlan.isEmpty()){
			return "WLAN";
		}else{
			return "false";
		}
	}
	
	public Object getParamByName(String name){
		switch (name.toLowerCase()) {
		case "name":
			return getName();
		case "iplan":
			return getIpLan();
		case "ipwlan":
			return getIpWlan();
		case "ip":
			return getIp();
		case "maclan":
			return getMacLan();
		case "macwlan":
			return getMacWlan();
		case "mac":
			return getMac();
		case "user":
			return getUser();
		case "reachabilitychecked":
			return isReachabilityChecked();
		case "pw":
			return settings.getPassword(this);
			
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return "Computer [name=" + name + ", ipLan=" + ipLan + ", ipWlan=" + ipWlan + ", user=" + user + ", macLan="
				+ macLan + ", macWlan=" + macWlan + ", reachabilityChecked=" + reachabilityChecked + ", thisPC="
				+ thisPC + "]";
	}

	

	
	
	
	
	
	
	//TODO
	

}
