package cns_communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

import cns_main.CnsConfig;
import config_utilities.Module;

public class CnsCommunication {
	private CnsConfig config;
	private boolean useTCP;
	
	Module brainModule;
	
	private TCPClient TCPToBrain;
	private UDPConnection UDPToBrain;
	
	public CnsCommunication(CnsConfig config, boolean useTCP){
		//Bei Initialisierung muss config mitgeladen werden und festgelegt werden, ob UPD (false) oder TCP (true)
		this.config = config;
		this.useTCP = useTCP;
	}
	
	private boolean setupConnectionToBrain(){
		if ((useTCP?TCPToBrain:UDPToBrain) != null) {
			//Als erstes wird geprüft, ob die Kommunication schon aufgebaut wurde.
			System.out.println("Connection to Brain via " + (useTCP?"TCP":"UDP") + " already set up");
			return false;
		}
		
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
			if (useTCP) {
				TCPToBrain = new TCPClient(InetAddress.getByName(brainModule.getComputer().getIp()), brainModule.getListeningPort(), true);
			}else{
				UDPToBrain = new UDPConnection();
			}
		} catch (UnknownHostException e) {
			System.out.println("Connection problem with Brain");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean endConnectionToBrain(){
		if (useTCP) {
			if (TCPToBrain == null) {return true;}
			if (TCPToBrain.endConnection()) {
				TCPToBrain = null;
				return true;
			}else{
				System.out.println("Nobody can stop the brain. Hehehe.");
				return false;
			}
		}else{
			//UDP
			return true;
		}
	}
	
	public boolean sendModulesToBrain(){
		
		if (config.getAll_modules().isEmpty()) {
			System.out.println("No modules to send.");
			return false;
		}

		if ((useTCP && TCPToBrain == null) || (!useTCP && UDPToBrain == null)) {
			//Connection to Brain not set up
			if (!setupConnectionToBrain()) { //Set Brain up
				//Setting up brain failed
				System.out.println("Setup communitation with Brain failed. -> Unable to send message.");
				return false;
			}
		}
		
		for (Module module : config.getAll_modules()) {
			String msg = "#CNS#" + module.getName() + ";" + module.getComputer().getIp() + ";" + module.getListeningPort() + "#";
			
			boolean sentDone;
			if (useTCP) {
				sentDone = TCPToBrain.send(msg);
			}else{
				try {
					UDPToBrain.send(msg, InetAddress.getByName(brainModule.getComputer().getIp()), brainModule.getListeningPort());
					sentDone = true;
				} catch (Exception e) {
					e.printStackTrace();
					sentDone = false;
				}
			}
			
			if (sentDone) {
				System.out.println(msg + " (done)");
			}else{
				System.out.println(msg + " (FAILED)");
			}
		}
		return true;
	}
	
//	public String receiveFromBrain(){
//		if ((useTCP && TCPToBrain == null) || (!useTCP && UDPToBrain == null)) {
//			//Connection to Brain not set up
//			if (!setupConnectionToBrain()) { //Set Brain up
//				//Setting up brain failed
//				System.out.println("Setup communitation with Brain failed. -> Unable to receive message.");
//				return null;
//			}
//		}
//		
//		if (useTCP) {
//			return TCPToBrain.receive();
//		}else{
//			try {
//				UDPToBrain.receive(InetAddress.getByName(brainModule.getComputer().getIp()), brainModule.getListeningPort(), true);
//				return UDPToBrain.getMessage();
//			} catch (Exception e) {
//				e.printStackTrace();
//				return null;
//			}
//			
//		}
//	}
	
}
