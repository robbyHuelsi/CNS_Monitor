package cns_controller;



import cns_main.CnsConfig;
import config_utilities.Module;

public class ModuleMonitor {
	/*
	 * This class should monitor the modules
	 * gets the information from the config
	 * updates the config
	 */
	
	private CnsConfig config;
	
	public boolean start_all_modules(){
		for (Module module : config.getAll_modules()){
			
			
			

			if (!module.getStartCommand().isEmpty()){
				ModuleStarter ms = new ModuleStarter(module);
				ms.start();
			}

		}
		return true;
	}
	
	public ModuleMonitor(CnsConfig config){
		this.config = config;
	}

}
