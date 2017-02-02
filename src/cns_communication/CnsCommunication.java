package cns_communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

import cns_main.CnsConfig;
import config_utilities.Module;

public class CnsCommunication {
	private CnsConfig config;
	private TCPClient brain;
	
	public CnsCommunication(CnsConfig config){
		this.config = config;
	}
	
	private boolean setupBrain(){
		if (brain != null) {
			System.out.println("Brain already set up");
			return false;
		}
		
		Module brainModule;
		try {
			brainModule = config.getModule("Brain");
		} catch (Exception e) {
			System.out.println("Problem with config");
			return false;
		}
		
		if (brainModule == null) {
			System.out.println("Brain not found");
			return false;
		}
		
		if (brainModule.getComputer().getIp() == null || brainModule.getComputer().getIp().isEmpty()) {
			System.out.println("Brain's IP unknown");
			return false;
		}
		
		if (brainModule.getListeningPort() == 0) {
			System.out.println("Brain's Port unknown");
			return false;
		}
		
		try {
			brain = new TCPClient(InetAddress.getByName(brainModule.getComputer().getIp()), brainModule.getListeningPort(), true);
		} catch (UnknownHostException e) {
			System.out.println("Connection problem with Brain");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean endBrain(){
		if (brain == null) {return true;}
		if (brain.endConnection()) {
			brain = null;
			return true;
		}else{
			System.out.println("Nobody can stop Brain. Hehehe.");
			return false;
		}
	}
	
	public boolean sendToBrain(String message){
		if (brain == null) {
			if (!setupBrain()) {
				System.out.println("Setup communitation with Brain failed. -> Unable to send message.");
				return false;
			}
		}
		return brain.send(message);
	}
	
	public String receiveFromBrain(){
		if (brain == null) {
			if (!setupBrain()) {
				System.out.println("Setup communitation with Brain failed. -> Unable to receive message.");
				return null;
			}
		}
		return brain.receive();
	}
	
}
