package config_utilities;

public class Computer {
	
	private String name;
	private String ipLan, ipWlan;
	private String user;
	private String macLan,macWlan;

	private boolean reachableLan, reachableWlan;
	private boolean reachabilityChecked;
	private boolean thisPC;
	
	
	public Computer(){
		this.reachabilityChecked = false;
	}
	
	public Computer (String name, String macLan, String macWlan, String user){
		this();
		this.name = name;
		this.macLan = macLan;
		this.macWlan = macWlan;
		this.user = user;
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
	
	/*public String getMac(){
		if (macLan != null && !macLan.isEmpty()) {
			return macLan;
		}else{
			return macWlan;
		}
	}*/
	
	public boolean isReachableLan() {
		return reachableLan;
	}
	public void setReachableLan(boolean reachable) {
		this.reachableLan = reachable;
	}
	public boolean isReachableWlan() {
		return reachableWlan;
	}
	public void setReachableWlan(boolean reachable) {
		this.reachableWlan = reachable;
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
	
	public String isReachableInfoText(){
		if (thisPC) {
			return "This Computer";
		}else if(!reachabilityChecked){
			return "";
		}else if(reachableLan && reachableWlan){
			return "LAN & WLAN";
		}else if(reachableLan){
			return "LAN";
		}else if(reachableWlan){
			return "WLAN";
		}else{
			return "false";
		}
	}

	@Override
	public String toString() {
		return "Computer [name=" + name + ", ipLan=" + ipLan + ", ipWlan=" + ipWlan + ", user=" + user + ", macLan="
				+ macLan + ", macWlan=" + macWlan + ", reachableLan=" + reachableLan + ", reachableWlan="
				+ reachableWlan + ", reachabilityChecked=" + reachabilityChecked + ", thisPC=" + thisPC + "]";
	}

	
	
	
	
	
	
	//TODO
	

}
