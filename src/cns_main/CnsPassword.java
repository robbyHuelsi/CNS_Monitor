package cns_main;

import java.io.Serializable;

public class CnsPassword implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String mac;
	private String user;
	private String password;
	
	
	public CnsPassword(String mac, String user, String password) {
		this.mac = mac;
		this.user = user;
		this.password = password;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "CnsPassword [mac=" + mac + ", user=" + user + ", password=" + password + "]";
	}
	
	
}
