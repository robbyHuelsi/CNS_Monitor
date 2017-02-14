package cns_communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

import cns_main.CnsConfig;
import config_utilities.Module;

public class CnsCommunication {
	private CnsConfig config;
	private TCPClient tcpToBrain;
	
	public CnsCommunication(CnsConfig config){
		this.config = config;
	}
	
	private boolean setupBrain(){
		if (tcpToBrain != null) {
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
			tcpToBrain = new TCPClient(InetAddress.getByName(brainModule.getComputer().getIp()), brainModule.getListeningPort(), true);
		} catch (UnknownHostException e) {
			System.out.println("Connection problem with Brain");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean endBrain(){
		if (tcpToBrain == null) {return true;}
		if (tcpToBrain.endConnection()) {
			tcpToBrain = null;
			return true;
		}else{
			System.out.println("Nobody can stop Brain. Hehehe.");
			return false;
		}
	}
	
	public boolean sendModulesToBrain(){
		
		if (config.getAll_modules().isEmpty()) {
			System.out.println("No modules to send.");
			return false;
		}

		if (tcpToBrain == null) {
			//Brain not set up
			if (!setupBrain()) { //Set Brain up
				//Setting up brain failed
				System.out.println("Setup communitation with Brain failed. -> Unable to send message.");
				return false;
			}
		}
		
		for (Module module : config.getAll_modules()) {
			String msg = "#CNS#[" + module.getName() + "];[" + module.getComputer().getIp() + "];[" + module.getListeningPort() + "]";
			if (tcpToBrain.send(msg)) {
				System.out.println(msg + " (done)");
			}else{
				System.out.println(msg + " (FAILED)");
			}
		}
		return true;
	}
	
	public String receiveFromBrain(){
		if (tcpToBrain == null) {
			if (!setupBrain()) {
				System.out.println("Setup communitation with Brain failed. -> Unable to receive message.");
				return null;
			}
		}
		return tcpToBrain.receive();
	}
	
}
