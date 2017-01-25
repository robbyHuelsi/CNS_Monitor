package cns_controller;


import cns_main.CnsConfig;
import cns_main.CnsSettings;
import config_utilities.Module;

public class ModuleMonitor {
	/*
	 * This class should monitor the modules
	 * gets the information from the config
	 * updates the config
	 */
	private ModuleStarter moduleStarters[];
	private int numberOfModules;
	
	private CnsConfig config;
	private CnsSettings setting;
	
	public boolean start_all_modules(){
		for (int i=0; i<numberOfModules; ++i){
			if (!config.getAll_modules().elementAt(i).getStartCommand().isEmpty()){
				moduleStarters[i] = new ModuleStarter(config.getAll_modules().elementAt(i), setting);
				moduleStarters[i].start();
			}
		}
		return true;
	}

	public boolean start_module(int module_num){
		Module module = config.getModule( module_num);
			if (!module.getStartCommand().isEmpty()){
				moduleStarters[module_num] = new ModuleStarter(module, setting);
				moduleStarters[module_num].start();
			}
		return true;
	}
	
	public boolean kill_module(int module_num){
		if ( moduleStarters[module_num] != null){
			moduleStarters[module_num].killModule();
		}
		return true;
	}
	
	public boolean kill_all_modules(){
		for (int i=0; i<numberOfModules; ++i){
			if (moduleStarters[i] != null)
				moduleStarters[i].killModule();
		}
		return true;
	}

	public ModuleMonitor(CnsConfig config, CnsSettings setting){
		this.config = config;
		this.setting = setting;
		//moduleStarters.setSize(config.getAll_modules().size());
		 moduleStarters= new ModuleStarter[config.getAll_modules().size()];
	}
	
	public void reset(){
		numberOfModules = config.getAll_modules().size();
		moduleStarters= new ModuleStarter[numberOfModules];
	}

}
